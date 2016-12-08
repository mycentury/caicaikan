/**
 * 
 */
package win.caicaikan.repository.mongodb.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import win.caicaikan.repository.mongodb.entity.MenuEntity;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月8日
 * @ClassName MemuDao
 */
public interface MenuDao extends MongoRepository<MenuEntity, String> {
}
