package com.ai3cloud.ml.model;

import java.util.List;

import com.ai3cloud.util.Tool;

/**
 * Modeling an instance for system to learn or classify. An instance has a bunch
 * of {@link NumericFeature}s. An instance with no category is to be classified while
 * an instance with category is used to train or test a model.
 * 
 * @author Jiayun Han
 * @since 1.0.0.0
 */
public class GenericInstance<T> extends InstanceParent {

	private final List<T> features;

	public GenericInstance(List<T> features) {
		this.features = features;
	}

	public GenericInstance(List<T> features, String klass) {
		super(klass);
		this.features = features;
	}

	/**
	 * Returns the features of this instance
	 * 
	 * @return The features of this instance; it should never be {@code null} or
	 *         have less than 2 features
	 */
	public List<T> getFeatures() {
		return features;
	}
	
	public GenericInstance<T> copyAndSkip(int index) {
		List<T> features2 = Tool.copyAndSkip(features, index);
		return new GenericInstance<T>(features2, getKlass());
	}
}
