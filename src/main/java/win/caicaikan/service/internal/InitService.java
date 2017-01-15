package win.caicaikan.service.internal;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import win.caicaikan.api.req.LotteryReq;
import win.caicaikan.api.res.Result;
import win.caicaikan.constant.ExecuteStatus;
import win.caicaikan.constant.LotteryType;
import win.caicaikan.constant.RuleType;
import win.caicaikan.repository.mongodb.entity.MenuEntity;
import win.caicaikan.repository.mongodb.entity.PredictRuleEntity;
import win.caicaikan.repository.mongodb.entity.PrizeRuleEntity;
import win.caicaikan.repository.mongodb.entity.SysConfigEntity;
import win.caicaikan.repository.mongodb.entity.TaskEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqPredictEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.external.SsqService;
import win.caicaikan.service.internal.DaoService.Condition;
import win.caicaikan.service.rule.RuleService;
import win.caicaikan.task.ssq.SsqCurrentTask;
import win.caicaikan.task.ssq.SsqPredictTask;
import win.caicaikan.util.DateUtil;
import win.caicaikan.util.GsonUtil;

@Service
public class InitService {
	private static final Logger logger = Logger.getLogger(InitService.class);
	@Autowired
	private DaoService daoService;
	@Autowired
	private RuleService ruleService;
	@Autowired
	private SsqService ssqService;
	@Autowired
	private SsqPredictTask ssqPredictTask;
	@Autowired
	private SsqCurrentTask ssqCurrentTask;

	public void initSsqResults() {
		LotteryReq req = new LotteryReq();
		req.setLotteryType(LotteryType.SSQ.getValue());
		req.setQueryType("range");
		Integer currentYear = Integer.valueOf(DateUtil.toYear(new Date()));
		for (int i = 2003; i <= currentYear; i++) {
			req.setStart(i + "001");
			req.setEnd(i + "200");
			Result<List<SsqResultEntity>> result = ssqService.getSsqHistoryByLotteryReq(req);
			daoService.save(result.getData());
		}
	}

	public void initSsqPredicts(int terms) {
		Condition condition = new Condition();
		List<PredictRuleEntity> rules = daoService.query(condition, PredictRuleEntity.class);
		condition.setOrder(Direction.DESC);
		condition.setOrderBy("id");
		List<SsqResultEntity> results = daoService.query(condition, SsqResultEntity.class);
		if (terms == 0) {
			terms = results.size() - 500;
		} else if (terms > results.size()) {
			terms = results.size() - 50;
		}
		for (int i = 0; i < terms; i++) {
			try {
				if (i >= 1) {
					results.remove(0);
				}
				logger.info("正在预测" + ruleService.getNextTermNoOfSsq(results.get(0)) + "期,"
						+ (i + 1) + "/" + terms);
				ssqPredictTask.predictByResults(results, rules);
			} catch (Throwable e) {
				e.printStackTrace();
			}
		}
	}

	public void initPredictRightPositions() {
		Condition condition = new Condition();
		List<SsqResultEntity> ssqResults = daoService.query(condition, SsqResultEntity.class);
		List<PredictRuleEntity> predictRules = daoService.query(condition, PredictRuleEntity.class);
		for (PredictRuleEntity predictRule : predictRules) {
			condition = new Condition();
			condition.addParam("ruleId", "=", predictRule.getId());
			List<SsqPredictEntity> ssqPredicts = daoService
					.query(condition, SsqPredictEntity.class);
			Double redPositionCount = 0D;
			Double bluePositionCount = 0D;
			for (SsqPredictEntity ssqPredict : ssqPredicts) {
				for (SsqResultEntity ssqResult : ssqResults) {
					if (ssqPredict.getTermNo().equals(ssqResult.getId())) {
						String rightPositions = ssqCurrentTask.getRightNumPositions(ssqPredict,
								ssqResult);
						String[] numPositions = rightPositions.split("\\+");
						String[] redPositions = numPositions[0].split(",");
						for (String red : redPositions) {
							redPositionCount += Integer.valueOf(red);
						}
						Integer bluePosition = Integer.valueOf(numPositions[1].split(",")[0]);
						bluePositionCount += bluePosition;
						ssqPredict.setRightNumbers(rightPositions);
						daoService.save(ssqPredict);
						break;
					}
				}
			}

			redPositionCount = (ssqPredicts.size() * 6 * 17) / redPositionCount;
			bluePositionCount = (ssqPredicts.size() * 8.5) / bluePositionCount;
			predictRule.setMultipleRedRate(redPositionCount);
			predictRule.setMultipleBlueRate(bluePositionCount);
			daoService.save(predictRule);
		}

	}

	public void initMenus() {
		List<MenuEntity> menuEntities = new ArrayList<MenuEntity>();
		MenuEntity entity = new MenuEntity();
		entity.setId("HOME");
		entity.setSeq(0);
		entity.setNameZh("主页");
		entity.setNameEn("Home");
		entity.setPath("/");
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		menuEntities.add(entity);

		List<MenuEntity> subMenus = new ArrayList<MenuEntity>();
		entity = new MenuEntity();
		entity.setId("HISTORY_SSQ");
		entity.setSeq(0);
		entity.setNameZh("双色球");
		entity.setNameEn("Double Color Ball");
		entity.setPath("/history/ssq");
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		subMenus.add(entity);

		entity = new MenuEntity();
		entity.setId("HISTORY_DLT");
		entity.setSeq(1);
		entity.setNameZh("大乐透");
		entity.setNameEn("Super Lotto");
		entity.setPath("/history/dlt");
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		subMenus.add(entity);

		entity = new MenuEntity();
		entity.setId("HISTORY");
		entity.setSeq(1);
		entity.setNameZh("往期数据");
		entity.setNameEn("History");
		entity.setPath("/history");
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		entity.setSubMenus(subMenus);
		menuEntities.add(entity);

		subMenus = new ArrayList<MenuEntity>();
		entity = new MenuEntity();
		entity.setId("PREDICT_SSQ");
		entity.setSeq(1);
		entity.setNameZh("双色球");
		entity.setNameEn("Double Color Ball");
		entity.setPath("/predict/ssq");
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		subMenus.add(entity);

		entity = new MenuEntity();
		entity.setId("PREDICT");
		entity.setSeq(1);
		entity.setNameZh("预测");
		entity.setNameEn("Predict");
		entity.setPath("/predict");
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		entity.setSubMenus(subMenus);
		menuEntities.add(entity);

		daoService.insert(menuEntities, MenuEntity.class);
	}

	public void initSysConfigs() {
		SysConfigEntity entity = new SysConfigEntity();
		entity.setPrimaryKey("SP", "00", "00");
		entity.setName("双色球下期期号");
		entity.setKey("2016-12-27 21:20:40");
		entity.setValue("2016152");
		entity.setStatus(1);
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		daoService.save(entity);
		entity = new SysConfigEntity();
		entity.setPrimaryKey("SP", "00", "01");
		entity.setName("双色球本期期号");
		entity.setKey("2016-12-25 21:20:40");
		entity.setValue("2016151");
		entity.setStatus(1);
		entity.setCreateTime(new Date());
		entity.setUpdateTime(new Date());
		daoService.save(entity);
	}

	public void initTasks() {

		TaskEntity entity = new TaskEntity();
		entity.setId("SsqCurrentTask");
		entity.setExecuteStatus(ExecuteStatus.SUCCESS.name());
		daoService.insert(entity);

		entity = new TaskEntity();
		entity.setId("SsqHistoryTask");
		entity.setExecuteStatus(ExecuteStatus.SUCCESS.name());
		daoService.insert(entity);

		entity = new TaskEntity();
		entity.setId("SsqPredictTask");
		entity.setExecuteStatus(ExecuteStatus.SUCCESS.name());
		daoService.insert(entity);
	}

	public void initPredictRules() {
		PredictRuleEntity rule = null;
		int[] termCounts = { 2000, 1000, 500, 200, 100, 50 };
		for (int i = 0; i < termCounts.length; i++) {
			for (RuleType ruleType : RuleType.values()) {
				if (ruleType.equals(RuleType.MULTI)) {
					continue;
				}
				rule = new PredictRuleEntity();
				rule.setPrimaryKey(LotteryType.SSQ.getCode(), ruleType.name(), termCounts[i]);
				rule.setRuleName(ruleType.getDesc() + termCounts[i]);
				rule.setStatus(1);
				rule.setExecuteStatus(ExecuteStatus.SUCCESS.name());
				rule.setCreateTime(new Date());
				rule.setUpdateTime(new Date());
				daoService.save(rule);
			}
		}

		for (int i = 0; i < termCounts.length; i++) {
			for (int j = 10; j < 90; j += 10) {
				for (int k = 0; k <= 10 && j + k < 100; k += 10) {
					String id = j + "," + (100 - j - k) + "," + k;
					rule = new PredictRuleEntity();
					rule.setPrimaryKey(LotteryType.SSQ.getCode(), RuleType.MULTI.name(),
							termCounts[i]);
					rule.setId(rule.getId() + "-" + id);
					rule.setRuleName(RuleType.MULTI.getDesc() + termCounts[i] + ",比例" + id);
					rule.setStatus(1);
					rule.setExecuteStatus(ExecuteStatus.SUCCESS.name());
					rule.setCreateTime(new Date());
					rule.setUpdateTime(new Date());
					Map<String, Integer> map = new HashMap<>();
					map.put(RuleType.DISPLAY_TIMES.name(), j);
					map.put(RuleType.SKIP_TIMES.name(), 100 - j - k);
					map.put(RuleType.DOUBLE_TIMES.name(), k);
					String ruleAndweights = GsonUtil.toJson(map);
					rule.setRuleAndweights(ruleAndweights);
					daoService.save(rule);
				}
			}
		}
	}

	public void initSsqPrizeRules() {
		List<PrizeRuleEntity> entityList = new ArrayList<PrizeRuleEntity>();
		PrizeRuleEntity entity = null;
		BigInteger allCases = ruleService.countSsqAllCases();
		BigInteger prizeCases = null;

		entity = new PrizeRuleEntity();
		entity.setPrimaryKey(LotteryType.SSQ.getCode(), "1");
		entity.setCondition("6+1");
		entity.setName("一等奖");
		entity.setDesc("当奖池资金低于1亿元时，奖金总额为当期高等奖奖金的75%与奖池中累积的奖金之和，单注奖金按注均分，单注最高限额封顶500万元。当奖池资金高于1亿元（含）时，奖金总额包括两部分，一部分为当期高等奖奖金的55%与奖池中累积的奖金之和，单注奖金按注均分，单注最高限额封顶500万元；另一部分为当期高等奖奖金的20%，单注奖金按注均分，单注最高限额封顶500万元。");
		prizeCases = ruleService.countSsqPrizeCases(entity.getCondition());
		entity.setRate(prizeCases, allCases);
		entityList.add(entity);

		entity = new PrizeRuleEntity();
		entity.setPrimaryKey(LotteryType.SSQ.getCode(), "2");
		entity.setCondition("6+0");
		entity.setName("二等奖");
		entity.setDesc("当期高等奖奖金的25%");
		prizeCases = ruleService.countSsqPrizeCases(entity.getCondition());
		entity.setRate(prizeCases, allCases);
		entityList.add(entity);

		entity = new PrizeRuleEntity();
		entity.setPrimaryKey(LotteryType.SSQ.getCode(), "3");
		entity.setCondition("5+1");
		entity.setName("三等奖");
		entity.setDesc("单注奖金额固定为3000元");
		prizeCases = ruleService.countSsqPrizeCases(entity.getCondition());
		entity.setRate(prizeCases, allCases);
		entityList.add(entity);

		entity = new PrizeRuleEntity();
		entity.setPrimaryKey(LotteryType.SSQ.getCode(), "4");
		entity.setCondition("5+0;4+1");
		entity.setName("四等奖");
		entity.setDesc("单注奖金额固定为200元");
		prizeCases = ruleService.countSsqPrizeCases(entity.getCondition());
		entity.setRate(prizeCases, allCases);
		entityList.add(entity);

		entity = new PrizeRuleEntity();
		entity.setPrimaryKey(LotteryType.SSQ.getCode(), "5");
		entity.setCondition("4+0;3+1");
		entity.setName("五等奖");
		entity.setDesc("单注奖金额固定为10元");
		prizeCases = ruleService.countSsqPrizeCases(entity.getCondition());
		entity.setRate(prizeCases, allCases);
		entityList.add(entity);

		entity = new PrizeRuleEntity();
		entity.setPrimaryKey(LotteryType.SSQ.getCode(), "6");
		entity.setCondition("1+1;0+1");
		entity.setName("六等奖");
		entity.setDesc("单注奖金额固定为5元");
		prizeCases = ruleService.countSsqPrizeCases(entity.getCondition());
		entity.setRate(prizeCases, allCases);
		entityList.add(entity);
		daoService.save(entityList);
	}
}
