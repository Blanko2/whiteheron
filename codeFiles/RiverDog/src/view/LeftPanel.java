/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import org.jdesktop.swingx.JXButton;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.ImagePainter;

/**
 *
 * @author rodrigo
 */
public class LeftPanel extends JXPanel{
    ArrayList<String> files;
    File[] fileList;
    
    JXPanel imageContainer;
    /**
     * Constructs the Left Panel of the GUI
     */
    public LeftPanel(File[] fileList){
        //TODO add in the JXList and selection 
        // add in a button to run the program
        //initializes an arraylist of strings and then puts
        //files into it so that it can list the files and
        //still grab the canonical location
        this.fileList = fileList;
        files = new ArrayList<String>();
        
        for(File file : fileList){
            files.add(file.getName());
        }
               
        this.setLayout(new BorderLayout());
        addComponents();
        
    }   
        
    private void addComponents(){
        JXPanel topPane = new JXPanel();
        JXPanel bottomPane = new JXPanel();
        
        addTopComponents(topPane);
        addBotComponents(bottomPane);
        
        this.add("North", topPane);
        this.add("South", bottomPane);
    }
    
    private void addTopComponents(JComponent topPane){
        JXList imageList = new JXList(files.toArray());
        imageList.addMouseListener(addMouseListener());
        imageContainer = new JXPanel();
        topPane.add(imageList);
    }
    
    private void addBotComponents(JComponent botPane){
        JXButton run = new JXButton("Run");
        botPane.add(run);
    }
    
    
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
            imageContainer.setBackgroundPainter(new ImagePainter(image));
        }
        
    }
    
    
    
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

