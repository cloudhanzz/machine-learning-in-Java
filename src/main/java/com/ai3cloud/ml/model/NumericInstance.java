package com.ai3cloud.ml.model;

/**
 * Modeling an instance for system to learn or classify. An instance has a bunch
 * of {@link NumericFeature}s. An instance with no category is to be classified while
 * an instance with category is used to train or test a model.
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
