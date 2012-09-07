package modules;

import java.awt.Color;
import java.awt.image.BufferedImage;

public class ColorClassifier implements Module {
	private int black = Color.BLACK.getRGB();
	private int white = Color.WHITE.getRGB();
	// Upper border hue is 265, so 265/360 is the percentage below
	public static final float HUE_UPPER_LIMIT = 0.736111f;
	public static final float HUE_LOWER_LIMIT = 0.444444f;
	public static final float SATURATION_UPPER_LIMIT = 1f;
	public static final float SATURATION_LOWER_LIMIT = 0.15f;
	public static final float BRIGHTNESS_UPPER_LIMIT = 0.9f;
	public static final float BRIGHTNESS_LOWER_LIMIT = 0.1f;
	private BufferedImage original;
	
	public ColorClassifier(BufferedImage img) {
		original = img;
	}
	
	@Override
	public BufferedImage getImage() {
		// Masks for retrieving RGB values
		int redMask = 0x00ff0000;
		int greenMask = 0x0000ff00;
		int blueMask = 0x000000ff;
		
		int width = original.getWidth();
		int height = original.getHeight();
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		// Go through each pixel in the original
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// Convert RGB value to HSB
				
				/* First use masks to retrieve RGB values,
				 * then shift them towards the least
				 * significant bit as appropriate */
				int rgb = original.getRGB(x, y);
				int red = rgb & redMask;
				red = red >> 16;
				int green = rgb & greenMask;
				green = green >> 8;
				int blue = rgb & blueMask;
				
				float[] hsb = Color.RGBtoHSB(red, green, blue, null);
				
				// Check that hue, saturation and brightness describe a color that could be a river
				// If it can be a river, output it as black
				if (checkRiverHSB(hsb))
					result.setRGB(x, y, black);
				// If it cannot be a river, output it as white				
				else
					result.setRGB(x, y, white);
					
			}
		}
		return result;
	}
	
	private boolean checkRiverHSB (float[] hsb) {
		float huePercent = hsb[0];
		float satPercent = hsb[1];
		float brightPercent = hsb[2];
		
		if (huePercent > HUE_UPPER_LIMIT || huePercent < HUE_LOWER_LIMIT )
			return false;
		if (satPercent > SATURATION_UPPER_LIMIT || satPercent < SATURATION_LOWER_LIMIT )
			return false;
		if (brightPercent > BRIGHTNESS_UPPER_LIMIT || brightPercent < BRIGHTNESS_LOWER_LIMIT )
			return false;
		
		return true;
	}
	
//	public static void main(String[] args) {
		//Test 1 - Masking test
//		int redMask = 0x00ff0000;
//		int greenMask = 0x0000ff00;
//		int blueMask = 0x000000ff;
//		
//		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_RGB);
//		Color c = new Color(10, 20, 220);
//		img.setRGB(0, 0, c.getRGB());
//		
//		int rgb = img.getRGB(0, 0);
//		int red = rgb & redMask;
//		red = red >> 16;
//		int green = rgb & greenMask;
//		green = green >> 8;
//		int blue = rgb & blueMask;
//		
//		if (red != 10)
//			System.out.println("Red doesn't work. It thinks it should be " + red);
//		if (green != 20)
//			System.out.println("Green doesn't work. It thinks it should be " + green);
//		if (blue != 220)
//			System.out.println("Blue doesn't work. It thinks it should be " + blue);
//		
//		System.out.println("Done");
		
		// Test 2 - HSB test
//		float[] hsb = Color.RGBtoHSB(255, 0, 89, null);
//		System.out.println("Hue: " + hsb[0]);
//		System.out.println("Saturation: " + hsb[1]);
//		System.out.println("Brightness: " + hsb[2]);
//	}

}
