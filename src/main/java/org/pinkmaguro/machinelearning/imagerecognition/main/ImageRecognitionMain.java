package org.pinkmaguro.machinelearning.imagerecognition.main;

import javax.swing.JFrame;


import org.pinkmaguro.machinelearning.imagerecognition.gui.DrawingPanel;
import org.pinkmaguro.machinelearning.imagerecognition.gui.MainFrame;
import org.pinkmaguro.machinelearning.imagerecognition.gui.data.GUICommonData;
import org.pinkmaguro.machinelearning.imagerecognition.learningsystem.ImageRecognitionTrainingModule;
import org.pinkmaguro.machinelearning.imagerecognition.learningsystem.OneVsAllTrainingModule;

public class ImageRecognitionMain {

	
	JFrame mainFrame;
	DrawingPanel drawingPanel;
	GUICommonData commonData;
	ImageRecognitionTrainingModule trainingModule;
	

	public ImageRecognitionMain() {

		commonData = new GUICommonData();
		drawingPanel = new DrawingPanel(commonData);
		trainingModule = new OneVsAllTrainingModule(commonData);
		
		mainFrame = new MainFrame(drawingPanel, commonData, trainingModule);

	}
	
	public static void main(String[] args) {
		new ImageRecognitionMain();
	}
	
}
