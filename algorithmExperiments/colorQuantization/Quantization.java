import java.awt.image.BufferedImage;

/**
 * This class performs simple color quantization of images,
 * utilising masks to diminish the amount of colors in a given picture.
 * Note that the masks below are not at all exhaustive of all palette
 * possibilities.
 */
public class Quantization {
    /*
     * The masks below are used for filtering the bits in an integer that
     * represents an RGB color. Note that the leftmost byte represents
     * the color's alpha channel, and the other bytes represent red, green
     * and blue (from left to right).
     */
    public static final int MASK_0 = 0x00800000; // 0 bits per channel (except red)
    public static final int MASK_1 = 0xff808080; // 1 bit per channel
    public static final int MASK_2 = 0xffc0c0c0; // 2 bits per channel
    public static final int MASK_3 = 0xffe0e0e0; // 3 bits per channel
    public static final int MASK_4 = 0xfff0f0f0; // 4 bits per channel

    /**
     * Returns a version of the original image with reduced amount of colours
     * (colours filtered using the mask passed in). The method returns the
     * processed image.
     * 
     * @param mask - to be used for color quantization
     * @param original - the original image
     * @return the processed image
     */
    public static BufferedImage quantizeImageColor(int mask, BufferedImage original) {
        int w = original.getWidth();
        int h = original.getHeight();
        // Create result image
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        
        // Go through every pixel of the image
        for(int x=0; x< w; x++){
            for(int y=0; y< h; y++){
                // Apply mask to original value and save it in result image
                result.setRGB(x,y, original.getRGB(x, y) & mask);
            }
        }
        return result;
    }
}

