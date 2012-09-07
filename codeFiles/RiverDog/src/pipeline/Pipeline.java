package pipeline;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.image.ColorModel;
import java.awt.image.WritableRaster;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.imageio.ImageIO;

import modules.BlobDetection;
import modules.BoundaryRenderer;
import modules.ColorClassifier;
import modules.ColorPaintover;
import modules.ColorQuantization;
import modules.EdgeMajority;
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
    private ArrayList< BufferedImage > originals;

    public Pipeline( String[] imageNames ) {
        originalNames = imageNames;
        originals = new ArrayList<BufferedImage>();
        // Proceed if there are image names
        if ( imageNames != null && imageNames.length > 0 ) {
            try {
                // Read all original images into a list
                for ( String name : imageNames ) {
                	originals.add( ImageIO.read( new File( name ) ) );
                }
            }
            catch ( IOException e ) {
                System.out.println( "Problem reading images from disk." );
                e.printStackTrace();
            }
        }
    }
    
    /**
     * Runs all images through the image processing pipeline filters, rendering the
     * image objects for each image (in the order they were put into the
     * pipeline). If a river could not be found, no boundaries will be drawn
     * on an image.
     */
    public void startPipeline() {
        
        // If there are originals, continue
        if (originals != null && originals.size() > 0) {
            
            System.out.println("Processing images...");
            
            int index = 0;
            // Go through each original
            for (BufferedImage img : originals) {
                ColorClassifier cc = new ColorClassifier(img);
                BufferedImage classifiedImg = cc.getImage();
                try {
                	ImageIO.write( classifiedImg, "png",
                			new File(originalNames[index].replaceAll( "\\..+$", "-classified.png")));
                }
                catch ( IOException e1 ) {
                	e1.printStackTrace();
                }
                
//                //==========================================================
//                //                  COLOR QUANTIZATION
//                //==========================================================
//                // Perform color quantization (creates copy of original)
//                ColorQuantization colorQ = new ColorQuantization(img);
//                BufferedImage quantImg = colorQ.getImage();
//                try {
//                    ImageIO.write( quantImg, "png",
//                            new File(originalNames[index].replaceAll( "\\..+$", "-quantized.png")));
//                }
//                catch ( IOException e1 ) {
//                    e1.printStackTrace();
//                }
//                
//                //==========================================================
//                //                  COLOR PAINT-OVER
//                //==========================================================
//                // Create list of colors that are not rivers
//                List<Color> notRiverColors = new ArrayList<Color>();
//                for(int i=0; i<notRiver.length; i++){
//                	notRiverColors.add( notRiver[i] );
//                }
//                ColorPaintover paintOver = new ColorPaintover(quantImg);
//                paintOver.setNewColor( Color.WHITE );
//                paintOver.setOldColor( notRiverColors );
//                BufferedImage paintedImg = paintOver.getImage();
//                try {
//                    ImageIO.write( paintedImg, "png",
//                            new File(originalNames[index].replaceAll( "\\..+$", "-painted.png")));
//                }
//                catch ( IOException e1 ) {
//                    e1.printStackTrace();
//                }
//                
//                //==========================================================
//                //             REMOVAL OF EDGE MAJORITY COLOR
//                //==========================================================
//                /* The most common color around the edge of the picture is
//                 * not likely to be a river, so remove it from the picture,
//                 * replacing it with white (if majority is not white 
//                 * already) */
//                 
//                EdgeMajority majority = new EdgeMajority(paintedImg);
//                paintedImg = majority.getImage();
//                try {
//                    ImageIO.write( paintedImg, "png",
//                            new File(originalNames[index].replaceAll( "\\..+$", "-edgeMajority.png")));
//                }
//                catch ( IOException e1 ) {
//                    e1.printStackTrace();
//                }
//                
                //==========================================================
                //				SHAPE FINDER
                //==========================================================
                /* Use the blob finder for finding the largest blob in
                 * the picture */
                
                BlobDetection blobs = new BlobDetection(classifiedImg);
                List<ImageShape> shapeList = blobs.findLargestShape();
                
                //==========================================================
                //				DRAW BOUNDARIES
                //==========================================================
                BoundaryRenderer renderer = new BoundaryRenderer(img);
                renderer.setOutlineColor( Color.RED );
                renderer.setImageShapes( shapeList );
                
                // Render the image result
                try {
                    ImageIO.write( renderer.getImage(), "png",
                            new File(originalNames[index].replaceAll( "\\..+$", "-riverDetected.png")));
                }
                catch (IOException e) {
                    System.out.println("Problem saving output image for " +
                                        originalNames[index]);
                    e.printStackTrace();
                }
                index++;
            }
            System.out.println("Done.");
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
