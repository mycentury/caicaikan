/**
 * 
 */
package win.caicaikan.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年12月16日
 * @ClassName SearchController
 */
@Controller
@RequestMapping("search")
public class SearchController {
	@RequestMapping("result")
	public String searchAll(HttpServletRequest request, ModelMap map) {
		map.put("result", "<p>还没实现，呵呵哒</p>");
		return "search/result";
	}
}
