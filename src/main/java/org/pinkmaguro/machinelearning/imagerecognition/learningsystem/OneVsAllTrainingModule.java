package org.pinkmaguro.machinelearning.imagerecognition.learningsystem;

import static org.pinkmaguro.machinelearning.imagerecognition.gui.data.GUICommonData.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

import javax.imageio.ImageIO;

import org.pinkmaguro.machinelearning.imagerecognition.gui.data.GUICommonData;
import org.pinkmaguro.math.matrix.SMatrix;
import org.pinkmaguro.math.regression.RegMath;
import org.pinkmaguro.utils.file.FileUtil;
import org.pinkmaguro.utils.image.ImageUtil;
import org.pinkmaguro.utils.text.TextUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class OneVsAllTrainingModule implements ImageRecognitionTrainingModule {

	
	private static Logger logger = LoggerFactory.getLogger(OneVsAllTrainingModule.class); 
	private GUICommonData commonData;
	
	private SMatrix X;
	private SMatrix y;
	private SMatrix bigTheta;
	public SMatrix prob;
	
	protected int K;
	protected  double alpha = 0.01;
	protected  int iterations = 1500;
	protected  double lambda = 0.1;
	
	
	
	public OneVsAllTrainingModule(GUICommonData commonData) {
		this.commonData = commonData;

	}
	
	

	@Override
	public void save(int num, BufferedImage panelImage) {
		try {
			
			String fileName = createHashedFileName(num);
			File imageFile = new File(FileUtil.getPath(commonData.getImageDir(), fileName));
			BufferedImage img = ImageUtil.simplifyImage(panelImage, 
					commonData.getImageSizeX(), commonData.getImageSizeY());
			ImageIO.write(img, "PNG", imageFile);
			logger.info("Saved file as " + fileName);
			
		} catch (IOException e) {
			e.printStackTrace();
		}

	}
	
	private String createHashedFileName(int num) {
		long time = System.currentTimeMillis();
		long hash = (long) (time % 100000000);
		
		String fileName = num + DASH + hash + PNG;
		
		return fileName;
	}

	@Override
	public void train() {
		logger.info("Training proccess starts...");
		
		Set<String> trainedData = collectHash();
		StringBuilder sb = new StringBuilder();
		File[] files = new File(commonData.getImageDir()).listFiles(commonData.getFileExtensionFilter());
		logger.info("Image Directory : " + commonData.getImageDir());
		logger.info("Number of files to train : " + files.length);
		
		int count = 0;
		for (File file: files) {
			
			String fileName = file.getName();
			int beginIndex = fileName.indexOf(DASH);
			int endIndex = fileName.lastIndexOf(DOT);
			String numStr = fileName.substring(0, beginIndex);
			String hash = fileName.substring(beginIndex + 1, endIndex);
			
			if (!trainedData.contains(hash)) {
				sb.append(hash).append(TAB);
				sb.append(numStr).append(TAB);
				sb.append(ImageUtil.imageToText(ImageUtil.getImage(file), TAB, true)); 
				sb.append(NEWLINE);
				count++;
			}
		}
		
		TextUtil.append(commonData.getImageDataFilePath(), sb.toString());
		logger.info("Image data file was updated : " + count );
		
		prepareXyMatrix();

		calcuateTheta();
	
		logger.info("Training proccess is done.");
		
		FileUtil.createTextFile(commonData.getThetaFilePath(), bigTheta.toString());
		
		logger.info("Theta file is created.");

	}
	
	private Set<String> collectHash() {
		Set<String> set = new HashSet<String>();
		String[] lines = TextUtil.readLines(commonData.getImageDataFilePath(), true);
		for (String line : lines) {
			String[] elements = line.split(TAB);
			if (elements.length > 2)
				set.add(elements[0]);
		}
		return set;
	}
	
	private void calcuateTheta() {
		logger.info("calucate Theta...");
		SMatrix initial_theta = SMatrix.zeros(X.getNumCols(), 1);
		this.bigTheta = RegMath.trainMultiClassificationRegression(X, y, initial_theta,
				lambda, alpha, iterations, K);
	}

	@Override
	public int guess(BufferedImage panelImage) {
		BufferedImage guessImage = ImageUtil.simplifyImage(panelImage, 
				commonData.getImageSizeX(), commonData.getImageSizeY());
		
		SMatrix x = new SMatrix(ImageUtil.imageToInt1DArray(guessImage, true), 
				1, commonData.getPixelSize());
		x = SMatrix.addOnes(x);
		
		prob = new SMatrix(1, K);
		int guess = RegMath.guessMultiClassificationRegression(bigTheta, x, prob);
		logger.info("X * theta: " + prob.showMatrix(2));
		prob = SMatrix.applySigmoid(prob);
		logger.info("Prob : " + prob.showMatrix(2));
		logger.info("Guess : " + guess);
		return guess;
	}

	@Override
	public int getK() {
		return K;
	}



	@Override
	public void setK(int K) {
		this.K = K;
	}
	
	private void prepareXyMatrix() {
		SMatrix x = new SMatrix(TextUtil.readDouble(commonData.getImageDataFilePath()
				, 2, commonData.getPixelSize(), TAB));
		this.X = SMatrix.addOnes(x);
		this.y = new SMatrix(TextUtil.readDouble(commonData.getImageDataFilePath()
				, 1, 1, TAB));
	}

}
