package modules;

import java.awt.image.BufferedImage;

/**
 * Module that performs color quantization of
 * an image.
 */
public class ColorQuantization implements Module {
    public static final int MASK = 0xff808080; // mask used for quantization, keeps the highest bit of R, G and B
    private BufferedImage original;
    
    public ColorQuantization(BufferedImage img) {
        original = img;
    }

	/**
	 * Applies color quantization to the original image,
	 * utilizing the constant mask defined in this class.
	 * 
	 * @return quantized image
	 */
	public BufferedImage getImage() {
	    int w = original.getWidth();
        int h = original.getHeight();
        // Create result image
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        
        // Go through every pixel of the image
        for(int x=0; x< w; x++){
            for(int y=0; y< h; y++){
                // Apply mask to original value and save it in result image
                result.setRGB(x,y, original.getRGB(x, y) & MASK);
            }
        }
        return result;
	}
}
