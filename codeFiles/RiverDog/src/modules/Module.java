package modules;

import java.awt.image.BufferedImage;

/**
 * Interface for modules to be connected
 * to the pipeline.
 */
public interface Module {
    
    /**
     * Returns the image after filtering
     * it through the module.
     * 
     * @return processed image
     */
	public BufferedImage getImage ();	
}
