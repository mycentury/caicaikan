/**
 * 
 */
package win.caicaikan.task.ssq;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import win.caicaikan.repository.mongodb.dao.PredictRuleDao;
import win.caicaikan.repository.mongodb.dao.ssq.SsqPredictDao;
import win.caicaikan.repository.mongodb.dao.ssq.SsqResultDao;
import win.caicaikan.repository.mongodb.entity.PredictRuleEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqPredictEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.rule.RuleTemplate;
import win.caicaikan.task.TaskTemplete;
import win.caicaikan.util.SpringContextUtil;

/**
 * @Desc 双色球及时任务：用于下期预测
 * @author wewenge.yan
 * @Date 2016年11月21日
 * @ClassName PredictTask
 */
@Service
public class SsqPredictTask extends TaskTemplete {
	private static final Logger logger = Logger.getLogger(SsqPredictTask.class);
	@Autowired
	private SsqResultDao ssqResultDao;
	@Autowired
	private SsqPredictDao ssqPredictDao;
	@Autowired
	private PredictRuleDao predictRuleDao;

	protected List<PredictRuleEntity> queryRule() throws Throwable {
		return predictRuleDao.findAll();
	}

	protected void saveResult(List<SsqPredictEntity> result) throws Throwable {
		ssqPredictDao.insert(result);
	}

	@Override
	public void run() throws Throwable {
		Map<String, RuleTemplate> beans = SpringContextUtil.getBeans(RuleTemplate.class);

		Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "termNo"));
		List<SsqResultEntity> list = ssqResultDao.findAll(sort);
		List<PredictRuleEntity> rules = queryRule();
		excuteRules(beans, list, rules);
	}

	private void excuteRules(Map<String, RuleTemplate> beans, List<SsqResultEntity> list, List<PredictRuleEntity> rules) throws Throwable {
		List<SsqPredictEntity> result = new ArrayList<SsqPredictEntity>();
		for (RuleTemplate ruleExcutor : beans.values()) {
			for (PredictRuleEntity rule : rules) {
				if (ruleExcutor.getRuleType().name().equals(rule.getRuleType())) {
					try {
						SsqPredictEntity predict = ruleExcutor.excute(list, rule);
						result.add(predict);
					} catch (Exception e) {
						logger.error("rule.excuteRules() excute error");
						e.printStackTrace();
					}
				}
			}
		}
		ssqPredictDao.save(result);
	}

	@Override
	@Scheduled(cron = "${task.cron.ssq.predict}")
	protected void execute() {
		super.execute();
	}
}
