/**
 * 
 */
package win.caicaikan.util;

import java.math.BigInteger;

import lombok.Data;

/**
 * @desc
 * @author yanwenge
 * @date 2017年1月8日
 * @class CalculateUtil
 */
public class MathUtil {

	public static BigInteger C(BigInteger n, BigInteger m) {
		if (n == null || m == null || n.compareTo(BigInteger.ZERO) <= 0 || n.compareTo(BigInteger.ZERO) < 0 || m.compareTo(n) > 0) {
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

	@Data
	public static class GaussianParam {
		private Double mean;
		private Double variance;
	}

	/**
	 * 计算高斯分布值
	 * 
	 * @param mean 期望
	 * @param variance 方差，开根号为标准差!!!!!
	 * @return
	 */
	public static Double calculateGaussianDistribution(GaussianParam gaussianParam, Double x) {
		Double mean = gaussianParam.getMean();
		Double variance = gaussianParam.getVariance();
		if (mean == null || variance == null || x == null) {
			throw new IllegalArgumentException("mean,variance,x不允许为null！");
		}
		return Math.exp(0 - Math.pow((x - mean), 2) / variance / 2) / Math.sqrt(2 * Math.PI * variance);
	}

	public static GaussianParam calculateGaussianParam(String[] numbers) {
		GaussianParam gaussianParam = new GaussianParam();
		Double count = 0D;
		for (String numberString : numbers) {
			count += Double.valueOf(numberString);
		}
		double mean = count / numbers.length;
		gaussianParam.setMean(mean);
		count = 0D;
		for (String numberString : numbers) {
			count += Math.pow(Double.valueOf(numberString) - mean, 2);
		}
		gaussianParam.setVariance(count / numbers.length);
		return gaussianParam;
	}

	public static void main(String[] args) {
		// BigInteger a = C(new BigInteger("8"), new BigInteger("6"));
		// BigInteger b = C(new BigInteger("33"), new BigInteger("8"));
		// BigInteger c = C(new BigInteger("33"), new BigInteger("6"));
		// BigInteger d = C(new BigInteger("27"), new BigInteger("8"));
		// BigInteger e = C(new BigInteger("27"), new BigInteger("6"));
		// BigInteger f = C(new BigInteger("27"), new BigInteger("2"));
		// BigInteger g = C(new BigInteger("25"), new BigInteger("8"));
		// BigInteger h = C(new BigInteger("25"), new BigInteger("6"));
		// BigInteger i = C(new BigInteger("25"), new BigInteger("2"));
		// System.out.println("a=" + a);
		// System.out.println("b=" + b);
		// System.out.println("c=" + c);
		// System.out.println("d=" + d);
		// System.out.println("e=" + e);
		// System.out.println("f=" + f);
		// System.out.println("g=" + g);
		// System.out.println("h=" + h);
		// System.out.println("i=" + i);
		// System.out.println(b.divide(c));
		// System.out.println(C(new BigInteger("14"), new BigInteger("6")));

		String[] numbers = { "2", "3", "3", "4", "5", "6" };
		GaussianParam gaussianParam = calculateGaussianParam(numbers);
		System.out.println(gaussianParam);
		Double rate = calculateGaussianDistribution(gaussianParam, 2.0);
		System.out.println(rate);
		rate = calculateGaussianDistribution(gaussianParam, 3.0);
		System.out.println(rate);
		rate = calculateGaussianDistribution(gaussianParam, 4.0);
		System.out.println(rate);
		rate = calculateGaussianDistribution(gaussianParam, 5.0);
		System.out.println(rate);
		rate = calculateGaussianDistribution(gaussianParam, 6.0);
		System.out.println(rate);
	}
}
