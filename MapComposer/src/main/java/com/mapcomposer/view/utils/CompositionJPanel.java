 package com.mapcomposer.view.utils;

import com.mapcomposer.controller.UIController;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * This panel extends from JPanel define the action to do when the user click on it.
 */
public class CompositionJPanel extends JPanel implements MouseListener{
    
    private JPanel panel;
    private final GraphicalElement ge;
    private boolean selected;
   // private int previousZ;
    
    public CompositionJPanel(GraphicalElement ge){
        super(new BorderLayout());
        this.ge=ge;
        panel=new JPanel();
        selected = false;
        this.add(panel, BorderLayout.CENTER);
        this.addMouseListener(this);
    }
    public void setPanel(JPanel panel){
        this.panel = panel;
        this.selected=false;
        setborders();
        panel.revalidate();
        panel.repaint();
    }

    private void setborders() {
       if(selected){
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        this.panel.setBorder(BorderFactory.createCompoundBorder(raisedbevel, loweredbevel));
       }
       else{
           panel.setBorder(null);
       }
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        this.selected=!selected;
        setborders();
        if(selected){
            UIController.getInstance().selectGE(ge);
            panel.setSize((int)this.getSize().getWidth()+8, (int)this.getSize().getHeight()+8);
            Rectangle r = panel.getBounds();
            r.translate(-4, -4);
            panel.setBounds(r);
        }
        else{
            UIController.getInstance().unselectGE(ge);
            panel.setSize((int)this.getSize().getWidth()-8, (int)this.getSize().getHeight()-8);
            Rectangle r = panel.getBounds();
            r.translate(4, 4);
            panel.setBounds(r);
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {
        
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        
    }

    @Override
    public void mouseExited(MouseEvent me) {
        
    }
}
