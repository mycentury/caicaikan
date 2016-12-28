/**
 * 
 */
package win.caicaikan.controller;

import java.text.ParseException;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import win.caicaikan.constant.SsqConstant;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.internal.DaoService;
import win.caicaikan.service.internal.DaoService.Condition;
import win.caicaikan.service.rule.RuleService;
import win.caicaikan.service.rule.RuleTemplate.Result;
import win.caicaikan.service.rule.ssq.RuleDisplayTimes;
import win.caicaikan.util.DateUtil;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月16日
 * @ClassName HistoryController
 */
@Controller
@RequestMapping(value = { "history" })
public class HistoryController {
	@Autowired
	private DaoService daoService;
	@Autowired
	private RuleService ruleService;
	@Autowired
	private RuleDisplayTimes displayTimes;

	@RequestMapping(value = { "" })
	public String index(HttpServletRequest request, ModelMap map) {
		return "history/index";
	}

	@RequestMapping(value = { "ssq" })
	public String queryHistoryOfSsq(HttpServletRequest request, ModelMap map) {
		Condition condition = new Condition();
		condition.setOrderBy("termNo");
		condition.setLimit(100);
		List<SsqResultEntity> ssqs = daoService.query(condition, SsqResultEntity.class);
		if (!CollectionUtils.isEmpty(ssqs)) {
			SsqResultEntity ssq = ssqs.get(0);
			try {
				String nextTermNoOfSsq = ruleService.getNextTermNoOfSsq(ssq);
				map.put("nextTermNoOfSsq", nextTermNoOfSsq);
				Date nextTermOpenDateOfSsq = ruleService.getNextTermOpenDateOfSsq(ssq.getOpenTime());
				map.put("nextTermOpenDateOfSsq", DateUtil._SECOND.format(nextTermOpenDateOfSsq));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			map.put("ssq", ssq);
			map.put("redBalls", Arrays.asList(ssq.getRedNumbers().split(",")));
			map.put("blueBalls", Arrays.asList(ssq.getBlueNumbers().split(",")));
		}

		Result countResult = displayTimes.countDisplayTimes(ssqs, 100);
		map.put("redBallLabels", SsqConstant.RED_NUMBERS);
		map.put("blueBallLabels", SsqConstant.BLUE_NUMBERS);
		map.put("countRedBalls", countResult.getRedMap().values());
		map.put("countBlueBalls", countResult.getBlueMap().values());
		return "history/ssq";
	}

	@RequestMapping(value = { "dlt" })
	public String queryHistoryOfDlt(HttpServletRequest request, ModelMap map) {
		map.put("words", "<p>Hello,World!</p>");
		return "history/dlt";
	}
}
