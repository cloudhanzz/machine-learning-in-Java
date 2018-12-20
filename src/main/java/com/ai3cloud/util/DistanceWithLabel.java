package com.ai3cloud.util;

/**
 * A helper class
 * 
 * @since 1.0.0.0
 * @author Jiayun Han
 *
 */
public class DistanceWithLabel {

	private final double distance;
	private final String label;

	public DistanceWithLabel(double distance, String label) {
		super();
		this.distance = distance;
		this.label = label;
	}

	public double getDistance() {
		return distance;
	}

	public String getLabel() {
		return label;
	}
}
