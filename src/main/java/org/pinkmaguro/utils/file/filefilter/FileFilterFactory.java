package org.pinkmaguro.utils.file.filefilter;

import java.io.FileFilter;

public class FileFilterFactory {
		
	public static FileFilter createFileExtensionFilter(String extensions) {
	
		FileExtensionFilter filter = new FileExtensionFilter();
		filter.setExtensions(extensions);
		
		return filter;
	}
}
