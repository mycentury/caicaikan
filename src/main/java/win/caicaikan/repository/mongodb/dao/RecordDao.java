/**
 * 
 */
package win.caicaikan.repository.mongodb.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import win.caicaikan.repository.mongodb.entity.RecordEntity;

/**
 * @desc
 * @author yanwenge
 * @date 2016年12月10日
 * @class UserDao
 */
public interface RecordDao extends MongoRepository<RecordEntity, String> {
}
