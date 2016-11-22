/**
 * 
 */
package win.caicaikan.task;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import win.caicaikan.api.domain.LotterySsq;
import win.caicaikan.api.req.LotteryReq;
import win.caicaikan.api.res.Result;
import win.caicaikan.constant.LotteryType;
import win.caicaikan.repository.mongodb.dao.LotterySsqDao;
import win.caicaikan.repository.mongodb.entity.LotterySsqEntity;
import win.caicaikan.service.LotterySsqService;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月21日
 * @ClassName DoubleColorBallTask
 */
@Service
public class LotterySsqTask extends TaskTemplete {

	@Autowired
	private LotterySsqService lotterySsqService;
	@Autowired
	private LotterySsqDao lotterySsqDao;

	@Override
	public void run() throws Throwable {
		String[] years = { "2003", "2004", "2005", "2006", "2007", "2008", "2009", "2010", "2011", "2012", "2013", "2014", "2015", "2016" };
		LotteryReq req = new LotteryReq();
		req.setLotteryType(LotteryType.SSQ.getCode());
		req.setQueryType("range");
		for (String year : years) {
			req.setStart(year + "000");
			req.setEnd(year + "200");
			Result<List<LotterySsq>> result = lotterySsqService.getSsqInfoByLotteryReq(req);
			for (LotterySsq LotterySsq : result.getData()) {
				LotterySsqEntity entity = new LotterySsqEntity();
				entity.setTermNo(LotterySsq.getTermNo());
				entity.setOpenDate(LotterySsq.getOpenDate());
				entity.setRedNumbers(LotterySsq.getRedNumbers());
				entity.setBlueNumber(LotterySsq.getBlueNumber());
				entity.setFirstPrizeCount(LotterySsq.getFirstPrizeCount());
				entity.setFirstPrizeAmount(LotterySsq.getFirstPrizeAmount());
				entity.setSecondPrizeCount(LotterySsq.getSecondPrizeCount());
				entity.setSecondPrizeAmount(LotterySsq.getSecondPrizeAmount());
				lotterySsqDao.insert(entity);
			}
		}
	}
}
