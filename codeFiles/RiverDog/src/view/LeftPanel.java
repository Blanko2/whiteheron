/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import org.jdesktop.swingx.JXPanel;

/**
 *
 * @author rodrigo
 */
public class LeftPanel extends JXPanel{
    
    /**
     * Constructs the Left Panel of the GUI
     */
    public LeftPanel(){
        //TODO add in the JXList and selection 
        // add in a button to run the program
        this.setLayout(new BorderLayout());
        addComponents();
    }   
        
    private void addComponents(){
        JXPanel topPane = new JXPanel();
        JXPanel bottomPane = new JXPanel();
        
        //topPane.add();
    }
}

