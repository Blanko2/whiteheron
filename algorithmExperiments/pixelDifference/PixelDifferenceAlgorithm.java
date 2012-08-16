package pixelDifference;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

public class PixelDifferenceAlgorithm {

    /**
     * Run the application, performing the horizontal
     * and vertical differences for an image.
     */
    public static void main(String[] args) {
        try {
            BufferedImage img = ImageIO.read( new File("house.jpg") );
            performDifferenceInImage(img, true, "horizDiff.png"); // do horizontal difference
            performDifferenceInImage(img, false, "vertDiff.png"); // do vertical difference
        }
        catch ( IOException e ) {
            System.out.println("Problem reading in image.");
            e.printStackTrace();
        }
    }
    
    /**
     * Performs a the simple difference between the pixels in a gray-scale image.
     * The objective of this difference is to highlight edges in a picture (i. e.
     * large variations of brightness between adjacent pixels which indicate a
     * likely edge).
     * 
     * @param img - the gray-scale image to be analysed
     * @param horizontal - boolean stating whether the difference calculation
     * should be horizontal or vertical
     * @param outputName - the output name to save the resulting image as
     */
    private static void performDifferenceInImage( BufferedImage img, boolean horizontal, String outputName ) {
        
        // Create new image to store result
        BufferedImage result = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        // Create variables to hold values in loop
        Color c1;
        Color c2;
        Color diff;
        int width = img.getWidth();
        int height = img.getHeight();
        
        // Go through each pixel in the picture
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                
                c1 = new Color(img.getRGB( x, y )); // Get original pixel
                
                // If doing horizontal difference and within bounds
                if (horizontal && x + 1 < width) {
                    c2 = new Color(img.getRGB( x + 1, y ));
                }
                // If doing vertical difference and within bounds
                else if (!horizontal && y + 1 < height){
                    c2 = new Color(img.getRGB( x, y + 1 ));
                }
                // If outside bounds
                else {
                    c2 = new Color(0);
                }
                
                // Calculate difference
                diff = new Color(Math.abs(c1.getRed() - c2.getRed()), Math.abs(c1.getGreen() - c2.getGreen()), Math.abs(c1.getBlue() - c2.getBlue()));
                
                // Write result
                result.setRGB( x, y, diff.getRGB() );
            }
        }
        
        try {
            ImageIO.write( result, "png", new File(outputName) );
        }
        catch ( IOException e ) {
            System.out.println("Problem writing result image to disk.");
            e.printStackTrace();
        }
    }
}
