/**
 * 
 */
package win.caicaikan.repository.mongodb.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import win.caicaikan.repository.mongodb.entity.UserEntity;

/**
 * @desc
 * @author yanwenge
 * @date 2016年12月10日
 * @class UserDao
 */
public interface UserDao extends MongoRepository<UserEntity, String> {
	UserEntity findById(String id);
}
