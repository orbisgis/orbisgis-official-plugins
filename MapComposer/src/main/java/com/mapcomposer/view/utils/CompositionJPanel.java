 package com.mapcomposer.view.utils;

import com.mapcomposer.controller.UIController;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import java.awt.BorderLayout;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * This panel extends from JPanel define the action to do when the user click on it.
 */
public class CompositionJPanel extends JPanel implements MouseListener, MouseMotionListener{
    
    private JPanel panel;
    private final GraphicalElement ge;
    private boolean selected;
    
    private int startX;
    private int startY;
    
    private char moveMod;
    
    public CompositionJPanel(GraphicalElement ge){
        super(new BorderLayout());
        moveMod = 0;
        this.ge=ge;
        panel=new JPanel();
        selected = false;
        this.add(panel, BorderLayout.CENTER);
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        startX=0;
        startY=0;
    }
    public void setPanel(JPanel panel){
        this.panel = panel;
        this.selected=false;
        setBorders();
    }

    private void setBorders() {
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
        setBorders();
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
        startX = me.getLocationOnScreen().x;
        startY = me.getLocationOnScreen().y;
        System.out.println("x:"+startX+", y:"+startY);
        if(me.getX()>0 && me.getX()<5){
            moveMod=1;
            this.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
        }
        else if(me.getX()>ge.getWidth()-5 && me.getX()<ge.getWidth()){
            moveMod=2;
            this.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
        }
        else if(startY>0 && startY<5){
            moveMod=3;
            this.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
        }
        else if(startY>ge.getHeight()-5 && startY<ge.getHeight()){
            moveMod=4;
            this.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
        }
        else{
            this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            moveMod=5;
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        switch(moveMod){
            case 1:
                ge.setWidth(startX-me.getLocationOnScreen().x+ge.getWidth());
                ge.setX(ge.getX()-(startX-me.getLocationOnScreen().x));
                panel.setBounds(ge.getX(), ge.getY(),  ge.getWidth(), ge.getHeight());
                break;
            case 2:
                ge.setWidth(-(startX-me.getLocationOnScreen().x)+ge.getWidth());
                panel.setBounds(ge.getX(), ge.getY(), ge.getWidth(), ge.getHeight());
                break;
            case 3:
                ge.setHeight(startY-me.getLocationOnScreen().y);
                ge.setY(ge.getY()-(startY-me.getLocationOnScreen().y));
                panel.setBounds(ge.getX(), ge.getY(), ge.getWidth(), ge.getHeight());
                break;
            case 4:
                ge.setHeight(-(startY-me.getLocationOnScreen().y));
                panel.setBounds(ge.getX(), ge.getY(), ge.getWidth(), ge.getHeight());
                break;
            case 5:
                ge.setX(ge.getX()-startX+me.getLocationOnScreen().x);
                ge.setY(ge.getY()-startY+me.getLocationOnScreen().y);
                this.panel.setLocation(ge.getX(), ge.getY());
                break;
        }
        if(selected){
            UIController.getInstance().unselectGE(ge);
            UIController.getInstance().selectGE(ge);
        }
        setBorders();
        moveMod=0;
        
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
    }

    @Override
    public void mouseEntered(MouseEvent me) {}

    @Override
    public void mouseExited(MouseEvent me) {}

    @Override
    public void mouseDragged(MouseEvent me) {
        panel.setLocation(ge.getX()-(startX-me.getLocationOnScreen().x), ge.getY()-(startY-me.getLocationOnScreen().y));
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        int x = me.getX();
        int y = me.getY();
        if(x>0 && x<10){
            this.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
        }
        else if(x>ge.getWidth()-10 && x<ge.getWidth()+10){
            this.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
        }
        else if(y>0 && y<10){
            this.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
        }
        else if(y>ge.getHeight()-10 && y<ge.getHeight()+10){
            this.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
        }
        else{
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }
}
