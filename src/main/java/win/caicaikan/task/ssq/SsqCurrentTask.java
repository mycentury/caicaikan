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
import win.caicaikan.repository.mongodb.entity.ssq.SsqPredictEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.external.SsqService;
import win.caicaikan.service.internal.DaoService;
import win.caicaikan.service.internal.DaoService.Condition;
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
		for (SsqResultEntity ssqResult : result.getData()) {
			if (!daoService.existsById(ssqResult.getTermNo(), SsqResultEntity.class)) {
				daoService.insert(ssqResult);
			}
			Condition condition = new Condition();
			condition.addParam("termNo", "=", ssqResult.getTermNo());
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
