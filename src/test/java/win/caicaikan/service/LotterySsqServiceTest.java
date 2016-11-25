/**
 * 
 */
package win.caicaikan.service;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;
import win.caicaikan.api.req.LotteryReq;
import win.caicaikan.constant.LotteryType;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName DoubleColorBallServiceTest
 */
public class LotterySsqServiceTest extends BaseTest {
	@Autowired
	private LotterySsqService doubleColorBallService;

	/**
	 * Test method for
	 * {@link win.caicaikan.service.LotterySsqService#getSsqInfoByLotteryReq(win.caicaikan.api.req.HistoryLotteryReq)}.
	 */
	@Test
	public void testGetSsqInfoByLotteryReq() {
		LotteryReq req = new LotteryReq();
		req.setLotteryType(LotteryType.SSQ.getCode());
		req.setQueryType("range");
		req.setStart("2003000");
		req.setEnd("2003200");
		doubleColorBallService.getSsqHistoryByLotteryReq(req);
	}

}
