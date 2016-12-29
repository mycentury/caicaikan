/**
 * 
 */
package win.caicaikan.service.internal;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;

import win.caicaikan.repository.mongodb.entity.BaseEntity;

/**
 * @Desc 统一处理更新时间和创建时间
 * @author wewenge.yan
 * @Date 2016年12月14日
 * @ClassName BaseDaoService
 */
@Service
public class DaoService {

	@Autowired
	private MongoTemplate mongoTemplate;

	public void insert(BaseEntity entity) {
		Date createTime = new Date();
		entity.setCreateTime(createTime);
		entity.setUpdateTime(createTime);
		mongoTemplate.insert(entity);
	}

	public <T extends BaseEntity> void insert(List<BaseEntity> entityList, Class<T> classOfT) {
		for (BaseEntity entity : entityList) {
			Date createTime = new Date();
			entity.setCreateTime(createTime);
			entity.setUpdateTime(createTime);
		}
		mongoTemplate.insert(entityList, classOfT);
	}

	public void delete(BaseEntity entity) {
		mongoTemplate.remove(entity);
	}

	public void delete(Condition condition, Class<?> entityClass) {
		Query query = this.assembleQuery(condition);
		mongoTemplate.remove(query, entityClass);
	}

	public void save(BaseEntity entity) {
		entity.setUpdateTime(new Date());
		mongoTemplate.save(entity);
	}

	public long count(Condition condition, Class<?> entityClass) {
		Query query = this.assembleQuery(condition);
		return mongoTemplate.count(query, entityClass);
	}

	public boolean exists(Condition condition, Class<?> entityClass) {
		Query query = this.assembleQuery(condition);
		return mongoTemplate.exists(query, entityClass);
	}

	public <T> T queryById(String id, Class<T> entityClass) {
		return mongoTemplate.findById(id, entityClass);
	}

	public <T> List<T> query(Condition condition, Class<T> entityClass) {
		Query query = this.assembleQuery(condition);
		return mongoTemplate.find(query, entityClass);
	}

	protected Query assembleQuery(Condition condition) {
		Query query = new Query();
		if (condition == null) {
			return query;
		}
		Criteria criteria = null;
		if (!CollectionUtils.isEmpty(condition.params)) {
			for (int i = 0; i < condition.params.size(); i++) {
				String[] param = condition.params.get(i);
				if (param == null) {
					continue;
				}
				if (i == 0) {
					criteria = Criteria.where(param[0]).is(param[1]);
				} else {
					criteria = criteria.and(param[0]).is(param[1]);
				}
			}
			query.addCriteria(criteria);
		}
		if (condition.limit > 0) {
			query.limit(condition.limit);
		}
		if (StringUtils.hasText(condition.orderBy) && condition.order != null) {
			Sort sort = new Sort(new Sort.Order(condition.order, condition.orderBy));
			query.with(sort);
		}
		if (condition.pageNo > 1) {
			query.skip((condition.pageNo - 1) * condition.limit);
		}
		return query;
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class Condition {
		private List<String[]> params;
		private String orderBy;
		private Sort.Direction order = Sort.Direction.DESC;
		private int limit = 0;
		private int pageNo = 1;
	}
}
