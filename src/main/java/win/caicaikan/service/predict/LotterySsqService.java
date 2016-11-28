/**
 * 
 */
package win.caicaikan.service.predict;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.stereotype.Service;

import win.caicaikan.api.domain.LotteryCurrentBo;
import win.caicaikan.api.req.LotteryReq;
import win.caicaikan.api.res.LotteryCurrentRes;
import win.caicaikan.api.res.Result;
import win.caicaikan.repository.mongodb.entity.LotterySsqEntity;
import win.caicaikan.service.BaseService;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月21日
 * @ClassName DoubleColorBallService
 */
@Service
public class LotterySsqService extends BaseService {
	public Result<List<LotterySsqEntity>> getSsqHistoryByLotteryReq(LotteryReq req) {
		return this.doGetHistory(req, LotterySsqEntity.class);
	}

	public Result<List<LotterySsqEntity>> getSsqCurrentByLotteryReq(LotteryReq req) {
		LotteryCurrentRes currentRes = this.doGetCurrent(req);
		Result<List<LotterySsqEntity>> result = new Result<List<LotterySsqEntity>>();
		List<LotterySsqEntity> data = new ArrayList<LotterySsqEntity>();
		for (LotteryCurrentBo bo : currentRes.getData()) {
			LotterySsqEntity entity = new LotterySsqEntity();
			entity.setTermNo(bo.getExpect());
			entity.setOpenTime(bo.getOpentime());
			String[] split = bo.getOpencode().split("\\+");
			entity.setRedNumbers(split[0]);
			entity.setBlueNumber(split[1]);
			Date now = new Date();
			entity.setCreateTime(now);
			entity.setUpdateTime(now);
			data.add(entity);
		}
		result.setData(data);
		return result;
	}

	@SuppressWarnings("unchecked")
	@Override
	protected Result<List<LotterySsqEntity>> convertDocumentToResult(Document document) {
		if (document == null) {
			return null;
		}
		Result<List<LotterySsqEntity>> result = new Result<List<LotterySsqEntity>>();
		Elements elements = document.getElementsByAttributeValueMatching("class", "historylist");
		List<LotterySsqEntity> list = new ArrayList<LotterySsqEntity>();
		Element element = elements.get(0).child(1);
		for (Element row : element.children()) {
			LotterySsqEntity entity = new LotterySsqEntity();
			entity.setTermNo(row.child(0).child(0).ownText());
			entity.setOpenTime(row.child(1).ownText().replaceAll("（.）", "") + " 21:20:40");
			entity.setRedNumbers(row.child(2).getElementsByAttributeValue("class", "redBalls").get(0).html().trim().replace("</em><em>", ",")
					.replace("<em>", "").replace("</em>", ""));
			entity.setBlueNumber(row.child(2).getElementsByAttributeValue("class", "blueBalls").get(0).html().trim().replace("<em>", "")
					.replace("</em>", ""));
			entity.setFirstPrizeCount(row.child(3).getElementsByAttributeValue("class", "NotNumber").get(0).ownText());
			entity.setFirstPrizeAmount(row.child(3).getElementsByAttributeValue("class", "cash").get(0).ownText());
			entity.setSecondPrizeCount(row.child(4).getElementsByAttributeValue("class", "NotNumber").get(0).ownText());
			entity.setSecondPrizeAmount(row.child(4).getElementsByAttributeValue("class", "cash").get(0).ownText());
			Date now = new Date();
			entity.setCreateTime(now);
			entity.setUpdateTime(now);
			list.add(entity);
		}
		result.setData(list);
		return result;
	}
}