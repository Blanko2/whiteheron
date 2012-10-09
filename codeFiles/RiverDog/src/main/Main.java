package main;

import pipeline.Pipeline;
import view.MainFrame;

/**
 * This class contains the application's
 * entry point. 
 */
public class Main {
    
    private final static String[] def = {"./images", "./results"};
    
	/**
	 * Starts the program running -- calls the pipeline class
	 * and passes images to it
	 * @param directories -- directory of images and directory of results
	 */
	public static void main(String[] directories) {
            if(directories.length == 0){
                MainFrame mainframe = new MainFrame(def);
            }
            else{
                MainFrame mainframe = new MainFrame(directories);
            }
            
            
	    //new Pipeline(images).startPipeline();
	}

}
