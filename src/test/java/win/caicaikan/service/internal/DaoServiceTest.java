/**
 * 
 */
package win.caicaikan.service.internal;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月28日
 * @ClassName DaoServiceTest
 */
public class DaoServiceTest extends BaseTest {
	@Autowired
	private DaoService daoService;

	/**
	 * Test method for {@link DaoService#getIdColName(Class)}.
	 */
	@Test
	public void testGetIdColName() {
		String idColName = daoService.getIdColName(SsqResultEntity.class);
		System.out.println(idColName);
	}
}
