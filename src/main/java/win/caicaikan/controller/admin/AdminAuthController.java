package win.caicaikan.controller.admin;

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
import org.springframework.web.bind.annotation.ResponseBody;

import win.caicaikan.annotation.Role;
import win.caicaikan.api.res.Result;
import win.caicaikan.constant.OperType;
import win.caicaikan.constant.ResultType;
import win.caicaikan.constant.RoleType;
import win.caicaikan.repository.mongodb.entity.RecordEntity;
import win.caicaikan.service.external.AliApiSecurityService;
import win.caicaikan.service.external.ApiConfig;
import win.caicaikan.service.external.domain.AliSecurityQueryReq;
import win.caicaikan.service.external.domain.AliSecurityQueryRes;
import win.caicaikan.service.external.domain.AliSecurityReq;
import win.caicaikan.service.external.domain.AliSecurityRes;
import win.caicaikan.service.internal.DaoService;
import win.caicaikan.service.internal.InitService;
import win.caicaikan.service.internal.RecordService;
import win.caicaikan.service.internal.SessionService;
import win.caicaikan.util.AddressUtil;
import win.caicaikan.util.DateUtil;
import win.caicaikan.util.TypeConverterUtil;

@Controller
@RequestMapping("admin")
public class AdminAuthController {
	private final static Logger logger = Logger.getLogger(AdminAuthController.class);

	@Autowired
	private RecordService recordService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private InitService initService;
	@Autowired
	private AliApiSecurityService aliApiSecurityService;
	@Autowired
	private ApiConfig apiConfig;
	@Autowired
	private DaoService daoService;

	@RequestMapping("auth")
	@Role({ RoleType.ADMIN })
	public String index(HttpServletRequest request, ModelMap map) {
		if (!sessionService.hasLogin(request)) {
			return "redirect:/admin/login";
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
		return "admin/auth";
	}

	@RequestMapping("auth/security")
	@Role({ RoleType.ADMIN })
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
}
