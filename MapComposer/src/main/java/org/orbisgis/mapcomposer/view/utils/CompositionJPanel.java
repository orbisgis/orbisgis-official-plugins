 package org.orbisgis.mapcomposer.view.utils;

import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.view.ui.CompositionArea;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Thread.sleep;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * This panel extends from JPanel define the action to do when the user click on it.
 */
public class CompositionJPanel extends JPanel implements MouseListener, MouseMotionListener{
    
    /**Panel contained by the CompositionJPanel.*/
    private JPanel panel;
    /**GraphicalElement displayed*/
    private final GraphicalElement ge;
    /**Select state of the panel*/
    private boolean selected;
    /**X initial position when user want to move the panel.*/
    private int startX;
    /**Y initial position when user want to move the panel.*/
    private int startY;
    /**Type of move the user want to do.*/
    private enum MoveMod{TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, CENTER, NONE;}
    
    private MoveMod moveMod;
            
    private final UIController uic;
    
    private final CompositionArea compArea;
    
    /**
     * Main constructor.
     * @param ge GraphicalElement to display.
     */
    public CompositionJPanel(GraphicalElement ge, UIController uic, CompositionArea compArea){
        super(new BorderLayout());
        this.uic=uic;
        this.compArea=compArea;
        moveMod = MoveMod.NONE;
        this.ge=ge;
        panel=new JPanel();
        selected = false;
        this.add(panel, BorderLayout.CENTER);
        startX=0;
        startY=0;
        //Disable listeners if it's a Document panel.
        if(ge instanceof Document)
            this.setEnabled(false);
        else{
            this.addMouseListener(this);
            this.addMouseMotionListener(this);
        }
    }
    
    /**
     * Sets the panel contained by the object.
     * @param panel New panel.
     */
    public void setPanel(JPanel panel){
        this.panel = panel;
        setBorders();
        compArea.refresh();
    }

    /**
     * Draw border if the CompositionJPanel is selected.
     */
    private void setBorders() {
        if(selected){
           panel.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        }
        else{
           panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }
        compArea.refresh();
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        if(selected)
            uic.unselectGE(ge);
        else
            uic.selectGE(ge);
    }

    @Override
    public void mousePressed(MouseEvent me) {
        double rad = Math.toRadians(ge.getRotation());
        double newWidth = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
        double newHeight = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());
        
        startX = me.getLocationOnScreen().x;
        startY = me.getLocationOnScreen().y;
        
        if((me.getY()>=0 && me.getY()<=10) && (me.getX()>=0 && me.getX()<=10)){
            moveMod=MoveMod.TOP_LEFT;
            this.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
        }
        else if((me.getX()>=0 && me.getX()<=10) && (me.getY()>=newHeight-10 && me.getY()<=newHeight)){
            moveMod=MoveMod.BOTTOM_LEFT;
            this.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
        }
        else if((me.getY()>=newHeight-10 && me.getY()<=newHeight) &&
                (me.getX()>=newWidth-10 && me.getX()<=newWidth)){
            moveMod=MoveMod.BOTTOM_RIGHT;
            this.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
        }
        else if((me.getX()>=newWidth-10 && me.getX()<=newWidth) && (me.getY()>=0 && me.getY()<=10)){
            moveMod=MoveMod.TOP_RIGHT;
            this.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
        }
        else if(me.getY()>=0 && me.getY()<=10){
            moveMod=MoveMod.TOP;
            this.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
        }
        else if(me.getX()>=0 && me.getX()<=10){
            moveMod=MoveMod.LEFT;
            this.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
        }
        else if(me.getY()>=newHeight-10 && me.getY()<=newHeight){
            moveMod=MoveMod.BOTTOM;
            this.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
        }
        else if(me.getX()>=newWidth-10 && me.getX()<=newWidth){
            moveMod=MoveMod.RIGHT;
            this.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
        }
        else{
            this.setCursor(new Cursor(Cursor.MOVE_CURSOR));
            moveMod=MoveMod.CENTER;
        }
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        switch(moveMod){
            case TOP:
                ge.setHeight(Math.abs(startY-me.getLocationOnScreen().y+ge.getHeight()));
                ge.setY(ge.getY()-(startY-me.getLocationOnScreen().y));
                panel.setBounds(ge.getX(), ge.getY(), ge.getWidth(), ge.getHeight());
                break;
            case TOP_LEFT:
                ge.setHeight(Math.abs(startY-me.getLocationOnScreen().y+ge.getHeight()));
                ge.setY(ge.getY()-(startY-me.getLocationOnScreen().y));
                ge.setWidth(Math.abs(startX-me.getLocationOnScreen().x+ge.getWidth()));
                ge.setX(ge.getX()-(startX-me.getLocationOnScreen().x));
                panel.setBounds(ge.getX(), ge.getY(), ge.getWidth(), ge.getHeight());
                break;
            case LEFT:
                ge.setWidth(Math.abs(startX-me.getLocationOnScreen().x+ge.getWidth()));
                ge.setX(ge.getX()-(startX-me.getLocationOnScreen().x));
                panel.setBounds(ge.getX(), ge.getY(),  ge.getWidth(), ge.getHeight());
                break;
            case BOTTOM_LEFT:
                ge.setWidth(Math.abs(startX-me.getLocationOnScreen().x+ge.getWidth()));
                ge.setX(ge.getX()-(startX-me.getLocationOnScreen().x));
                ge.setHeight(Math.abs(-(startY-me.getLocationOnScreen().y)+ge.getHeight()));
                panel.setBounds(ge.getX(), ge.getY(), ge.getWidth(), ge.getHeight());
                break;
            case BOTTOM:
                ge.setHeight(Math.abs(-(startY-me.getLocationOnScreen().y)+ge.getHeight()));
                panel.setBounds(ge.getX(), ge.getY(), ge.getWidth(), ge.getHeight());
                break;
            case BOTTOM_RIGHT:
                ge.setHeight(Math.abs(-(startY-me.getLocationOnScreen().y)+ge.getHeight()));
                ge.setWidth(Math.abs(-(startX-me.getLocationOnScreen().x)+ge.getWidth()));
                panel.setBounds(ge.getX(), ge.getY(),  ge.getWidth(), ge.getHeight());
                break;
            case RIGHT:
                ge.setWidth(Math.abs(-(startX-me.getLocationOnScreen().x)+ge.getWidth()));
                panel.setBounds(ge.getX(), ge.getY(), ge.getWidth(), ge.getHeight());
                break;
            case TOP_RIGHT :
                ge.setWidth(Math.abs(-(startX-me.getLocationOnScreen().x)+ge.getWidth()));
                ge.setHeight(Math.abs(startY-me.getLocationOnScreen().y+ge.getHeight()));
                ge.setY(ge.getY()-(startY-me.getLocationOnScreen().y));
                panel.setBounds(ge.getX(), ge.getY(), ge.getWidth(), ge.getHeight());
                break;
            case CENTER:
                ge.setX(ge.getX()-startX+me.getLocationOnScreen().x);
                ge.setY(ge.getY()-startY+me.getLocationOnScreen().y);
                this.panel.setLocation(ge.getX(), ge.getY());
                break;
        }
        uic.validateGE(ge);
        if(selected){
            uic.unselectGE(ge);
            uic.selectGE(ge);
        }
        else{
            uic.selectGE(ge);
            uic.unselectGE(ge);
        }
        setBorders();
        moveMod=MoveMod.NONE;
        
        this.setCursor(new Cursor(Cursor.DEFAULT_CURSOR));
        this.revalidate();
        this.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent me) {}

    @Override
    public void mouseExited(MouseEvent me) {}

    @Override
    public void mouseDragged(MouseEvent me) {
        if(moveMod==MoveMod.CENTER){
            panel.setLocation(ge.getX()-(startX-me.getLocationOnScreen().x), ge.getY()-(startY-me.getLocationOnScreen().y));
        }
    }

    @Override
    public void mouseMoved(MouseEvent me) {
        int x = me.getX();
        int y = me.getY();
        //Need to recalculate the GE width and height beacause of the rotation
        double rad = Math.toRadians(ge.getRotation());
        double newWidth = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
        double newHeight = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());
        
        if((y>=0 && y<=10) && (x>=0 && x<=10)){
            this.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
        }
        else if((x>=0 && x<=10) && (y>=newHeight-5 && y<=newHeight)){
            this.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
        }
        else if((y>=newHeight-10 && y<=newHeight) &&
                (x>=newWidth-10 && x<=newWidth)){
            this.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
        }
        else if((x>=newWidth-10 && x<=newWidth) && (y>=0 && y<=10)){
            this.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
        }
        else if(x>=0 && y<=10){
            this.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
        }
        else if(x>=0 && x<=10){
            this.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
        }
        else if(y>=newHeight-5 && y<=newHeight){
            this.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
        }
        else if(x>=newWidth-5 && x<=newWidth){
            this.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
        }
        else{
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
        }
    }
    
    /**
     * Draw red border to the element for one second.
     */
    public void hightlight(){
        try {
            Rectangle r = new Rectangle(this.getLocation(), this.getSize());
            panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
            this.paintImmediately(this.getVisibleRect());
            sleep(1000);
            setBorders();
        } catch (InterruptedException ex) {
            Logger.getLogger(CompositionJPanel.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /** Unselect the CompositionJPanel (remove the orange borders). */
    public void unselect(){ this.selected=false; setBorders(); }
    
    /** Select the CompositionJPanel. */
    public void select(){ this.selected=true; setBorders(); }
    
    /**
     * Enable or disable the grey and orange borders of the displayed elements.
     * @param enable Display the borders if true, hide them otherwise.
     */
    public void enableBorders(boolean enable){
        if(enable)
            this.setBorders();
        else{
            panel.setBorder(null);
            compArea.refresh();
        }
    }
}
