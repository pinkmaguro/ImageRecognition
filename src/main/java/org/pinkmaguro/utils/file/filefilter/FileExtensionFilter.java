package org.pinkmaguro.utils.file.filefilter;

import java.io.File;
import java.io.FileFilter;
import java.util.Arrays;

public class FileExtensionFilter implements FileFilter {

	private static final String DOT = ".";
	private static final String COMMA = ",";
	
	private String[] extensions;
	
	public void setExtensions(String extensions) {
		setExtensions(extensions.split(COMMA));
	}
	
	public void setExtensions(String[] extensions) {
		this.extensions = addDot(extensions);
	}
	
	public boolean accept(File file) {
		for (String extension : extensions) {
			if (file.getName().toLowerCase().endsWith(extension))
				return true;
		}
		return false;
	}
	
	private String[] addDot(String[] extensions) {
		String[] extensionsWithDot = new String[extensions.length];
		
		for (int i = 0; i < extensions.length; i++) {
			String ext = extensions[i];
			extensionsWithDot[i] =  (ext.startsWith(DOT)?ext : DOT + ext);
		}
		
		return extensionsWithDot;
	}		

}
