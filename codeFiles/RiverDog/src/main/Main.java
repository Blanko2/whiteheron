package main;

import pipeline.Pipeline;

public class Main {

	/**
	 * Starts the program running -- calls the pipeline class
	 * and passes images to it
	 * @param images -- batch of images to be checked
	 */
	public static void main(String[] images) {
	    new Pipeline(images).renderRiverBoundaries();
	}

}
