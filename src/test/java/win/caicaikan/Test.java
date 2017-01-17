/**
 * 
 */
package win.caicaikan;

import java.io.File;

import win.caicaikan.service.rule.RuleService;
import win.caicaikan.util.SpringContextUtil;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月22日
 * @ClassName Test
 */
public class Test extends BaseTest {
	@org.junit.Test
	public void test() {
		File file = new File("a 2");
		System.out.println(file.getPath());
	}

	@org.junit.Test
	public void test2() {
		RuleService bean = SpringContextUtil.getBean(RuleService.class);
		System.out.println(bean);
	}
}
