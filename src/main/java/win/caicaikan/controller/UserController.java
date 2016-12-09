/**
 * 
 */
package win.caicaikan.controller;

import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import win.caicaikan.annotation.Token;
import win.caicaikan.constant.Constant;
import win.caicaikan.repository.mongodb.entity.UserEntity;
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
	@Autowired
	private MongoTemplate mongoTemplate;

	@RequestMapping("login")
	@Token(save = true)
	public String login(HttpServletRequest request, ModelMap map) {
		map.put("usertype", "G");
		return "login";
	}

	@RequestMapping(value = "do_login", method = { RequestMethod.POST })
	@Token(remove = true)
	public String doLogin(UserEntity user, String checkcode, ModelMap map) {
		return "redirect:/user/login";
	}

	@RequestMapping("register")
	public String register(HttpServletRequest request, ModelMap map) {
		map.put("words", "<p>Hello,World!</p>");
		return "register";
	}

	@RequestMapping(value = { "/checkcode" })
	public void checkCode(HttpServletRequest request, HttpServletResponse response, ModelMap map) {
		String checkCode = CheckCodeUtil.getRandomCode(Constant.CHECK_CODE, 4);
		map.put("checkcode", checkCode);
		BufferedImage image = CheckCodeUtil.getCheckCodeImg(checkCode, 60, 22);
		try {
			ImageIO.write(image, Constant.IMAGE_TYPE, response.getOutputStream());
			response.getOutputStream().flush();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
