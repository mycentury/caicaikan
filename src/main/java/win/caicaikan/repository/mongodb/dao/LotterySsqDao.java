/**
 * 
 */
package win.caicaikan.repository.mongodb.dao;

import org.springframework.data.mongodb.repository.MongoRepository;

import win.caicaikan.repository.mongodb.entity.LotterySsqEntity;

/**
 * @Desc
 * @author wewenge.yan
 * @param <T>
 * @Date 2016年9月10日
 * @ClassName BaseDao
 */
public interface LotterySsqDao extends MongoRepository<LotterySsqEntity, String> {

}
