/**
 * 
 */
package win.caicaikan.repository.mongodb.dao.ssq;

import org.springframework.data.mongodb.repository.MongoRepository;

import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;

/**
 * @Desc
 * @author wewenge.yan
 * @param <T>
 * @Date 2016年9月10日
 * @ClassName BaseDao
 */
public interface SsqResultDao extends MongoRepository<SsqResultEntity, String> {
	SsqResultEntity findById(String id);
}
