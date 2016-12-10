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
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import win.caicaikan.annotation.Token;
import win.caicaikan.constant.Constant;
import win.caicaikan.repository.mongodb.entity.UserEntity;
import win.caicaikan.service.UserService;
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
public class UserController {
	private final static Logger logger = Logger.getLogger(UserController.class);

	@Autowired
	private UserService userService;

	@RequestMapping("login")
	@Token(save = true)
	public String userLogin(HttpServletRequest request, ModelMap map) {
		map.put("usertype", "G");
		return "user/login";
	}

	@RequestMapping(value = "user_login", method = { RequestMethod.POST })
	@Token(remove = true)
	public String userLogin(HttpServletRequest request, UserEntity user, String checkcode,
			ModelMap map) {
		if (!checkcode.equals(map.get("checkcode"))) {
			map.put("errorMsg", "验证码错误！");
			return "redirect:/user/login";
		}
		if ("G".equals(user.getUsertype())) {
			user.setUsertype("G");
			logger.error("检测到调试攻击，IP=" + AddressUtil.getIpAddress(request));
		}
		if (!userService.exists(user)) {
			map.put("errorMsg", "username or password error");
			return "redirect:/user/login";
		}

		request.getSession().setAttribute("username", user.getUsername());
		request.getSession().setAttribute("usertype", user.getUsertype());
		return "redirect:" + request.getHeader("Referer");
	}

	@RequestMapping("register")
	public String register(HttpServletRequest request, ModelMap map) {
		return "user/register";
	}

	@RequestMapping("user_register")
	public String register(HttpServletRequest request, UserEntity user, ModelMap map) {
		String password = user.getPassword();
		user.setPassword(null);
		if (userService.exists(user)) {
			map.put("errorMsg", "username allready exists");
			return "redirect:/user/register";
		}
		user.setPassword(password);
		userService.insert(user);
		request.getSession().setAttribute("username", user.getUsername());
		request.getSession().setAttribute("usertype", user.getUsertype());
		return "user/console";
	}

	@RequestMapping("admin")
	public String adminLogin(HttpServletRequest request, ModelMap map) {
		map.put("usertype", "A");
		return "user/admin/login";
	}

	@RequestMapping(value = "admin_login", method = { RequestMethod.POST })
	public String adminLogin(HttpServletRequest request, UserEntity user, String checkcode,
			ModelMap map) {
		if (!checkcode.equals(request.getSession().getAttribute("checkcode"))) {
			map.put("errorMsg", "验证码错误！");
			return "user/admin/login";
		}
		if (!"A".equals(user.getUsertype())) {
			user.setUsertype("A");
			logger.error("检测到调试攻击，IP=" + AddressUtil.getIpAddress(request));
		}
		if (!userService.exists(user)) {
			map.put("errorMsg", "username or password error");
			return "user/admin/login";
		}
		request.getSession().setAttribute("username", user.getUsername());
		request.getSession().setAttribute("usertype", user.getUsertype());
		return "redirect:/user/admin/console";
	}

	@RequestMapping("admin/console")
	public String adminConsole(HttpServletRequest request, ModelMap map) {
		return "user/admin/console";
	}

	@RequestMapping(value = { "/checkcode" })
	public void checkCode(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
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
