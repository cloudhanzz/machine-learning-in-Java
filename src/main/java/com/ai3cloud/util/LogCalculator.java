package com.ai3cloud.util;

/**
 * An utility class to calculate the logarithm of a number of any base. This
 * motivation for this is that Java only provides natural logarithm and base-10
 * logarithm.
 * 
 * @author Jiayun Han
 *
 */
public final class LogCalculator {

	private LogCalculator() {
	}

	/**
	 * Returns the base-2 logarithm of the given number
	 * 
	 * @param number
	 *            The number whose base-2 logarithm to be calculated
	 * @return The base-2 logarithm of the given number
	 */
	public static double log2(double number) {
		return logb(number, 2);
	}

	/**
	 * Returns the logarithm of the given number of the required base
	 * 
	 * @param number
	 *            The number whose logarithm of the specified base to be calculated
	 * @return The logarithm of the given number of the required base
	 */
	public static double log(double number, double base) {
		return logb(number, base);
	}

	private static double logb(double number, double base) {
		return Math.log(number) / Math.log(base);
	}
}
