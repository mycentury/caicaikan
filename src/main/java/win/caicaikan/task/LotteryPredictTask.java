/**
 * 
 */
package win.caicaikan.task;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import win.caicaikan.repository.mongodb.dao.LotteryPredictDao;
import win.caicaikan.repository.mongodb.dao.LotteryRuleDao;
import win.caicaikan.repository.mongodb.dao.LotterySsqDao;
import win.caicaikan.repository.mongodb.entity.LotteryPredictEntity;
import win.caicaikan.repository.mongodb.entity.LotteryRuleEntity;
import win.caicaikan.repository.mongodb.entity.LotterySsqEntity;
import win.caicaikan.service.predict.RuleTemplate;
import win.caicaikan.util.SpringContextUtil;

/**
 * @Desc 双色球及时任务：用于下期预测
 * @author wewenge.yan
 * @Date 2016年11月21日
 * @ClassName DoubleColorBallTask
 */
@Service
public class LotteryPredictTask extends TaskTemplete {
	private static final Logger logger = Logger.getLogger(LotteryPredictTask.class);
	@Autowired
	private LotterySsqDao lotterySsqDao;
	@Autowired
	private LotteryPredictDao lotteryPredictDao;
	@Autowired
	private LotteryRuleDao lotteryRuleDao;

	protected List<LotteryRuleEntity> queryRule() throws Throwable {
		return lotteryRuleDao.findAll();
	}

	protected void saveResult(List<LotteryPredictEntity> result) throws Throwable {
		lotteryPredictDao.insert(result);
	}

	@Override
	public void run() throws Throwable {
		Map<String, RuleTemplate> beans = SpringContextUtil.getBeans(RuleTemplate.class);

		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "termNo"));
		List<LotterySsqEntity> list = lotterySsqDao.findAll(sort);
		List<LotteryRuleEntity> rules = queryRule();
		excuteRules(beans, list, rules);
	}

	private void excuteRules(Map<String, RuleTemplate> beans, List<LotterySsqEntity> list,
			List<LotteryRuleEntity> rules) throws Throwable {
		List<LotteryPredictEntity> result = new ArrayList<LotteryPredictEntity>();
		for (RuleTemplate ruleExcutor : beans.values()) {
			for (LotteryRuleEntity rule : rules) {
				if (ruleExcutor.getRule().getRuleNo().equals(rule.getRuleNo())) {
					try {
						LotteryPredictEntity predict = ruleExcutor.excute(list, rule);
						result.add(predict);
					} catch (Exception e) {
						logger.error("rule.getRuleNo() excute error");
						e.printStackTrace();
					}
				}
			}
		}
		lotteryPredictDao.save(result);
	}

	@Override
	@Scheduled(cron = "${task.cron.ssq.predict}")
	protected void execute() {
		super.execute();
	}
}
