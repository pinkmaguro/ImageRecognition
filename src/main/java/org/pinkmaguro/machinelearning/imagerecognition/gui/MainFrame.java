package org.pinkmaguro.machinelearning.imagerecognition.gui;


import java.awt.FlowLayout;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextField;

import org.pinkmaguro.machinelearning.imagerecognition.gui.data.GUICommonData;
import org.pinkmaguro.machinelearning.imagerecognition.learningsystem.ImageRecognitionTrainingModule;

public class MainFrame extends JFrame {

	private static final long serialVersionUID = 1L;

	DrawingPanel drawingPanel;
	
	GUICommonData commonData;
	ImageRecognitionTrainingModule trainingModule;
	
	JLabel lb_train;
	JLabel lb_guess;
	JLabel lb_imageDir;
	JLabel lb_trainingK;
	
	JTextField tf_train;
	JTextField tf_guess;
	JTextField tf_imageDir;
	JComboBox combo_trainingK;
	
	JButton btn_setup;
	JButton btn_clear;
	
	// save image data and number for the image
	//push this button after entering a number in training text field.
	JButton btn_save;  
	
	// this button initiate calculating theta from all images
	JButton btn_train;     
	
	// test your number image
	JButton btn_guess;

	public MainFrame(DrawingPanel drawingPanel, GUICommonData commonData,
			ImageRecognitionTrainingModule trainingModule) {

		this.drawingPanel = drawingPanel;
		this.commonData = commonData;
		this.trainingModule = trainingModule;
		

		
		lb_train = new JLabel("Train: ");
		lb_guess = new JLabel("Guess: ");
		lb_imageDir = new JLabel("Image storing directory: ");
		lb_trainingK = new JLabel("K(<= 10): ");
		
		tf_train = new JTextField(10);
		tf_guess = new JTextField(10);
		tf_imageDir = new JTextField(20);  tf_imageDir.setText(commonData.getImageDir());
		
		String[] kItems = new String[]{"2","3","4","5","6","7","8","9","10"}; 
		combo_trainingK = new JComboBox<Object>(kItems);
		combo_trainingK.setSelectedIndex(kItems.length - 1);
	
		
		ActionListener myActionHandler = new ActionHandler();
		
		btn_setup = new JButton("Setup");
		btn_setup.addActionListener(myActionHandler);
		
		btn_save = new JButton("Save");
		btn_save.addActionListener(myActionHandler);
		
		
		btn_guess = new JButton("Guess");
		btn_guess.addActionListener(myActionHandler);
		
		btn_clear = new JButton("Clear");
		btn_clear.addActionListener(myActionHandler);
		
		btn_train = new JButton("Train");
		btn_train.addActionListener(myActionHandler);
		
		setTitle("Machine Learning - Image recognition by Pinkmaguro");
		setBounds(commonData.getFramePosX(),commonData.getFramePosY(),
				commonData.getFrameSizeX(), commonData.getFrameSizeY());
		
		setLayout(new FlowLayout(FlowLayout.LEFT));
		add(drawingPanel);
		add(lb_train);
		add(tf_train);
		add(lb_guess);
		add(tf_guess);
		add(btn_clear);
		add(btn_save);
		add(btn_train);
		add(btn_guess);
		add(lb_trainingK);
		add(combo_trainingK);
		add(lb_imageDir);
		add(tf_imageDir);
		add(btn_setup);
		
		setVisible(true);
	}

	private class ActionHandler implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent event) {
			
			Object src = event.getSource();
			
			if ( src == btn_setup) {
				String dir = tf_imageDir.getText().trim();
				String K = (String)(combo_trainingK.getSelectedItem());
				commonData.setUp(dir); 
				trainingModule.setK(Integer.parseInt(K));
			} else if ( src == btn_save ) {
				int num = Integer.parseInt(tf_train.getText().trim());
				if (num > trainingModule.getK())
					throw new IllegalArgumentException("Training number is greater than K");
				trainingModule.save(num, drawingPanel.getImage());
				clear();
					
			} else if ( src == btn_clear) {
				 drawingPanel.clear();
				 
			} else if  (src == btn_train) {
				try {
					trainingModule.train();
				} catch (NumberFormatException e) {
					e.printStackTrace();
				}
			} else if ( src == btn_guess) {
				int guess = trainingModule.guess(drawingPanel.getImage());
				tf_guess.setText(guess + "");
			} 
		}
		
	}
	
	private void clear() {
		drawingPanel.clear();
		tf_guess.setText("");
	}
}
