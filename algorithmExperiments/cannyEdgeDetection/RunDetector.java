import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;

/**
 * This class is used to run the implementation
 * of Canny's edge detection algorithm.
 */
public class RunDetector {
    
    public static void main(String[] args) {
        try {
            //create the detector
            CannyEdgeDetector detector = new CannyEdgeDetector();
        
            //adjust its threshold parameters as desired
            detector.setLowThreshold(.5f);
            detector.setHighThreshold(1f);
        
            // Read in original image
            BufferedImage img = ImageIO.read( new File("river1.jpg"));
            
            // Set source image, run detector and get result
            detector.setSourceImage(img);
            detector.process();
            BufferedImage edges = detector.getEdgesImage();
            
            // Save version with edges
            ImageIO.write(edges, "png", new File("edges1.png"));
        }
        catch ( IOException e ) {
            System.out.println("Problem reading image.");
            e.printStackTrace();
        }
    }
}
