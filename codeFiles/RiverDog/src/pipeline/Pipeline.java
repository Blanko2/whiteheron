package pipeline;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.imageio.ImageIO;

import modules.BlobDetection;
import modules.BoundaryRenderer;
import modules.ColorClassifier;
import riverObjects.ImageShape;

/**
 * This class implements a pipeline which
 * process river images, filtering them through
 * image processing modules and saving the
 * results to disk. The objective of the pipeline
 * is to detect river boundaries in images.
 */
public class Pipeline {
	//declaring quantization colors
	Color quantBlack 	= new Color (0, 0, 0);
	Color quantGrey 	= new Color (128,128,128);
	Color quantRed 		= new Color (128,0,0);
	Color quantGreen 	= new Color (0,128,0);
	Color quantBlue		= new Color (0,0,128);
	Color quantYello 	= new Color (128,128,0);
	Color quantPurp 	= new Color (128,0,128);
	Color quantAqua		= new Color (0,128,128);
	
	Color[] RiverCols 	= {quantBlack, quantBlue, quantAqua, quantGrey};
	Color[] notRiver	= {quantRed, quantGreen, quantYello, quantPurp};
	
    private String[] originalNames;

    public Pipeline( String[] imageNames ) {
        originalNames = imageNames;
    }  
    
   
    public Pipeline(){
        
    }
    
    public BufferedImage startPipeline(File image){
        BufferedImage img = null;
        try{
            img = ImageIO.read(image);
        }catch(IOException e){System.out.printf("Failed to read image: '%s'\n", 
                image.getName());}
        
        //==========================================================
        //              COLOR CLASSIFIER
        //==========================================================
        /* Use the color classifier to determine which pixels are
         * likely to be part of a river */

        ColorClassifier cc = new ColorClassifier(img);
        System.out.println("\tRun color classifier");
        BufferedImage classifiedImg = cc.getImage();
//        try {
//                ImageIO.write( classifiedImg, "png",
//                                new File(image.getCanonicalPath().replaceAll( "\\..+$", "-classified.png")));
//        }
//        catch ( IOException e1 ) {
//                e1.printStackTrace();
//        }


        //==========================================================
        //				SHAPE FINDER
        //==========================================================
        /* Use the blob finder for finding the largest blob in
         * the picture */

        BlobDetection blobs = new BlobDetection(classifiedImg, img);
        System.out.println("\tRun blob detection");
        List<ImageShape> shapeList = blobs.findLargestRelatedShapes();

        //==========================================================
        //				DRAW BOUNDARIES
        //==========================================================
        BoundaryRenderer renderer = new BoundaryRenderer(img);
        renderer.setOutlineColor( Color.RED );
        renderer.setImageShapes( shapeList );

        return deepCopy(renderer.getImage());
        
    }
    /**
     * Runs all images through the image processing pipeline filters, rendering the
     * image objects for each image (in the order they were put into the
     * pipeline). If a river could not be found, no boundaries will be drawn
     * on an image.
     */
    public void startPipeline() {
        
        // If there are originals, continue
        if (originalNames != null && originalNames.length > 0) {
            
            System.out.println("Processing images...\n");
            
            int index = 0;
            
            // Create reference to image outside of loop (for memory efficiency)
            BufferedImage img = null;
            
            // Go through each name
            for (String imgName : originalNames) {
                try {
                    // Load image
                    img = ImageIO.read( new File(imgName) );
                    System.out.printf("Loaded image '%s'\n", imgName);
                }
                catch ( IOException e2 ) {
                    System.out.printf("Failed to read image: '%s'\n", imgName);
                }
                
                //==========================================================
                //              COLOR CLASSIFIER
                //==========================================================
                /* Use the color classifier to determine which pixels are
                 * likely to be part of a river */
                
                ColorClassifier cc = new ColorClassifier(img);
                System.out.println("\tRun color classifier");
                BufferedImage classifiedImg = cc.getImage();
                try {
                	ImageIO.write( classifiedImg, "png",
                			new File(originalNames[index].replaceAll( "\\..+$", "-classified.png")));
                }
                catch ( IOException e1 ) {
                	e1.printStackTrace();
                }
                

                //==========================================================
                //				SHAPE FINDER
                //==========================================================
                /* Use the blob finder for finding the largest blob in
                 * the picture */
                
                BlobDetection blobs = new BlobDetection(classifiedImg, img);
                System.out.println("\tRun blob detection");
                List<ImageShape> shapeList = blobs.findLargestRelatedShapes();
                
                //==========================================================
                //				DRAW BOUNDARIES
                //==========================================================
                BoundaryRenderer renderer = new BoundaryRenderer(img);
                renderer.setOutlineColor( Color.RED );
                renderer.setImageShapes( shapeList );
                
                // Render the image result
                try {
                    String saveName = originalNames[index].replaceAll( "\\..+$", "-riverDetected.png");
                    System.out.printf("\tRender boundaries and save result as '%s'\n\n", saveName);
                    ImageIO.write( renderer.getImage(), "png",
                            new File(saveName));
                }
                catch (IOException e) {
                    System.out.println("Problem saving output image for " +
                                        originalNames[index]);
                    e.printStackTrace();
                }
                index++;
            }
            int size = originalNames.length;
            String suffix = size == 1 ? "" : "s";
            System.out.printf("Done. %d image%s processed.\n", size, suffix);
            return;
        }
        
        System.out.println("Please provide images (none provided).");
    }

    /**
     * Helper method to deep-copy a buffered image.
     * Retrieved from http://stackoverflow.com/questions/3514158/how-do-you-clone-a-bufferedimage
     * 
     * @param bi
     * @return deep copy of buffered image
     */
    public static BufferedImage deepCopy( BufferedImage bi ) {
        // Get the same color model
        ColorModel cm = bi.getColorModel();
        // Get alpha channel info
        boolean isAlphaPremultiplied = cm.isAlphaPremultiplied();
        // Get raster
        WritableRaster raster = bi.copyData( null );
        return new BufferedImage( cm, raster, isAlphaPremultiplied, null );
    }

}
