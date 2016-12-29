/**
 * 
 */
package win.caicaikan.task.ssq;

import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import win.caicaikan.api.req.LotteryReq;
import win.caicaikan.api.res.Result;
import win.caicaikan.constant.LotteryType;
import win.caicaikan.repository.mongodb.dao.ssq.SsqResultDao;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.external.SsqService;
import win.caicaikan.task.TaskTemplete;
import win.caicaikan.util.DateUtil;

/**
 * @Desc 双色球历史任务：用于补齐一等奖、二等奖中奖信息
 * @author wewenge.yan
 * @Date 2016年11月21日
 * @ClassName SsqHistoryTask
 */
@Service
public class SsqHistoryTask extends TaskTemplete {
	private static final Logger logger = Logger.getLogger(SsqHistoryTask.class);
	private static final int START_YEAR = 2003;
	private static final String END_NO = "200";
	private static final String START_NO = "001";
	@Autowired
	private SsqService ssqService;
	@Autowired
	private SsqResultDao ssqResultDao;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void doInTask() {
		LotteryReq req = new LotteryReq();
		req.setLotteryType(LotteryType.SSQ.getValue());
		req.setQueryType("range");
		Criteria criteria = Criteria.where("firstPrizeCount").is(null);
		criteria.orOperator(Criteria.where("secondPrizeCount").is(null));
		Query query = new Query().addCriteria(criteria);
		List<SsqResultEntity> list = mongoTemplate.find(query, SsqResultEntity.class);
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		for (int i = 0; i < list.size(); i++) {
			String termNo = list.get(i).getTermNo();
			req.setStart(termNo);
			req.setEnd(termNo);
			Result<List<SsqResultEntity>> result = ssqService.getSsqHistoryByLotteryReq(req);
			ssqResultDao.save(result.getData());
		}
	}

	@Override
	@Scheduled(cron = "${task.cron.ssq.history}")
	public void execute() {
		super.execute();
	}

	public void synchronizeHistoryData() {
		LotteryReq req = new LotteryReq();
		req.setLotteryType(LotteryType.SSQ.getValue());
		req.setQueryType("range");
		Integer currentYear = Integer.valueOf(DateUtil.toYear(new Date()));
		for (int i = START_YEAR; i <= currentYear; i++) {
			req.setStart(i + START_NO);
			req.setEnd(i + END_NO);
			Result<List<SsqResultEntity>> result = ssqService.getSsqHistoryByLotteryReq(req);
			ssqResultDao.save(result.getData());
		}
	}

}
