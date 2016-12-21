/**
 * 
 */
package win.caicaikan.service.external;

import java.util.Date;
import java.util.UUID;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;
import win.caicaikan.service.external.domain.AliSecurityQueryReq;
import win.caicaikan.service.external.domain.AliSecurityQueryRes;
import win.caicaikan.util.DateUtil;
import win.caicaikan.util.TypeConverterUtil;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月19日
 * @ClassName AliSecurityServiceTest
 */
public class AliSecurityServiceTest extends BaseTest {

	@Autowired
	private AliSecurityService aliSecurityService;
	@Autowired
	private ApiConfig apiConfig;

	/**
	 * Test method for
	 * {@link win.caicaikan.service.external.AliSecurityService#querySecurityRules(win.caicaikan.service.external.domain.AliSecurityReq)}
	 * .
	 */
	@Test
	public void testQuerySecurityRules() {
		AliSecurityQueryReq req = TypeConverterUtil.map(apiConfig, AliSecurityQueryReq.class);
		req.setAction("DescribeSecurityGroupAttribute");
		req.setSignatureNonce(UUID.randomUUID().toString());
		req.setNicType("intranet");
		req.setDirection("all");
		Date timeByTimeZone = DateUtil.getUtcTime();
		req.setTimestamp(DateUtil.toChar(timeByTimeZone, DateUtil.ISO8601));
		AliSecurityQueryRes result = aliSecurityService.querySecurityRules(req);
		System.out.println(result);
	}

}
