/**
 * 
 */
package win.caicaikan.controller;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Controller;
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
public class IndexController {
	@Autowired
	private MongoTemplate mongoTemplate;

	@RequestMapping(value = "/")
	public String queryLottery(HttpServletRequest request) {
		return "index";
	}
}
