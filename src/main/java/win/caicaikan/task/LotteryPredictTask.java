/**
 * 
 */
package win.caicaikan.task;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import win.caicaikan.api.req.LotteryReq;
import win.caicaikan.constant.LotteryType;
import win.caicaikan.constant.SsqConstant;
import win.caicaikan.repository.mongodb.dao.LotteryPredictDao;
import win.caicaikan.repository.mongodb.dao.LotterySsqDao;
import win.caicaikan.repository.mongodb.entity.LotteryPredictEntity;
import win.caicaikan.repository.mongodb.entity.LotterySsqEntity;
import win.caicaikan.util.DateUtil;
import win.caicaikan.util.MapUtil;

/**
 * @Desc 双色球及时任务：用于下期预测
 * @author wewenge.yan
 * @Date 2016年11月21日
 * @ClassName DoubleColorBallTask
 */
@Service
public class LotteryPredictTask extends TaskTemplete {
	private static final String START_NO = "001";
	private static final Logger logger = Logger.getLogger(LotteryPredictTask.class);
	@Autowired
	private LotterySsqDao lotterySsqDao;
	@Autowired
	private LotteryPredictDao lotteryPredictDao;

	@Override
	public void run() throws Throwable {
		LotteryReq req = new LotteryReq();
		req.setLotteryType(LotteryType.SSQ.getCode());
		try {
			Sort sort = new Sort(new Sort.Order(Sort.Direction.DESC, "termNo"));
			List<LotterySsqEntity> list = lotterySsqDao.findAll(sort);
			if (CollectionUtils.isEmpty(list)) {
				return;
			}
			String termNo = this.getNextTermNo(list.get(0));
			if (lotterySsqDao.exists(termNo)) {
				return;
			}
			String numbers = this.calculateByRules(list);
			LotteryPredictEntity entity = new LotteryPredictEntity();
			entity.setTermNo(termNo);
			entity.setType(LotteryType.SSQ.getCode());
			entity.setNumbers(numbers);
			Date now = new Date();
			entity.setCreateTime(now);
			entity.setUpdateTime(now);
			lotteryPredictDao.save(entity);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * @规则A：出次均衡，权重：50%（1、所有：70%，2、1000：16%，3、500：8%，4、200：4%,5、100:2%）
	 * @规则B：遗漏均衡，权重40%（1、所有：70%，2、1000：16%，3、500：8%，4、200：4%,5、100:2%）
	 * @规则C：连出均衡，权重10%（1、所有：70%，2、1000：16%，3、500：8%，4、200：4%,5、100:2%）
	 * 
	 * @param list
	 * @return
	 */
	private String calculateByRules(List<LotterySsqEntity> list) {
		// 规则A：出次均衡
		Map<String, Integer> A1_red = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 0);
		Map<String, Integer> A2_red = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 0);
		Map<String, Integer> A3_red = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 0);
		Map<String, Integer> A4_red = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 0);
		Map<String, Integer> A5_red = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 0);
		Map<String, Integer> A1_blue = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 0);
		Map<String, Integer> A2_blue = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 0);
		Map<String, Integer> A3_blue = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 0);
		Map<String, Integer> A4_blue = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 0);
		Map<String, Integer> A5_blue = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 0);
		// 规则B：遗漏均衡
		Map<String, Integer> B1_red = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 2000);
		Map<String, Integer> B2_red = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 1000);
		Map<String, Integer> B3_red = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 500);
		Map<String, Integer> B4_red = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 200);
		Map<String, Integer> B5_red = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 100);
		Map<String, Integer> B1_blue = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 2000);
		Map<String, Integer> B2_blue = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 1000);
		Map<String, Integer> B3_blue = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 500);
		Map<String, Integer> B4_blue = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 200);
		Map<String, Integer> B5_blue = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 100);
		// 规则C：连出均衡
		LotterySsqEntity lastEntity = null;
		Map<String, Integer> C1_red = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 0);
		Map<String, Integer> C2_red = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 0);
		Map<String, Integer> C3_red = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 0);
		Map<String, Integer> C4_red = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 0);
		Map<String, Integer> C5_red = initMapKeysWithValue(SsqConstant.RED_NUMBERS, 0);
		Map<String, Integer> C1_blue = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 0);
		Map<String, Integer> C2_blue = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 0);
		Map<String, Integer> C3_blue = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 0);
		Map<String, Integer> C4_blue = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 0);
		Map<String, Integer> C5_blue = initMapKeysWithValue(SsqConstant.BLUE_NUMBERS, 0);

		for (int i = 0; i < list.size(); i++) {
			LotterySsqEntity entity = list.get(i);
			String[] redNumbers = entity.getRedNumbers().split(",");
			for (String num : redNumbers) {
				A1_red.put(num, A1_red.get(num) + 1);
				if (B1_red.get(num) == 2000) {
					B1_red.put(num, i);
				}
				if (lastEntity != null && lastEntity.getRedNumbers().contains(num)) {
					C1_red.put(num, C1_red.get(num) + 1);
				}
				if (i < 1000) {
					A2_red.put(num, A2_red.get(num) + 1);
					if (B2_red.get(num) == 1000) {
						B2_red.put(num, i);
					}
					if (lastEntity != null && lastEntity.getRedNumbers().contains(num)) {
						C2_red.put(num, C2_red.get(num) + 1);
					}
				}
				if (i < 500) {
					A3_red.put(num, A3_red.get(num) + 1);
					if (B3_red.get(num) == 500) {
						B3_red.put(num, i);
					}
					if (lastEntity != null && lastEntity.getRedNumbers().contains(num)) {
						C3_red.put(num, C3_red.get(num) + 1);
					}
				}
				if (i < 200) {
					A4_red.put(num, A4_red.get(num) + 1);
					if (B4_red.get(num) == 200) {
						B4_red.put(num, i);
					}
					if (lastEntity != null && lastEntity.getRedNumbers().contains(num)) {
						C4_red.put(num, C4_red.get(num) + 1);
					}
				}
				if (i < 100) {
					A5_red.put(num, A5_red.get(num) + 1);
					if (B5_red.get(num) == 100) {
						B5_red.put(num, i);
					}
					if (lastEntity != null && lastEntity.getRedNumbers().contains(num)) {
						C5_red.put(num, C5_red.get(num) + 1);
					}
				}
			}
			// 蓝球去除幸运球
			String num = entity.getBlueNumber().trim().substring(0, 2);
			A1_blue.put(num, A1_blue.get(num) + 1);
			if (B1_blue.get(num) == 2000) {
				B1_blue.put(num, i);
			}
			if (lastEntity != null && lastEntity.getBlueNumber().equals(num)) {
				C1_blue.put(num, C1_blue.get(num) + 1);
			}
			if (i < 1000) {
				A2_blue.put(num, A2_blue.get(num) + 1);
				if (B2_blue.get(num) == 1000) {
					B2_blue.put(num, i);
				}
				if (lastEntity != null && lastEntity.getBlueNumber().equals(num)) {
					C2_blue.put(num, C2_blue.get(num) + 1);
				}
			}
			if (i < 500) {
				A3_blue.put(num, A3_blue.get(num) + 1);
				if (B3_blue.get(num) == 500) {
					B3_blue.put(num, i);
				}
				if (lastEntity != null && lastEntity.getBlueNumber().equals(num)) {
					C3_blue.put(num, C3_blue.get(num) + 1);
				}
			}
			if (i < 200) {
				A4_blue.put(num, A4_blue.get(num) + 1);
				if (B4_blue.get(num) == 200) {
					B4_blue.put(num, i);
				}
				if (lastEntity != null && lastEntity.getBlueNumber().equals(num)) {
					C4_blue.put(num, C4_blue.get(num) + 1);
				}
			}
			if (i < 100) {
				A5_blue.put(num, A5_blue.get(num) + 1);
				if (B5_blue.get(num) == 100) {
					B5_blue.put(num, i);
				}
				if (lastEntity != null && lastEntity.getBlueNumber().equals(num)) {
					C5_blue.put(num, C5_blue.get(num) + 1);
				}
			}
			lastEntity = entity;
		}
		lastEntity = list.get(0);
		list.clear();
		Map<String, Integer> redMap = new HashMap<String, Integer>();
		for (String key : SsqConstant.RED_NUMBERS) {
			int red_A = A1_red.get(key) * 70 + A2_red.get(key) * 16 + A3_red.get(key) * 8
					+ A4_red.get(key) * 4 + A5_red.get(key) * 2;
			int red_B = B1_red.get(key) * 70 + B2_red.get(key) * 16 + B3_red.get(key) * 8
					+ B4_red.get(key) * 4 + B5_red.get(key) * 2;
			int red_C = C1_red.get(key) * 70 + C2_red.get(key) * 16 + C3_red.get(key) * 8
					+ C4_red.get(key) * 4 + C5_red.get(key) * 2;
			// 连出次数越大，本次几率越小
			if (!lastEntity.getRedNumbers().contains(key)) {
				red_C = 0;
			}
			int value = red_A * 50 + red_B * 40 - red_C * 10;
			redMap.put(key, value);
		}

		Map<String, Integer> blueMap = new HashMap<String, Integer>();
		for (String key : SsqConstant.BLUE_NUMBERS) {
			int blue_A = A1_blue.get(key) * 70 + A2_blue.get(key) * 16 + A3_blue.get(key) * 8
					+ A4_blue.get(key) * 4 + A5_blue.get(key) * 2;
			int blue_B = B1_blue.get(key) * 70 + B2_blue.get(key) * 16 + B3_blue.get(key) * 8
					+ B4_blue.get(key) * 4 + B5_blue.get(key) * 2;
			int blue_C = C1_blue.get(key) * 70 + C2_blue.get(key) * 16 + C3_blue.get(key) * 8
					+ C4_blue.get(key) * 4 + C5_blue.get(key) * 2;
			// 连出次数越大，本次几率越小
			if (!lastEntity.getRedNumbers().contains(key)) {
				blue_C = 0;
			}
			int value = blue_A * 50 + blue_B * 40 - blue_C * 10;
			blueMap.put(key, value);
		}

		List<String> redNumList = new ArrayList<String>();
		int[][] redNums = new int[SsqConstant.RED_NUMBERS.length][2];
		List<Entry<String, Integer>> entries = MapUtil.sortToListByValue(redMap, MapUtil.DESC);
		for (Entry<String, Integer> entry : entries) {

		}

		System.out.println(A1_red);
		System.out.println(A2_red);
		System.out.println(A3_red);
		System.out.println(A4_red);
		System.out.println(A5_red);
		System.out.println(A1_blue);
		System.out.println(A2_blue);
		System.out.println(A3_blue);
		System.out.println(A4_blue);
		System.out.println(A5_blue);
		System.out.println(B1_red);
		System.out.println(B2_red);
		System.out.println(B3_red);
		System.out.println(B4_red);
		System.out.println(B5_red);
		System.out.println(B1_blue);
		System.out.println(B2_blue);
		System.out.println(B3_blue);
		System.out.println(B4_blue);
		System.out.println(B5_blue);
		System.out.println(C1_red);
		System.out.println(C2_red);
		System.out.println(C3_red);
		System.out.println(C4_red);
		System.out.println(C5_red);
		System.out.println(C1_blue);
		System.out.println(C2_blue);
		System.out.println(C3_blue);
		System.out.println(C4_blue);
		System.out.println(C5_blue);
		return null;
	}

	private Map<String, Integer> initMapKeysWithValue(String[] keys, int value) {
		Map<String, Integer> map = new HashMap<String, Integer>();
		for (String key : keys) {
			map.put(key, value);
		}
		return map;
	}

	private String getNextTermNo(LotterySsqEntity entity) throws ParseException {
		Date thisDate = DateUtil._SECOND.parse(entity.getOpenTime());
		int yearOfThisTerm = DateUtil.getYear(thisDate);
		int week = DateUtil.getWeek(thisDate);

		int daysToNextTerm = 2;
		// 周四到下期要三天
		if (week == Calendar.THURSDAY) {
			daysToNextTerm++;
		}
		Date nextDate = DateUtil.addDays(thisDate, daysToNextTerm);
		int yearOfNextTerm = DateUtil.getYear(nextDate);
		if (yearOfNextTerm != yearOfThisTerm) {
			return yearOfNextTerm + START_NO;
		} else {
			return String.valueOf(Integer.valueOf(entity.getTermNo()) + 1);
		}
	}

	@Override
	@Scheduled(cron = "${task.cron.ssq.predict}")
	protected void execute() {
		super.execute();
	}
}
