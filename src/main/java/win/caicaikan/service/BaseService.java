/**
 * 
 */
package win.caicaikan.service;

import java.util.List;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.api.domain.BaseLottery;
import win.caicaikan.api.req.LotteryReq;
import win.caicaikan.api.res.Result;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName BaseService
 */
public abstract class BaseService {
	@Autowired
	private HttpService httpService;

	protected String apiUrl = "http://baidu.lecai.com/lottery/draw/list/";

	protected <T extends BaseLottery> Result<List<T>> doGet(LotteryReq req, Class<T> clazz) {
		String url = getLotteryQueryRealUrl(req);
		Document document = httpService.getDocumentByGetWithJsoup(url);
		Result<List<T>> result = convertDocumentToResult(document);
		return result;
	}

	/**
	 * @param req
	 * @return
	 */
	private String getLotteryQueryRealUrl(LotteryReq req) {
		StringBuilder sb = new StringBuilder();
		sb.append(apiUrl).append(req.getLotteryType()).append("?");
		sb.append("&type=").append(req.getQueryType());
		sb.append("&start=").append(req.getStart());
		sb.append("&end=").append(req.getEnd());
		return sb.toString();
	}

	protected abstract <T extends BaseLottery> Result<List<T>> convertDocumentToResult(Document document);
}
