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

import modules.ColorPaintover;
import modules.ColorQuantization;
import riverObjects.River;

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
	
	Color[] RiverCols 	= {quantBlack, quantBlue, quantAqua};
	Color[] notRiver	= {quantRed, quantGreen, quantYello, quantPurp, quantGrey};
	
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
     * Runs all original images through the pipeline,
     * retrieving their river boundaries and drawing
     * them onto a copy of the originals.
     */
    public void renderRiverBoundaries() {
        // Run pipeline
        List<River> rivers = startPipeline();
        
        // Go through each result
        for (int i = 0; i < rivers.size(); i++) {
            
            // If a river is not null
            River r = rivers.get( i );
            if (r != null) {
                // TODO: Render boundaries on copy of original and save it using original name with suffix
            }
        }
    }
    
    /**
     * Runs all images through the image processing pipeline filters, returning the
     * River objects for each image (in the order they were put into the
     * pipeline). If a river could not be found, the river object will be null
     * for that image.
     * 
     * @return river objects for each image
     */
    private List<River> startPipeline() {
        // Create list to store results
        List<River> rivers = new ArrayList<River>();
        
        // If there are originals, continue
        if (originals != null && originals.size() > 0) {
            BufferedImage currImg;
            
            // Go through each original
            for (BufferedImage img : originals) {
                
                //==========================================================
                //                  COLOR QUANTIZATION
                //==========================================================
                // Perform color quantization (creates copy of original)
                ColorQuantization colorQ = new ColorQuantization(img);
                BufferedImage quantImg = colorQ.getImage();
                
                //==========================================================
                //                  COLOR PAINT-OVER
                //==========================================================
                // Create list of colors that are not rivers
                List<Color> notRiverColors = new ArrayList<Color>();
                for(int i=0; i<notRiver.length; i++){
                	notRiverColors.add( notRiver[i] ); // XXX needs testing
                }
                ColorPaintover paintOver = new ColorPaintover(quantImg);
                paintOver.setNewColor( Color.WHITE );
                paintOver.setOldColor( notRiverColors );
                BufferedImage paintedImg = paintOver.getImage();
                
                
                //==========================================================
                //				SHAPE FINDER
                //==========================================================
                
                
                // TODO: Continue algorithm
                try {
					ImageIO.write(paintedImg, "png", new File("painted.png"));
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
            }
        }
        
        
        return rivers;
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
