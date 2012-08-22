import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This class is used to run the implementation
 * of the color quantization algorithm.
 */
public class RunQuantization {
    
    public static void main(String[] args) {
        try {      
            // Read in original image
            BufferedImage img = ImageIO.read( new File("river1.jpg"));
            
            // Process image and obtain result
            BufferedImage quantImg =  Quantization.quantizeImageColor( Quantization.MASK_1, img );
            
            // Save quantized version
            ImageIO.write(quantImg, "png", new File("quant1.png"));
        }
        catch ( IOException e ) {
            System.out.println("Problem reading image.");
            e.printStackTrace();
        }
    }
}
