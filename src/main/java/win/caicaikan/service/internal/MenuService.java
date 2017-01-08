/**
 * 
 */
package win.caicaikan.service.internal;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import win.caicaikan.repository.mongodb.entity.MenuEntity;
import win.caicaikan.service.internal.DaoService.Condition;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName HttpService
 */
@Service
public class MenuService {
	@Autowired
	private DaoService daoService;

	private List<MenuEntity> menuEntities = new ArrayList<MenuEntity>();

	public List<MenuEntity> getMenuEntities() {
		if (CollectionUtils.isEmpty(menuEntities)) {
			synchronized (menuEntities) {
				if (CollectionUtils.isEmpty(menuEntities)) {
					menuEntities = daoService.query(new Condition(), MenuEntity.class);
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
		}
		return menuEntities;
	}
}
