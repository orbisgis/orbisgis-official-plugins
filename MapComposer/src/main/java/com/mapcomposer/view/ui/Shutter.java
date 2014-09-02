package com.mapcomposer.view.ui;

import com.mapcomposer.Configuration;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Root class for the laterals shutters.
 */
public abstract class Shutter extends JPanel{    
    /** JPanel containing the body ofthe shutter */
    private final JPanel body;
    
    /**
     * Main constructor.
     */
    public Shutter(){
        body = new JPanel(new BorderLayout());
        
        //Shutter creation.
        //this.setPreferredSize(new Dimension(width, 0));
        this.setMinimumSize(new Dimension(Configuration.defaultShutterSize, 0));
        this.setMaximumSize(new Dimension(Configuration.defaultShutterSize, 0));
        
        //Positionning the shutter
        this.setLayout(new BorderLayout());
        this.add(body, BorderLayout.CENTER);
    }
    
    /**
     * Sets the JPanel of the body of the Shutter.
     * @param panel Body JPanel.
     */
    public void setBodyPanel(JPanel panel){
        body.removeAll();
        body.revalidate();
        body.repaint();
        JScrollPane pane = new JScrollPane(panel);
        body.add(pane);
    }
    
    /**
     * Resets the JPanel of the body of the Shutter.
     */
    public void resetBodyPanel(){
        body.removeAll();
    }
    
    /**
     * Open the shutter
     */
    public void open(){
        this.setMinimumSize(new Dimension(Configuration.defaultShutterSize, 0));
        this.setMaximumSize(new Dimension(Configuration.defaultShutterSize, 0));
    }
    
    /**
     * Open the shutter
     */
    public void close(){
        this.setMinimumSize(new Dimension(0, 0));
        this.setMaximumSize(new Dimension(0, 0));
    }
}
