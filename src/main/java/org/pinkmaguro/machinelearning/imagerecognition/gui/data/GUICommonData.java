package org.pinkmaguro.machinelearning.imagerecognition.gui.data;

import java.io.FileFilter;

import org.pinkmaguro.utils.file.FileUtil;
import org.pinkmaguro.utils.file.filefilter.FileFilterFactory;

public class GUICommonData {
	
	public static final String DASH = "-";
	public static final String PNG = ".png";
	public static final String TAB = "\t";
	public static final String DOT = ".";
	public static final String NEWLINE = "\r\n";

	int framePosX = 100;
	int framePosY = 100;
	
	int frameSizeX = 350;
	int frameSizeY = 600;
	
	int panelSizeX = 300;
	int panelSizeY = 300;
	
	int penSize = 25;
	
	int imageSizeX = 28;
	int imageSizeY = 28;
	
	String imageDir;
	String dataDir;
	
	String imageDataFileName = "imageData.txt";
	String thetaFileName = "theta.txt";
	
	FileFilter fileExtensionFilter;

	public GUICommonData() {
		fileExtensionFilter = FileFilterFactory.createFileExtensionFilter(PNG);
		imageDir = "D:\\ml-imageDir";
	}
	
	public void setUp(String imageDir) {
		this.imageDir = imageDir;
		this.dataDir = FileUtil.getPath(imageDir, "data");
		
		FileUtil.createDirectory(imageDir);
		FileUtil.createDirectory(dataDir);
		FileUtil.createFile(FileUtil.getPath(dataDir, imageDataFileName));
	}
	
	public int getPixelSize() {
		return imageSizeX * imageSizeY;
	}
	
	public String getImageDataFilePath() {
		return FileUtil.getPath(dataDir, imageDataFileName);
	}
	
	public int getFramePosX() {
		return framePosX;
	}

	public int getFramePosY() {
		return framePosY;
	}

	public int getFrameSizeX() {
		return frameSizeX;
	}

	public int getFrameSizeY() {
		return frameSizeY;
	}

	public int getPanelSizeX() {
		return panelSizeX;
	}

	public int getPanelSizeY() {
		return panelSizeY;
	}
	public String getImageDir() {
		return imageDir;
	}

	public void setImageDir(String imageDir) {
		this.imageDir = imageDir;
	}

	public String getDataDir() {
		return dataDir;
	}

	public void setDataDir(String dataDir) {
		this.dataDir = dataDir;
	}

	public int getPenSize() {
		return penSize;
	}

	public void setPenSize(int penSize) {
		this.penSize = penSize;
	}

	public int getImageSizeX() {
		return imageSizeX;
	}

	public void setImageSizeX(int imageSizeX) {
		this.imageSizeX = imageSizeX;
	}

	public int getImageSizeY() {
		return imageSizeY;
	}

	public FileFilter getFileExtensionFilter() {
		return fileExtensionFilter;
	}

	public void setFileExtensionFilter(FileFilter fileExtensionFilter) {
		this.fileExtensionFilter = fileExtensionFilter;
	}

	public String getThetaFilePath() {
		return FileUtil.getPath(dataDir, thetaFileName);
	}	
	
}
