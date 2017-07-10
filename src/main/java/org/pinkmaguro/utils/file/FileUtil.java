package org.pinkmaguro.utils.file;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class FileUtil {
	
	
	public static void createDirectory(String dirName) {
		File dir = new File(dirName);
		if ( !dir.exists() || !dir.isDirectory()) {
			dir.mkdirs();
		}
	}
	
	public static String getPath(String dirName, String fileName) {
		if (dirName.endsWith("\\") || dirName.endsWith("/"))
			return dirName + fileName;
		else 
			return  dirName + "\\" + fileName;
	}

	public static void createFile(String path) {
		File file = new File(path);
		if (!file.exists())
			try {
				file.createNewFile();
			} catch (IOException e) {
				e.printStackTrace();
			}
	}
	
	public static void createTextFile(String fileName, String text) {
		
		try {
			Files.write(Paths.get(fileName), text.getBytes());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}


}
