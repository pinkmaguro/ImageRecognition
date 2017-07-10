package org.pinkmaguro.math.matrix;

public class SMatrixMath {
	
	public final static int ROW = 1;
	public final static int COL = 2;
	

	// find max value position in nth row or col
	public static int maxPosition(SMatrix matrix, int nth, int dim) {
		int rows = matrix.getNumRows();
		int cols = matrix.getNumCols();
		
		double max = -Double.MAX_VALUE;
		int pos = -1;
		
		if (dim == ROW) {
			for (int j = 1; j <= cols; j++) {
				if (matrix.get(nth, j) > max) {
					max = matrix.get(nth, j);
					pos = j;
				}
			}
			
		} else if (dim == COL) {
			for (int i = 1; i <= rows; i++) {
				if (matrix.get(i, nth) > max) {
					max = matrix.get(i, nth);
					pos = i;
				}
			}
		}
		return pos;
	}
}
