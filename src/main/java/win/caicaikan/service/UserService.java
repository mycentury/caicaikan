package win.caicaikan.service;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import win.caicaikan.repository.mongodb.dao.UserDao;
import win.caicaikan.repository.mongodb.entity.UserEntity;

@Service
public class UserService {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private UserDao userDao;

	public UserEntity findById(String id) {
		return userDao.findById(id);
	}

	public UserEntity insert(UserEntity user) {
		user.setPrimaryKey(user.getUsertype(), user.getUsername());
		Date createTime = new Date();
		user.setCreateTime(createTime);
		user.setUpdateTime(createTime);
		return userDao.insert(user);
	}

	public UserEntity save(UserEntity user) {
		user.setUpdateTime(new Date());
		return userDao.save(user);
	}

	/**
	 * @param user
	 * @return
	 */
	public boolean exists(UserEntity user) {
		Criteria criteria = Criteria.where("usertype").is(user.getUsertype());
		if (StringUtils.hasText(user.getUsername())) {
			criteria.and("username").is(user.getUsername());
		}
		if (StringUtils.hasText(user.getPassword())) {
			criteria.and("password").is(user.getPassword());
		}
		Query query = new Query(criteria);
		return mongoTemplate.exists(query, UserEntity.class);
	}

	/**
	 * @param user
	 * @return
	 */
	public List<UserEntity> findByEntity(UserEntity user) {
		Criteria criteria = Criteria.where("1").is("1");
		if (StringUtils.hasText(user.getUsertype())) {
			criteria.and("usertype").is(user.getUsertype());
		}
		if (StringUtils.hasText(user.getUsername())) {
			criteria.and("username").is(user.getUsername());
		}
		if (StringUtils.hasText(user.getPassword())) {
			criteria.and("password").is(user.getPassword());
		}
		if (StringUtils.hasText(user.getLevel())) {
			criteria.and("level").is(user.getLevel());
		}
		if (StringUtils.hasText(user.getStatus())) {
			criteria.and("status").is(user.getStatus());
		}
		Query query = new Query(criteria);
		return mongoTemplate.find(query, UserEntity.class);
	}
}
