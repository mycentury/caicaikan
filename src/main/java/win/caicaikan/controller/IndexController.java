/**
 * 
 */
package win.caicaikan.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

import win.caicaikan.service.SsqResultService;

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
	private SsqResultService ssqResultService;

	@RequestMapping(value = { "/index" })
	public String index(HttpServletRequest request, ModelMap map) {
		map.put("words", "<p>Hello,World!</p>");
		return "direct:/";
	}

	@RequestMapping(value = { "/" })
	public String home(HttpServletRequest request, ModelMap map) {
		map.put("words", "<p>Hello,World!</p>");
		return "index";
	}
}
