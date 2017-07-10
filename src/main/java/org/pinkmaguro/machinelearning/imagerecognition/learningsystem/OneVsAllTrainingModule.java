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
	
	private SMatrix bigTheta;
	
	protected int K;
	protected  double alpha = 0.01;
	protected  int iterations = 1500;
	protected  double lambda = 0;
	
	public OneVsAllTrainingModule(GUICommonData commonData) {
		this.commonData = commonData;
	}
	
	

	@Override
	public void save(int num, BufferedImage panelImage) {
		try {
			
			String fileName = createHashedFileName(num);
			System.out.println(fileName);
			System.out.println(commonData.getImageDir());
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
				sb.append(numStr).append(TAB);
				sb.append(hash).append(TAB);
				sb.append(ImageUtil.imageToText(ImageUtil.getImage(file), TAB, true)); 
				sb.append(NEWLINE);
				count++;
			}
		}
		
		TextUtil.append(commonData.getImageDataFilePath(), sb.toString());
	
		logger.info("Image data file was updated : " + count );
		
		bigTheta = calcuateTheta();
	
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
				set.add(elements[1]);
		}
		return set;
	}
	
	private SMatrix calcuateTheta() {
		logger.info("calucate Theta...");
		
		String[] lines = TextUtil.readLines(commonData.getImageDataFilePath(), true);
		int m = lines.length;
		
		SMatrix X  = new SMatrix(m, 1 + commonData.getImageSizeX() * commonData.getImageSizeY());
		SMatrix y  = new SMatrix(m, 1);
		SMatrix initial_theta = SMatrix.zeros(X.getNumCols(), 1);
		
		for (int i = 1; i <= m; i++) {
			
			String[] elements = lines[i - 1].split(TAB);
			String numStr = elements[0];
			
			y.set(i, Double.parseDouble(numStr));
			
			for (int j = 2; j < elements.length; j++) 
				X.set(i, j, Double.parseDouble(elements[j]));
		}
		
		for (int i = 1; i<= m ; i++)
			X.set(i, 1, 1);
		
		logger.info("Reading X, y matrix from file is done.");
		SMatrix bigTheta = RegMath.trainMultiClassificationRegression(X, y, initial_theta,
				lambda, alpha, iterations, K);
		return bigTheta;
	}

	@Override
	public int guess(BufferedImage panelImage) {
		BufferedImage guessImage = ImageUtil.simplifyImage(panelImage, 
				commonData.getImageSizeX(), commonData.getImageSizeY());
		
		SMatrix x = new SMatrix(ImageUtil.imageToInt1DArray(guessImage, true), 
				1, commonData.getPixelSize());
		SMatrix X = SMatrix.addOnes(x);
		
		bigTheta = new SMatrix(TextUtil.readAll(commonData.getThetaFilePath(), true));
		
		int guess = RegMath.guessMultiClassificationRegression(bigTheta, X);
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
	
	

}
