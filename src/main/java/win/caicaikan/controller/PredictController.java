/**
 * 
 */
package win.caicaikan.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import win.caicaikan.repository.mongodb.entity.ssq.SsqPredictEntity;
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
		try {
			String nextTermNo = ruleService.getNextTermNoOfSsq();
			Condition condition = new Condition();
			List<String[]> params = new ArrayList<String[]>();
			String[] param = { "termNo", "=", nextTermNo };
			params.add(param);
			condition.setParams(params);
			daoService.query(condition, SsqPredictEntity.class);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "predict/index";
	}

	@RequestMapping(value = { "ssq" })
	public String queryHistoryOfSsq(HttpServletRequest request, ModelMap map) {
		return "predict/ssq";
	}

	@RequestMapping(value = { "dlt" })
	public String queryHistoryOfDlt(HttpServletRequest request, ModelMap map) {
		return "predict/dlt";
	}
}
