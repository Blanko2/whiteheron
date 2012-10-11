/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import javax.swing.JFrame;
import org.jdesktop.swingx.JXButton;
import pipeline.Pipeline;

/**
 *
 * @author rodrigo
 */
public class MainFrame extends Frame {
    
    Pipeline pipe;
    
    String[] locations;
    String origin;
    String result;
    
    LeftPanel leftPanel;
    RightPanel rightPanel;
    
    /**
     * 
     * @param locations 
     */
    public MainFrame(String[] locations){
        this.locations = locations;
        this.origin = locations[0];
        this.result = locations[1];
        
        File dir = new File(locations[0]);
        File res = new File(locations[1]);
        File[] fileList = dir.listFiles();
        
        
        
        JFrame mainFrame = new JFrame("River Detection");
        
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new BorderLayout());
        
        leftPanel = new LeftPanel(fileList, this);
        rightPanel = new RightPanel(res);
       
        mainFrame.add("West", leftPanel);
        
        mainFrame.add("Center", rightPanel);
        JXButton update = new JXButton("update");
        update.addActionListener(new ActionListener(){

            @Override
            public void actionPerformed(ActionEvent ae) {
               updatePane(rightPanel);
            }
            
        });
        mainFrame.add("East",update);
        this.setMinimumSize(new Dimension(800,600));
        mainFrame.setVisible(true);
        mainFrame.pack();
    }
    
    /**
     * 
     * @return 
     */
    public Pipeline getPipeline(){
        return pipe;
    }
    
    /**
     * 
     * @param pipe 
     */
    public void setPipeline(Pipeline pipe){
        this.pipe = pipe;
    }
    
    
    /**
     * 
     * @return 
     */
    public String getOrigin(){
        return origin;
    }
    
    /**
     * 
     * @return 
     */
    public String getResults(){
        return result;
    }
    
    /**
     * 
     * @param pane 
     */
    public void updatePane(RightPanel pane){
        pane.updateResults();
    }
    
}
