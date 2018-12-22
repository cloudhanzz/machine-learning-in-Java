package com.ai3cloud.ml.classifier;

import static java.util.Comparator.comparingDouble;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.DoubleStream;

import com.ai3cloud.ml.model.NumericInstance;
import com.ai3cloud.util.DistanceWithLabel;
import com.ai3cloud.util.SortOrder;
import com.ai3cloud.util.Tool;

/**
 * It implements a k-nearest-neighbors classification machine learning
 * algorithm.
 * 
 * @author Jiayun Han
 * @since 1.0.0.0
 */
public class KNN {

	private final double[][] dataSet;
	private final String[] labels;

	/**
	 * Constructs an instance of KNN classifier
	 * 
	 * @param classifiedInstances
	 *            The classified instances used to train the model to classify a new
	 *            instance. The following conditions will be checked
	 *            <ul>
	 *            <li>All instances must be categorized
	 *            <li>All instances must have the same number of {@link NumericFeature}s
	 *            </ul>
	 * @throws NullPointerException
	 *             if {@code classifiedInstances} is {@code null}
	 * @throws IllegalArgumntException
	 *             if {@code classifiedInstances} have less than 2
	 * @throws IllegalArgumntException
	 *             if not all instances have the same amount of {@link NumericFeature}s.
	 */
	public KNN(List<NumericInstance> classifiedInstances) {
		dataSet = classifiedInstances.stream().map(NumericInstance::getFeatures).toArray(double[][]::new);
		labels = classifiedInstances.stream().map(NumericInstance::getKlass).toArray(String[]::new);
	}

	/**
	 * Classifies the unclassified instance
	 * 
	 * @param unclassified
	 *            The new unclassified {@link NumericInstance} to be classified
	 * @param k
	 *            The number of neighbors to be used
	 * @return The computed category
	 */
	public String classify(NumericInstance unclassified, int k) {

		double[][] diffs = squareDiffs(unclassified.getFeatures(), dataSet);
		double[] distances = calcDistances(diffs);

		Map<String, Long> neighbors = findNeighbors(distances, labels, k);
		Map<String, Long> sortedLabels = Tool.sortByValue(neighbors, SortOrder.DESCENDING);

		return Tool.getFirstKey(sortedLabels);
	}

	private double[][] squareDiffs(double[] input, double[][] dataSet) {

		int rows = dataSet.length;
		int cols = input.length;

		double[][] squareDiffs = new double[rows][cols];

		for (int r = 0; r < rows; r++) {

			double[] currRow = dataSet[r];
			double[] diffRow = new double[cols];

			for (int c = 0; c < cols; c++) {
				double diff = currRow[c] - input[c];
				diffRow[c] = diff * diff;
			}

			squareDiffs[r] = diffRow;
		}

		return squareDiffs;
	}

	private double[] calcDistances(double[][] diffs) {
		return Arrays.stream(diffs) //
				.map(Arrays::stream) //
				.map(DoubleStream::sum) //
				.map(Math::sqrt) //
				.mapToDouble(e -> e) //
				.toArray();
	}

	private Map<String, Long> findNeighbors(double[] distances, String[] labels, int k) {

		DistanceWithLabel[] labeledDistances = new DistanceWithLabel[distances.length];
		for (int i = 0; i < distances.length; i++) {
			DistanceWithLabel dl = new DistanceWithLabel(distances[i], labels[i]);
			labeledDistances[i] = dl;
		}

		return Arrays.stream(labeledDistances)//
				.sorted(comparingDouble(DistanceWithLabel::getDistance))//
				.limit(k) //
				.map(DistanceWithLabel::getLabel) //
				.collect(groupingBy(identity(), counting()));
	}
}
