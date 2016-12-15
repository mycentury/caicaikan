package win.caicaikan.service.internal;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

import win.caicaikan.repository.mongodb.dao.ssq.SsqResultDao;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;

@Service
public class SsqResultService extends DaoService {
	@Autowired
	private MongoTemplate mongoTemplate;
	@Autowired
	private SsqResultDao ssqResultDao;

	public SsqResultEntity insert(SsqResultEntity ssqResultEntity) {
		Date createTime = new Date();
		ssqResultEntity.setCreateTime(createTime);
		ssqResultEntity.setUpdateTime(createTime);
		return ssqResultDao.insert(ssqResultEntity);
	}

	public SsqResultEntity save(SsqResultEntity ssqResultEntity) {
		ssqResultEntity.setUpdateTime(new Date());
		return ssqResultDao.save(ssqResultEntity);
	}

	public boolean exists(String primaryKey) {
		return ssqResultDao.exists(primaryKey);
	}

}
