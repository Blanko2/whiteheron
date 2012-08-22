package modules;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import pipeline.Pipeline;

/**
 * Module that determines the color majority
 * around the four edges of a picture, overwriting
 * that color with white.
 */
public class EdgeMajority implements Module {
    private BufferedImage original;
    // Create map to store color ints and frequency ints
    private HashMap<Integer, Integer> colorHistogram = new HashMap<Integer, Integer>();
    private int width;
    private int height;
    
    public EdgeMajority(BufferedImage img) {
        original = img;
        if (img != null) {
            width = img.getWidth();
            height = img.getHeight();
        }
    }

    /**
     * Determines the color majority around the edges
     * of an image. If the majority is not white, replaces
     * all occurrences of that color with white. Else,
     * if the majority is white, an unaltered copy of
     * the original is returned.
     */
    public BufferedImage getImage() {
        
        int x;
        int y;

        // Go through top edge
        y = 0;
        for (x = 0; x < width; x++)
            addColorToHistogram( original.getRGB( x, y ), colorHistogram);
        
        // Go through bottom edge
        y = height - 1;
        for (x = 0; x < width; x++)
            addColorToHistogram( original.getRGB( x, y ), colorHistogram);
        
        // Go through left edge (excluding first and last pixels - already checked)
         x = 0;
        for (y = 0; y < height; y++)
            addColorToHistogram( original.getRGB( x, y ), colorHistogram);
        
        // Go through right edge (excluding first and last pixels - already checked)
        x = width - 1;
       for (y = 0; y < height; y++)
           addColorToHistogram( original.getRGB( x, y ), colorHistogram);
        
       // Get int representation of color that makes up the majority of edges
       int color = getColorMajority(colorHistogram);
       
       // If majority is white, return copy of original picture
       if (color == Color.WHITE.getRGB()) {
           return Pipeline.deepCopy( original );
       }
       
       // If not, repaint picture, eliminating the majority color
       else {
           // Create ColorPaintover
           ColorPaintover paint = new ColorPaintover(original);
           // Set colors
           paint.setNewColor( Color.WHITE );
           ArrayList<Color> oldColor = new ArrayList<Color>();
           oldColor.add(new Color(color));
           paint.setOldColor(oldColor);
           // Use module to replace colors and return the result
           return paint.getImage();
       }
    }
    
    /**
     * Helper method to add a color to the color frequency histogram.
     * 
     * @param color
     * @param colorHistogram
     */
    private void addColorToHistogram(int color, Map<Integer, Integer> colorHistogram) {
        
        // If histogram doesn't contain color, put it in
        if (!colorHistogram.containsKey( color ))
            colorHistogram.put( color, 1 );
        // else, update histogram
        else {
            int curCount = colorHistogram.get( color );
            colorHistogram.put( color, curCount + 1);
        }
    }
    
    /**
     * Helper method to analyze the color frequency histogram and
     * determine which color makes up for the majority of the pixels
     * around the image edges.
     * 
     * @param colorHistogram
     * @return integer representation of majority color
     */
    private int getColorMajority(Map<Integer, Integer> colorHistogram) {
        // Retrieve set of entries for histogram and wrap them in a list (for sorting)
        ArrayList <Entry<Integer, Integer>> entries = new ArrayList<Entry <Integer, Integer>>(colorHistogram.entrySet());
        
        /* Create a comparator to sort entry set by color frequency. We want to sort the list
         * backwards (from highest to lowest frequency), so return negative int if o1 > o2
         * and positive int if o2 > o1. */
        Comparator<Entry<Integer, Integer>> comparator = new Comparator<Entry<Integer, Integer>>() {
            public int compare( Entry< Integer, Integer > o1, Entry< Integer, Integer > o2 ) {
                return o2.getValue() - o1.getValue();
            }
        };
        
        // Sort list
        Collections.sort( entries, comparator );
        
        // Return the most frequent color (first position in list)
        return entries.get( 0 ).getKey();
    }

}
