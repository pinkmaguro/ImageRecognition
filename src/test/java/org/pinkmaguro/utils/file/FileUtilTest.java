package org.pinkmaguro.utils.file;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import org.junit.Before;
import org.junit.Test;

public class FileUtilTest {

	
	private String dirName1;
	private String dirName2;
	private String dirName3;
	private String fileName;
	
	@Before
	public void setUp() {
		dirName1 = "D:\\ML";
		dirName2 = "D:\\ML\\";
		dirName3 = "D:\\ML/";
		fileName = "1-234.pNg";
	}
	
	@Test
	public void testGetPath() {
		System.out.println("dir1 : " + dirName1);
		System.out.println("dir2 : " + dirName2);
		System.out.println("dir3 : " + dirName3);
		
		
		System.out.println(fileName);
		String path1 = FileUtil.getPath(dirName1, fileName);
		String path2 = FileUtil.getPath(dirName2, fileName);
		String path3 = FileUtil.getPath(dirName3, fileName);
		
		System.out.println(path1);
		System.out.println(path2);
		System.out.println(path3);
		assertThat(path1, is(path2));
		
	}
	
	@Test
	public void testCreateTextFile() {
		System.out.println("test createTextFile(fileName, text)");
		FileUtil.createTextFile("D://test.txt", "some text한글");
	}
}


