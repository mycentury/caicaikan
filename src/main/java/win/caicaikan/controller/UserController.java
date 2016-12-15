/**
 * 
 */
package win.caicaikan.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

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
import win.caicaikan.constant.Constant;
import win.caicaikan.constant.OperType;
import win.caicaikan.repository.mongodb.entity.RecordEntity;
import win.caicaikan.repository.mongodb.entity.UserEntity;
import win.caicaikan.service.internal.RecordService;
import win.caicaikan.service.internal.UserService;
import win.caicaikan.util.AddressUtil;
import win.caicaikan.util.CheckCodeUtil;

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
public class UserController extends BaseController {
	private final static Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private UserService userService;
	@Autowired
	private RecordService recordService;

	@RequestMapping("login")
	public String userLogin(HttpServletRequest request, ModelMap map) {
		if (hasLogin(request)) {
			return "redirect:/";
		}
		map.put("usertype", "G");
		return "user/login";
	}

	@RequestMapping(value = "user_login", method = { RequestMethod.POST })
	public String userLogin(HttpServletRequest request, UserEntity user, String checkcode, RedirectAttributes attr) {
		RecordEntity record = recordService.assembleRocordEntity(request);
		record.setOpertype(OperType.LOGIN.name());
		if (StringUtils.isEmpty(record.getUsername())) {
			record.setUsername(user.getUsername());
			record.setUsertype(user.getUsertype());
		}
		if (!checkcode.equalsIgnoreCase(request.getSession().getAttribute("checkcode").toString())) {
			attr.addFlashAttribute("errorMsg", "验证码错误！");
			attr.addFlashAttribute("usertype", "G");
			record.setAfter(checkcode + "-验证码错误！");
			recordService.insert(record);
			return "redirect:/user/login";
		}
		if (!"G".equals(user.getUsertype())) {
			user.setUsertype("G");
			RecordEntity record2 = recordService.assembleRocordEntity(request);
			record2.setOpertype(OperType.ATTACK.name());
			record2.setAfter(user.getUsertype() + "-用户类型被调试修改！");
			recordService.insert(record2);
			logger.error("检测到调试攻击，IP=" + AddressUtil.getIpAddress(request));
		}
		if (!userService.exists(user)) {
			attr.addFlashAttribute("usertype", "G");
			attr.addFlashAttribute("errorMsg", "username or password error");
			record.setAfter(user.getUsername() + "/" + user.getPassword() + "-username or password error");
			recordService.insert(record);
			return "redirect:/user/login";
		}

		request.getSession().setAttribute("username", user.getUsername());
		request.getSession().setAttribute("usertype", user.getUsertype());
		record.setAfter(user.getUsername() + "-login successfully");
		recordService.insert(record);
		return "redirect:/";
	}

	@RequestMapping(value = "user_logout")
	public String userLogout(HttpServletRequest request) {
		request.getSession().removeAttribute("username");
		request.getSession().removeAttribute("usertype");
		return "redirect:/user/login";
	}

	@RequestMapping("register")
	public String register(HttpServletRequest request, ModelMap map) {
		map.put("usertype", "G");
		return "user/register";
	}

	@RequestMapping("check_username")
	public @ResponseBody Result<Boolean> checkUsername(HttpServletRequest request, String username, ModelMap map) {
		Result<Boolean> result = new Result<Boolean>();
		if (userService.exists("G" + "-" + username)) {
			result.setStatus(400);
			result.setMessage("username allready exists");
			result.setData(false);
		}
		return result;
	}

	@RequestMapping(value = "user_register", method = { RequestMethod.POST })
	public String register(HttpServletRequest request, UserEntity user, RedirectAttributes attr) {
		RecordEntity record = recordService.assembleRocordEntity(request);
		record.setOpertype(OperType.REGISTER.name());
		String password = user.getPassword();
		user.setPassword(null);
		if (userService.exists(user)) {
			record.setAfter(user.getUsername() + "-username allready exists");
			recordService.insert(record);
			attr.addFlashAttribute("errorMsg", "username allready exists");
			return "redirect:/user/register";
		}
		if (!"G".equals(user.getUsertype())) {
			RecordEntity record2 = recordService.assembleRocordEntity(request);
			record2.setOpertype(OperType.ATTACK.name());
			record2.setAfter(user.getUsertype() + "-用户类型被调试修改！");
			recordService.insert(record2);
			user.setUsertype("G");
			logger.error("检测到调试攻击，IP=" + AddressUtil.getIpAddress(request));
		}
		user.setPassword(password);
		userService.insert(user);
		request.getSession().setAttribute("username", user.getUsername());
		request.getSession().setAttribute("usertype", user.getUsertype());
		record.setAfter(user.getUsername() + "-register successfully！");
		recordService.insert(record);
		return "redirect:/";
	}

	@RequestMapping("admin")
	public String adminLogin(HttpServletRequest request, ModelMap map) {
		if (hasLogin(request)) {
			return "redirect:/user/admin/console";
		}
		map.put("usertype", "A");
		return "user/admin/login";
	}

	@RequestMapping(value = "admin_login", method = { RequestMethod.POST })
	public String adminLogin(HttpServletRequest request, UserEntity user, String checkcode, RedirectAttributes attr) {
		RecordEntity record = recordService.assembleRocordEntity(request);
		record.setOpertype(OperType.LOGIN.name());
		if (!checkcode.equalsIgnoreCase(request.getSession().getAttribute("checkcode").toString())) {
			attr.addFlashAttribute("errorMsg", "验证码错误！");
			attr.addFlashAttribute("usertype", "A");
			record.setAfter(checkcode + "!=" + request.getSession().getAttribute("checkcode").toString() + "-验证码错误！");
			recordService.insert(record);
			return "redirect:/user/admin";
		}
		if (!"A".equals(user.getUsertype())) {
			RecordEntity record2 = recordService.assembleRocordEntity(request);
			record2.setOpertype(OperType.ATTACK.name());
			record2.setAfter(user.getUsertype() + "-用户类型被调试修改！");
			recordService.insert(record2);
			user.setUsertype("A");
			logger.error("检测到调试攻击，IP=" + AddressUtil.getIpAddress(request));
		}
		if (!userService.exists(user)) {
			attr.addFlashAttribute("errorMsg", "username or password error");
			attr.addFlashAttribute("usertype", "A");
			record.setAfter(user.getUsername() + "/" + user.getPassword() + "-username or password error");
			recordService.insert(record);
			return "redirect:/user/admin";
		}
		request.getSession().setAttribute("username", user.getUsername());
		request.getSession().setAttribute("usertype", user.getUsertype());
		record.setAfter(user.getUsername() + "-login successfully！");
		recordService.insert(record);
		return "redirect:/user/admin/console";
	}

	@RequestMapping(value = "admin_logout")
	public String adminLogout(HttpServletRequest request) {
		request.getSession().removeAttribute("username");
		request.getSession().removeAttribute("usertype");
		return "redirect:/user/admin";

	}

	@RequestMapping("admin/console")
	public String adminConsole(HttpServletRequest request) {
		return "user/admin/console";
	}

	@RequestMapping(value = { "/checkcode" })
	public void checkCode(HttpServletRequest request, HttpServletResponse response) {
		String checkCode = CheckCodeUtil.getRandomCode(Constant.CHECK_CODE, 4);
		request.getSession().setAttribute("checkcode", checkCode);
		BufferedImage image = CheckCodeUtil.getCheckCodeImg(checkCode, 60, 22);
		try {
			ImageIO.write(image, Constant.IMAGE_TYPE, response.getOutputStream());
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
