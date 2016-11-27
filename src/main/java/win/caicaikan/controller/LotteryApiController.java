/**
 * 
 */
package win.caicaikan.controller;

import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import win.caicaikan.api.req.LotteryReq;
import win.caicaikan.api.res.Result;
import win.caicaikan.repository.mongodb.entity.LotterySsqEntity;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月25日
 * @ClassName LotterySsqController
 */
@Controller
@RequestMapping("api")
public class LotteryApiController {
	@Autowired
	private MongoTemplate mongoTemplate;

	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Result<List<LotterySsqEntity>> queryLottery(HttpServletRequest request, LotteryReq req) {
		Query query = new Query();
		if (StringUtils.isEmpty(req.getStart()) || StringUtils.isEmpty(req.getEnd())) {
			query = query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "termNo"))).limit(1);
		} else {
			query = new Query().addCriteria(Criteria.where("termNo").gt(req.getStart()).lt(req.getEnd())).with(
					new Sort(new Sort.Order(Sort.Direction.ASC, "termNo")));
		}
		List<LotterySsqEntity> data = mongoTemplate.find(query, LotterySsqEntity.class);
		Result<List<LotterySsqEntity>> result = new Result<List<LotterySsqEntity>>();
		if (CollectionUtils.isEmpty(data)) {
			result.setMessage("empty");
		}else {
			result.setData(data);
			result.setMessage("success");
		}
		return result;
	}
}
