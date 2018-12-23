package com.ai3cloud.ml.classifier;

import static java.util.stream.Collectors.toList;
import static org.junit.Assert.assertEquals;

import java.io.BufferedReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.ai3cloud.ml.model.GenericInstance;

/**
 * 
 * @author Jiayun Han
 * @since 1.1.0.0
 *
 */
public class DecisionTreeTest {
	
	private static final Logger LOGGER = LoggerFactory.getLogger(DecisionTreeTest.class);
	
	@Test
	public void createTreeAndTestClassifier() throws IOException, URISyntaxException {
		
		List<String> labels = null;
		List<GenericInstance<String>> instances = new ArrayList<>();		
		Path path = Paths.get(Thread.currentThread().getContextClassLoader().getResource("lenses.txt").toURI());
		
		try (BufferedReader br = Files.newBufferedReader(path)) {
			
			int i = 0;
			String line;
			while((line = br.readLine()) != null) {
				line = line.trim();
				List<String> list = Arrays.stream(line.split("\t+")).collect(toList());
				List<String> sublist = list.subList(0, list.size() - 1);
				if(i == 0) {
					labels = sublist;
				}else {
					String klass = list.get(list.size() - 1);
					GenericInstance<String> instance = new GenericInstance<>(sublist, klass);
					instances.add(instance);
				}
				
				i++;
			}
		}
		
		DecisionTree<String> decisionTree = new DecisionTree<>();
		Map<String, Map<String, Object>> tree = decisionTree.createTree(instances, labels);		

		LOGGER.info("Built Decision Tree as:\n{}", tree);
		
		for(GenericInstance<String> instance : instances) {
			String realKlass = instance.getKlass();
			String result = decisionTree.classify(tree, instance, labels);
			assertEquals(realKlass, result);
		}
	}
}
