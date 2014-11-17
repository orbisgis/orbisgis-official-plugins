 package org.orbisgis.mapcomposer.view.utils;

import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.slf4j.LoggerFactory;
import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.beans.EventHandler;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import javax.swing.BorderFactory;
import javax.swing.JPanel;

/**
 * This panel extends from JPanel define the action to do when the user click on it.
 */
public class CompositionJPanel extends JPanel{
    
    /**Panel contained by the CompositionJPanel. */
    private JPanel panel = new JPanel();
    
    /**GraphicalElement displayed. */
    private final GraphicalElement ge;
    
    /**Select state of the panel. */
    private boolean selected=false;
    
    /**X initial position when user want to move the panel. */
    private int startX=0;
    
    /**Y initial position when user want to move the panel. */
    private int startY=0;
    
    /**Type of move the user want to do. */
    private enum MoveMod{TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, CENTER, NONE;}
    
    /**Id of the move that the user is doing. */
    private MoveMod moveMod=MoveMod.NONE;
    
    /** Reference to the UIController. */
    private final UIController uic;
    
    /** Size of the margin of the border for resizing. */
    private static final int margin = 5;
    
    /**
     * Main constructor.
     * @param ge GraphicalElement to display.
     * @param uic UIController of the application.
     */
    public CompositionJPanel(GraphicalElement ge, UIController uic){
        super(new BorderLayout());
        this.uic=uic;
        this.ge=ge;
        this.add(panel, BorderLayout.CENTER);
        //Disable mouse listeners if it's a Document panel.
        if(ge instanceof Document)
            this.setEnabled(false);
        else{
            this.addMouseListener(EventHandler.create(MouseListener.class, this, "mouseClicked", "", "mouseClicked"));
            this.addMouseListener(EventHandler.create(MouseListener.class, this, "mousePressed", "", "mousePressed"));
            this.addMouseListener(EventHandler.create(MouseListener.class, this, "mouseReleased", "getLocationOnScreen", "mouseReleased"));
            this.addMouseMotionListener(EventHandler.create(MouseMotionListener.class, this, "mouseDragged", "getLocationOnScreen", "mouseDragged"));
            this.addMouseMotionListener(EventHandler.create(MouseMotionListener.class, this, "mouseMoved", "getPoint", "mouseMoved"));
        }
    }
    
    /**
     * Sets the panel contained by the object.
     * @param panel The new panel.
     */
    public void setPanel(JPanel panel){
        this.panel = panel;
        this.panel.revalidate();
        setBorders();
    }

    /**
     * Draw border if the CompositionJPanel is selected.
     */
    private void setBorders() {
        if(selected)
           panel.setBorder(BorderFactory.createLineBorder(Color.ORANGE));
        else
           panel.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }

    /**
     * Select or unselect the panel on click.
     * @param me Mouse Event.
     */
    public void mouseClicked(MouseEvent me) {
        if(selected)
            uic.unselectGE(ge);
        else
            uic.selectGE(ge);
    }

    /**
     * Set the moveMod according to the panel border clicked.
     * @param me Mouse Event.
     */
    public void mousePressed(MouseEvent me) {
        double rad = Math.toRadians(ge.getRotation());
        double newWidth = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
        double newHeight = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());
        
        startX = me.getLocationOnScreen().x;
        startY = me.getLocationOnScreen().y;
        
        if(me.getY()<=margin && me.getX()<=margin)
            moveMod=MoveMod.TOP_LEFT;
        else if(me.getX()<=margin && me.getY()>=newHeight-margin)
            moveMod=MoveMod.BOTTOM_LEFT;
        else if(me.getY()>=newHeight-margin && me.getX()>=newWidth-margin)
            moveMod=MoveMod.BOTTOM_RIGHT;
        else if(me.getX()>=newWidth-margin && me.getY()<=margin)
            moveMod=MoveMod.TOP_RIGHT;
        else if(me.getY()<=margin)
            moveMod=MoveMod.TOP;
        else if(me.getX()<=margin)
            moveMod=MoveMod.LEFT;
        else if(me.getY()>=newHeight-margin)
            moveMod=MoveMod.BOTTOM;
        else if(me.getX()>=newWidth-margin)
            moveMod=MoveMod.RIGHT;
        else
            moveMod=MoveMod.CENTER;
    }

    /**
     * Sets the new dimension and position of the ge when the mouse is released.
     * @param p Location on screen of the mouse when it's released.
     */
    public void mouseReleased(Point p) {
        switch(moveMod){
            case TOP:
                ge.setHeight(Math.abs(startY-p.y+ge.getHeight()));
                ge.setY(ge.getY()-(startY-p.y));
                break;
            case TOP_LEFT:
                ge.setHeight(Math.abs(startY-p.y+ge.getHeight()));
                ge.setY(ge.getY()-(startY-p.y));
                ge.setWidth(Math.abs(startX-p.x+ge.getWidth()));
                ge.setX(ge.getX()-(startX-p.x));
                break;
            case LEFT:
                ge.setWidth(Math.abs(startX-p.x+ge.getWidth()));
                ge.setX(ge.getX()-(startX-p.x));
                break;
            case BOTTOM_LEFT:
                ge.setWidth(Math.abs(startX-p.x+ge.getWidth()));
                ge.setX(ge.getX()-(startX-p.x));
                ge.setHeight(Math.abs(-(startY-p.y)+ge.getHeight()));
                break;
            case BOTTOM:
                ge.setHeight(Math.abs(-(startY-p.y)+ge.getHeight()));
                break;
            case BOTTOM_RIGHT:
                ge.setHeight(Math.abs(-(startY-p.y)+ge.getHeight()));
                ge.setWidth(Math.abs(-(startX-p.x)+ge.getWidth()));
                break;
            case RIGHT:
                ge.setWidth(Math.abs(-(startX-p.x)+ge.getWidth()));
                break;
            case TOP_RIGHT :
                ge.setWidth(Math.abs(-(startX-p.x)+ge.getWidth()));
                ge.setHeight(Math.abs(startY-p.y+ge.getHeight()));
                ge.setY(ge.getY()-(startY-p.y));
                break;
            case CENTER:
                ge.setX(ge.getX()-startX+p.x);
                ge.setY(ge.getY()-startY+p.y);
                this.panel.setLocation(ge.getX(), ge.getY());
                break;
        }
        panel.setBounds(ge.getX(), ge.getY(),  ge.getWidth(), ge.getHeight());
        uic.validateGE(ge);
        
        this.moveMod=MoveMod.NONE;
    }

    /**
     * Refresh the panel position when it's dragged.
     * @param p Location of the mouse inside the panel.
     */
    public void mouseDragged(Point p) {
        if(moveMod==MoveMod.CENTER)
            panel.setLocation(ge.getX()-(startX-p.x), ge.getY()-(startY-p.y));
    }

    /**
     * Changes the mouse cursor appearence according to the border hovered.
     * @param p Point of the mouse.
     */
    public void mouseMoved(Point p) {
        int x = p.x;
        int y = p.y;
        //Need to recalculate the GE width and height beacause of the rotation
        double rad = Math.toRadians(ge.getRotation());
        double newWidth = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
        double newHeight = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());
        
        if(y<=margin && x<=margin)
            this.setCursor(new Cursor(Cursor.NW_RESIZE_CURSOR));
        else if(x<=margin && y>=newHeight-margin)
            this.setCursor(new Cursor(Cursor.SW_RESIZE_CURSOR));
        else if(y>=newHeight-margin && x>=newWidth-margin)
            this.setCursor(new Cursor(Cursor.SE_RESIZE_CURSOR));
        else if(x>=newWidth-margin &&  y<=margin)
            this.setCursor(new Cursor(Cursor.NE_RESIZE_CURSOR));
        else if(y<=margin)
            this.setCursor(new Cursor(Cursor.N_RESIZE_CURSOR));
        else if(x<=margin)
            this.setCursor(new Cursor(Cursor.W_RESIZE_CURSOR));
        else if(y>=newHeight-margin)
            this.setCursor(new Cursor(Cursor.S_RESIZE_CURSOR));
        else if(x>=newWidth-margin)
            this.setCursor(new Cursor(Cursor.E_RESIZE_CURSOR));
        else
            this.setCursor(new Cursor(Cursor.HAND_CURSOR));
    }
    
    /**
     * Draw red border to the element for one second.
     */
    public void hightlight(){
        try {
            panel.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
            this.paintImmediately(this.getVisibleRect());
            Thread.sleep(1000);
            setBorders();
        } catch (InterruptedException ex) {
            LoggerFactory.getLogger(CompositionJPanel.class).error(ex.getMessage());
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
        else
            panel.setBorder(null);
    }
}
