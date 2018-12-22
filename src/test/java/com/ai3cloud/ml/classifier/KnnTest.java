package com.ai3cloud.ml.classifier;

import static org.junit.Assert.assertEquals;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.ai3cloud.ml.model.NumericInstance;

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

		List<NumericInstance> classifiedInstances = new ArrayList<>();

		classifiedInstances.add(new NumericInstance(new double[] { 1.0, 1.1 }, "A"));
		classifiedInstances.add(new NumericInstance(new double[] { 1.0, 1.0 }, "A"));
		classifiedInstances.add(new NumericInstance(new double[] { 0, 0 }, "B"));
		classifiedInstances.add(new NumericInstance(new double[] { 0, 0.1}, "B"));

		NumericInstance newRecord = new NumericInstance(new double[] { 0, 0 });
		
		KNN knn = new KNN(classifiedInstances);
		String actual = knn.classify(newRecord, 3);

		assertEquals(expected, actual);
	}
}
