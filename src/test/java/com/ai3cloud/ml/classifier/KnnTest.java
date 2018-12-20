package com.ai3cloud.ml.classifier;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ai3cloud.ml.classifier.knn.KNN;
import com.ai3cloud.ml.model.Feature;
import com.ai3cloud.ml.model.Instance;

/**
 * 
 * @author Jiayun Han
 * @since 1.0.0.0
 *
 */
public class KnnTest {

	@Test
	public void classify() {
		
		String expected = "B";

		List<Instance> classifiedInstances = new ArrayList<>();

		classifiedInstances.add(Instance.classified(new Feature[] { () -> 1.0, () -> 1.1 }, "A"));
		classifiedInstances.add(Instance.classified(new Feature[] { () -> 1.0, () -> 1.0 }, "A"));
		classifiedInstances.add(Instance.classified(new Feature[] { () -> 0, () -> 0 }, "B"));
		classifiedInstances.add(Instance.classified(new Feature[] { () -> 0, () -> 0.1 }, "B"));

		Instance newRecord = Instance.unclassified(new Feature[] { () -> 0, () -> 0 });
		
		KNN knn = new KNN(classifiedInstances);
		String actual = knn.classify(newRecord, 3);

		assertEquals(expected, actual);
	}
}
