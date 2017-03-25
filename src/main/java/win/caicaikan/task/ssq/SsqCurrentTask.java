/**
 * 
 */
package win.caicaikan.task.ssq;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import win.caicaikan.api.req.LotteryReq;
import win.caicaikan.api.res.Result;
import win.caicaikan.constant.LotteryType;
import win.caicaikan.constant.SysConfig;
import win.caicaikan.repository.mongodb.entity.PredictRuleEntity;
import win.caicaikan.repository.mongodb.entity.SysConfigEntity;
import win.caicaikan.repository.mongodb.entity.ssq.GsCountEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqPredictEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.external.SsqService;
import win.caicaikan.service.internal.CalculateService;
import win.caicaikan.service.internal.DaoService;
import win.caicaikan.service.internal.DaoService.Condition;
import win.caicaikan.service.rule.RuleService;
import win.caicaikan.task.TaskTemplete;

/**
 * @Desc 双色球及时任务：用于获取当天晚上开奖信息本期同步
 * @author wewenge.yan
 * @Date 2016年11月21日
 * @ClassName SsqCurrentTask
 */
@Service
public class SsqCurrentTask extends TaskTemplete {
	private static final Logger logger = Logger.getLogger(SsqCurrentTask.class);
	@Autowired
	private SsqService ssqService;
	@Autowired
	private DaoService daoService;
	@Autowired
	private RuleService ruleService;
	@Autowired
	private CalculateService calculateService;

	@Override
	public void doInTask() {
		LotteryReq req = new LotteryReq();
		req.setLotteryType(LotteryType.SSQ.getCode());
		// 本期同步
		Result<List<SsqResultEntity>> result = ssqService.getSsqCurrentByLotteryReq(req);
		if (CollectionUtils.isEmpty(result.getData())) {
			logger.error("getSsqCurrentByLotteryReq获取数据失败");
			return;
		}
		SsqResultEntity currSsqResult = result.getData().get(0);
		SysConfigEntity currTerm = daoService.queryById(SysConfig.SSQ_CURRENT_TERM.getId(),
				SysConfigEntity.class);
		SysConfigEntity nextTerm = daoService.queryById(SysConfig.SSQ_NEXT_TERM.getId(),
				SysConfigEntity.class);
		if (!currSsqResult.getId().equals(currTerm.getValue())) {
			currTerm.setKey(currSsqResult.getOpenTime());
			currTerm.setValue(currSsqResult.getId());
			nextTerm.setKey(ruleService.getNextTermOpenDateOfSsq(currSsqResult.getOpenTime()));
			nextTerm.setValue(ruleService.getNextTermNoOfSsq(currSsqResult));
			daoService.save(currTerm);
			daoService.save(nextTerm);
		}

		for (SsqResultEntity ssqResult : result.getData()) {
			if (!daoService.existsById(ssqResult.getId(), SsqResultEntity.class)) {
				daoService.insert(ssqResult);
				this.updateGsCount(ssqResult);
			}
			Condition condition = new Condition();
			condition.addParam("termNo", "=", ssqResult.getId());
			List<SsqPredictEntity> ssqPredicts = daoService
					.query(condition, SsqPredictEntity.class);
			if (CollectionUtils.isEmpty(ssqPredicts)) {
				continue;
			}
			for (SsqPredictEntity ssqPredict : ssqPredicts) {
				if (ssqPredict == null || !StringUtils.isEmpty(ssqPredict.getRightNumbers())) {
					continue;
				}
				String rightNumPoses = this.getRightNumPositions(ssqPredict, ssqResult);
				ssqPredict.setRightNumbers(rightNumPoses);
				daoService.save(ssqPredict);
			}
		}

		// 计算更新概率
		List<PredictRuleEntity> allPredictRules = daoService.query(null, PredictRuleEntity.class);
		for (PredictRuleEntity predictRule : allPredictRules) {
			predictRule = calculateService.countRate(predictRule, 6, 1);
			daoService.save(predictRule);
		}
	}

	private void updateGsCount(SsqResultEntity ssqResult) {
		Integer key = 0;
		String[] reds = ssqResult.getRedNumbers().split(",");
		for (String red : reds) {
			key += Integer.valueOf(red);
		}
		long terms = daoService.count(null, SsqResultEntity.class);
		SysConfigEntity redsumCount = daoService.queryById(SysConfig.SSQ_REDSUM_COUNT.getId(),
				SysConfigEntity.class);
		Integer size = Integer.valueOf(redsumCount.getValue());
		List<GsCountEntity> gsCounts = daoService.query(null, GsCountEntity.class);
		for (GsCountEntity gsCount : gsCounts) {
			Integer count = gsCount.getCount();
			if (key == gsCount.getKey()) {
				gsCount.setCount(++count);
			}
			Integer weight = (int) terms * gsCount.getRate() / size - count;
			gsCount.setWeight(weight);
		}
		daoService.save(gsCounts);
	}

	public String getRightNumPositions(SsqPredictEntity ssqPredict, SsqResultEntity ssqResult) {
		String[] redNumbers = ssqResult.getRedNumbers().split(",");
		StringBuilder rightPoses = new StringBuilder();
		for (String rightRedNumber : redNumbers) {
			for (int i = 0; i < ssqPredict.getRedNumbers().size(); i++) {
				String predictRedNumber = ssqPredict.getRedNumbers().get(i);
				if (rightRedNumber.equals(predictRedNumber.split("=")[0])) {
					rightPoses.append(",").append(i + 1);
					break;
				}
			}
		}
		rightPoses.append("+");
		String rightBlueNumber = ssqResult.getBlueNumbers().split(",")[0];
		for (int i = 0; i < ssqPredict.getBlueNumbers().size(); i++) {
			String predictBlueNumber = ssqPredict.getBlueNumbers().get(i);
			if (rightBlueNumber.equals(predictBlueNumber.split("=")[0])) {
				rightPoses.append(i + 1);
				break;
			}
		}
		return rightPoses.substring(1);
	}

	@Override
	@Scheduled(cron = "${task.cron.ssq.current}")
	public void execute() {
		super.execute();
	}
}
