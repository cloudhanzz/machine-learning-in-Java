package com.ai3cloud.util;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * 
 * @author Jiayun Han
 * @since 1.1.0.0
 *
 */
public class LogCalculatorTest {

	@Test
	public void testLog2() {
		
		double expected = 1.3219280948873624;
		double number = 2.5;
		
		double actual = LogCalculator.log2(number);

		assertEquals(expected, actual, 0.00000001);
	}
}
