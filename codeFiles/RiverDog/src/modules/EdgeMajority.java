package modules;

import java.awt.image.BufferedImage;

/**
 * Module that determines the color majority
 * around the four edges of a picture, overwriting
 * that color with white.
 */
public class EdgeMajority implements Module {
    private BufferedImage original;
    
    public EdgeMajority(BufferedImage img) {
        original = img;
    }

    public BufferedImage getImage() {
        // TODO Auto-generated method stub
        return original;
    }

}
