/**
 * 
 */
package win.caicaikan.service.internal;

import java.math.BigInteger;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import win.caicaikan.repository.mongodb.entity.PredictRuleEntity;
import win.caicaikan.repository.mongodb.entity.PrizeRuleEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqPredictEntity;
import win.caicaikan.service.internal.DaoService.Condition;
import win.caicaikan.util.MathUtil;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2017年3月15日
 * @ClassName CalculateService
 */
@Service
public class CalculateService implements InitializingBean {
	@Autowired
	private DaoService daoService;

	private Map<String, PrizeRuleEntity> prizeRuleMap;

	public void initPrizeRuleMap() {
		prizeRuleMap = new HashMap<String, PrizeRuleEntity>();
		List<PrizeRuleEntity> allPrizeRules = daoService.query(null, PrizeRuleEntity.class);
		for (PrizeRuleEntity prizeRule : allPrizeRules) {
			prizeRuleMap.put(prizeRule.getId(), prizeRule);
		}
	}

	/**
	 * @param predictRuleId 规则ID
	 * @param redCount 红球数
	 * @param blueCount 蓝球数
	 * @return
	 */
	public PredictRuleEntity countRate(PredictRuleEntity predictRule, int redCount, int blueCount) {
		Condition condition = new Condition();
		condition.addParam("ruleId", "=", predictRule.getId());
		List<SsqPredictEntity> ssqPredicts = daoService.query(condition, SsqPredictEntity.class);
		Double firstRate = 0D;// 一等奖准确率
		Double secondRate = 0D;// 二等奖准确率
		Double firstRedRate = 0D;// 1号球准确率
		Double secondRedRate = 0D;// 2号球准确率
		Double thirdRedRate = 0D;// 3号球准确率
		Double forthRedRate = 0D;// 4号球准确率
		Double fifthRedRate = 0D;// 5号球准确率
		Double sixthRedRate = 0D;// 6号球准确率
		Double blueRate = 0D;// 蓝球准确率
		int count = 0;
		for (SsqPredictEntity ssqPredict : ssqPredicts) {
			String rightNumbers = ssqPredict.getRightNumbers();
			if (StringUtils.isEmpty(rightNumbers)) {
				continue;
			}
			count++;
			int rightRedCount = 0;
			int rightBlueCount = 0;
			String[] split = rightNumbers.split("\\+");
			String[] rightReds = split[0].split(",");
			String rightBlue = split[1];
			for (int i = 0; i < rightReds.length; i++) {
				if (Integer.valueOf(rightReds[i]) <= redCount) {
					if (i == 0) {
						firstRedRate++;
					} else if (i == 1) {
						secondRedRate++;
					} else if (i == 2) {
						thirdRedRate++;
					} else if (i == 3) {
						forthRedRate++;
					} else if (i == 4) {
						fifthRedRate++;
					} else if (i == 5) {
						sixthRedRate++;
					}
					rightRedCount++;
				}
			}
			if (Integer.valueOf(rightBlue) <= blueCount) {
				rightBlueCount++;
				blueRate++;
			}
			String con = rightRedCount + "+" + rightBlueCount;
			if ("6+1".equals(con)) {
				firstRate++;
			} else if ("6+0".equals(con)) {
				secondRate++;
			}
		}
		// 注数
		BigInteger number = MathUtil.C(new BigInteger(String.valueOf(redCount)), new BigInteger("6")).multiply(
				MathUtil.C(new BigInteger(String.valueOf(redCount)), new BigInteger("6")));
		predictRule.setFirstRate(firstRate / count / number.intValue());
		predictRule.setSecondRate(secondRate / count / number.intValue());
		predictRule.setFirstRedRate(firstRedRate);
		predictRule.setSecondRedRate(secondRedRate);
		predictRule.setThirdRedRate(thirdRedRate);
		predictRule.setForthRedRate(forthRedRate);
		predictRule.setFifthRedRate(fifthRedRate);
		predictRule.setSixthRedRate(sixthRedRate);
		predictRule.setBlueRate(blueRate);
		return predictRule;
	}

	@Override
	public void afterPropertiesSet() throws Exception {
		this.initPrizeRuleMap();
	}
}
