/**
 * 
 */
package win.caicaikan.repository.mongodb.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import win.caicaikan.repository.mongodb.entity.SysConfigEntity;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月28日
 * @ClassName SysConfigDao
 */
public interface SysConfigDao extends MongoRepository<SysConfigEntity, String> {
	SysConfigEntity findById(String id);
}
