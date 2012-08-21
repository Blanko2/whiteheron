package tests;

import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

import modules.ColorQuantization;

import org.junit.Test;

/**
 * Unit tests for color quantization module,
 * which reduces the color palette of an image
 * to eight values.
 */
public class TestColorQuantization {
    // All expected colors after quantization
    private int black = new Color(0, 0, 0).getRGB();
    private int grey = new Color(128, 128, 128).getRGB();
    private int red = new Color(128, 0, 0).getRGB();
    private int green = new Color(0, 128, 0).getRGB();
    private int blue = new Color(0, 0, 128).getRGB();
    private int purple = new Color(128, 0, 128).getRGB();
    private int golden = new Color(128, 128, 0).getRGB();
    private int aquamarine = new Color(0, 128, 128).getRGB();

    @Test
    /**
     * Test that the module quantizes image colors
     * to one of the eight expected colors.
     */
    public void testGetImage() {
        try {
            // Read in test image
            BufferedImage img = ImageIO.read( new File("src/tests/testImage1.jpg") );
            
            // Run color quantization
            ColorQuantization quant = new ColorQuantization(img);
            img = quant.getImage();
            
            // Go through each pixel in the resulting image
            int width = img.getWidth();
            int height = img.getHeight();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    // Get color of current pixel
                    int c = img.getRGB( x, y );
                    if (c != black && c != grey && c != red &&
                        c != green && c != blue && c != purple &&
                        c != golden && c != aquamarine)
                        fail ("Resulting picture contains non-quantized color -- code " + c);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            fail( "Could not load test image." );
        }
    }

}
