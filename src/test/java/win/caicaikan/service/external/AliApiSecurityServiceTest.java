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
import win.caicaikan.service.external.domain.AliSecurityReq;
import win.caicaikan.service.external.domain.AliSecurityRes;
import win.caicaikan.util.DateUtil;
import win.caicaikan.util.TypeConverterUtil;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月19日
 * @ClassName AliSecurityServiceTest
 */
public class AliApiSecurityServiceTest extends BaseTest {

	@Autowired
	private AliApiSecurityService aliApiSecurityService;
	@Autowired
	private ApiConfig apiConfig;

	/**
	 * Test method for
	 * {@link win.caicaikan.service.external.AliApiSecurityService#querySecurityRules(AliSecurityQueryReq)} .
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
		AliSecurityQueryRes result = aliApiSecurityService.querySecurityRules(req);
		System.out.println(result);
	}

	/**
	 * Test method for {@link win.caicaikan.service.external.AliApiSecurityService#updateSecurityRule(AliSecurityReq)} .
	 */
	@Test
	public void testUpdateSecurityRule() {
		AliSecurityReq req = TypeConverterUtil.map(apiConfig, AliSecurityReq.class);
		req.setAction("AuthorizeSecurityGroup");
		req.setIpProtocol("all");
		req.setPortRange("-1/-1");
		req.setSourceCidrIp("140.207.185.107");
		req.setSignatureNonce(UUID.randomUUID().toString());
		req.setNicType("internet");
		Date timeByTimeZone = DateUtil.getUtcTime();
		req.setTimestamp(DateUtil.toChar(timeByTimeZone, DateUtil.ISO8601));
		AliSecurityRes result = aliApiSecurityService.updateSecurityRule(req);
		System.out.println(result);
	}

	/**
	 * Test method for {@link win.caicaikan.service.external.AliApiSecurityService#updateSecurityRule(AliSecurityReq)} .
	 */
	@Test
	public void testDeleteSecurityRule() {
		AliSecurityReq req = TypeConverterUtil.map(apiConfig, AliSecurityReq.class);
		req.setAction("RevokeSecurityGroup");
		req.setIpProtocol("all");
		req.setPortRange("-1/-1");
		req.setSourceCidrIp("211.95.25.35");
		req.setSignatureNonce(UUID.randomUUID().toString());
		req.setNicType("intranet");
		Date timeByTimeZone = DateUtil.getUtcTime();
		req.setTimestamp(DateUtil.toChar(timeByTimeZone, DateUtil.ISO8601));
		AliSecurityRes result = aliApiSecurityService.updateSecurityRule(req);
		System.out.println(result);
	}
}
