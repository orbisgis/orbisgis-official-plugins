package com.mapcomposer.view.ui;

import com.mapcomposer.view.utils.CompositionJPanel;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.border.Border;

/**
 * Area for the map document composition.
 */
public class CompositionArea extends JPanel{
    
    /**Unique instance of the class.*/
    private static CompositionArea INSTANCE=null;
    /**JscrollPane of the CompositionArea*/
    private JScrollPane pane;
    private JPanel panel;
    
    /**Private constructor.*/
    private CompositionArea(){
        super(new BorderLayout());
        panel = new JPanel();
        pane = new JScrollPane(panel);
        //this.add(pane, BorderLayout.CENTER);
    }
    
    /**
     * Returns the unique instance of the class.
     * @return The unique instance of the class.
     */
    public static CompositionArea getInstance(){
        if(INSTANCE==null){
            INSTANCE = new CompositionArea();
        }
        return INSTANCE;
    }
    
    /**
     * Adds a CompositionPanel to itself. Should be call only once for each GraphicalElement.
     * @param panel CompositionPanel to add.
     */
    public void addGE(CompositionJPanel panel){
        this.panel.add(panel);
    }
    
    public void removeGE(CompositionJPanel panel){
        this.panel.remove(panel);
    }

    public void setPanel(JPanel render, Dimension dim) {
        panel = render;
        panel.setOpaque(true);
        panel.setBackground(Color.WHITE);
        panel.setLayout(null);
        pane = new JScrollPane(render);
        panel.setPreferredSize(dim);
        panel.setLocation(0, 0);
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        panel.setBorder(BorderFactory.createCompoundBorder(raisedbevel, loweredbevel));
        JPanel body = new JPanel();
        body.add(panel);
        JScrollPane pane = new JScrollPane(body, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.add(pane, BorderLayout.CENTER);
    }

    public void setZIndex(CompositionJPanel comp, int i) {
        panel.setComponentZOrder(comp, i);
    }
}
