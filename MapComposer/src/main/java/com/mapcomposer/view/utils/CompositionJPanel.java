/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mapcomposer.view.utils;

import com.mapcomposer.controller.utils.GEMouseListener;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.view.ui.ConfigurationShutter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * This panel extends from JPanel define the action to do when the user click on it.
 */
public class CompositionJPanel implements GEMouseListener, MouseListener{
    
    private JPanel panel;
    private GraphicalElement ge;
    private boolean selected;
    
    public CompositionJPanel(GraphicalElement ge){
        this.ge=ge;
        panel=new JPanel();
        selected = false;
        this.panel.addMouseListener(this);
    }
    
    public void setPanel(JPanel panel){
        this.panel = panel;
        this.selected=false;
        setborders();
        panel.revalidate();
        panel.repaint();
    }

    public JPanel getPanel() {
        return this.panel;
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
        System.out.println("click");
        this.selected=!selected;
        setborders();
        if(selected){
            ConfigurationShutter.getInstance().setSelected(ge);
        }
        else{
            ConfigurationShutter.getInstance().resetSelected();
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
