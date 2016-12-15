/**
 * 
 */
package win.caicaikan.service.internal;

import java.util.Date;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import win.caicaikan.repository.mongodb.entity.BaseEntity;

/**
 * @Desc
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

	public void delete(BaseEntity entity) {
		mongoTemplate.remove(entity);
	}

	public void delete(GeneralQuery generalQuery, Class<?> entityClass) {
		Query query = this.assembleQuery(generalQuery);
		mongoTemplate.remove(query, entityClass);
	}

	public void save(BaseEntity entity) {
		entity.setUpdateTime(new Date());
		mongoTemplate.save(entity);
	}

	public long count(GeneralQuery generalQuery, Class<?> entityClass) {
		Query query = this.assembleQuery(generalQuery);
		return mongoTemplate.count(query, entityClass);
	}

	public boolean exists(GeneralQuery generalQuery, Class<?> entityClass) {
		Query query = this.assembleQuery(generalQuery);
		return mongoTemplate.exists(query, entityClass);
	}

	public List<?> query(GeneralQuery generalQuery, Class<?> entityClass) {
		Query query = this.assembleQuery(generalQuery);
		return mongoTemplate.find(query, entityClass);
	}

	protected Query assembleQuery(GeneralQuery generalQuery) {
		Query query = new Query();
		if (generalQuery == null) {
			return query;
		}
		List<String[]> params = generalQuery.getParams();
		Criteria criteria = null;
		if (CollectionUtils.isEmpty(params)) {
			for (int i = 0; i < params.size(); i++) {
				String[] param = params.get(i);
				if (param == null) {
					continue;
				}
				if (i == 0) {
					criteria = Criteria.where(param[0]).is(param[1]);
				} else {
					criteria = criteria.and(param[0]).is(param[1]);
				}
			}
		}
		query.addCriteria(criteria);
		return query;
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class GeneralQuery {
		private List<String[]> params;
		private String orderBy;
		private String order = "ASC";
		private int limit = 0;
	}
}
