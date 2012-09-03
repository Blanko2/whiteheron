package modules;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import pipeline.Pipeline;
import riverObjects.ImageShape;

/**
 * This module detects and saves the ImageShapes
 * of all the non-white blobs in a picture. A 
 * shape is defined here as a group of pixels with
 * exactly the same RGB values.
 */
public class BlobDetection implements Module {
    private int white = Color.white.getRGB(); // Int representation of white
    private BufferedImage original;
    private List<ImageShape> imageShapes = new ArrayList<ImageShape>();
    
    public BlobDetection(BufferedImage img) {
        original = img;
    }
    
    /**
     * Finds and returns a list of all non-white
     * ImageShapes within the picture.
     * 
     * @return 
     */
    public List<ImageShape> findImageShapes() {
        // If image shapes have already been found, no need to re-detect
        if (!imageShapes.isEmpty()) {
            return imageShapes;
        }
        // Else, detect shapes
        else {
            int width = original.getWidth();
            int height = original.getHeight();

            // Create a boolean 2D array for storing visited places
            boolean[][] visited = new boolean[height][width];
            Point current = new Point();
           // Store the entry point for backtracking in Moore neighborhood algorithm
            Point previous = new Point(0, height);
            /* From bottom to top and left to right scan the pixels until a non-white,
             * not previously visited pixel is found */
            for (int x = 0; x < width; x++) {
                for (int y = height - 1; y >= 0; y--) {
                    if (!visited[y][x] && original.getRGB( x, y ) != white) {
                        // Add image shape to list
                        current.x = x;
                        current.y = y;
                        imageShapes.add(mooreNeighborhood(current, previous, visited, original));
                    }
                    
                    // Remember previously visited point
                    previous.x = x;
                    previous.y = y;
                }
            }
            return imageShapes;
        }
    }
    
    /**
     * This method uses the Moore neighborhood contour-finding algorithm 
     * to determine the boundaries of a group of pixels. It returns an
     * ImageShape which contains the group's color and boundaries. Note
     * that this algorithm may fail to find the complete contour of an
     * 8-connected group of pixels which is not 4-connected (i.e. a group
     * of pixels only connected by diagonals).
     * 
     * @param start - A starting point on the edge of the pixel group
     * @param entry - The point through which the starting point was found
     * @param visited - Array of visited pixels
     * @param img - Image being examined
     * @return an ImageShape containing group color and boundaries
     */
    public ImageShape mooreNeighborhood(Point start, Point entry, boolean[][] visited, BufferedImage img) {
        // Pixel color
        int color = img.getRGB( start.x, start.y );
        // Create resulting image shape
        Polygon p = new Polygon();
        ImageShape result = new ImageShape(p, new Color(color));
        
        // Set the current boundary point to be the starting point
        Point current = start;
        
        // Add first pixel to polygon
        p.addPoint( start.x, start.y );
        visited[start.y][start.x] = true;
        
        // Remember which pixel to backtrack to
        Point backtrack = entry;
        
        // Set clockwise to be the next clockwise pixel (from backtrack, with current as the center)
        Point clockwise = findNextClockwisePixel(backtrack, current, img);
        // While c not equal to s do
        while (!start.equals( clockwise )) {
            // If clockwise pixel is of the right color
            System.out.println(clockwise.x + ", " + clockwise.y);
            if (img.getRGB( clockwise.x, clockwise.y ) == color) {
                // Add it to polygon
                p.addPoint( clockwise.x, clockwise.y );
                visited[clockwise.y][clockwise.x] = true;
                // Step to new boundary point
                current = clockwise;
                // Set clockwise to be the next clockwise pixel (from backtrack, with current as the center)
                clockwise = findNextClockwisePixel(backtrack, current, img);
            }
            else {
                // Not the right color, so move on to the next pixel
                backtrack = clockwise;
                // Set clockwise to be the next clockwise pixel (from backtrack, with current as the center)
                clockwise = findNextClockwisePixel(backtrack, current, img);
            }
        }       
        return result;
    }
    
    /**
     * Finds the next clockwise pixel in the image, going around the
     * center point, starting after the from point. Note that this method
     * will skip pixels which are out of the image boundaries.
     * 
     * @param from - point from which to start the clockwise search
     * @param center - reference point which will be looked around
     * @param img - the source image
     * @return the next clockwise point within the boundaries of the image
     */
    private Point findNextClockwisePixel(Point from, Point center, BufferedImage img) {
        Point result;
        int width = img.getWidth();
        int height = img.getHeight();
        
        // Remember initial starting point
        Point start = from;
        
        // Determine where start is in relation to center
        Point offset = new Point (start.x - center.x, start.y - center.y);
        
        // Look for the next clockwise pixel while one is not found
        while(true) {
            // If in same column
            if (offset.x == 0) {
                // If one row above
                if (offset.y == -1)
                    // We are above center, so move to upper right corner
                    from.x++;
                // Else it will be one row below
                else
                    // We are below center, so move to lower left corner
                    from.x--;
            }
            // Else, if one column to the left
            else if (offset.x == -1) {
                // If one row above
                if (offset.y == -1)
                    // We are in upper left corner, so move above center
                    from.x++;
                // Else, if in same row
                else if (offset.y == 0)
                    // We are to the left of center, so move to upper left corner
                    from.y--;
                // Else it will be one row below
                else
                    // We are in lower left corner, so move to the left of center
                    from.y--;
            }
            // Else it will be one column to the right
            else {
                // If one row above
                if (offset.y == -1)
                    // We are in upper right corner, so move to the right of center
                    from.y++;
                // Else, if in same row
                else if (offset.y == 0)
                    // We are to the right of center, so move to lower right corner
                    from.y++;
                // Else it will be one row below
                else
                    // We are in lower right corner, so move below center
                    from.y--;
            }
            
            // If we got to the start or the new value is within bounds, we are done
            if (from.x >= 0 && from.x < width && from.y >= 0 && from.y < height) {
                result = from;
                return result;
            }
            // Else we haven't found the next pixel yet, so recalculate offset and continue
            offset = new Point(from.x - center.x, from.y - center.y);
        }
    }

    /**
     * Returns a copy of the original image
     * provided to the module.
     */
    public BufferedImage getImage() {
        return Pipeline.deepCopy( original );
    }

}
