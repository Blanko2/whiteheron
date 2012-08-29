package modules;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.List;

import riverObjects.ImageShape;

public class BoundaryRenderer implements Module {
	private BufferedImage img;
	private List<ImageShape> shapes;
	private Color outlineColor;
	
	public BoundaryRenderer(BufferedImage img) {
		this.img = img;
	}
	
	public void setOutlineColor(Color c) {
		outlineColor = c;
	}
	
	/**
	 * Set the image shapes to be outlined
	 * on the picture
	 * @param shapes
	 */
	public void setImageShapes(List<ImageShape> shapes) {
		this.shapes = shapes;
	}

	@Override
	/**
	 * Returns the original image with the
	 * user-defined boundaries drawn on.
	 */
	public BufferedImage getImage() {
		// Return null if user hasn't specified outlined color/shapes
		if (outlineColor == null || shapes == null)
			return null;
		
		// Retrieve the image's Graphics2D
		// Set Graphics2D color
		// Iterate through all image shapes
			// For each shape, retrieve polygon (one of its fields)
			// Get all the points of polygon (they will be split into an xPoints and a yPoints array)
			// Iterate through the points
				// for each point, look at the next one
				// Draw a line from x1, y1 to x2, y2
			// Once you exit iteration, draw the line between the first and the last points
		// Dispose of Graphics2D object
		// Return the buffered image
		
		// Code fragment:
		// for (ImageShape shape : shapes) {
		// Polygon p = shape.getPolygon();
		// int[] xPoints = p.xpoints;
		// int[] yPoints = p.ypoints;
		// int length = xPoints.length; // Iterate through xpoints and ypoints
	//}
		
		// TODO Auto-generated method stub
		return null;
	}

}
