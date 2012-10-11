/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package view;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import javax.imageio.ImageIO;
import javax.swing.JComponent;
import javax.swing.JScrollPane;
import org.jdesktop.swingx.JXList;
import org.jdesktop.swingx.JXPanel;
import org.jdesktop.swingx.painter.ImagePainter;

/**
 *
 * @author rodrigo
 */
public class RightPanel extends JXPanel{
    File results;
    ArrayList<String> files;
    File[] resultList;
    
    JXList resultLister;    
    JXPanel imageContainer;
    
    int prefWidth = 500;
    int prefHeight = 800;
    
    /**
     * 
     * @param results 
     */
    public RightPanel(File results){
        this.results = results;
        
        
        this.resultList = results.listFiles();
        files = new ArrayList<String>();
        
        for(File file : resultList){
            files.add(file.getName());
        }
        
        this.setMinimumSize(new Dimension(prefWidth, prefHeight));
        this.setLayout(new BorderLayout());
        addComponents();
    }
    
    public void updateResults(){
        resultList = results.listFiles();
        resultLister.setListData(resultList);
        resultLister.updateUI();
    }
    
    /**
     * 
     */
    private void addComponents(){
        JXPanel containerPane = new JXPanel();
        containerPane.setMinimumSize(new Dimension(prefWidth,prefHeight));
        containerPane.setLayout(new GridLayout(0,1));
        //JXPanel bottomPane = new JXPanel();
        
        addTopComponents(containerPane);
        //addBotComponents(bottomPane);
        this.add(containerPane);
    }
    
    /**
     * 
     * @param ContainerPane 
     */
    private void addTopComponents(JComponent ContainerPane){
        resultLister = new JXList(files.toArray());
        JScrollPane scroll = new JScrollPane(resultLister);
        
        int listHeight = prefHeight/3;
        scroll.setMinimumSize(new Dimension(prefWidth, listHeight));
        resultLister.addMouseListener(addMouseListener());
        
        imageContainer = new JXPanel();
        imageContainer.setMinimumSize(new Dimension(prefWidth, prefHeight/2));
        
        ContainerPane.add(scroll);
        ContainerPane.add(imageContainer);
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
        for (File file : resultList){
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
