/**
 * 
 */
package win.caicaikan.service;

import java.util.ArrayList;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import win.caicaikan.api.domain.LotterySsq;
import win.caicaikan.api.req.LotteryReq;
import win.caicaikan.api.res.Result;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月21日
 * @ClassName DoubleColorBallService
 */
@Service
public class LotterySsqService extends BaseService {
	public Result<List<LotterySsq>> getSsqInfoByLotteryReq(LotteryReq req) {
		return doGet(req, LotterySsq.class);
	}

	@Override
	protected Result<List<LotterySsq>> convertDocumentToResult(Document document) {
		if (document == null) {
			return null;
		}
		Result<List<LotterySsq>> result = new Result<List<LotterySsq>>();
		Elements elements = document.getElementsByAttributeValueMatching("class", "historylist");
		List<LotterySsq> list = new ArrayList<LotterySsq>();
		Element element = elements.get(0).child(1);
		for (Element row : element.children()) {
			LotterySsq lotterySsq = new LotterySsq();
			lotterySsq.setTermNo(row.child(0).child(0).ownText());
			lotterySsq.setOpenDate(row.child(1).ownText().replaceAll("（.）", ""));
			lotterySsq.setRedNumbers(row.child(2).getElementsByAttributeValue("class", "redBalls").get(0).ownText().trim().replace("</em><em>", ",")
					.replace("<em>", "").replace("</em>", ""));
			lotterySsq.setBlueNumber(row.child(2).getElementsByAttributeValue("class", "blueBalls").get(0).ownText().trim().replace("<em>", "")
					.replace("</em>", ""));
			lotterySsq.setFirstPrizeCount(row.child(3).getElementsByAttributeValue("class", "NotNumber").get(0).ownText());
			lotterySsq.setFirstPrizeAmount(row.child(3).getElementsByAttributeValue("class", "cash").get(0).ownText());
			lotterySsq.setSecondPrizeCount(row.child(4).getElementsByAttributeValue("class", "NotNumber").get(0).ownText());
			lotterySsq.setSecondPrizeAmount(row.child(4).getElementsByAttributeValue("class", "cash").get(0).ownText());
			list.add(lotterySsq);
		}
		result.setData(list);
		return result;
	}
}