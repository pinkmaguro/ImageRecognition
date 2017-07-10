package org.pinkmaguro.machinelearning.imagerecognition.learningsystem;

import java.awt.image.BufferedImage;

public interface ImageRecognitionTrainingModule {
	
	void save(int num, BufferedImage panelImage);
	public void train();
	public  int guess(BufferedImage panelImage);
	public int getK();
	public void setK(int K);
	
}
