/**
 * 
 */
package win.caicaikan.service.internal;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.annotation.Id;
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
	private static final Logger logger = Logger.getLogger(DaoService.class);

	@Autowired
	private MongoTemplate mongoTemplate;

	public <T extends BaseEntity> void insert(T entity) {
		try {
			Date now = new Date();
			entity.setCreateTime(now);
			entity.setUpdateTime(now);
			mongoTemplate.insert(entity);
		} catch (Exception e) {
			logger.error("insert:" + entity);
			throw new RuntimeException(e);
		}
	}

	public <T extends BaseEntity> void insert(List<T> entityList, Class<T> classOfT) {
		try {
			for (BaseEntity entity : entityList) {
				Date createTime = new Date();
				entity.setCreateTime(createTime);
				entity.setUpdateTime(createTime);
			}
			mongoTemplate.insert(entityList, classOfT);
		} catch (Exception e) {
			logger.error("insert:" + entityList);
			throw new RuntimeException(e);
		}
	}

	public <T extends BaseEntity> void delete(T entity) {
		try {
			mongoTemplate.remove(entity);
		} catch (Exception e) {
			logger.error("delete:" + entity);
			throw new RuntimeException(e);
		}
	}

	public void delete(Condition condition, Class<?> entityClass) {
		try {
			Query query = this.assembleQuery(condition);
			mongoTemplate.remove(query, entityClass);
		} catch (Exception e) {
			logger.error("delete:" + condition + ",class=" + entityClass.getSimpleName());
			throw new RuntimeException(e);
		}
	}

	public <T extends BaseEntity> void save(T entity) {
		try {
			entity.setUpdateTime(new Date());
			if (!existsById(entity.getId(), entity.getClass())) {
				entity.setCreateTime(new Date());
			}
			mongoTemplate.save(entity);
		} catch (Exception e) {
			logger.error("save:" + entity);
			throw new RuntimeException(e);
		}
	}

	public <T extends BaseEntity> void save(List<T> entityList) {
		for (T entity : entityList) {
			this.save(entity);
		}
	}

	public long count(Condition condition, Class<?> entityClass) {
		try {
			Query query = this.assembleQuery(condition);
			return mongoTemplate.count(query, entityClass);
		} catch (Exception e) {
			logger.error("count:" + condition + ",class=" + entityClass.getSimpleName());
			throw new RuntimeException(e);
		}
	}

	public boolean exists(Condition condition, Class<?> entityClass) {
		try {
			Query query = this.assembleQuery(condition);
			return mongoTemplate.exists(query, entityClass);
		} catch (Exception e) {
			logger.error("exists:" + condition + ",class=" + entityClass.getSimpleName());
			throw new RuntimeException(e);
		}
	}

	public <T> boolean existsById(String id, Class<T> entityClass) {
		try {
			Condition condition = new Condition();
			condition.addParam(this.getIdColName(entityClass), "=", id);
			return this.exists(condition, entityClass);
		} catch (Exception e) {
			logger.error("existsById:" + id + ",class=" + entityClass.getSimpleName());
			throw new RuntimeException(e);
		}
	}

	public <T> T queryById(String id, Class<T> entityClass) {
		try {
			return mongoTemplate.findById(id, entityClass);
		} catch (Exception e) {
			logger.error("queryById:" + id + ",class=" + entityClass.getSimpleName());
			throw new RuntimeException(e);
		}
	}

	public <T> List<T> query(Condition condition, Class<T> entityClass) {
		try {
			Query query = this.assembleQuery(condition);
			return mongoTemplate.find(query, entityClass);
		} catch (Exception e) {
			logger.error("query:" + condition + ",class=" + entityClass.getSimpleName());
			throw new RuntimeException(e);
		}
	}

	protected Query assembleQuery(Condition condition) {
		Query query = new Query();
		if (condition == null) {
			return query;
		}
		Criteria criteria = null;
		if (!CollectionUtils.isEmpty(condition.params)) {
			boolean isFirst = true;
			for (String[] param : condition.params) {
				if (param == null) {
					continue;
				}
				if (param.length == 2) {
					param = new String[] { param[0], "=", param[1] };
				}
				if (isFirst) {
					criteria = Criteria.where(param[0]);
					isFirst = false;
				} else {
					criteria = criteria.and(param[0]);
				}
				if (param.length == 3) {
					if ("=".equals(param[1])) {
						criteria = criteria.is(param[2]);
					} else if ("<=".equals(param[1])) {
						criteria = criteria.lte(param[2]);
					} else if (">=".equals(param[1])) {
						criteria = criteria.gte(param[2]);
					} else if ("<".equals(param[1])) {
						criteria = criteria.lt(param[2]);
					} else if (">".equals(param[1])) {
						criteria = criteria.gt(param[2]);
					} else {
						throw new RuntimeException("参数不合法：" + Arrays.toString(param));
					}
				} else {
					throw new RuntimeException("参数不合法：" + Arrays.toString(param));
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
		if (condition.limit > 0 && condition.pageNo > 1) {
			query.skip((condition.pageNo - 1) * condition.limit);
		}
		return query;
	}

	public <T> String getIdColName(Class<T> entityClass) {
		while (!entityClass.equals(Object.class)) {
			Field fields[] = entityClass.getDeclaredFields();
			for (Field field : fields) {
				Id[] annotations = field.getAnnotationsByType(Id.class);
				if (annotations != null && annotations.length == 1) {
					return field.getName();
				}
			}
		}
		throw new RuntimeException("无ID字段");
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class Condition {
		private List<String[]> params;
		private String orderBy;
		private Sort.Direction order = Sort.Direction.DESC;
		private int limit = 0;
		private int pageNo = 1;

		public void addParam(String column, String oper, String value) {
			String[] param = { column, oper, value };
			if (CollectionUtils.isEmpty(params)) {
				params = new ArrayList<String[]>();
			}
			params.add(param);
		}
	}
}
