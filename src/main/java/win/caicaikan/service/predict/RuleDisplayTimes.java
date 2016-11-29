/**
 * 
 */
package win.caicaikan.service.predict;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import win.caicaikan.constant.Rule;
import win.caicaikan.repository.mongodb.entity.LotteryPredictEntity;
import win.caicaikan.repository.mongodb.entity.LotteryRuleEntity;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月21日
 * @ClassName DoubleColorBallService
 */
@Service
public class RuleDisplayTimes extends RuleTemplate {

	@Override
	public Rule getRule() {
		return Rule.SSQ_0_001;
	}

	@Override
	public List<LotteryPredictEntity> excuteRule(LotteryRuleEntity entity) throws Throwable {
		List<LotteryPredictEntity> result = new ArrayList<LotteryPredictEntity>();

		return result;
	}
}