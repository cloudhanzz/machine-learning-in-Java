package com.ai3cloud.ml.model;

import java.util.Objects;

/**
 * Modeling an instance for system to learn or classify. An instance has a bunch
 * of {@link Feature}s. An instance with no category is to be classified while
 * an instance with category is used to train or test a model.
 * 
 * @author Jiayun Han
 * @since 1.0.0.0
 */
public class Instance {

	private final Feature[] features;
	private String category;

	private Instance(Feature[] features) {
		validate(features);
		this.features = features;
	}

	private Instance(Feature[] features, String klass) {
		validate(features);
		this.features = features;
		this.category = Objects.requireNonNull(klass);
	}

	/**
	 * Returns the features of this instance
	 * 
	 * @return The features of this instance; it should never be
	 *         {@code null or empty}
	 */
	public Feature[] getFeatures() {
		return features;
	}

	/**
	 * Returns the category of this instance
	 * 
	 * @return The category of this instance if it is classified; {@code null}
	 *         otherwise
	 */
	public String getCategory() {
		return category;
	}

	private void validate(Feature[] features) {
		Objects.requireNonNull(features);
		if (features.length < 1) {
			throw new IllegalArgumentException("At least 1 feature is required.");
		}
	}

	/**
	 * Validates the passed-in instance against this instance
	 * 
	 * @param other
	 *            The instance to be validated
	 * @throws IllegalArgumntException
	 *             if {@code other} has different number of {@code features} from
	 *             this instance
	 */
	public void validate(Instance other) {
		if (this.features.length != other.features.length) {
			throw new IllegalArgumentException("Records must have the same number of features.");
		}
	}

	/**
	 * Returns an unclassified instance
	 * 
	 * @param features
	 *            The features of the instance to be returned
	 * @return An unclassified instance
	 * @throws NullPointerException
	 *             if {@code features} is {@code null}
	 * @throws IllegalArgumntException
	 *             if {@code features} is {@code empty}
	 */
	public static Instance unclassified(Feature[] features) {
		return new Instance(features);
	}

	/**
	 * Returns a classified instance
	 * 
	 * @param features
	 *            The features of the instance to be returned
	 * @param category
	 *            The category of this instance to be returned
	 * @return A classified instance
	 * @throws NullPointerException
	 *             if {@code features} is {@code null}
	 * @throws IllegalArgumntException
	 *             if {@code features} is {@code empty}
	 * @throws NullPointerException
	 *             if {@code category} is {@code null}
	 */
	public static Instance classified(Feature[] features, String category) {
		return new Instance(features, category);
	}
}
