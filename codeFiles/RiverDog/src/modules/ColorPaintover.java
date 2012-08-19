package modules;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

/**
 * Module which replaces pixels of a given color
 * with pixels of another specified color.
 */
public class ColorPaintover implements Module {
    private int[] oldColors;
    private int newColor;
    private BufferedImage original;
    
    public ColorPaintover(BufferedImage img) {
        original = img;
    }
    
    /**
     * Sets the new color to be using
     * when painting over the old image
     * colors
     * 
     * @param c - the new color
     */
    public void setNewColor(Color c) {
        if (c != null)
            newColor = c.getRGB();
    }
    
    /**
     * Sets the old colors, which will be
     * painted over when processing the
     * image.
     * 
     * @param old - list of old colors
     */
    public void setOldColor(List<Color> old) {
        if (old != null) {
            oldColors = new int[old.size()];
            // Go through color objects, saving integer representation
            for (int i = 0; i < old.size(); i++)
                oldColors[i] = old.get(i).getRGB();
        }
    }

    /**
     * Creates a new image, similar to the original
     * but replacing the requested colors with the new
     * color. The default new color is black. This method
     * returns a copy of the original image.
     * 
     * @return image with pixels painted over
     */
    public BufferedImage getImage() {
        // Return null if there are no old colors set, or if original is null
        if (oldColors == null || oldColors.length == 0 || original == null)
            return null;
        
        int w = original.getWidth();
        int h = original.getHeight();
        // Create result image
        BufferedImage result = new BufferedImage(w, h, BufferedImage.TYPE_INT_RGB);
        
        // Go through every pixel of the image
        for(int x=0; x< w; x++){
            for(int y=0; y< h; y++){
                // Get color
                int color = original.getRGB( x, y );
                
                // Check if it is an old color
                for (int oldColor : oldColors) {
                    // If it is, set it to new color
                    if (color == oldColor)
                        color = newColor;
                }
                // Save pixel to new image
                result.setRGB(x, y, color);
            }
        }
        return result;
    }  
}
