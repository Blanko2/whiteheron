package tests;

import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import modules.EdgeMajority;

import org.junit.Test;

/**
 * Unit tests for EdgeMajority module,
 * which determines the color that appears
 * most frequently around the edges of an
 * image, eliminating it from the picture
 * in case it is not white.
 */
public class TestEdgeMajority {

    /**
     * Test that the module paints over a 
     * non-white color majority around the 
     * edges of a picture.
     */
    @Test
    public void testNonWhiteMajority() {
        // int representation of the color known to be the majority
        int colorCode = Color.red.getRGB();
        try {
            // Read in test image
            BufferedImage img = ImageIO.read( new File("src/tests/testImage3.png") );
            
            // Run edge majority
            EdgeMajority majority = new EdgeMajority(img);
            img = majority.getImage();
            
            // Go through each pixel in the resulting image
            int width = img.getWidth();
            int height = img.getHeight();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    // Get color of current pixel
                    int c = img.getRGB( x, y );
                    // If majority color was not painted over, fail
                    if (c == colorCode)
                        fail ("Resulting picture contains color that should have been painted over -- code " + c);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            fail( "Could not load test image." );
        }
    }
    
    /**
     * Test that the module does not modify
     * an image when the color majority around the
     * edges of a picture is white. The image should
     * remain unaltered.
     */
    @Test
    public void testWhiteMajority() {
        try {
            // Read in test image
            BufferedImage original = ImageIO.read( new File("src/tests/testImage4.png") );
            
            // Run edge majority
            EdgeMajority majority = new EdgeMajority(original);
            BufferedImage result = majority.getImage();
            
            // Go through each pixel in the resulting image
            int width = result.getWidth();
            int height = result.getHeight();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    // If color has changed from the original to the result, fail
                    int c = result.getRGB(x, y);
                    if (c != original.getRGB( x, y ))
                        fail ("Resulting picture contains color modification that should not have happened -- code " + c);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            fail( "Could not load test image." );
        }
    }

}
