package win.caicaikan.controller.admin;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import win.caicaikan.annotation.Role;
import win.caicaikan.api.res.Result;
import win.caicaikan.constant.ExecuteStatus;
import win.caicaikan.constant.ResultType;
import win.caicaikan.constant.RoleType;
import win.caicaikan.constant.RuleType;
import win.caicaikan.repository.mongodb.entity.PredictRuleEntity;
import win.caicaikan.service.internal.DaoService;
import win.caicaikan.service.internal.DaoService.Condition;
import win.caicaikan.service.internal.InitService;
import win.caicaikan.service.internal.RecordService;
import win.caicaikan.service.internal.SessionService;
import win.caicaikan.util.GsonUtil;

import com.google.gson.reflect.TypeToken;

@Controller
@RequestMapping("admin")
public class AdminRuleController {
	private final static Logger logger = Logger.getLogger(AdminRuleController.class);

	@Autowired
	private RecordService recordService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private InitService initService;
	@Autowired
	private DaoService daoService;

	@RequestMapping("rule")
	@Role({ RoleType.ADMIN })
	public String index(HttpServletRequest request, ModelMap map) {
		if (!sessionService.hasLogin(request)) {
			return "redirect:/admin/login";
		}
		if (!sessionService.isAdmin(request)) {
			return "redirect:/user/login";
		}
		Condition condition = new Condition();
		List<PredictRuleEntity> rules = daoService.query(condition, PredictRuleEntity.class);
		List<PredictRuleEntity> displayRules = new ArrayList<PredictRuleEntity>();
		List<PredictRuleEntity> skipRules = new ArrayList<PredictRuleEntity>();
		List<PredictRuleEntity> doubleRules = new ArrayList<PredictRuleEntity>();
		List<PredictRuleEntity> multiRules = new ArrayList<PredictRuleEntity>();
		for (PredictRuleEntity predictRuleEntity : rules) {
			if (RuleType.DISPLAY_TIMES.name().equals(predictRuleEntity.getRuleType())) {
				displayRules.add(predictRuleEntity);
			} else if (RuleType.SKIP_TIMES.name().equals(predictRuleEntity.getRuleType())) {
				skipRules.add(predictRuleEntity);
			} else if (RuleType.DOUBLE_TIMES.name().equals(predictRuleEntity.getRuleType())) {
				doubleRules.add(predictRuleEntity);
			} else if (RuleType.MULTI.name().equals(predictRuleEntity.getRuleType())) {
				multiRules.add(predictRuleEntity);
			} else {
				logger.error("无效规则：" + predictRuleEntity);
			}
		}
		map.put("displayRules", displayRules);
		map.put("skipRules", skipRules);
		map.put("doubleRules", doubleRules);
		map.put("multiRules", multiRules);
		String tab = request.getParameter("tab");
		if (StringUtils.isEmpty(tab)) {
			tab = "panel-1";
		}
		map.put("tab", tab);
		return "admin/rule";
	}

	@RequestMapping("save_rule")
	@Role({ RoleType.ADMIN })
	public @ResponseBody Result<PredictRuleEntity> saveRule(HttpServletRequest request,
			PredictRuleEntity rule, ModelMap map) {
		Result<PredictRuleEntity> result = new Result<PredictRuleEntity>();
		if (!sessionService.hasLogin(request)) {
			result.setResultStatusAndMsg(ResultType.NOT_LOGIN, null);
			return result;
		}
		if (!sessionService.isAdmin(request)) {
			result.setResultStatusAndMsg(ResultType.NO_AUTHORITY, null);
			return result;
		}
		if (StringUtils.isEmpty(rule.getLotteryType()) || StringUtils.isEmpty(rule.getRuleType())
				|| StringUtils.isEmpty(rule.getTerms())) {
			result.setResultStatusAndMsg(ResultType.PARAMETER_ERROR, null);
			return result;
		}
		if (RuleType.MULTI.name().equals(rule.getRuleType())) {
			if (StringUtils.isEmpty(rule.getRuleAndweights())) {
				result.setResultStatusAndMsg(ResultType.PARAMETER_ERROR, "规则ID和权值mapping不能为空");
				return result;
			}

		}
		rule.setCreateTime(new Date());
		daoService.save(rule);
		StringBuilder message = new StringBuilder("保存成功！");
		try {
			if (RuleType.MULTI.name().equals(rule.getRuleType())) {
				Type typeOfT = new TypeToken<Map<String, Integer>>() {
				}.getType();
				Map<String, Integer> ruleAndweights = GsonUtil.fromJson(rule.getRuleAndweights(),
						typeOfT);

				for (Entry<String, Integer> entry : ruleAndweights.entrySet()) {
					if (entry.getValue() != null && entry.getValue() != 0) {
						PredictRuleEntity baseRule = new PredictRuleEntity();
						baseRule.setPrimaryKey(rule.getLotteryType(), entry.getKey(),
								rule.getTerms());
						if (daoService.queryById(baseRule.getId(), PredictRuleEntity.class) != null) {
							continue;
						}
						baseRule.setRuleName("generate by " + rule.getId());
						baseRule.setStatus(1);
						baseRule.setExecuteStatus(ExecuteStatus.SUCCESS.name());
						baseRule.setCreateTime(new Date());
						daoService.insert(baseRule);
						message.append("附增规则：" + baseRule.getId()).append("。");
					}

				}
			}
		} catch (Exception e) {
			logger.error("附增基础规则产生失败！");
			message.append("附增基础规则产生失败！");
		}
		result.setResultStatusAndMsg(ResultType.SUCCESS, message.toString());
		return result;
	}

	@RequestMapping("delete_rule")
	@Role({ RoleType.ADMIN })
	public @ResponseBody Result<Boolean> deleteRule(HttpServletRequest request,
			PredictRuleEntity rule, ModelMap map) {
		Result<Boolean> result = new Result<Boolean>();
		if (!sessionService.hasLogin(request)) {
			result.setResultStatusAndMsg(ResultType.NOT_LOGIN, null);
			return result;
		}
		if (!sessionService.isAdmin(request)) {
			result.setResultStatusAndMsg(ResultType.NO_AUTHORITY, null);
			return result;
		}
		if (StringUtils.isEmpty(rule.getId())) {
			result.setResultStatusAndMsg(ResultType.PARAMETER_ERROR, null);
			return result;
		}
		daoService.delete(rule);
		result.setResultStatusAndMsg(ResultType.SUCCESS, "删除成功！");
		return result;
	}
}
