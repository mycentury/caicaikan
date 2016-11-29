package win.caicaikan.service.predict;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.constant.Rule;
import win.caicaikan.repository.mongodb.dao.LotteryPredictDao;
import win.caicaikan.repository.mongodb.dao.LotteryRuleDao;
import win.caicaikan.repository.mongodb.entity.LotteryPredictEntity;
import win.caicaikan.repository.mongodb.entity.LotteryRuleEntity;

public abstract class RuleTemplate {
	private final static Logger logger = Logger.getLogger(RuleTemplate.class);

	@Autowired
	private LotteryRuleDao lotteryRuleDao;
	@Autowired
	private LotteryPredictDao lotteryPredictDao;

	protected LotteryRuleEntity queryRule() throws Throwable {
		return lotteryRuleDao.findById(getRule().getRuleNo());
	}

	protected void saveResult(List<LotteryPredictEntity> result) throws Throwable {
		lotteryPredictDao.insert(result);
	}

	abstract List<LotteryPredictEntity> excuteRule(LotteryRuleEntity entity) throws Throwable;

	abstract Rule getRule();

	public void excute() {
		Date startDate = new Date();
		Rule rule = getRule();
		if (rule == null) {
			logger.error("No rule provided,exit!");
			return;
		}
		try {
			logger.info("任务【" + rule.getRuleNo() + "】START ");
			LotteryRuleEntity entity = queryRule();
			List<LotteryPredictEntity> result = excuteRule(entity);
			saveResult(result);
			Thread.sleep(1000 * 5);// 休眠5s
		} catch (Throwable e) {
			e.printStackTrace();
			// logger.error(rule.getRuleNo() + " execute error", e);
		} finally {
			long second = (new Date().getTime() - startDate.getTime()) / 1000;
			logger.info("任务【" + rule.getRuleNo() + "】 END,耗时:" + second / 3600 + "h"
					+ (second / 60) % 60 + "m" + second % 60 + "s");
		}
	}
}
