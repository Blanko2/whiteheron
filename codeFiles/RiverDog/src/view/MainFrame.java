/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.io.File;
import javax.swing.JFrame;

/**
 *
 * @author rodrigo
 */
public class MainFrame extends Frame {
    
    String[] locations;
    
    LeftPanel leftPanel;
    RightPanel rightPanel;
    
    /**
     * 
     * @param locations 
     */
    public MainFrame(String[] locations){
        this.locations = locations;
        
        File dir = new File(locations[0]);
        File[] fileList = dir.listFiles();
        
        
        
        JFrame mainFrame = new JFrame("River Detection");
        
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        
        leftPanel = new LeftPanel(fileList, this);
       // rightPanel = new RightPanel();
        // TODO add in Right Panel
        // add in image viewer and JXList with
        // processed images
        mainFrame.add("West", leftPanel);
        
        
        //leftPanel.setPreferredSize(new Dimension(this.getWidth(), this.getHeight()));
        this.setPreferredSize(new Dimension(800,600));
        mainFrame.setVisible(true);
        mainFrame.pack();
    }
    
}
