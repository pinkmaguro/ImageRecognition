package org.pinkmaguro.utils.text;

import java.awt.Color;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {
	
	public static final String NEWLINE = "\r\n";

	public static String[] readLines(String fileName, boolean skipEmptyLine) {
		
		String text = readAll(fileName, skipEmptyLine);
		
		return text.split(NEWLINE);
	}
	
	public static String readAll(String fileName, boolean skipEmptyLine) {
		FileReader fr = null;
		BufferedReader br = null;
		StringBuilder sb = new StringBuilder();
		
		try {
			
			fr = new FileReader(fileName);
			br = new BufferedReader(fr);
			
			String line = "";
			
			while ( (line = br.readLine()) != null) {
				if (skipEmptyLine && isEmptyLine(line)) continue;
				sb.append(line).append(NEWLINE);
			}
			
			br.close();
			fr.close();
			
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return sb.toString();
	}
	
	
	public static void showLines(String[] lines) {
		for (int i = 0; i < lines.length; i++)
			System.out.println(i +  " : " + lines[i]);
	}
	
	
	public static boolean isEmptyLine(String line) {
		
		String p = "\\w";
		Pattern pattern = Pattern.compile(p);
		Matcher m = pattern.matcher(line);
		if (m.find()) 
			return false;
		
		return true;
	}
	
	public static void append(String fileName, String addtionalText) {
		
		try {
		    Files.write(Paths.get(fileName), addtionalText.getBytes(), StandardOpenOption.APPEND);
		} catch (IOException e) {
		  e.printStackTrace(); 
		}
		
	}
	 


}
