/*
 * Part of the Java Image Processing Cookbook, please see
 * http://www.lac.inpe.br/~rafael.santos/JIPCookbook/index.jsp for information
 * on usage and distribution. Rafael Santos (rafael.santos@lac.inpe.br)
 */

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.StringTokenizer;
import java.util.TreeMap;

import javax.imageio.ImageIO;

/**
 * This application classifies images using the minimum distance classifier.
 * Please see http://www.lac.inpe.br/~rafael.santos/JIPCookbook for more
 * information on the files and formats used in this class.
 */
public class ClassifyWithMinimumDistanceAlgorithm {
    /**
     * The application entry point. We must pass at least three parameters: the
     * original image file name, the name of the file with the description of
     * the classes, and the name of the file with the signatures for the
     * classes. If an additional numeric parameter is passed, it will be used as
     * a threshold for classification (see tutorial for more information).
     * 
     * @throws IOException
     */
    public static void main( String[] args ) throws IOException {
        // Check parameters names.
        if ( args.length < 3 ) {
            System.err.println( "Must pass at least three command-line parameters to this application:" );
            System.err.println( " - The original image (from which samples will be extracted;" );
            System.err.println( " - The file with the classes names and colors" );
            System.err.println( " - The file with the signatures for each class" );
            System.err.println( " - (optionally) a threshold for minimum distance for classification" );
            System.exit( 1 );
        }
        // Open the original image.
        BufferedImage input = ImageIO.read( new File( args[ 0 ] ) );
        // Read the classes description file.
        BufferedReader br = new BufferedReader( new FileReader( args[ 1 ] ) );
        // Store the classes color in a map.
        TreeMap< Integer, Color > classMap = new TreeMap< Integer, Color >();
        while ( true ) {
            String line = br.readLine();
            if ( line == null ) break;
            if ( line.startsWith( "#" ) ) continue;
            StringTokenizer st = new StringTokenizer( line );
            if ( st.countTokens() < 4 ) continue;
            int classId = Integer.parseInt( st.nextToken() );
            int r = Integer.parseInt( st.nextToken() );
            int g = Integer.parseInt( st.nextToken() );
            int b = Integer.parseInt( st.nextToken() );
            classMap.put( classId, new Color( r, g, b ) );
        }
        br.close();
        // Read the signatures from a file.
        TreeMap< Integer, double[] > avgMap = new TreeMap< Integer, double[] >();
        br = new BufferedReader( new FileReader( args[ 2 ] ) );
        while ( true ) {
            String line = br.readLine();
            if ( line == null ) break;
            if ( line.startsWith( "#" ) ) continue;
            StringTokenizer st = new StringTokenizer( line );
            if ( st.countTokens() < 4 ) continue;
            int classId = Integer.parseInt( st.nextToken() );
            double[] avg = new double[ 3 ];
            avg[ 0 ] = Double.parseDouble( st.nextToken() );
            avg[ 1 ] = Double.parseDouble( st.nextToken() );
            avg[ 2 ] = Double.parseDouble( st.nextToken() );
            avgMap.put( classId, avg );
        }
        br.close();
        // Use a threshold (if passed in the command-line).
        boolean useThreshold = ( args.length > 3 );
        double threshold = Double.MAX_VALUE;
        if ( useThreshold ) threshold = Double.parseDouble( args[ 3 ] );
        // Create a color image to hold the results of the classification.
        int w = input.getWidth();
        int h = input.getHeight();
        BufferedImage results = new BufferedImage( w, h, BufferedImage.TYPE_INT_RGB );
        // Do the classification, pixel by pixel, selecting which class they
        // should be assigned to.
        for ( int row = 0; row < h; row++ )
            for ( int col = 0; col < w; col++ ) {
                int rgb = input.getRGB( col, row );
                int r = ( ( rgb & 0x00FF0000 ) >>> 16 ); // Red level
                int g = ( ( rgb & 0x0000FF00 ) >>> 8 ); // Green level
                int b = ( rgb & 0x000000FF ); // Blue level
                // To which class should we assign this pixel?
                double minDist = Double.MAX_VALUE;
                Color assignedClass = Color.BLACK;
                for ( int key : avgMap.keySet() ) {
                    double dist = calcDistance( r, g, b, avgMap.get( key ) );
                    // Do we use a minimum distance threshold?
                    if ( useThreshold ) {
                        if ( ( dist < minDist ) && ( dist < threshold ) ) {
                            minDist = dist;
                            assignedClass = classMap.get( key );
                        }
                    }
                    else // any distance will do
                    {
                        if ( dist < minDist ) {
                            minDist = dist;
                            assignedClass = classMap.get( key );
                        }
                    }
                }
                // With the color, paint the output image.
                results.setRGB( col, row, assignedClass.getRGB() );
            }
        // At the end, store the resulting image.
        ImageIO.write( results, "PNG", new File( "classified-with-mindist.png" ) );
    }

    // This method does not return the Euclidean distance, but since we are
    // using distances only for comparison, it is OK.
    private static double calcDistance( int r, int g, int b, double[] avg ) {
        return ( r - avg[ 0 ] ) * ( r - avg[ 0 ] ) + ( g - avg[ 1 ] ) * ( g - avg[ 1 ] ) + ( b - avg[ 2 ] )
                * ( b - avg[ 2 ] );
    }
}