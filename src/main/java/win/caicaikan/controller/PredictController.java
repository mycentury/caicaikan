/**
 * 
 */
package win.caicaikan.controller;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import win.caicaikan.constant.RuleType;
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
	private String[] names = { "李大头", "刘一天", "钱二毛", "张三拳", "赵四方", "王老五", "郝六" };

	@RequestMapping(value = { "" })
	public String index(HttpServletRequest request, ModelMap map) {
		return "predict/index";
	}

	@RequestMapping(value = { "ssq" })
	public String queryHistoryOfSsq(HttpServletRequest request, ModelMap map) {
		try {
			Map<String, String> nameMap = new HashMap<String, String>();
			nameMap.put(RuleType.DISPLAY_TIMES.name(), "刘一天");
			nameMap.put(RuleType.SKIP_TIMES.name(), "钱二毛");
			nameMap.put(RuleType.MULTI.name(), "李大头");
			String nextTermNo = ruleService.getNextTermNoOfSsq();
			Condition condition = new Condition();
			List<String[]> params = new ArrayList<String[]>();
			String[] param = { "termNo", nextTermNo };
			params.add(param);
			condition.setParams(params);
			List<SsqPredictEntity> predictEntities = daoService.query(condition, SsqPredictEntity.class);
			for (int i = 0; i < predictEntities.size(); i++) {
				SsqPredictEntity ssqPredictEntity = predictEntities.get(i);
				ssqPredictEntity.setRuleId(names[i % names.length]);
			}
			map.put("predictEntities", predictEntities);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return "predict/ssq";
	}

	@RequestMapping(value = { "dlt" })
	public String queryHistoryOfDlt(HttpServletRequest request, ModelMap map) {
		return "predict/dlt";
	}
}
