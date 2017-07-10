package org.pinkmaguro.math.matrix;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class SMatrixTest {

	private int numRows;
	private int numCols;
	double[] data;
	double[] baddata1;
	double[] baddata2;
	private SMatrix A;
	private SMatrix B;
	
	@Before
	public void setUp() {
		numRows = 3;
		numCols = 2;
		data = new double[] {1,2,3,4,5,6};
		baddata1 = new double[] {1,2,3,4,5};
		baddata2 = new double[] {1,2,3,4, Double.NaN};
		A = new SMatrix(data, numRows, numCols);
		B = new SMatrix(new double[]{1,0},2,1);
		
	}
	
//	@Test
	public void testConstructor() {
		SMatrix bad1 = new SMatrix(baddata1, numRows, numCols);
	}
	
//	@Test
	public void testConstructor2() {
		SMatrix bad1 = new SMatrix(baddata2, numRows, numCols);
	}
	
	@Test
	public void testGet() {
		assertThat(A.get(numRows, numCols), is(data[data.length - 1]));
	}
	
	@Test
	public void getIndex() {
		System.out.println("test getIndex");
		assertThat(A.getRow(6), is(numRows));
		assertThat(A.getCol(6), is(numCols));
		assertThat(A.getRow(1), is(1));
		assertThat(A.getCol(1), is(1));
	}
	
	@Test
	public void testToString(){
		System.out.println("test toString()");
		System.out.println(A);
	}
	
	@Test
	public void testSet(){
		System.out.println("test set()");
		A.set(numRows, numCols, 0);
		assertThat(A.get(numRows, numCols), is(0.0));
		System.out.println(A);
	}
	
	@Test
	public void testTranpose() {
		System.out.println("test transpose()");
		SMatrix C = SMatrix.transpose(A);
		assertThat(C.getNumRows(), is(A.getNumCols()));
		assertThat(C.getNumCols(), is(A.getNumRows()));
		System.out.println(C);
	}
	
	@Test
	public void testSum() {
		System.out.println("test sum()");

		assertThat(SMatrix.sum(A), is(21.0));

	}
	
	@Test
	public void testAddOnes() {
		System.out.println("test addOnes()");
		SMatrix C = SMatrix.addOnes(A);
		System.out.println(A);
		assertThat(C.getNumCols(), is(A.getNumCols() + 1));
		assertThat(C.get(1,1), is(1.0));
	}
	
	@Test
	public void testMul() {
		System.out.println("test mul(double, Matrix)");
		SMatrix C = SMatrix.mul(2, A);
		assertThat(SMatrix.sum(C), is(42.0));
		System.out.println(C);
	}
	
	@Test
	public void testMul2() {
		System.out.println("test mul(Matrix, Matrix)");
		SMatrix C = SMatrix.mul(A, B);
		assertThat(SMatrix.sum(C), is(9.0));
		assertThat(C.getNumRows(), is(3));
		assertThat(C.getNumCols(), is(1));
		System.out.println(C);
	}
	
	@Test
	public void testMulEl() {
		System.out.println("test mulEl(Matrix, Matrix)");
		SMatrix C = SMatrix.mulEl(A, A);
		assertThat(C.get(1, 1), is(1.0));
		assertThat(C.get(C.getNumRows(), C.getNumCols()), is(36.0));
		System.out.println(C);
	}
	
	@Test
	public void testSub() {
		System.out.println("test sub(double, Matrix)");
		SMatrix C = SMatrix.sub(10, A);
		assertThat(C.get(1, 1), is(9.0));
		
		assertThat(C.get(C.getNumRows(), C.getNumCols()), is(4.0));
		System.out.println(C);
	}
}

