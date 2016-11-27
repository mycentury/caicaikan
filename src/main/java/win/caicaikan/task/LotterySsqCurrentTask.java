/**
 * 
 */
package win.caicaikan.task;

import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import win.caicaikan.api.req.LotteryReq;
import win.caicaikan.api.res.Result;
import win.caicaikan.constant.LotteryType;
import win.caicaikan.repository.mongodb.dao.LotterySsqDao;
import win.caicaikan.repository.mongodb.entity.LotterySsqEntity;
import win.caicaikan.service.LotterySsqService;

/**
 * @Desc 双色球及时任务：用于获取当天晚上开奖信息本期同步
 * @author wewenge.yan
 * @Date 2016年11月21日
 * @ClassName DoubleColorBallTask
 */
@Service
public class LotterySsqCurrentTask extends TaskTemplete {
	private static final Logger logger = Logger
			.getLogger(LotterySsqCurrentTask.class);
	@Autowired
	private LotterySsqService lotterySsqService;
	@Autowired
	private LotterySsqDao lotterySsqDao;

	@Override
	public void run() throws Throwable {
		LotteryReq req = new LotteryReq();
		req.setLotteryType(LotteryType.SSQ.getCode());
		try {
			// 本期同步
			Result<List<LotterySsqEntity>> result = lotterySsqService
					.getSsqCurrentByLotteryReq(req);
			if (CollectionUtils.isEmpty(result.getData())) {
				logger.error("getSsqCurrentByLotteryReq获取数据失败");
				return;
			}
			for (LotterySsqEntity entity : result.getData()) {
				if (lotterySsqDao.exists(entity.getTermNo())) {
					continue;
				}
				lotterySsqDao.insert(result.getData());
			}

		} catch (Exception e) {
			logger.error(e);
		}
	}

	@Override
	@Scheduled(cron = "${task.cron.ssq.current}")
	protected void execute() {
		super.execute();
	}
}
