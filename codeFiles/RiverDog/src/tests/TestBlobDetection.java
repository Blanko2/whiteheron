package tests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.awt.Point;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import modules.BlobDetection;

import org.junit.Test;

import riverObjects.ImageShape;

public class TestBlobDetection {

    @Test
    /**
     * Test that the non-white image shapes are
     * detected correctly by the module.
     */
    public void testFindImageShapes() {
        try {
            // Read in test image
            BufferedImage img = ImageIO.read( new File("src/tests/testImage5.png") );
            
            // Run blob detection
            BlobDetection detection = new BlobDetection(img, img);
            List<ImageShape> shapes = detection.findImageShapes();

            // It should find one shape
            assertEquals(shapes.size(), 1);
            
            // The shape should have the following boundary points:
            Point[] boundaries = {
                    new Point(0, 5),
                    new Point(0, 4),
                    new Point(1, 4),
                    new Point(2, 3),
                    new Point(2, 2),
                    new Point(2, 1),
                    new Point(2, 0),
                    new Point(3, 0),
                    new Point(3, 1),
                    new Point(4, 2),
                    new Point(5, 2),
                    new Point(6, 2),
                    new Point(7, 2),
                    new Point(7, 3),
                    new Point(6, 3),
                    new Point(5, 4),
                    new Point(5, 5),
                    new Point(5, 6),
                    new Point(5, 7),
                    new Point(4, 7),
                    new Point(4, 6),
                    new Point(3, 5),
                    new Point(2, 6),
                    new Point(2, 7),
                    new Point(2, 6),
                    new Point(1, 5)
            };
            
            // Get shape boundaries
            int[] xpoints = shapes.get( 0 ).getPolygon().xpoints;
            int[] ypoints = shapes.get( 0 ).getPolygon().ypoints;
            
            // Check if they match the expected
            for (int i = 0; i < boundaries.length; i++) {
                if (xpoints[i] != boundaries[i].x || ypoints[i] != boundaries[i].y) {
                    fail("Boundaries don't match the expected at point (" + xpoints[i] + ", " + ypoints[i] + ")");
                }
            }
            
        }
        catch (IOException e) {
            e.printStackTrace();
            fail( "Could not load test image." );
        }
    }
    
    @Test
    /**
     * Test that both the red and the green shapes
     * are detected. The shapes touch each other as well
     * as the borders of the picture.
     */
    public void testFindImageShapesColours() {
        try {
            // Read in test image
            BufferedImage img = ImageIO.read( new File("src/tests/testImage6.png") );
            
            // Run blob detection
            BlobDetection detection = new BlobDetection(img, img);
            List<ImageShape> shapes = detection.findImageShapes();

            // It should find one shape
            assertEquals(shapes.size(), 2);
            
            // The red shape (found first) should have the following boundary points:
            Point[] boundariesRed = {
                    new Point(0, 3),
                    new Point(0, 2),
                    new Point(1, 2),
                    new Point(1, 3)
            };
            
            // Get red shape boundaries
            int[] xpointsRed = shapes.get( 0 ).getPolygon().xpoints;
            int[] ypointsRed = shapes.get( 0 ).getPolygon().ypoints;
            
            // Check if they match the expected
            for (int i = 0; i < boundariesRed.length; i++) {
                if (xpointsRed[i] != boundariesRed[i].x || ypointsRed[i] != boundariesRed[i].y) {
                    fail("Red boundaries don't match the expected at point (" + xpointsRed[i] + ", " + ypointsRed[i] + ")");
                }
            }
            
            // The green shape (found last) should have the following boundary points:
            Point[] boundariesGreen = {
                    new Point(0, 1),
                    new Point(0, 0),
                    new Point(1, 0),
                    new Point(1, 1)
            };
            
            // Get green shape boundaries
            int[] xpointsGreen = shapes.get( 1 ).getPolygon().xpoints;
            int[] ypointsGreen = shapes.get( 1 ).getPolygon().ypoints;
            
            // Check if they match the expected
            for (int i = 0; i < boundariesGreen.length; i++) {
                if (xpointsGreen[i] != boundariesGreen[i].x || ypointsGreen[i] != boundariesGreen[i].y) {
                    fail("Green boundaries don't match the expected at point (" + xpointsGreen[i] + ", " + ypointsGreen[i] + ")");
                }
            }
            
        }
        catch (IOException e) {
            e.printStackTrace();
            fail( "Could not load test image." );
        }
    }
    
    @Test
    /**
     * Test that the detection algorithm
     * successfully terminates for an isolated
     * pixel.
     */
    public void testFindSinglePixelImage() {
        try {
            // Read in test image
            BufferedImage img = ImageIO.read( new File("src/tests/testImage7.png") );
            
            // Run blob detection
            BlobDetection detection = new BlobDetection(img, img);
            List<ImageShape> shapes = detection.findImageShapes();

            // It should find one shape
            assertEquals(shapes.size(), 1);
            
            // The shape should have the following boundary point:
            Point[] boundaries = { new Point(1, 1) };
            
            // Get shape boundaries
            int[] xpoints = shapes.get( 0 ).getPolygon().xpoints;
            int[] ypoints = shapes.get( 0 ).getPolygon().ypoints;
            
            // Check if they match the expected
            if (xpoints[0] != boundaries[0].x || ypoints[0] != boundaries[0].y) {
                fail("Boundaries don't match the expected at point (" + xpoints[0] + ", " + ypoints[0] + ")");
            }
            
        }
        catch (IOException e) {
            e.printStackTrace();
            fail( "Could not load test image." );
        }
    }
}
