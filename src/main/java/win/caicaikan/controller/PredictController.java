/**
 * 
 */
package win.caicaikan.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import win.caicaikan.repository.mongodb.entity.ssq.SsqPredictEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.internal.DaoService;
import win.caicaikan.service.internal.DaoService.Condition;
import win.caicaikan.service.rule.RuleService;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月16日
 * @ClassName PredictController
 */
@Controller
@RequestMapping(value = { "predict" })
public class PredictController {
	@Autowired
	private DaoService daoService;
	@Autowired
	private RuleService ruleService;

	@RequestMapping(value = { "" })
	public String index(HttpServletRequest request, ModelMap map) {
		return "predict/index";
	}

	@RequestMapping(value = { "ssq" })
	public String queryHistoryOfSsq(HttpServletRequest request, String termNo, ModelMap map) {
		if (StringUtils.isEmpty(termNo)) {
			termNo = ruleService.getNextTermNoOfSsq();
		}
		Condition condition = new Condition();
		List<String[]> params = new ArrayList<String[]>();
		String[] param = { "termNo", termNo };
		params.add(param);
		condition.setParams(params);
		List<SsqPredictEntity> predictEntities = daoService
				.query(condition, SsqPredictEntity.class);

		map.put("predictEntities", predictEntities);

		condition = new Condition();
		condition.setOrderBy("termNo");
		condition.setOrder(Direction.DESC);
		condition.setLimit(10);
		List<SsqResultEntity> ssqResults = daoService.query(condition, SsqResultEntity.class);
		List<String> termNos = new ArrayList<String>();
		for (SsqResultEntity ssqResultEntity : ssqResults) {
			termNos.add(ssqResultEntity.getTermNo());
		}
		map.put("conut", 10);
		map.put("currentTermNo", termNo);
		map.put("termNos", termNos);
		return "predict/ssq";
	}

	@RequestMapping(value = { "dlt" })
	public String queryHistoryOfDlt(HttpServletRequest request, ModelMap map) {
		return "predict/dlt";
	}
}
