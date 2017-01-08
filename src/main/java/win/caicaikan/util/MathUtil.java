/**
 * 
 */
package win.caicaikan.util;

import java.math.BigInteger;

/**
 * @desc
 * @author yanwenge
 * @date 2017年1月8日
 * @class CalculateUtil
 */
public class MathUtil {

	public static BigInteger C(BigInteger n, BigInteger m) {
		if (n == null || m == null || n.compareTo(BigInteger.ZERO) <= 0
				|| n.compareTo(BigInteger.ZERO) < 0 || m.compareTo(n) > 0) {
			throw new RuntimeException("n必须大于0，m必须大于等于0，n必须大于m。实际：n=" + n + ",m=" + m);
		}
		if (BigInteger.ZERO.compareTo(m) == 0 || n.compareTo(m) == 0) {
			return BigInteger.ONE;
		}
		return A(n).divide(A(m)).divide(A(n.subtract(m)));
	}

	/**
	 * 阶乘
	 * 
	 * @param n
	 * @return
	 */
	public static BigInteger A(BigInteger n) {
		if (n == null || n.compareTo(BigInteger.ZERO) <= 0) {
			throw new RuntimeException("n必须大于0，实际：n=" + n);
		}
		if (BigInteger.ONE.compareTo(n) == 0) {
			return BigInteger.ONE;
		} else {
			return n.multiply(A(n.subtract(BigInteger.ONE)));
		}
	}
}
