/**
 * 
 */
package win.caicaikan.controller;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.CollectionUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import win.caicaikan.annotation.Role;
import win.caicaikan.api.res.Result;
import win.caicaikan.constant.OperType;
import win.caicaikan.constant.ResultType;
import win.caicaikan.constant.RoleType;
import win.caicaikan.repository.mongodb.entity.PredictRuleEntity;
import win.caicaikan.repository.mongodb.entity.RecordEntity;
import win.caicaikan.repository.mongodb.entity.UserEntity;
import win.caicaikan.service.external.AliApiSecurityService;
import win.caicaikan.service.external.ApiConfig;
import win.caicaikan.service.external.domain.AliSecurityQueryReq;
import win.caicaikan.service.external.domain.AliSecurityQueryRes;
import win.caicaikan.service.external.domain.AliSecurityReq;
import win.caicaikan.service.external.domain.AliSecurityRes;
import win.caicaikan.service.internal.DaoService;
import win.caicaikan.service.internal.DaoService.Condition;
import win.caicaikan.service.internal.RecordService;
import win.caicaikan.service.internal.SessionService;
import win.caicaikan.service.internal.UserService;
import win.caicaikan.util.AddressUtil;
import win.caicaikan.util.DateUtil;
import win.caicaikan.util.TypeConverterUtil;

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
@RequestMapping("admin")
public class AdminController {
	private final static Logger logger = Logger.getLogger(AdminController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private RecordService recordService;
	@Autowired
	private AliApiSecurityService aliApiSecurityService;
	@Autowired
	private ApiConfig apiConfig;
	@Autowired
	private DaoService daoService;
	@Autowired
	private SessionService sessionService;

	@RequestMapping("login")
	public String login(HttpServletRequest request, ModelMap map) {
		if (sessionService.hasLogin(request)) {
			return "redirect:/";
		}
		map.put("usertype", RoleType.ADMIN.getCode());
		return "admin/login";
	}

	@RequestMapping(value = "admin_login", method = { RequestMethod.POST })
	public String adminLogin(HttpServletRequest request, UserEntity user, String checkcode,
			RedirectAttributes attr) {
		RecordEntity record = recordService.assembleRocordEntity(request);
		record.setOpertype(OperType.LOGIN.name());
		if (!checkcode.equalsIgnoreCase(sessionService.getCheckcode(request))) {
			attr.addFlashAttribute("errorMsg", "验证码错误！");
			attr.addFlashAttribute("usertype", RoleType.ADMIN.getCode());
			record.setAfter(checkcode + "!=" + sessionService.getCheckcode(request) + "-验证码错误！");
			recordService.insert(record);
			return "redirect:/admin/login";
		}
		if (!RoleType.ADMIN.getCode().equals(user.getUsertype())) {
			RecordEntity record2 = recordService.assembleRocordEntity(request);
			record2.setOpertype(OperType.ATTACK.name());
			record2.setAfter(user.getUsertype() + "-用户类型被调试修改！");
			recordService.insert(record2);
			user.setUsertype(RoleType.ADMIN.getCode());
			logger.error("检测到调试攻击，IP=" + AddressUtil.getIpAddress(request));
		}
		if (!userService.exists(user)) {
			attr.addFlashAttribute("errorMsg", "username or password error");
			attr.addFlashAttribute("usertype", RoleType.ADMIN.getCode());
			record.setAfter(user.getUsername() + "/" + user.getPassword()
					+ "-username or password error");
			recordService.insert(record);
			return "redirect:/admin/login";
		}
		sessionService.setUsername(request, user.getUsername());
		sessionService.setUsertype(request, user.getUsertype());
		record.setAfter(user.getUsername() + "-login successfully！");
		recordService.insert(record);
		return "redirect:/admin/console";
	}

	@RequestMapping(value = "logout")
	public String logout(HttpServletRequest request) {
		sessionService.removeUsername(request);
		sessionService.removeUsertype(request);
		return "redirect:/admin/login";

	}

	@RequestMapping("console")
	public String console(HttpServletRequest request, ModelMap map) {
		if (!sessionService.hasLogin(request)) {
			return "redirect:/admin/login";
		}
		if (!sessionService.isAdmin(request)) {
			return "redirect:/user/login";
		}
		AliSecurityQueryReq req = TypeConverterUtil.map(apiConfig, AliSecurityQueryReq.class);
		req.setSignatureNonce(UUID.randomUUID().toString());
		Date timeByTimeZone = DateUtil.getUtcTime();
		req.setTimestamp(DateUtil.toChar(timeByTimeZone, DateUtil.ISO8601));
		AliSecurityQueryRes internets = aliApiSecurityService.querySecurityRules(req);

		String ipAddress = AddressUtil.getIpAddress(request);
		boolean hasAuthorized = false;

		if (internets != null && internets.getPermissions() != null
				&& !CollectionUtils.isEmpty(internets.getPermissions().getPermission())) {
			List<AliSecurityQueryRes.Permission> internetIns = new ArrayList<AliSecurityQueryRes.Permission>();
			List<AliSecurityQueryRes.Permission> internetOuts = new ArrayList<AliSecurityQueryRes.Permission>();
			for (AliSecurityQueryRes.Permission permission : internets.getPermissions()
					.getPermission()) {
				if ("ingress".equals(permission.getDirection())) {
					internetIns.add(permission);
					if (!hasAuthorized && permission.getSourceCidrIp().equals(ipAddress)) {
						hasAuthorized = true;
						map.put("hasAuthorized", hasAuthorized);
					}
				} else if ("egress".equals(permission.getDirection())) {
					internetOuts.add(permission);
				}
			}
			map.put("internetIns", internetIns);
			map.put("internetOuts", internetOuts);
		}
		req.setSignatureNonce(UUID.randomUUID().toString());
		timeByTimeZone = DateUtil.getUtcTime();
		req.setTimestamp(DateUtil.toChar(timeByTimeZone, DateUtil.ISO8601));
		req.setNicType("intranet");
		AliSecurityQueryRes intranets = aliApiSecurityService.querySecurityRules(req);

		if (intranets != null && intranets.getPermissions() != null
				&& !CollectionUtils.isEmpty(intranets.getPermissions().getPermission())) {
			List<AliSecurityQueryRes.Permission> intranetIns = new ArrayList<AliSecurityQueryRes.Permission>();
			List<AliSecurityQueryRes.Permission> intranetOuts = new ArrayList<AliSecurityQueryRes.Permission>();
			for (AliSecurityQueryRes.Permission permission : intranets.getPermissions()
					.getPermission()) {
				if ("ingress".equals(permission.getDirection())) {
					intranetIns.add(permission);
				} else if ("egress".equals(permission.getDirection())) {
					intranetOuts.add(permission);
				}
			}
			map.put("intranetIns", intranetIns);
			map.put("intranetOuts", intranetOuts);
		}
		return "admin/console";
	}

	@RequestMapping("security")
	public @ResponseBody Result<Boolean> authorize(HttpServletRequest request, String action) {
		Result<Boolean> result = new Result<Boolean>();
		if (StringUtils.isEmpty(action)) {
			result.setResultStatusAndMsg(ResultType.PARAMETER_ERROR, "No Action Providered!");
			result.setData(false);
			return result;
		}
		try {
			AliSecurityReq req = TypeConverterUtil.map(apiConfig, AliSecurityReq.class);
			req.setAction(action);
			req.setIpProtocol("all");
			req.setPortRange("-1/-1");
			String ipAddress = AddressUtil.getIpAddress(request);
			req.setSourceCidrIp(ipAddress);
			req.setSignatureNonce(UUID.randomUUID().toString());
			req.setNicType("internet");
			Date timeByTimeZone = DateUtil.getUtcTime();
			req.setTimestamp(DateUtil.toChar(timeByTimeZone, DateUtil.ISO8601));
			AliSecurityRes res = aliApiSecurityService.updateSecurityRule(req);
			if (res != null && res.getHostId() == null & res.getCode() == null
					&& res.getMessage() == null) {
				result.setResultStatusAndMsg(ResultType.SUCCESS, null);
				result.setData(true);
			} else {
				result.setResultStatusAndMsg(ResultType.API_ERROR, res.getMessage());
			}
		} catch (Exception e) {
			result.setResultStatusAndMsg(ResultType.SERVICE_ERROR, null);
			result.setData(false);
			e.printStackTrace();
		}

		try {
			RecordEntity record = recordService.assembleRocordEntity(request);
			record.setOpertype(OperType.AUTHORIZE.name());
			record.setAfter("action=" + action + ",result=" + result);
			recordService.insert(record);
		} catch (Exception e) {
		}
		return result;
	}

	@RequestMapping("rule")
	@Role()
	public String rule(HttpServletRequest request, ModelMap map) {
		if (!sessionService.hasLogin(request)) {
			return "redirect:/admin/login";
		}
		if (!sessionService.isAdmin(request)) {
			return "redirect:/user/login";
		}
		Condition condition = new Condition();
		List<PredictRuleEntity> rules = daoService.query(condition, PredictRuleEntity.class);
		map.put("rules", rules);
		return "admin/rule";
	}

	@RequestMapping("save_rule")
	@Role({ RoleType.ADMIN })
	public @ResponseBody Result<PredictRuleEntity> saveRule(HttpServletRequest request,
			PredictRuleEntity entity, ModelMap map) {
		Result<PredictRuleEntity> result = new Result<PredictRuleEntity>();
		if (!sessionService.hasLogin(request)) {
			result.setResultStatusAndMsg(ResultType.NOT_LOGIN, null);
			return result;
		}
		if (!sessionService.isAdmin(request)) {
			result.setResultStatusAndMsg(ResultType.NO_AUTHORITY, null);
			return result;
		}
		if (StringUtils.isEmpty(entity.getLotteryType())
				|| StringUtils.isEmpty(entity.getRuleType())
				|| StringUtils.isEmpty(entity.getTerms())) {
			result.setResultStatusAndMsg(ResultType.PARAMETER_ERROR, null);
			return result;
		}
		entity.setPrimaryKey(entity.getLotteryType(), entity.getRuleType(), entity.getTerms());
		daoService.save(entity);
		return result;
	}
}
