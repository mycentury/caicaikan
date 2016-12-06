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
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import win.caicaikan.constant.ExcuteStatus;
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
	/**
	 * 查询最大值
	 */
	private static final int MAX_SIZE = 2000;
	private static final Logger logger = Logger.getLogger(LotteryPredictTask.class);
	@Autowired
	private LotterySsqDao lotterySsqDao;
	@Autowired
	private LotteryPredictDao lotteryPredictDao;
	@Autowired
	private LotteryRuleDao lotteryRuleDao;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void run() throws Throwable {
		Map<String, RuleTemplate> beans = SpringContextUtil.getBeans(RuleTemplate.class);

		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "termNo"));
		Query query = new Query().limit(MAX_SIZE).with(sort);
		List<LotterySsqEntity> list = mongoTemplate.find(query, LotterySsqEntity.class);
		List<LotteryRuleEntity> rules = lotteryRuleDao.findAll();
		if (CollectionUtils.isEmpty(list) || CollectionUtils.isEmpty(rules)) {
			throw new IllegalStateException("未查到彩票结果和预测规则！");
		}
		for (LotteryRuleEntity lotteryRuleEntity : rules) {
			lotteryRuleEntity.setExcuteStatus(ExcuteStatus.READY.name());
		}
		lotteryRuleDao.save(rules);
		excuteRules(beans, list, rules);
	}

	private void excuteRules(Map<String, RuleTemplate> beans, List<LotterySsqEntity> list, List<LotteryRuleEntity> rules) throws Throwable {
		List<LotteryPredictEntity> result = new ArrayList<LotteryPredictEntity>();
		for (RuleTemplate ruleExcutor : beans.values()) {
			for (LotteryRuleEntity rule : rules) {
				if (ruleExcutor.getRule().getRuleNo().equals(rule.getRuleNo())) {
					try {
						rule.setExcuteStatus(ExcuteStatus.RUNNING.name());
						lotteryRuleDao.save(rule);
						LotteryPredictEntity predict = ruleExcutor.excute(list, rule);
						result.add(predict);
					} catch (Exception e) {
						logger.error(rule.getRuleNo() + " excute error");
						e.printStackTrace();
					}
				}
			}
		}
		lotteryPredictDao.insert(result);
	}

	@Override
	@Scheduled(cron = "${task.cron.ssq.predict}")
	protected void execute() {
		super.execute();
	}
}
