/**
 * 
 */
package win.caicaikan.task.ssq;

import java.util.List;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;

import win.caicaikan.BaseTest;
import win.caicaikan.repository.mongodb.entity.ssq.SsqPredictEntity;
import win.caicaikan.repository.mongodb.entity.ssq.SsqResultEntity;
import win.caicaikan.service.internal.DaoService;

/**
 * @Desc
 * @author wewenge.yan
 * @Date 2016年11月25日
 * @ClassName LotterySsqCurrentTaskTest
 */
public class SsqCurrentTaskTest extends BaseTest {

	@Autowired
	private SsqCurrentTask ssqCurrentTask;
	@Autowired
	private DaoService daoService;

	/**
	 * Test method for {@link win.caicaikan.task.ssq.SsqCurrentTask#execute()}.
	 */
	@Test
	public void testExecute() {
		ssqCurrentTask.execute();
	}

	@Test
	public void testSaveRightNumbers() {
		List<SsqResultEntity> ssqResults = daoService.query(null, SsqResultEntity.class);
		List<SsqPredictEntity> ssqPredicts = daoService.query(null, SsqPredictEntity.class);
		for (SsqPredictEntity ssqPredict : ssqPredicts) {
			for (SsqResultEntity ssqResult : ssqResults) {
				if (StringUtils.isEmpty(ssqPredict.getRightNumbers())
						|| ssqPredict.getTermNo().equals(ssqResult.getTermNo())) {
					String rightNumPoses = getRightNumPoses(ssqPredict, ssqResult);
					ssqPredict.setRightNumbers(rightNumPoses);
					for (int i = 0; i < 5; i++) {
						try {
							Thread.sleep(1000);
							daoService.save(ssqPredict);
							continue;
						} catch (Exception e) {
							if (i == 5) {
								System.out.println("fail:" + ssqPredict);
							}
						}
					}
				}
			}
		}
	}

	private String getRightNumPoses(SsqPredictEntity ssqPredict, SsqResultEntity ssqResult) {
		String[] redNumbers = ssqResult.getRedNumbers().split(",");
		StringBuilder rightPoses = new StringBuilder();
		for (String rightRedNumber : redNumbers) {
			for (int i = 0; i < ssqPredict.getRedNumbers().size(); i++) {
				String predictRedNumber = ssqPredict.getRedNumbers().get(i);
				if (rightRedNumber.equals(predictRedNumber.split("=")[0])) {
					rightPoses.append(",").append(i + 1);
					break;
				}
			}
		}
		rightPoses.append("+");
		String rightBlueNumber = ssqResult.getBlueNumbers().split(",")[0];
		for (int i = 0; i < ssqPredict.getBlueNumbers().size(); i++) {
			String predictBlueNumber = ssqPredict.getBlueNumbers().get(i);
			if (rightBlueNumber.equals(predictBlueNumber.split("=")[0])) {
				rightPoses.append(i + 1);
				break;
			}
		}
		return rightPoses.substring(1);
	}

}
