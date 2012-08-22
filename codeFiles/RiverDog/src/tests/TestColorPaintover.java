package tests;

import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import modules.ColorPaintover;

import org.junit.Test;

/**
 * Unit tests for color paint-over module,
 * which replaces the listed colors of an image
 * with the new specified color.
 */
public class TestColorPaintover {
    // Colors to be replaced
    private Color grey = new Color(128, 128, 128);
    private int greyCode = grey.getRGB();
    private Color red = new Color(128, 0, 0);
    private int redCode = red.getRGB();
    private Color green = new Color(0, 128, 0);
    private int greenCode = green.getRGB();
    
    // New color
    private Color white = Color.white;
    
    @Test
    /**
     * Test that the module paints over colors
     * according to what is specified by user.
     */
    public void testGetImage() {
        try {
            // Read in test image
            BufferedImage img = ImageIO.read( new File("src/tests/testImage2.png") );
            
            List<Color> oldColors = new ArrayList<Color>();
            oldColors.add( grey );
            oldColors.add( red );
            oldColors.add( green );
            
            // Run color paint-over
            ColorPaintover paint = new ColorPaintover(img);
            paint.setOldColor( oldColors );
            paint.setNewColor( white );
            img = paint.getImage();
            
            // Go through each pixel in the resulting image
            int width = img.getWidth();
            int height = img.getHeight();
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    // Get color of current pixel
                    int c = img.getRGB( x, y );
                    if (c == greyCode || c == redCode || c == greenCode)
                        fail ("Resulting picture contains color that should have been painted over -- code " + c);
                }
            }
        }
        catch (IOException e) {
            e.printStackTrace();
            fail( "Could not load test image." );
        }
    }

}
