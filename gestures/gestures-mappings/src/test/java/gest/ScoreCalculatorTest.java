package gest;

import static gest.Compass.*;

import java.awt.Point;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class ScoreCalculatorTest {

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	@Test
	public void testComputeScore() {
			Gesture up = new Gesture(NORTH, NORTH);
			Gesture upRight = new Gesture(NORTH, NORTH_EAST);
			Gesture upLeft= new Gesture(NORTH, NORTH_WEST);
			
			Gesture right = new Gesture(EAST, EAST);
			//Gesture left = new Gesture(WEST, WEST);
			Gesture down = new Gesture(SOUTH, SOUTH);
			
			Gesture[] vocabulary = {up, upRight, upLeft, right, down};
//			Gesture[] vocabulary = {left, up, right, upRight, upLeft};
			ScoreCalculator sc = new ScoreCalculator(vocabulary);
			
			
			MenuOption save 		= new MenuOption("save", 475, 148);
			MenuOption save_as 		= new MenuOption("save as", 414, 219);
			MenuOption save_as_html = new MenuOption("save_html", 547, 210);
			MenuOption open 		= new MenuOption("open", 424, 460);
			MenuOption close 		= new MenuOption("close", 538, 461);
			
			MenuOption[] points = {save, save_as, save_as_html, open, close};
			sc.computeScore(points);
			
			List<List<MenuOption>> allPermutations = allPermutations(Arrays.asList(points));
			LOG.info("nr permutations = " + allPermutations.size());
			
			double minScore = Double.MAX_VALUE;
			double maxScore = Double.MIN_VALUE;
			MenuOption[] winningOptions = null;
			for (List<MenuOption> permList : allPermutations) {
				MenuOption[] perm = (MenuOption[]) permList.toArray(new MenuOption[permList.size()]);
				double score = sc.computeScore(perm);
				LOG.info("crt score : " + score);
				if (score < minScore) { minScore = score; winningOptions = perm; }
				if (score > maxScore) maxScore = score;
				
			}
			
			LOG.info("min score" + minScore);
			LOG.info("max score" + maxScore);
			
			LOG.info("for gestures " + ToStringBuilder.reflectionToString(vocabulary));
			LOG.info("winning options: " + ToStringBuilder.reflectionToString(winningOptions));
	}

	static List<List<MenuOption>> allPermutations(List<MenuOption> points) {
		if (points.size() == 1) {
			ArrayList<List<MenuOption>> result = new ArrayList<List<MenuOption>>();
			result.add(points);
			return result;
		}
		
		List<List<MenuOption>> result = new ArrayList<List<MenuOption>>();
		
		for (MenuOption p : points) {
			List<MenuOption> remainder = getRemainder(points, p);
			List<List<MenuOption>> remainderPermutations = allPermutations(remainder);
			for (List<MenuOption> remainderPermutation : remainderPermutations) {
				List<MenuOption> elem = new ArrayList<MenuOption>();
				elem.add(p);
				elem.addAll(remainderPermutation);
				result.add(elem);
			}
		}
		return result;
	}

	private static List<MenuOption> getRemainder(List<MenuOption> points, MenuOption pointToExclude) {
		ArrayList<MenuOption> result = new ArrayList<MenuOption>(points.size() - 1);
		for (MenuOption p : points) 
			if (!p.equals(pointToExclude))
				result.add(p);
		return result;
	}
	
	static Logger LOG = Logger.getLogger(ScoreCalculatorTest.class);
}
