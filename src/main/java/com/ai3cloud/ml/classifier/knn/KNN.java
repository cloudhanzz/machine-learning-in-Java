package com.ai3cloud.ml.classifier.knn;

import static java.util.Comparator.comparingDouble;
import static java.util.Map.Entry.comparingByValue;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toMap;
import static java.util.Collections.reverseOrder;

import java.util.Arrays;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Objects;
import java.util.function.Function;
import java.util.stream.DoubleStream;

import com.ai3cloud.ml.model.Feature;
import com.ai3cloud.ml.model.Instance;
import com.ai3cloud.util.DistanceWithLabel;
import com.ai3cloud.util.SortOrder;

/**
 * It implements a k-nearest-neighbors classification machine learning
 * algorithm.
 * 
 * @author Jiayun Han
 * @since 1.0.0.0
 */
public class KNN {

	private final Feature[][] dataSet;
	private final String[] labels;

	/**
	 * Constructs an instance of KNN classifier
	 * 
	 * @param classifiedInstances
	 *            The classified instances used to train the model to classify a new
	 *            instance. The following conditions will be checked
	 *            <ul>
	 *            <li>All instances must be categorized
	 *            <li>All instances must have the same number of {@link Feature}s
	 *            </ul>
	 * @throws NullPointerException
	 *             if {@code classifiedInstances} is {@code null}
	 * @throws IllegalArgumntException
	 *             if {@code classifiedInstances} have less than 2
	 * @throws IllegalArgumntException
	 *             if not all instances have the same amount of {@link Feature}s.
	 */
	public KNN(List<Instance> classifiedInstances) {

		Objects.requireNonNull(classifiedInstances);
		verifyInput(classifiedInstances);

		dataSet = classifiedInstances.stream().map(Instance::getFeatures).toArray(Feature[][]::new);
		labels = classifiedInstances.stream().map(Instance::getCategory).toArray(String[]::new);
	}

	private void verifyInput(List<Instance> classifiedData) {

		if (classifiedData.size() < 2) {
			throw new IllegalArgumentException("At least 2 training records are required.");
		}

		Instance record = classifiedData.get(0);

		for (int i = 1; i < classifiedData.size(); i++) {
			Instance other = classifiedData.get(i);
			record.validate(other);
		}
	}

	/**
	 * Classifies the unclassified instance
	 * 
	 * @param unclassified
	 *            The new unclassified {@link Instance} to be classified
	 * @param k
	 *            The number of neighbors to be used
	 * @return The computed category
	 */
	public String classify(Instance unclassified, int k) {

		double[][] diffs = squareDiffs(unclassified.getFeatures(), dataSet);
		double[] distances = calcDistances(diffs);

		// here
		Map<String, Long> neighbors = findNeighbors(distances, labels, k);
		Map<String, Long> sortedLabels = sortByValue(neighbors, SortOrder.DESCENDING);

		return getFirstKey(sortedLabels);
	}

	private double[][] squareDiffs(Feature[] input, Feature[][] dataSet) {

		int rows = dataSet.length;
		int cols = input.length;

		double[][] squareDiffs = new double[rows][cols];

		for (int r = 0; r < rows; r++) {

			Feature[] currRow = dataSet[r];
			double[] diffRow = new double[cols];

			for (int c = 0; c < cols; c++) {
				double diff = currRow[c].getValue() - input[c].getValue();
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
				.collect(groupingBy(Function.identity(), counting()));
	}

	private Map<String, Long> sortByValue(Map<String, Long> map, SortOrder sortOrder) {

		Comparator<? super Entry<String, Long>> comparator = //
				sortOrder == SortOrder.DESCENDING ? //
				reverseOrder(comparingByValue()) : //
				comparingByValue();

		return map.entrySet() //
				.stream() //
				.sorted(comparator)//
				.collect(toMap(Entry::getKey, Entry::getValue, (e1, e2) -> e2, LinkedHashMap::new));
	}

	private String getFirstKey(Map<String, Long> map) {
		return map.keySet().iterator().next();
	}
}
