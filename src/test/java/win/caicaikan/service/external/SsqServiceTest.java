/**
 * 
 */
package win.caicaikan.service.external;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;
import win.caicaikan.api.req.LotteryReq;
import win.caicaikan.constant.LotteryType;
import win.caicaikan.service.external.SsqService;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName DoubleColorBallServiceTest
 */
public class SsqServiceTest extends BaseTest {
	@Autowired
	private SsqService ssqService;

	/**
	 * Test method for
	 * {@link win.caicaikan.service.external.SsqService#getSsqInfoByLotteryReq(win.caicaikan.api.req.HistoryLotteryReq)}.
	 */
	@Test
	public void testGetSsqInfoByLotteryReq() {
		LotteryReq req = new LotteryReq();
		req.setLotteryType(LotteryType.SSQ.getCode());
		req.setQueryType("range");
		req.setStart("2003000");
		req.setEnd("2003200");
		ssqService.getSsqHistoryByLotteryReq(req);
	}

}
