package tests;

import static org.junit.Assert.fail;

import java.awt.Color;
import java.awt.image.BufferedImage;

import modules.ColorClassifier;

import org.junit.Test;

/**
 * Unit tests for color classifier module,
 * which goes through all the pixels in an image.
 * If the pixel is within the threshold for a river
 * color (HSB), it colors the corresponding pixel
 * in the resulting image black. Otherwise, the
 * pixel is colored white.
 */
public class TestColorClassifier {

    @Test
    /**
     * Tests that the correct pixels are
     * painted black for an image with potential
     * river colors.
     */
    public void testGetImage() {
        // Create a new 3x3 image that has some river colors
        BufferedImage image = new BufferedImage(3, 3, BufferedImage.TYPE_INT_RGB);
        // Find a color that is within the threshold for river colors
        float hue = (float)Math.random() * (ColorClassifier.HUE_UPPER_LIMIT - ColorClassifier.HUE_LOWER_LIMIT) + ColorClassifier.HUE_LOWER_LIMIT;
        float saturation = (float)Math.random() * (ColorClassifier.SATURATION_UPPER_LIMIT - ColorClassifier.SATURATION_LOWER_LIMIT) + ColorClassifier.SATURATION_LOWER_LIMIT;
        float brightness = (float)Math.random() * (ColorClassifier.BRIGHTNESS_UPPER_LIMIT - ColorClassifier.BRIGHTNESS_LOWER_LIMIT) + ColorClassifier.BRIGHTNESS_LOWER_LIMIT;
        
        // Turn the color into RGB
        int riverColor = Color.HSBtoRGB( hue, saturation, brightness );
        
        /* NOTE: some of the lines above are marked as dead code because
         * the constant values do not match the if conditions. However, please
         * keep the code there in case the constant values are changed later.*/
        
        // Find a color that is not within the threshold for river colors
        if (ColorClassifier.HUE_LOWER_LIMIT == 0f)
            // Use upper limit
            hue = (float)Math.random() * (1f - ColorClassifier.HUE_UPPER_LIMIT) + ColorClassifier.HUE_UPPER_LIMIT; 
        else
            // Use lower limit
            hue = (float)Math.random() * ColorClassifier.HUE_LOWER_LIMIT;
        
        if (ColorClassifier.SATURATION_LOWER_LIMIT == 0f)
            // Use upper limit
            saturation = (float)Math.random() * (1f - ColorClassifier.SATURATION_UPPER_LIMIT) + ColorClassifier.SATURATION_UPPER_LIMIT; 
        else
            // Use lower limit
            saturation = (float)Math.random() * ColorClassifier.SATURATION_LOWER_LIMIT;
        
        if (ColorClassifier.BRIGHTNESS_LOWER_LIMIT == 0f)
            // Use upper limit
            brightness = (float)Math.random() * (1f - ColorClassifier.BRIGHTNESS_UPPER_LIMIT) + ColorClassifier.BRIGHTNESS_UPPER_LIMIT; 
        else
            // Use lower limit
            brightness = (float)Math.random() * ColorClassifier.BRIGHTNESS_LOWER_LIMIT;
        
        // Turn the color into RGB
        int notRiverColor = Color.HSBtoRGB( hue, saturation, brightness );
        
        // Write a diagonal of river colors into the image (all the others should
        // not be river colors)
        image.setRGB( 0, 0, riverColor );
        image.setRGB( 1, 1, riverColor );
        image.setRGB( 2, 2, riverColor );
        image.setRGB( 0, 1, notRiverColor );
        image.setRGB( 0, 2, notRiverColor );
        image.setRGB( 1, 0, notRiverColor );
        image.setRGB( 1, 2, notRiverColor );
        image.setRGB( 2, 0, notRiverColor );
        image.setRGB( 2, 1, notRiverColor );
        
        // Now run the color classification module
        ColorClassifier cc = new ColorClassifier(image);
        BufferedImage result = cc.getImage();
        
        // Create 2D array of expected colors
        int white = Color.WHITE.getRGB();
        int black = Color.BLACK.getRGB();
        
        int[][] expected= {{black, white, white}, 
                           {white, black, white}, 
                           {white, white, black}};
        
        // Check if classified image matches the expected colors
        for (int x = 0; x < result.getWidth(); x++) {
            for (int y = 0; y < result.getHeight(); y++) {
                
                // Fail if colors don't match
                if (result.getRGB( x, y ) != expected[y][x])
                    fail( "Color is not the expected at position " + x + ", " + y + " of the picture." );
            }
        }
    }
}