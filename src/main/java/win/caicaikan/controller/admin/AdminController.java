/**
 * 
 */
package win.caicaikan.controller.admin;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import win.caicaikan.constant.OperType;
import win.caicaikan.constant.RoleType;
import win.caicaikan.repository.mongodb.entity.RecordEntity;
import win.caicaikan.repository.mongodb.entity.UserEntity;
import win.caicaikan.service.external.AliApiSecurityService;
import win.caicaikan.service.external.ApiConfig;
import win.caicaikan.service.internal.DaoService;
import win.caicaikan.service.internal.DaoService.Condition;
import win.caicaikan.service.internal.InitService;
import win.caicaikan.service.internal.RecordService;
import win.caicaikan.service.internal.SessionService;
import win.caicaikan.util.AddressUtil;

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
	private RecordService recordService;
	@Autowired
	private AliApiSecurityService aliApiSecurityService;
	@Autowired
	private ApiConfig apiConfig;
	@Autowired
	private DaoService daoService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private InitService initService;

	@RequestMapping("login")
	public String login(HttpServletRequest request, ModelMap map) {
		if (sessionService.hasLogin(request)) {
			return "redirect:/";
		}
		map.put("usertype", RoleType.ADMIN.getCode());
		String errorMsg = request.getParameter("errorMsg");

		if (!StringUtils.isEmpty(errorMsg)) {
			map.put("errorMsg", errorMsg);
		}
		return "admin/login";
	}

	@RequestMapping(value = "admin_login", method = { RequestMethod.POST })
	public String adminLogin(HttpServletRequest request, UserEntity user, String checkcode,
			RedirectAttributes attr) {
		RecordEntity record = recordService.assembleRocordEntity(request);
		attr.addFlashAttribute("user", user);
		record.setOpertype(OperType.LOGIN.name());
		if (!checkcode.equalsIgnoreCase(sessionService.getCheckcode(request))) {
			attr.addFlashAttribute("errorMsg", "验证码错误！");
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
		Condition condition = new Condition();
		condition.addParam("usertype", "=", RoleType.ADMIN.getCode());
		condition.addParam("username", "=", user.getUsername());
		condition.addParam("password", "=", user.getPassword());
		boolean exists = daoService.exists(condition, UserEntity.class);
		if (!exists) {
			attr.addFlashAttribute("errorMsg", "username or password error");
			record.setAfter(user.getUsername() + "/" + user.getPassword()
					+ "-username or password error");
			recordService.insert(record);
			return "redirect:/admin/login";
		}
		sessionService.setUsername(request, user.getUsername());
		sessionService.setUsertype(request, user.getUsertype());
		record.setAfter(user.getUsername() + "-login successfully！");
		recordService.insert(record);
		return "redirect:/admin/init";
	}

	@RequestMapping(value = "logout")
	public String logout(HttpServletRequest request) {
		sessionService.removeUsername(request);
		sessionService.removeUsertype(request);
		return "redirect:/admin/login";

	}
}
