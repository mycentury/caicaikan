/**
 * 
 */
package win.caicaikan.controller;

import java.text.ParseException;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.RequestMapping;

import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.internal.DaoService;
import win.caicaikan.service.internal.DaoService.Condition;
import win.caicaikan.service.rule.RuleService;
import win.caicaikan.util.DateUtil;

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
public class IndexController {
	@Autowired
	private DaoService daoService;
	@Autowired
	protected RuleService ruleService;

	@RequestMapping(value = { "/index" })
	public String index(HttpServletRequest request, ModelMap map) {
		return "direct:/";
	}

	@RequestMapping(value = { "/" })
	public String home(HttpServletRequest request, ModelMap map) {
		Condition condition = new Condition();
		condition.setOrderBy("termNo");
		condition.setLimit(1);
		List<SsqResultEntity> ssqs = daoService.query(condition, SsqResultEntity.class);
		if (!CollectionUtils.isEmpty(ssqs)) {
			SsqResultEntity ssq = ssqs.get(0);
			try {
				String nextTermNoOfSsq = ruleService.getNextTermNoOfSsq(ssq);
				map.put("nextTermNoOfSsq", nextTermNoOfSsq);
				Date nextTermOpenDateOfSsq = ruleService
						.getNextTermOpenDateOfSsq(ssq.getOpenTime());
				map.put("nextTermOpenDateOfSsq", DateUtil._SECOND.format(nextTermOpenDateOfSsq));
			} catch (ParseException e) {
				e.printStackTrace();
			}
			map.put("ssq", ssq);
		}
		return "index";
	}
}
