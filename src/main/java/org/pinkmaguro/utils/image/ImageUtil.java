package org.pinkmaguro.utils.image;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageUtil {
	
	public static BufferedImage getImage(File file) {
		BufferedImage image = null;
		try {
		    image = ImageIO.read(file);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return image;
	}

	public static BufferedImage simplifyImage(BufferedImage img, int numRows, int numCols) {
		int[][] pixel = sliceImageToGrayScale(img, numRows, numCols);
		BufferedImage simpleImage = new BufferedImage(numCols,
				numRows, BufferedImage.TYPE_BYTE_GRAY );
		Color color = null;
		for (int i = 0; i < numRows; i++ ) {
			for (int j = 0; j < numCols; j++) {
				int gray = pixel[i][j];
				color = new Color(gray, gray, gray);
				simpleImage.setRGB(j, i, color.getRGB());
			}
		}
		
		return simpleImage;
	}
	
	public static int[][] sliceImageToGrayScale(BufferedImage img, int numRows, int numCols) {
		
		int height	= img.getHeight();
		int width 	= img.getWidth();
		
		double rowPixel =  1.0 * height / numRows;
		double colPixel =  1.0 * width  / numCols;
		
		int[][] result = new int[numRows][numCols];
		
		for (int i = 0; i < numRows ; i++) {
			int startY = (int) Math.ceil(i * rowPixel);
			int endY = (int) Math.ceil((i + 1) * rowPixel) - 1;
			for (int j = 0; j < numCols; j++) {
				int startX = (int) Math.ceil(j * colPixel);
				int endX = (int) Math.ceil((j + 1) * colPixel) - 1;
				result[i][j] = averageGrayScale(img, startX, startY,
						endX - startX + 1, endY - startY + 1);
				
			}
		}
		
		return result;
	}
	
	public static Color averageColor(BufferedImage img, int posX, int posY,
			int width, int height) {
		double redSum = 0;
		double greenSum = 0;
		double blueSum = 0;
		
		int numPixels = width * height;
		
		for (int i = posX; i < posX + width; i++) {
			for (int j = posY; j < posY + height; j++) {
				Color c = new Color(img.getRGB(i, j));
				redSum 		+= c.getRed();
				greenSum 	+= c.getGreen();
				blueSum 	+= c.getBlue();
			}
		}
		
		redSum /= numPixels;
		greenSum /= numPixels;
		blueSum /= numPixels;
		
		int red = (int) redSum;
		int green = (int) greenSum;
		int blue = (int) blueSum;
		
		return new Color(red, green, blue);
	}
	
	public static int averageGrayScale(BufferedImage img, int posX, int posY,
			int width, int height) {
		Color color = averageColor(img, posX, posY, width, height);
		return averageGrayScale(color);
	}
	
	public   static int averageGrayScale(Color color) {
		int red = color.getRed();
		int green = color.getGreen();
		int blue = color.getBlue();
		int gray = (int) ( (red + green + blue) / 3  );
		return gray;
	}
	
	public static int[] imageToInt1DArray(BufferedImage image, boolean grayScale) {
		int height = image.getHeight();
		int width = image.getWidth();
		
		int[] imageData = new int[3 * height * width];
		int[] grayImageData = new int[height * width];
		
		int indexForColr = 0;
		int indexForGray = 0;
		
		for (int i = 0; i < height; i ++) {
			for (int j = 0; j < width; j++) {
				int rgb = image.getRGB(i, j);
				Color color = new Color(rgb);
				
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				int gray = (red + green + blue) / 3 ;
				
				imageData[indexForColr++] = red;
				imageData[indexForColr++] = green;
				imageData[indexForColr++] = blue;
				grayImageData[indexForGray++] = gray;
			}
		}
		
		if (grayScale)
			return grayImageData;
		else
			return imageData;
	}
	
	public static String imageToText(BufferedImage image, String separator, boolean grayScale) {
		
		StringBuilder sb = new StringBuilder();
		
		int height = image.getHeight();
		int width = image.getWidth();
		
		for (int i = 0; i < height; i ++) {
			for (int j = 0; j < width; j++) {
				int rgb = image.getRGB(i, j);
				Color color = new Color(rgb);
				
				int red = color.getRed();
				int green = color.getGreen();
				int blue = color.getBlue();
				int gray = (red + green + blue) / 3 ;
				if (grayScale) 
					sb.append(gray + separator);
				else {
					sb.append(red + separator);
					sb.append(green + separator);
					sb.append(blue + separator);
				}
			}
		}
		sb.delete(sb.length() - separator.length(), sb.length());
		return sb.toString();
	}
	
	
}
