package org.pinkmaguro.util.file.filefileter;

import java.io.File;
import java.io.FileFilter;

import org.junit.Before;
import org.junit.Test;
import org.pinkmaguro.utils.file.filefilter.FileFilterFactory;

public class FileFilterTest {
	
	
	
	private String dirName;
	
	@Before
	public void setUp() {
		dirName = "D:\\ml-imageDir1";
	}
	
	@Test
	public void testFileFilter() {
		FileFilter filter = FileFilterFactory.createFileExtensionFilter("png"); 
		File[] files = new File(dirName).listFiles(filter);
		
		System.out.println(files.length);
	}

}
