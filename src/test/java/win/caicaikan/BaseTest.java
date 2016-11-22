/**
 * 
 */
package win.caicaikan;

import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.transaction.annotation.Transactional;

/**
 * @Desc
 * @author wenge.yan
 * @Date 2016年6月20日
 * @ClassName BaseTest
 */
@ContextConfiguration(locations = { "classpath:spring/spring-context.xml", "classpath:spring/springmvc-servlet.xml",
		"classpath:spring/spring-mongodb.xml" })
@RunWith(SpringJUnit4ClassRunner.class)
@Transactional
public abstract class BaseTest extends AbstractJUnit4SpringContextTests {

}
