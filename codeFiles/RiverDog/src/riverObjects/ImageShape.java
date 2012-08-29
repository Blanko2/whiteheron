package riverObjects;

import java.awt.Color;
import java.awt.Polygon;

/**
 *  Stores the polygon boundary and color of a 
 *  given object in a river image
 *  
 * @author mendesrodr
 *
 */
public class ImageShape {
	private Polygon polygon;
	private Color color;
	
	public ImageShape(Polygon boundaries, Color c) {
		polygon = boundaries;
		color = c;
	}
	
	public Polygon getPolygon() {
		return polygon;
	}
	
	public Color getColor() {
		return color;
	}
}
