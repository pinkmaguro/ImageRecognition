package org.pinkmaguro.math.regression;

import static org.pinkmaguro.math.matrix.SMatrix.*;

import org.pinkmaguro.machinelearning.imagerecognition.learningsystem.OneVsAllTrainingModule;
import org.pinkmaguro.math.matrix.SMatrix;
import org.pinkmaguro.math.matrix.SMatrixMath;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class RegMath { 
	
	private static Logger logger = LoggerFactory.getLogger(OneVsAllTrainingModule.class); 
	
	public static SMatrix hypothesis(SMatrix X, SMatrix theta) {
		return mul(X, theta);
	}
	
	public static SMatrix hypothesisLogisticReg(SMatrix X, SMatrix theta)  {

		SMatrix h_theta = mul(X, theta);
		
		for (int i = 1; i <= h_theta.getNumElements(); i++)
			h_theta.set(i, sigmoid(h_theta.get(i)));
		
		return h_theta;
	}
	
	public static double sigmoid(double x) {
		return 1.0 / (1 + Math.exp(-x));
	}
	
	
	public static double costLinearReg(SMatrix X, SMatrix y, SMatrix theta, double lambda) {
		int m = X.getNumRows();
		SMatrix theta0 = theta.copy();	theta0.set(1, 0);
		
		double cost = squareSum(sub(mul(X, theta), y)) + lambda * squareSum(theta0);
		cost /= 2 * m;
		
		return cost;
	}
	
	public static double costLogisticReg(SMatrix X, SMatrix y, SMatrix theta, double lambda) {
		int m = X.getNumRows();
		
		double cost = 0.0;

		SMatrix h_theta = hypothesisLogisticReg(X, theta);		
		SMatrix log_h_theta = applyLog(h_theta);
		SMatrix log_one_minus_h_theta = applyLog(sub(1, h_theta));
		
		SMatrix theta0 = theta.copy();	theta0.set(0, 0);
		
		cost += -1.0 * sum( mulEl(y, log_h_theta) );
		cost += -1.0 * sum( mulEl(sub(1, y), log_one_minus_h_theta) ); 
		cost += +0.5 * lambda * squareSum(theta0);		
		cost /= m;
		
		return cost;
	}
	
	public static SMatrix gradientLinearReg (SMatrix X, SMatrix y, SMatrix theta, double lambda) {
		int m = X.getNumRows();
		SMatrix h_theta = hypothesis(X, theta);
		SMatrix theta0 = theta.copy();	theta0.set(0, 0);
		
		SMatrix grad = add(mul(transpose(X), sub(h_theta, y)), mul(lambda, theta0));
		return mul(1.0/m, grad);
	}
	
	public static SMatrix gradientLogisticReg (SMatrix X, SMatrix y, SMatrix theta, double lambda) {
		int m = X.getNumRows();
		SMatrix h_theta = hypothesisLogisticReg(X, theta);
		SMatrix theta0 = theta.copy();	theta0.set(1, 0);
		
		SMatrix grad = add(mul(transpose(X), sub(h_theta, y)), mul(lambda, theta0));
		return mul(1.0/m, grad);
	}
	

	public static SMatrix gradientDescentLinearReg(SMatrix X, SMatrix y, SMatrix initial_theta,
			double lambda, double alpha, int iterations) {
		
		SMatrix theta = initial_theta;
		
		for (int i = 0; i < iterations; i++) {
			theta = sub(theta, mul(alpha, gradientLinearReg(X, y, theta, lambda)));
		}
		return theta;
	}
	
	public static SMatrix gradientDescentLogisticReg(SMatrix X, SMatrix y, SMatrix initial_theta,
			double lambda, double alpha, int iterations) {
		
		SMatrix theta = initial_theta;
		
		for (int i = 0; i < iterations; i++) {
			theta = sub(theta, mul(alpha, gradientLogisticReg(X, y, theta, lambda)));
		}
		return theta;
	}
	
	public static SMatrix trainMultiClassificationRegression(SMatrix X, SMatrix y, SMatrix initial_theta,
			double lambda, double alpha, int iterations, int K) {
		assert(K <= 10);
		int m = X.getNumRows();
		SMatrix bigTheta = new SMatrix(X.getNumCols(), K);
	
		for (int k = 0; k < K; k++) {
			logger.info("Training precess for " + k + " started...");
			SMatrix yk = SMatrix.zeros(m, 1);
			for (int i = 1; i <= m; i++)
				if (y.get(i, 1) == k)	yk.set(i, 1, 1);

			SMatrix theta = gradientDescentLogisticReg(X, yk, initial_theta, lambda, alpha, iterations);
			
			for (int i = 1; i <= theta.getNumElements(); i++)
				bigTheta.set(i, k + 1, theta.get(i, 1));
		}
		
		return bigTheta;  // n + 1 by K matrix
	}
	
	public static int guessMultiClassificationRegression(SMatrix bigTheta, SMatrix x) {
		
		SMatrix prob = mul(x, bigTheta);
		int maxPos = SMatrixMath.maxPosition(prob, 1, SMatrixMath.ROW);
		
		return maxPos - 1;
	}
	
	public static int guessMultiClassificationRegression(SMatrix bigTheta, SMatrix x, SMatrix prob) {
		
		prob = mul(x, bigTheta);
		System.out.println("Prob in RegMath : " + prob.toString());
		int maxPos = SMatrixMath.maxPosition(prob, 1, SMatrixMath.ROW);
		
		return maxPos - 1;
	}

}
