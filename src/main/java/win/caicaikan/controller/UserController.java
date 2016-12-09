/**
 * 
 */
package win.caicaikan.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

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
public class UserController {
	@Autowired
	private MongoTemplate mongoTemplate;

	@RequestMapping(value = { "/login" })
	public String login(HttpServletRequest request, ModelMap map) {
		map.put("words", "<p>Hello,World!</p>");
		return "login";
	}

	@RequestMapping(value = { "/register" })
	public String register(HttpServletRequest request, ModelMap map) {
		map.put("words", "<p>Hello,World!</p>");
		return "register";
	}
}
