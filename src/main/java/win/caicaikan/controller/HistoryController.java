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
@RequestMapping(value = { "history" })
public class HistoryController {
	@Autowired
	private MongoTemplate mongoTemplate;

	@RequestMapping(value = { "" })
	public String index(HttpServletRequest request, ModelMap map) {
		map.put("words", "<p>Hello,World!</p>");
		return "history/index";
	}

	@RequestMapping(value = { "ssq" })
	public String queryHistoryOfSsq(HttpServletRequest request, ModelMap map) {
		map.put("words", "<p>Hello,World!</p>");
		return "history/ssq";
	}

	@RequestMapping(value = { "dlt" })
	public String queryHistoryOfDlt(HttpServletRequest request, ModelMap map) {
		map.put("words", "<p>Hello,World!</p>");
		return "history/dlt";
	}
}
