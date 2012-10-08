/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.FlowLayout;
import java.awt.Frame;
import javax.swing.JFrame;

/**
 *
 * @author rodrigo
 */
public class MainFrame extends Frame {
    
    LeftPanel leftPanel;
    
    public MainFrame(){
        JFrame mainFrame = new JFrame("River Detection");
        
        mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        mainFrame.setLayout(new FlowLayout());
        
        leftPanel = new LeftPanel();
        // TODO add in Right Panel
        // add in image viewer and JXList with
        // processed images
        mainFrame.add(leftPanel);
    }
    
}
