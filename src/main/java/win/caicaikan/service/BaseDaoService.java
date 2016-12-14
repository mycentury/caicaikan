/**
 * 
 */
package win.caicaikan.service;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.util.CollectionUtils;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月14日
 * @ClassName BaseDaoService
 */
public abstract class BaseDaoService {
	protected Query assembleQuery(GeneralQuery generalQuery) {
		Query query = new Query();
		if (generalQuery == null) {
			return query;
		}
		List<String> params = generalQuery.getParams();
		Criteria criteria = null;
		if (CollectionUtils.isEmpty(params)) {
			for (int i = 0; i < params.size(); i++) {
				String param = params.get(i);
				if (param == null) {
					continue;
				}
				String[] split = param.split(" ");
				if (i == 0) {
					criteria = Criteria.where(split[0]).is(split[2]);
				} else {
					criteria = criteria.and(split[0]).is(split[2]);
				}
			}
		}
		query.addCriteria(criteria);
		return query;
	}

	@Data
	@EqualsAndHashCode(callSuper = false)
	public static class GeneralQuery {
		private List<String> params;
		private String orderBy;
		private String order = "ASC";
		private int limit = 0;
	}
}
