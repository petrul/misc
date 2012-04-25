package gest;

import java.awt.Point;

import org.apache.log4j.Logger;

public class ScoreCalculator {
	
	Gesture[] gestures;
	double[][] gest_distances;
	double[][] gest_distances_normalized;
	
	public ScoreCalculator(Gesture[] gestures) {
		
		this.gestures = gestures;
		
		Gesture[] vocabulary = gestures; // alias
		
		int nrGestures = vocabulary.length;
		
		double[][] gest_distances = new double[nrGestures][nrGestures];
		double sum_gest_distance = 0.0;
		for (int i = 0; i < nrGestures; i++) {
			Gesture dest = vocabulary[i];
			for (int j = 0; j < nrGestures; j++) {
				Gesture src = vocabulary[j];
				
				int dist = dest.distanceFrom(src);
				sum_gest_distance += dist;
				gest_distances[i][j] = gest_distances[j][i] = dist;
			}
		}
		
		printMatrix(gest_distances, "\nGestures distances");
		
		gest_distances_normalized = new double[nrGestures][nrGestures];
		for (int i = 0; i < nrGestures; i++) {
			for (int j = 0; j < nrGestures; j++) {
				gest_distances_normalized[i][j] = gest_distances[i][j] / sum_gest_distance;
			}
		}
		
		printMatrix(gest_distances_normalized, "\nGestures distances normalized");
		
	}
	
	/**
	 * Computes the score of the affectation of options to gestures. 
	 */
	public double computeScore(MenuOption[] options) {
		
		Point[] points = new Point[options.length];
		for (int i = 0; i < options.length; i++)
			points[i] = options[i].point;
		
		int nrPoints = points.length;
		if (nrPoints != this.gestures.length)
			throw new IllegalArgumentException("should have the same number of gestures and options");
		
		double[][] option_distances = new double[nrPoints][nrPoints];
		double[][] option_distances_normalized = new double[nrPoints][nrPoints];
		double sum_option_distances = 0.0;
		for (int i = 0; i < nrPoints; i++) {
			Point p_i = points[i];
			for (int j = i; j < nrPoints; j++) {
				Point p_j = points[j];
				double dx = p_i.x - p_j.x;
				double dy = p_i.y - p_j.y;
				
				double dist = Math.sqrt(dx * dx + dy * dy);
				sum_option_distances += dist;
				
				option_distances[i][j] = dist;
				option_distances[j][i] = dist;
			}
		}
		
		printMatrix(option_distances, "Option distances");
		
		for (int i = 0; i < nrPoints; i++) {
			for (int j = 0; j < nrPoints; j++) {
				option_distances_normalized[i][j] = option_distances[i][j] / sum_option_distances;
			}
		}

		printMatrix(option_distances_normalized, "Option distances normalized");
		
		double score = 0.0;
		for (int i = 0; i < nrPoints; i++) {
			for (int j = i + 1; j < nrPoints; j++) {
				score += this.gest_distances_normalized[i][j] / option_distances_normalized[i][j];
			}
		}
		
		LOG.debug("returning " + score);
		return score;
	}
	
	public static void printMatrix(double[][] matrix, String title) {
		System.out.println("\n\n*** " + title);
		for (int i = 0; i < matrix.length; i++) {
			System.out.println();
			for (int j = 0; j < matrix[0].length; j++) {
				System.out.printf("%10.2f ", matrix[i][j]);
			}
		}
	}

	static Logger LOG = Logger.getLogger(ScoreCalculator.class);
}
