package com.ai3cloud.ml.classifier;

import static java.util.function.Function.identity;
import static java.util.stream.Collectors.counting;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai3cloud.ml.model.GenericInstance;
import com.ai3cloud.util.LogCalculator;
import com.ai3cloud.util.Tool;

/**
 * It implements a decision tree classification machine learning algorithm.
 * 
 * @author Jiayun Han
 * @since 1.1.0.0
 */
public class DecisionTree<T> {

	private static final Logger LOGGER = LoggerFactory.getLogger(DecisionTree.class);

	public String classify(Map<String, Map<T, Object>> tree, GenericInstance<T> instance, List<String> labels) {

		List<T> features = instance.getFeatures();

		for (int i = 0; i < labels.size(); i++) {

			String label = labels.get(i);

			if (tree.containsKey(label)) {

				Map<T, Object> valueTree = tree.get(label);
				T feature = features.get(i);

				Object result = valueTree.get(feature);
				if (result instanceof String) {
					return (String) result;
				} else {
					@SuppressWarnings("unchecked")
					Map<String, Map<T, Object>> labelTree = (Map<String, Map<T, Object>>) result;
					return classify(labelTree, instance, labels);
				}
			}
		}

		return null;
	}

	// label -> {featValue -> decision; eatValue -> labeled_tree}
	public Map<String, Map<T, Object>> createTree(List<GenericInstance<T>> instances, List<String> labels) {

		LOGGER.info("Calling createTree with {} features and {} lables", instances.get(0).getFeatures().size(),
				labels.size());

		// Find best feature
		int index = chooseFeature(instances);
		String label = labels.get(index);

		Map<T, Object> valueTree = createValueTree(instances, labels, index);

		Map<String, Map<T, Object>> tree = new HashMap<>();
		tree.put(label, valueTree);

		return tree;
	}

	private int chooseFeature(List<GenericInstance<T>> instances) {

		int numOfInstances = instances.size();
		int numOfFeatures = instances.get(0).getFeatures().size();

		double minMessiness = Double.MAX_VALUE;
		int bestFeatureIndex = 0;

		for (int i = 0; i < numOfFeatures; i++) {

			final int index = i;
			Set<T> values = instances.stream() //
					.map(instance -> instance.getFeatures().get(index)) //
					.collect(toSet());

			double messiness = 0;

			for (T value : values) {
				List<GenericInstance<T>> group = partition(instances, index, value);
				double prob = (double) group.size() / numOfInstances;
				double entropy = computeEntropy(group);
				entropy *= prob;
				messiness += entropy;
			}

			if (Double.compare(messiness, minMessiness) < 0) {
				minMessiness = messiness;
				bestFeatureIndex = index;
			}
		}

		return bestFeatureIndex;
	}

	/**
	 * 
	 * @param instances
	 *            The classified instances
	 * @return
	 */
	private double computeEntropy(List<GenericInstance<T>> instances) {

		double entropy = 0.0;
		int total = instances.size();

		Map<String, Long> freqMap = instances //
				.stream() //
				.map(GenericInstance::getKlass) //
				.collect(groupingBy(identity(), counting()));

		for (Long freq : freqMap.values()) {
			double prob = (double) freq / total;
			double log2 = LogCalculator.log2(prob);
			double ent = prob * log2;
			entropy -= ent;
		}

		return entropy;
	}

	private List<GenericInstance<T>> partition(List<GenericInstance<T>> instances, int index, T value) {

		return instances.stream() //
				.filter(instance -> instance.getFeatures().get(index).equals(value)) //
				.collect(toList());
	}

	private Map<T, Object> createValueTree(List<GenericInstance<T>> instances, List<String> labels, int index) {

		Map<T, Object> valueTree = new HashMap<>();
		Set<T> features = findFeatureValues(instances, index);

		for (T feature : features) {
			List<GenericInstance<T>> group = partition(instances, index, feature);

			Object result = onPartition(group, labels, index);
			valueTree.put(feature, result);
		}

		return valueTree;
	}

	private Set<T> findFeatureValues(List<GenericInstance<T>> instances, int featureIndex) {
		return instances.stream() //
				.map(instance -> instance.getFeatures().get(featureIndex)) //
				.collect(toSet());
	}

	private Object onPartition(List<GenericInstance<T>> group, List<String> labels, int index) {
		if (group.size() == 1) {
			return group.get(0).getKlass();
		}

		Set<String> categories = group.stream().map(GenericInstance::getKlass).collect(toSet());
		if (categories.size() == 1) {
			return group.get(0).getKlass();
		}

		if (group.get(0).getFeatures().size() == 1) {
			return Tool.getMostFrequent(group.stream().map(GenericInstance::getKlass).collect(toList()));
		}

		labels = Tool.copyAndSkip(labels, index);
		group = group.stream().map(instance -> instance.copyAndSkip(index)).collect(toList());

		return createTree(group, labels);
	}
}
