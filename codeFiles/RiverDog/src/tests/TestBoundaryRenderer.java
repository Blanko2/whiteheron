package tests;

import static org.junit.Assert.*;

import java.awt.Color;
import java.awt.Point;
import java.awt.Polygon;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import modules.BoundaryRenderer;

import org.junit.Test;

import riverObjects.ImageShape;

/**
 * Unit tests for the boundary renderer module, 
 * which takes image shapes as parameters and
 * renders their boundaries on the images passed 
 * in.
 */
public class TestBoundaryRenderer {

	@Test
    /**
     * Test that the module is drawing colors
     * at the correct coordinates and drawing 
     * a red line between the points (inclusive).
     */
	public void testGetImage() {
		//Create blank image
		BufferedImage img = new BufferedImage(9,9, BufferedImage.TYPE_INT_RGB);
		
		//Create a shape based on boundaries
		int xPoints[] = {0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 8, 7, 6, 5, 4, 3, 2, 1};
		int yPoints[] = {5, 4, 3, 2, 1, 0, 0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 8, 8, 7, 6};
		
		// Create a list of boundary points
		List<Point> pointList = new ArrayList<Point>();
		for (int i = 0; i < xPoints.length; i++) {
			pointList.add(new Point(xPoints[i], yPoints[i]));
		}
		
		Polygon p = new Polygon(xPoints, yPoints, xPoints.length);
		ImageShape is = new ImageShape(p, Color.RED);
		//Create and run boundary renderer
		BoundaryRenderer b = new BoundaryRenderer(img);
		b.setOutlineColor(Color.RED);
		
		ArrayList<ImageShape> list = new ArrayList<ImageShape>();
		list.add(is);
		b.setImageShapes(list);
		img = b.getImage();
		
		// Go through each pixel in the image and check that it matches the expected
		for (int x = 0; x < img.getWidth(); x++) {
			for (int y = 0; y < img.getHeight(); y++) {
				int color = img.getRGB(x, y);
				
				if (color == Color.BLACK.getRGB() && pointList.contains(new Point(x, y)))
					fail ("Point (" + x + ", " + y + ") should have been drawn on.");
				else if (color == Color.RED.getRGB() && !pointList.contains(new Point(x, y)))
					fail ("Point (" + x + ", " + y + ") should not have been drawn on.");
				else if (color != Color.BLACK.getRGB() && color != Color.RED.getRGB())
					fail ("Point (" + x + ", " + y + ") has a color that should not be in the image.");
			}
		}
	}

}
