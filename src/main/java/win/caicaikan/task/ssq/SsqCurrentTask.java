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

import win.caicaikan.api.req.LotteryReq;
import win.caicaikan.api.res.Result;
import win.caicaikan.constant.LotteryType;
import win.caicaikan.repository.mongodb.dao.ssq.SsqResultDao;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.external.SsqService;
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
	private SsqResultDao ssqResultDao;

	@Override
	public void run() throws Throwable {
		LotteryReq req = new LotteryReq();
		req.setLotteryType(LotteryType.SSQ.getCode());
		try {
			// 本期同步
			Result<List<SsqResultEntity>> result = ssqService.getSsqCurrentByLotteryReq(req);
			if (CollectionUtils.isEmpty(result.getData())) {
				logger.error("getSsqCurrentByLotteryReq获取数据失败");
				return;
			}
			for (SsqResultEntity entity : result.getData()) {
				ssqResultDao.insert(result.getData());
				if (ssqResultDao.exists(entity.getTermNo())) {
					continue;
				}
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
