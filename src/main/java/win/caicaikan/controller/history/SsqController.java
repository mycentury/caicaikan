/**
 * 
 */
package win.caicaikan.controller.history;

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
public class SsqController {
	@Autowired
	private MongoTemplate mongoTemplate;

	@RequestMapping(value = { "ssq" })
	public String queryLottery(HttpServletRequest request, ModelMap map) {
		map.put("words", "<p>Hello,World!</p>");
		return "history/ssq";
	}
}
