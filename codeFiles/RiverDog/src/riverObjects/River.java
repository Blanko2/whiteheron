package riverObjects;

import java.awt.Color;
import java.awt.Polygon;
import java.util.ArrayList;

/**
 *  stores the river object from an image
 *  boundary of the river is stored as an array of
 *  points -- maybe this can change
 *  
 * @author mendesrodr
 *
 */
public class River extends ImageShape {
	
	public River(Polygon boundaries, Color c) {
		super(boundaries, c);
	}

	//not sure how to store this yet, is a WIP
	private ArrayList<Integer> boundary;
}
