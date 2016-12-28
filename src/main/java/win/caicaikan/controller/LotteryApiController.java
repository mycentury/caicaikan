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
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import win.caicaikan.api.req.LotteryReq;
import win.caicaikan.api.res.Result;
import win.caicaikan.constant.ResultType;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月25日
 * @ClassName LotterySsqController
 */
/**
 * @author yanwenge
 */
@Controller
@RequestMapping("api")
public class LotteryApiController {
	@Autowired
	private MongoTemplate mongoTemplate;

	/**
	 * http://localhost:8280/api/query?start=2016130&end=2016140
	 * 
	 * @param request
	 * @param req
	 * @return
	 */
	@RequestMapping(value = "query", method = { RequestMethod.GET, RequestMethod.POST })
	public @ResponseBody Result<List<SsqResultEntity>> queryLottery(HttpServletRequest request, LotteryReq req) {
		Result<List<SsqResultEntity>> result = new Result<List<SsqResultEntity>>();
		try {
			Query query = new Query();
			if (StringUtils.isEmpty(req.getStart()) || StringUtils.isEmpty(req.getEnd())) {
				query = query.with(new Sort(new Sort.Order(Sort.Direction.DESC, "termNo"))).limit(1);
			} else {
				query = new Query().addCriteria(Criteria.where("termNo").gt(req.getStart()).lt(req.getEnd())).with(
						new Sort(new Sort.Order(Sort.Direction.ASC, "termNo")));
			}
			List<SsqResultEntity> data = mongoTemplate.find(query, SsqResultEntity.class);
			result.setResultStatusAndMsg(ResultType.SUCCESS, null);
			result.setData(data);
		} catch (Exception e) {
			result.setResultStatusAndMsg(ResultType.API_ERROR, null);
		}
		return result;
	}
}
