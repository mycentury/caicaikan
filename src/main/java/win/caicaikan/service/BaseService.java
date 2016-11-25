/**
 * 
 */
package win.caicaikan.service;

import java.util.List;

import org.jsoup.nodes.Document;
import org.springframework.beans.factory.annotation.Autowired;

import win.caicaikan.api.req.LotteryReq;
import win.caicaikan.api.res.LotteryCurrentRes;
import win.caicaikan.api.res.Result;
import win.caicaikan.repository.mongodb.entity.BaseEntity;

import com.google.gson.Gson;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName BaseService
 */
public abstract class BaseService {
	@Autowired
	private HttpService httpService;

	protected String historyUrl = "http://baidu.lecai.com/lottery/draw/list/";

	protected String currentUrl = "http://f.apiplus.cn/";

	protected <T extends BaseEntity> Result<List<T>> doGetHistory(LotteryReq req, Class<T> clazz) {
		String url = this.getHistoryQueryRealUrl(historyUrl, req);
		Document document = httpService.getDocumentByGetWithJsoup(url);
		Result<List<T>> result = convertDocumentToResult(document);
		return result;
	}

	protected LotteryCurrentRes doGetCurrent(LotteryReq req) {
		String url = this.getCurrentQueryRealUrl(currentUrl, req);
		String json = httpService.getDocumentByGetWithHttpClient(url);
		LotteryCurrentRes currentRes = new Gson().fromJson(json, LotteryCurrentRes.class);
		return currentRes;
	}

	/**
	 * @param req
	 * @return
	 */
	private String getHistoryQueryRealUrl(String baseUrl, LotteryReq req) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl).append(req.getLotteryType()).append("?");
		sb.append("&type=").append(req.getQueryType());
		sb.append("&start=").append(req.getStart());
		sb.append("&end=").append(req.getEnd());
		return sb.toString();
	}

	/**
	 * @param req
	 * @return
	 */
	private String getCurrentQueryRealUrl(String baseUrl, LotteryReq req) {
		StringBuilder sb = new StringBuilder();
		sb.append(baseUrl).append(req.getLotteryType()).append("-").append(req.getCount()).append(".json");
		return sb.toString();
	}

	protected abstract <T extends BaseEntity> Result<List<T>> convertDocumentToResult(Document document);
}
