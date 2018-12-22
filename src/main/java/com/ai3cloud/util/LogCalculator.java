package com.ai3cloud.util;

public final class LogCalculator {
	
	private LogCalculator() {		
	}

	public static double log2(double number) {
		return logb(number, 2);
	}

	public static double log(double number, double base) {
		return logb(number, base);
	}

	private static double logb(double number, double base) {
		return Math.log(number) / Math.log(base);
	}
}
