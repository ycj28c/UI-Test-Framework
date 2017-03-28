package io.ycj28c.uitest.util;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import org.openqa.selenium.Dimension;
import org.openqa.selenium.Point;
import org.openqa.selenium.WebElement;

/**
 * ImageContrast changes image from RGB to LAB and make comparison
 * 
 * Reference:
 * https://github.com/NetEase/Dagger/blob/master/src/com/netease/imagecheck/ImageContrast.java
 */
public class ImageContrastUtil {
	
	private static Point wePosition;
	private static Dimension weDim;
	private static int maxColorThreshold = 0;

	/**
	 * Change RGB to XYZ
	 * 
	 * @param rgb
	 * @return xyz
	 */
	private static ColorXYZ rgb2xyz(int rgb) {
		ColorXYZ xyz = new ColorXYZ();
		int r = (rgb & 0xff0000) >> 16;
		int g = (rgb & 0xff00) >> 8;
		int b = (rgb & 0xff);
		if ((r == 0) && (g == 0) && (b == 0)) {
			xyz.x = 0;
			xyz.y = 0;
			xyz.z = 0;
		} else {
			xyz.x = (0.490 * r + 0.310 * g + 0.200 * b) / (0.667 * r + 1.132 * g + 1.200 * b);
			xyz.y = (0.117 * r + 0.812 * g + 0.010 * b) / (0.667 * r + 1.132 * g + 1.200 * b);
			xyz.z = (0.000 * r + 0.010 * g + 0.990 * b) / (0.667 * r + 1.132 * g + 1.200 * b);
		}
		return xyz;
	}

	/**
	 * Change XYZ to LAB
	 * 
	 * @param xyz
	 * @return lab
	 */
	private static ColorLAB xyz2lab(ColorXYZ xyz) {
		ColorLAB lab = new ColorLAB();
		double x = xyz.x / 95.047;
		double y = xyz.y / 100.000;
		double z = xyz.z / 108.883;
		x = (x > 0.008856) ? Math.pow(x, 1.0 / 3.0) : (7.787 * x + 16.0 / 116);
		y = (y > 0.008856) ? Math.pow(y, 1.0 / 3.0) : (7.787 * y + 16.0 / 116);
		z = (z > 0.008856) ? Math.pow(z, 1.0 / 3.0) : (7.787 * z + 16.0 / 116);
		lab.l = 116 * Math.pow(y, 1.0 / 3.0) - 16;
		lab.a = 500 * (Math.pow(x, 1.0 / 3.0) - Math.pow(y, 1.0 / 3.0));
		lab.b = 200 * (Math.pow(y, 1.0 / 3.0) - Math.pow(z, 1.0 / 3.0));
		return lab;
	}

	/**
	 * Calculate the color difference
	 * 
	 * @param lab1
	 * @param lab2
	 * @return totalColorDifference
	 */
	private static double getDelta(ColorLAB lab1, ColorLAB lab2) {
		double deltaL = lab1.l - lab2.l; // lightness difference
		double deltaA = lab1.a - lab2.a; // chromaticity difference
		double deltaB = lab1.b - lab2.b; // chromaticity difference
		return Math.pow((Math.pow(deltaL, 2) + Math.pow(deltaA, 2) + Math.pow(deltaB, 2)), 0.5); // total color difference
	}
	
	/**
	 * Contrast images
	 * 
	 * @param sourceImagePath
	 * @param destImagePath
	 */
	public static boolean contrastImages(String sourceImagePath, String destImagePath) throws Exception {
		BufferedImage sample = null;
		try {
			sample = ImageIO.read(new File(sourceImagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		int left = 0;
		int top = 0;
		Integer width = sample.getWidth();
		Integer height = sample.getHeight();
		
		return contrastImages(sourceImagePath, destImagePath, left, top, width, height);
	}
	
	/**
	 * Contrast images
	 * 
	 * @param sourceImagePath
	 * @param destImagePath
	 */
	public static boolean contrastImages(String sourceImagePath, String destImagePath, int left, int top, int width, int height) throws Exception {
		BufferedImage sample = null, actual = null;
		try {
			sample = ImageIO.read(new File(sourceImagePath));
			actual = ImageIO.read(new File(destImagePath));
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		boolean isMatched = true;
		for (int y = top; y < top + height; ++y) {
			for (int x = left; x < left + width; ++x) {
				int expRGB = sample.getRGB(x, y);
				int actRGB = actual.getRGB(x, y);
				if (expRGB != actRGB) {
					double deltaE = getDelta(xyz2lab(rgb2xyz(expRGB)), xyz2lab(rgb2xyz(actRGB)));
					if (deltaE > maxColorThreshold) {
						//TODO
					}
					isMatched = false;
				}
			}
		}
		return isMatched;
	}

	/**
	 * Contrast images
	 * 
	 * @param sourceImagePath
	 * @param destImagePath
	 */
	public static boolean contrastImages(String sourceImagePath, String destImagePath, WebElement we) throws Exception {
		wePosition = we.getLocation();
		weDim = we.getSize();
		int left = wePosition.getX();
		int top = wePosition.getY();
		Integer width = weDim.getWidth();
		Integer height = weDim.getHeight();
		
		return contrastImages(sourceImagePath, destImagePath, left, top, width, height);
	}
	
	/**
	 * RGB color space
	 * @author LingFei
	 */
	private static class ColorLAB {
		public double l;
		public double a;
		public double b;
	}
	
	/**
	 * XYZ color space
	 * @author LingFei
	 */
	private static class ColorXYZ {
		public double x;
		public double y;
		public double z;
	}
}