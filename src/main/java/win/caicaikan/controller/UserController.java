/**
 * 
 */
package win.caicaikan.controller;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import win.caicaikan.api.res.Result;
import win.caicaikan.constant.OperType;
import win.caicaikan.constant.ResultType;
import win.caicaikan.constant.RoleType;
import win.caicaikan.repository.mongodb.entity.RecordEntity;
import win.caicaikan.repository.mongodb.entity.UserEntity;
import win.caicaikan.service.internal.DaoService;
import win.caicaikan.service.internal.DaoService.Condition;
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
@RequestMapping("user")
public class UserController {
	private final static Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private RecordService recordService;
	@Autowired
	private SessionService sessionService;
	@Autowired
	private DaoService daoService;

	@RequestMapping("login")
	public String login(HttpServletRequest request, ModelMap map) {
		if (sessionService.hasLogin(request)) {
			return "redirect:/";
		}
		map.put("usertype", RoleType.USER.getCode());
		String errorMsg = request.getParameter("errorMsg");
		if (!StringUtils.isEmpty(errorMsg)) {
			map.put("errorMsg", errorMsg);
		}
		return "user/login";
	}

	@RequestMapping(value = "user_login", method = { RequestMethod.POST })
	public String userLogin(HttpServletRequest request, UserEntity user, String checkcode,
			RedirectAttributes attr) {
		attr.addFlashAttribute("user", user);
		RecordEntity record = recordService.assembleRocordEntity(request);
		record.setOpertype(OperType.LOGIN.name());
		if (StringUtils.isEmpty(record.getUsername())) {
			record.setUsername(user.getUsername());
			record.setUsertype(user.getUsertype());
		}
		if (!checkcode.equalsIgnoreCase(sessionService.getCheckcode(request))) {
			attr.addFlashAttribute("errorMsg", "验证码错误！");
			record.setAfter(checkcode + "-验证码错误！");
			recordService.insert(record);
			return "redirect:/user/login";
		}
		if (!RoleType.USER.getCode().equals(user.getUsertype())) {
			user.setUsertype(RoleType.USER.getCode());
			RecordEntity record2 = recordService.assembleRocordEntity(request);
			record2.setOpertype(OperType.ATTACK.name());
			record2.setAfter(user.getUsertype() + "-用户类型被调试修改！");
			recordService.insert(record2);
			logger.error("检测到调试攻击，IP=" + AddressUtil.getIpAddress(request));
		}
		Condition condition = new Condition();
		condition.addParam("usertype", "=", RoleType.USER.getCode());
		condition.addParam("username", "=", user.getUsername());
		condition.addParam("password", "=", user.getPassword());
		boolean exists = daoService.exists(condition, UserEntity.class);
		if (!exists) {
			attr.addFlashAttribute("errorMsg", "username or password error");
			record.setAfter(user.getUsername() + "/" + user.getPassword()
					+ "-username or password error");
			recordService.insert(record);
			return "redirect:/user/login";
		}

		sessionService.setUsername(request, user.getUsername());
		sessionService.setUsertype(request, user.getUsertype());
		record.setAfter(user.getUsername() + "-login successfully");
		recordService.insert(record);
		return "redirect:/";
	}

	@RequestMapping(value = "logout")
	public String logout(HttpServletRequest request) {
		sessionService.removeUsername(request);
		sessionService.removeUsertype(request);
		return "redirect:/user/login";
	}

	@RequestMapping("register")
	public String register(HttpServletRequest request, ModelMap map) {
		map.put("usertype", RoleType.USER.getCode());
		return "user/register";
	}

	@RequestMapping("check_username")
	public @ResponseBody Result<Boolean> checkUsername(HttpServletRequest request, String username,
			ModelMap map) {
		Result<Boolean> result = new Result<Boolean>();
		String id = RoleType.USER.getCode() + "-" + username;
		boolean exists = daoService.existsById(id, UserEntity.class);
		if (exists) {
			result.setResultStatusAndMsg(ResultType.USER_EXISTS, null);
			result.setData(false);
			return result;
		}
		result.setResultStatusAndMsg(ResultType.SUCCESS, null);
		result.setData(false);
		return result;
	}

	@RequestMapping(value = "user_register", method = { RequestMethod.POST })
	public String register(HttpServletRequest request, UserEntity user, RedirectAttributes attr) {
		attr.addFlashAttribute("user", user);
		RecordEntity record = recordService.assembleRocordEntity(request);
		record.setOpertype(OperType.REGISTER.name());
		String id = user.getUsertype() + "-" + user.getUsername();
		boolean exists = daoService.existsById(id, UserEntity.class);
		if (exists) {
			record.setAfter(user.getUsername() + "-username allready exists");
			recordService.insert(record);
			attr.addFlashAttribute("errorMsg", "username allready exists");
			return "redirect:/user/register";
		}
		if (!RoleType.USER.getCode().equals(user.getUsertype())) {
			RecordEntity record2 = recordService.assembleRocordEntity(request);
			record2.setOpertype(OperType.ATTACK.name());
			record2.setAfter(user.getUsertype() + "-用户类型被调试修改！");
			recordService.insert(record2);
			user.setUsertype(RoleType.USER.getCode());
			logger.error("检测到调试攻击，IP=" + AddressUtil.getIpAddress(request));
		}
		user.setPrimaryKey(user.getUsertype(), user.getUsername());
		daoService.insert(user);
		sessionService.setUsername(request, user.getUsername());
		sessionService.setUsertype(request, user.getUsertype());
		record.setAfter(user.getUsername() + "-register successfully！");
		recordService.insert(record);
		return "redirect:/";
	}
}
