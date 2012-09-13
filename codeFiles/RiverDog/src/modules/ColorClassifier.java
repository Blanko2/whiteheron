package modules;

import java.awt.Color;
import java.awt.image.BufferedImage;

/**
 * This module translates each pixel in an image to the
 * HSB color model, classifying it as a potential river pixel
 * if it is within the limits of hue, saturation and brightness.
 * If a pixel is within those limits, it is a potential river
 * pixel and will be painted black in the resulting image.
 * Otherwise, it will be painted white.
 */
public class ColorClassifier implements Module {
    // Masks for retrieving RGB values
    private int redMask = 0x00ff0000;
    private int greenMask = 0x0000ff00;
    private int blueMask = 0x000000ff;
    // Integer representations of black/white
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
	/**
	 * Translates the color of each pixel to the HSB
	 * model, checking if it is a potential river color
	 * and painting the pixel location with black in
	 * the resulting image if it is.
	 */
	public BufferedImage getImage() {

		
		int width = original.getWidth();
		int height = original.getHeight();
		BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		
		// Go through each pixel in the original
		for (int x = 0; x < width; x++) {
			for (int y = 0; y < height; y++) {
				// Convert RGB value to HSB
				int[] rgb = getRGBComponents(original.getRGB(x, y));
				float[] hsb = Color.RGBtoHSB(rgb[0], rgb[1], rgb[2], null);
				
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
	
	/**
	 * Helper method which checks whether the HSB
	 * values of a color are within the set thresholds
	 * for hue, saturation and brightness.
	 * 
	 * @param hsb
	 * @return true if color within all thresholds,
	 * false otherwise.
	 */
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
	
	/**
	 * Separates the RGB components of an integer
	 * representation of a color, returning an
	 * array with three integers (R, G and B).
	 * 
	 * @param color
	 * @return array of R,G,B
	 */
	private int[] getRGBComponents(int color) {
	    int[] result = new int[3];
        /* First use masks to retrieve RGB values,
         * then shift them towards the least
         * significant bit as appropriate */
        result[0] = color & redMask;
        result[0] = result[0] >> 16;
        result[1]= color & greenMask;
        result[1] = result[1] >> 8;
        result[2] = color & blueMask;
	    return result;
	}
}