/**
 * 
 */
package win.caicaikan.repository.mongodb.dao;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.BaseTest;
import win.caicaikan.repository.mongodb.entity.MenuEntity;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月16日
 * @ClassName MenuDaoTest
 */
public class MenuDaoTest extends BaseTest {

	@Autowired
	private MenuDao menuDao;

	/**
	 * Test method for {@link org.springframework.data.mongodb.repository.MongoRepository#save(java.lang.Iterable)}.
	 */
	@Test
	public void testSaveIterableOfS() {
		List<MenuEntity> menuEntities = new ArrayList<MenuEntity>();
		MenuEntity entity = new MenuEntity();
		entity.setId("HOME");
		entity.setSeq(0);
		entity.setNameZh("主页");
		entity.setNameEn("Home");
		entity.setPath("/");
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		menuEntities.add(entity);

		List<MenuEntity> subMenus = new ArrayList<MenuEntity>();
		entity = new MenuEntity();
		entity.setId("HISTORY_SSQ");
		entity.setSeq(0);
		entity.setNameZh("双色球");
		entity.setNameEn("Double Color Ball");
		entity.setPath("/history/ssq");
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		subMenus.add(entity);

		entity = new MenuEntity();
		entity.setId("HISTORY_DLT");
		entity.setSeq(1);
		entity.setNameZh("大乐透");
		entity.setNameEn("Super Lotto");
		entity.setPath("/history/dlt");
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		subMenus.add(entity);

		entity = new MenuEntity();
		entity.setId("HISTORY");
		entity.setSeq(1);
		entity.setNameZh("往期数据");
		entity.setNameEn("History");
		entity.setPath("/history");
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		entity.setSubMenus(subMenus);
		menuEntities.add(entity);

		subMenus = new ArrayList<MenuEntity>();
		entity = new MenuEntity();
		entity.setId("PREDICT_SSQ");
		entity.setSeq(1);
		entity.setNameZh("双色球");
		entity.setNameEn("Double Color Ball");
		entity.setPath("/predict/ssq");
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		subMenus.add(entity);

		entity = new MenuEntity();
		entity.setId("PREDICT");
		entity.setSeq(1);
		entity.setNameZh("预测");
		entity.setNameEn("Predict");
		entity.setPath("/predict");
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		entity.setSubMenus(subMenus);
		menuEntities.add(entity);

		menuDao.save(menuEntities);
	}

}
