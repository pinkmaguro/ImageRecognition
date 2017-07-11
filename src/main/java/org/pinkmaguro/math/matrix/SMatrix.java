package org.pinkmaguro.math.matrix;

import static org.pinkmaguro.machinelearning.imagerecognition.gui.data.GUICommonData.*;

import java.util.Arrays;

import org.pinkmaguro.math.regression.RegMath;


public class SMatrix {


	private int numRows;
	private int numCols;

	private double[][] data;
	
	public SMatrix(int numRows, int numCols) {
		initSMatrix(numRows, numCols);
	}
	
	private void initSMatrix(int numRows, int numCols) {
		this.numRows = numRows;
		this.numCols = numCols;
		data = new double[numRows][numCols];
	}
	
	public SMatrix(double[][] data) {
		assert(validateMatrix(data));
		
		initSMatrix(data.length, data[0].length);
		
		for(int i= 0; i < data.length; i++)
			for(int j= 0; j < data[i].length; j++)
				this.data[i][j] = data[i][j];
	}
	
	public SMatrix(String text) {
		String[] lines = text.split(NEWLINE);
		int rows = lines.length;
		int cols = lines[0].split(TAB).length;
		
		double [][] data = new double[rows][cols];

		for (int i = 0; i < rows; i++) {
			String[] elements = lines[i].split(TAB);
			for (int j = 0; j < elements.length; j++)
				data[i][j] = Double.parseDouble(elements[j]);
		}
		
		assert(validateMatrix(data));
		
		initSMatrix(data.length, data[0].length);
		
		for(int i= 0; i < data.length; i++)
			for(int j= 0; j < data[i].length; j++)
				this.data[i][j] = data[i][j];
	}
	
	public String toString() {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				sb.append(data[i][j]);
				if (j < numCols - 1) sb.append(TAB);
			}
			sb.append(NEWLINE);
		}
		
		return sb.toString();
	}
	
	
	public String showSize() {
		return  "matrix  size: " + numRows + " × " + numCols;
	}
	
	public String showMatrix(int digits) {
		if (digits < 0)  throw new IllegalArgumentException("digits should be non-negative integer");
		StringBuilder sb = new StringBuilder();
		int multiply = (int) Math.pow(10, digits);
		for (int i = 0; i < numRows; i++) {
			for (int j = 0; j < numCols; j++) {
				double val = Math.floor(multiply * data[i][j]) / multiply;
				sb.append(val);
				if (j < numCols - 1) sb.append(TAB);
			}
			sb.append(NEWLINE);
		}
		
		return sb.toString();
	}
	
	public SMatrix(double[] data, int numRows, int numCols) {
		assert(validateMatrix(new double[][]{data}));
		assert(data.length == numRows * numCols);
		
		initSMatrix(numRows, numCols);
		for (int i = 0; i < numRows; i++) 
			for (int j = 0; j < numCols; j++)
				this.data[i][j] = data[getIndex(i,j)];
		
	}
	
	public SMatrix(int[] data, int numRows, int numCols) {
		this(Arrays.stream(data).asDoubleStream().toArray(), numRows, numCols);
	}
	
	// java array 2D index -> j java array 1D index
    private int getIndex(int i, int j) {
		return  numCols * i  +  j;
	}
    

	// 1-base index -> matrix row
	public int getRow(int index) {
		return (int) ((index - 1) / numCols) + 1;
	}
	// 1-base index -> matrix col	
	public int getCol(int index) {
		return (index - 1) % numCols + 1;
	}

	public int getNumElements() {
		return numRows * numCols;
	}
	
	public double get(int row, int col) {
		assert(validateIndex(row, col));
		return this.data[row - 1][col - 1];
	}
	
	
	public double get(int index) {
		return get(getRow(index), getCol(index));
	}


	public int getNumRows() {
		return numRows;
	}

	public void setNumRows(int numRows) {
		this.numRows = numRows;
	}

	public int getNumCols() {
		return numCols;
	}

	public void setNumCols(int numCols) {
		this.numCols = numCols;
	}
	
	
	// instance method
	public SMatrix copy() {
		return new SMatrix(data);
	}
	
	public SMatrix createLike() {
		return new SMatrix(numRows, numCols);
	}
	
	public boolean isInBounds(int row, int col) {
		return validateIndex(row, col);
	}
	
	

	public  void set(int row, int col, double value) {
		assert(validateIndex(row, col));
		data[row - 1][col - 1] = value;
	}
	
	public void set(int index, double value) {
		set(getRow(index), getCol(index), value);
	}
	
	public void set(int startRow, int startCol, int rows, int cols, double value) {
		for (int i = 0; i < rows; i++)
			for (int j = 0; j < cols; j++) 
				data[startRow + i -1][startCol + j - 1] = value;
	}
	
	public SMatrix transpose() {
		return transpose(this);
	}
	
	public static SMatrix transpose(SMatrix A) {
		SMatrix modified = new SMatrix(A.getNumCols(), A.getNumRows());
		
		for (int i = 1; i <= A.getNumRows(); i++)
			for (int j = 1; j <= A.getNumCols(); j++)
				modified.set(j, i, A.get(i, j));
		
		return modified;
	}
	
	public static SMatrix addOnes(SMatrix matrix) {
		SMatrix modified = new SMatrix(matrix.getNumRows(), matrix.getNumCols() + 1);
		for (int i = 1; i <= matrix.getNumRows(); i++)
			for (int j = 1; j <= matrix.getNumCols(); j++)
				modified.set(i, j + 1, matrix.get(i, j));
		modified.set(1, 1, modified.getNumRows(), 1, 1.0);
		return modified;
	}
	
	public static double sum(SMatrix A) {
		double sum = 0.0;
		
		for (int index = 1; index <= A.getNumElements(); index++)
			sum += A.get(index);
		return sum;
	}
	
	public static double squareSum(SMatrix A) {
		return sum ( mulEl(A, A) );
	}
	
	public static SMatrix mul(SMatrix A, SMatrix B) {
		assert(isMultipliable(A, B));
			
		double[][] data = new double[A.getNumRows()][B.getNumCols()];
		
		for (int i = 1; i <= A.getNumRows(); i++) {
			for (int j = 1; j <= B.getNumCols(); j++) {
				double sum = 0.0;
				for (int k = 1; k <= A.getNumCols(); k++) 
					sum += A.get(i, k) * B.get(k, j);
				data[i - 1][j - 1] = sum;
			}
		}
		
		return new SMatrix(data);
	}
	
	public static SMatrix mul(double a, SMatrix A) {
		SMatrix modified = A.createLike();
		
		for (int index = 1; index <= A.getNumElements(); index++) 
			modified.set(index, a * A.get(index));
		
		return modified;
	}

	public static SMatrix mulEl(SMatrix A, SMatrix B) {
		assert(isSameDimension(A, B));
		
		SMatrix modified = A.createLike();
		
		for (int i = 1; i <= A.getNumElements(); i++)
			modified.set(i, A.get(i) * B.get(i));
		
		return modified;
	}
	
	
	public static SMatrix add(SMatrix A, SMatrix B) {
		assert(isSameDimension(A, B));
		
		SMatrix modified = A.createLike();
		
		for (int i = 1; i <= A.getNumElements(); i++)
			modified.set(i, A.get(i) + B.get(i));
		
		return modified;
	}
	
	public static SMatrix add(double value, SMatrix A) {
		SMatrix modified = A.createLike();
		
		for (int i = 1; i <= A.getNumElements(); i++)
			modified.set(i, value + A.get(i));
		
		return modified;
	}
	
	public static SMatrix sub(SMatrix A, SMatrix B) {
		assert(isSameDimension(A, B));
		
		SMatrix modified = A.createLike();
		
		for (int i = 1; i <= A.getNumElements(); i++)
			modified.set(i, A.get(i) - B.get(i));
		
		return modified;
	}
	
	public static SMatrix sub(double value, SMatrix A) {
		SMatrix modified = A.copy();
		
		for (int i = 1; i <= A.getNumElements(); i++)
			modified.set(i, value - A.get(i));
		
		return modified;
	}
	public static SMatrix div(double a, SMatrix A) {

		SMatrix modified = A.createLike();
		
		for (int i = 1; i <= A.getNumElements(); i++)
			modified.set(i, A.get(i) / a);
		
		return modified;
	}
	
	public static SMatrix applyLog(SMatrix A) {
		SMatrix modified = A.copy();
		
		for (int i = 1; i <= A.getNumElements(); i++)
			modified.set(i,  Math.log(A.get(i)));
		
		return modified;
	}
	
	public static SMatrix applySigmoid(SMatrix A) {
		SMatrix modified = A.copy();
		
		for (int i = 1; i <= A.getNumElements(); i++)
			modified.set(i,  RegMath.sigmoid(A.get(i)));
		
		return modified;
	}
	
	public static SMatrix ones(int n, int m) {
		return fill(1.0, n, m);
	}
	
	public static SMatrix zeros(int n, int m) {
		return fill(0.0, n, m);
	}
	
	public static SMatrix fill(double value,int n, int m) {
		SMatrix matrix = new SMatrix(n, m);
		for (int i = 1; i <= matrix.getNumElements(); i++)
			matrix.set(i, value);
		return matrix;
	}
	
	
	
	public static boolean isSameDimension(SMatrix A, SMatrix B) {
		return A.getNumRows() == B.getNumRows() && A.getNumCols() == B.getNumCols(); 
	}
	
	public static boolean isMultipliable(SMatrix A, SMatrix B) {
		return A.getNumCols() == B.getNumRows();
	}

	private boolean validateMatrix(double[][] data) {
		if (data == null)
			throw new IllegalArgumentException("The matrix data is null.");
		int numRows = data.length;
		if (numRows < 1)
			throw new IllegalArgumentException("The matrix data has zero rows.");
		int numCols = data[0].length;
		
		if (numCols < 1)
			throw new IllegalArgumentException("The matrix data has zero cols.");

		return validateElements(data);
	}
	
	private boolean validateElements(double[][] data) {
		for (int i = 0; i < numRows; i++) {
			if (numCols != data[i].length)
				throw new IllegalArgumentException("The matrix data doesn't have a consistent column numbers");
			for (int j = 0; j < data[i].length; j++) {
				if (data[i][j] == Double.NaN ||
						data[i][j] == Double.POSITIVE_INFINITY ||
						data[i][j] == Double.NEGATIVE_INFINITY )
					throw new IllegalArgumentException("The matrix has invalid data.");
			}
		}
		
		return true;
	}
	
	private boolean validateIndex(int row, int col) {
		return validateRowIndex(row) && validateColIndex(col);
	}
	private boolean validateRowIndex(int row) {
		if (row < 1 || row > numRows)
			return false;
		return true;
	}
	
	private boolean validateColIndex(int col) {
		if (col < 1 || col > numCols)
			return false;
		return true;
	}

	
}
