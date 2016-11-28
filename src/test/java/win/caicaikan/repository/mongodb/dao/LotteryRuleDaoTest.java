/**
 * 
 */
package win.caicaikan.repository.mongodb.dao;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;
import win.caicaikan.constant.LotteryType;
import win.caicaikan.repository.mongodb.entity.LotteryRuleEntity;

/**
 * @author yanwenge
 *
 */
public class LotteryRuleDaoTest extends BaseTest {

	@Autowired
	private LotteryRuleDao dao;
	/**
	 * Test method for {@link org.springframework.data.mongodb.repository.MongoRepository#insert(java.lang.Iterable)}.
	 */
	@Test
	public void testInsertIterableOfS() {
		LotteryRuleEntity entity = new LotteryRuleEntity();
		entity.setLotteryType(LotteryType.SSQ.getCode());
		entity.setRuleNo(entity.getLotteryType()+"-"+"001");
		entity.setRuleType("0");
		entity.setRuleName("出次均衡");
		List<LotteryRuleEntity> list = new ArrayList<LotteryRuleEntity>();
		dao.insert(list);
	}

}
