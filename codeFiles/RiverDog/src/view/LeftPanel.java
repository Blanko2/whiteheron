/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.ImagePainter;
import pipeline.Pipeline;

/**
 *
 * @author rodrigo
 */
public class LeftPanel extends JXPanel{
    MainFrame parent;
    
    ArrayList<String> files;
    File[] fileList;
    
    JXPanel imageContainer;
    
    int prefWidth = 340;
    int prefHeight = 700;
    
    /**
     * Constructs the Left Panel of the GUI
     */
    public LeftPanel(File[] fileList, MainFrame parent){
        //TODO add in the JXList and selection 
        // add in a button to run the program
        //initializes an arraylist of strings and then puts
        //files into it so that it can list the files and
        //still grab the canonical location
        this.parent = parent;
        this.fileList = fileList;
        files = new ArrayList<String>();
        
        for(File file : fileList){
            files.add(file.getName());
        }
        this.setMinimumSize(new Dimension(prefWidth, prefHeight));       
        this.setLayout(new BorderLayout());
        addComponents();       
    }   
     
    /**
     * 
     */
    private void addComponents(){
        JXPanel topPane = new JXPanel();
        JXPanel bottomPane = new JXPanel();
        
        topPane.setLayout(new GridLayout(0,1));
        bottomPane.setLayout(new GridLayout());
        
        addTopComponents(topPane);
        addBotComponents(bottomPane);
              
        
        this.add("North", topPane);
        this.add("South", bottomPane);
        
        
    }
    
    /**
     * 
     * @param topPane 
     */
    private void addTopComponents(JComponent topPane){
        
        JXList imageList = new JXList(files.toArray());
        JScrollPane scroll = new JScrollPane(imageList);
        
        int listHeight = prefHeight/3;
        scroll.setPreferredSize(new Dimension(prefWidth, listHeight));
        imageList.addMouseListener(addMouseListener());
        
        imageContainer = new JXPanel();
        imageContainer.setMinimumSize(new Dimension(prefWidth, prefHeight-listHeight-10));

        topPane.add(scroll);
        topPane.add(imageContainer);
    }
    
    /**
     * 
     * @param botPane 
     */
    private void addBotComponents(JComponent botPane){
        JXButton run = new JXButton("Run");
        
        run.addActionListener(new ActionListener(){
            @Override
            public void actionPerformed(ActionEvent ae) {
                runProgram();
            }
            
        });
        
        botPane.add(run);
    }
    
    /**
     * 
     */
    private void runProgram(){
        /*
         * run pipeline - then update the right panel.
         * pipeline needs to be run with the file list 
         * needs to run through all images/files in the list
         * update can just be called however
         */
        
        
        Pipeline pipe = new Pipeline();
        for(File file: fileList){
            BufferedImage image = pipe.startPipeline(file);
            saveImage(image, parent.getResults(), file.getName());
        }
    }
    
    /**
     * need to later add a check if file is an image or not!
     * just put that somewhere
     * 
     * @param index 
     */
    private void updateImage(int index){
        String imageName = files.get(index);
        BufferedImage image = null;
        for (File file : fileList){
            if(imageName.equals(file.getName())){
                try{
                image = ImageIO.read(file);
                break;
                }catch(IOException e){System.out.println("images broke");}
            }
        }
        
        if(image != null){
            ImagePainter paint = new ImagePainter(image);
            paint.setScaleToFit(true);
            imageContainer.setBackgroundPainter(paint);
        }
    }
    
    /**
     *  needs to write image to file in specified location
     * @param img 
     */
    private void saveImage(BufferedImage img, String result, String fname){
        try{
            ImageIO.write(img, "png", new File(result+"/"+fname));
        }catch(IOException i){}
    }
    
    /**
     * 
     * @return 
     */
    private MouseAdapter addMouseListener(){
        return new MouseAdapter(){
            @Override
            public void mouseClicked(MouseEvent e){
                JXList list = (JXList) e.getSource();
                if(e.getClickCount() == 2){
                    int index = list.locationToIndex(e.getPoint());
                    updateImage(index);
                }
            }
        };
    }
}

