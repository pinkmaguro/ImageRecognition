package org.pinkmaguro.math.matrix;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class SMatrixMathTest {

	private double[] data;
	private SMatrix matrix;
	
	@Before
	public void setUp() {
		data = new double[] {1,2,3,4,5,6};
		matrix = new SMatrix(data,3,2);
	}
	
	@Test
	public void testMaxPosition() {
		
		assertThat(SMatrixMath.maxPosition(matrix, 1, 1), is(2));
		assertThat(SMatrixMath.maxPosition(matrix, 2, 1), is(2));
	}
	
	@Test
	public void testOdd() {
		double d1 = Double.MIN_VALUE;
		double d2 = Double.MIN_NORMAL;
		double d3 = Double.MIN_EXPONENT;
		System.out.println(d1);
		System.out.println(d2);
		System.out.println(d3);
	}
}
