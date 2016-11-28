/**
 * 
 */
package win.caicaikan.repository.mongodb.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @Desc 权值计算规则
 * @author wewenge.yan
 * @Date 2016年11月22日
 * @ClassName SsqLotteryEntity
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class RuleWeight extends BaseEntity {
	private static final long serialVersionUID = 1L;
	/**
	 * 统计最近期数：0代表所有
	 */
	private int nearlyCount;
	/**
	 * 权值
	 */
	private int weight;
}
