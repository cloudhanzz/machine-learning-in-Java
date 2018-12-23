package com.ai3cloud.ml.model;

/**
 * Modeling an instance whose features are represented by floating point numbers.
 * 
 * @author Jiayun Han
 * @since 1.0.0.0
 */
public class NumericInstance extends InstanceParent {

	private final double[] features;

	public NumericInstance(double[] features) {		
		this.features = features;
	}

	public NumericInstance(double[] features, String klass) {
		super(klass);
		this.features = features;
	}

	/**
	 * Returns the features of this instance
	 * 
	 * @return The features of this instance; it should never be
	 *         {@code null or empty}
	 */
	public double[] getFeatures() {
		return features;
	}
}
