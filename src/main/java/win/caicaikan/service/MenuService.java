/**
 * 
 */
package win.caicaikan.service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import win.caicaikan.repository.mongodb.dao.MenuDao;
import win.caicaikan.repository.mongodb.entity.MenuEntity;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName HttpService
 */
@Service
public class MenuService {
	@Autowired
	private MenuDao menuDao;

	private List<MenuEntity> menuEntities;

	public List<MenuEntity> getMenuEntities() {
		if (CollectionUtils.isEmpty(menuEntities)) {
			synchronized (menuEntities) {
				if (CollectionUtils.isEmpty(menuEntities)) {
					menuEntities = menuDao.findAll();
				}
			}
		}
		return menuEntities;
	}

	/**
	 * 模拟先用
	 * 
	 * @return
	 */
	public List<MenuEntity> initMenuEntities() {
		if (CollectionUtils.isEmpty(menuEntities)) {
			menuEntities = new ArrayList<MenuEntity>();
			MenuEntity entity = new MenuEntity();
			entity.setPrimaryKey("H", 0, "0");
			entity.setNameZh("主页");
			entity.setNameEn("Home");
			entity.setPath("/index");
			entity.setCreateTime(new Date());
			entity.setUpdateTime(new Date());
			menuEntities.add(entity);

			entity = new MenuEntity();
			entity.setPrimaryKey("H", 1, "0");
			entity.setNameZh("双色球");
			entity.setNameEn("Double Color Ball");
			entity.setPath("/history/ssq");
			entity.setCreateTime(new Date());
			entity.setUpdateTime(new Date());
			menuEntities.add(entity);

			entity = new MenuEntity();
			entity.setPrimaryKey("H", 1, "1");
			entity.setNameZh("往期数据");
			entity.setNameEn("Super Lotto");
			entity.setPath("/history/dlt");
			entity.setCreateTime(new Date());
			entity.setUpdateTime(new Date());
			menuEntities.add(entity);

			entity = new MenuEntity();
			entity.setPrimaryKey("H", 0, "1");
			entity.setNameZh("往期数据");
			entity.setNameEn("History");
			entity.setPath("/history");
			entity.setCreateTime(new Date());
			entity.setUpdateTime(new Date());
			menuEntities.add(entity);
		}
		return menuEntities;
	}
}