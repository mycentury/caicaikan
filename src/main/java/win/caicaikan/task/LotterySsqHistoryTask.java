/**
 * 
 */
package win.caicaikan.task;

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
import win.caicaikan.repository.mongodb.dao.LotterySsqDao;
import win.caicaikan.repository.mongodb.entity.LotterySsqEntity;
import win.caicaikan.service.LotterySsqService;
import win.caicaikan.util.DateUtil;

/**
 * @Desc 双色球历史任务：用于补齐一等奖、二等奖中奖信息
 * @author wewenge.yan
 * @Date 2016年11月21日
 * @ClassName DoubleColorBallTask
 */
@Service
public class LotterySsqHistoryTask extends TaskTemplete {
	private static final Logger logger = Logger
			.getLogger(LotterySsqHistoryTask.class);
	private static final int START_YEAR = 2003;
	private static final String END_NO = "200";
	private static final String START_NO = "001";
	@Autowired
	private LotterySsqService lotterySsqService;
	@Autowired
	private LotterySsqDao lotterySsqDao;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Override
	public void run() throws Throwable {
		LotteryReq req = new LotteryReq();
		req.setLotteryType(LotteryType.SSQ.getValue());
		req.setQueryType("range");
		Criteria criteria = Criteria.where("firstPrizeCount").is("");
		criteria.orOperator(Criteria.where("secondPrizeCount").is(""));
		Query query = new Query().addCriteria(criteria);
		List<LotterySsqEntity> list = mongoTemplate.find(query,
				LotterySsqEntity.class);
		if (CollectionUtils.isEmpty(list)) {
			return;
		}
		for (int i = 1; i < list.size(); i++) {
			String termNo = list.get(i).getTermNo();
			req.setStart(termNo);
			req.setEnd(termNo);
			Result<List<LotterySsqEntity>> result = lotterySsqService
					.getSsqHistoryByLotteryReq(req);
			lotterySsqDao.save(result.getData());
		}
	}

	@Override
	@Scheduled(cron = "${task.cron.ssq.history}")
	protected void execute() {
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
			Result<List<LotterySsqEntity>> result = lotterySsqService
					.getSsqHistoryByLotteryReq(req);
			lotterySsqDao.save(result.getData());
		}
	}

}
