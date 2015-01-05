 package org.orbisgis.mapcomposer.view.utils;

import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.image.BufferedImage;
import java.beans.EventHandler;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

import javax.swing.*;

 /**
  * This panel extends from JPanel define the action to do when the user click on it.
  */
public class CompositionJPanel extends JPanel{

    /**GraphicalElement displayed. */
    private final GraphicalElement ge;

    /**Select state of the panel. */
    private boolean selected=false;

    /**X initial position when user want to move the panel. */
    private int startX=0;

    /**Y initial position when user want to move the panel. */
    private int startY=0;

     /**Type of move the user want to do. */
     private enum MoveDirection {TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, CENTER, NONE;}

     /**Id of the move that the user is doing. */
     private MoveDirection moveDirection = MoveDirection.NONE;

     /**Type of move the user want to do. */
     private enum MoveMode {NONE, SHIFT, ALTGRAPH, CTRL;}

     /**Id of the move that the user is doing. */
     private MoveMode moveMode = MoveMode.NONE;

    /** Reference to the UIController. */
    private final UIController uic;

    /** Size of the margin of the border for resizing. */
    private static final int margin = 5;

    private WaitLayerUI waitLayer;
    private JPanel panel;

    /**
     * Main constructor.
     * @param ge GraphicalElement to display.
     * @param uic UIController of the application.
     */
    public CompositionJPanel(GraphicalElement ge, UIController uic){
        super(new BorderLayout());
        this.setSize(ge.getWidth(), ge.getHeight());
        panel = new JPanel(new BorderLayout());
        this.add(panel);
        this.uic=uic;
        this.ge=ge;
        //Disable mouse listeners if it's a Document panel.
        if(ge instanceof Document)
            this.setEnabled(false);
        else{
            this.addMouseListener(EventHandler.create(MouseListener.class, this, "mouseClicked", "", "mouseClicked"));
            this.addMouseListener(EventHandler.create(MouseListener.class, this, "mousePressed", "", "mousePressed"));
            this.addMouseListener(EventHandler.create(MouseListener.class, this, "mouseReleasedHub", "getLocationOnScreen", "mouseReleased"));
            this.addMouseMotionListener(EventHandler.create(MouseMotionListener.class, this, "mouseDragged", "getLocationOnScreen", "mouseDragged"));
            this.addMouseMotionListener(EventHandler.create(MouseMotionListener.class, this, "mouseMoved", "getPoint", "mouseMoved"));
        }
        this.setToolTipText("<html>Holding <strong>Alt Gr</strong> : resize the representation of the element.<br/>" +
                "Holding <strong>Shift</strong> : resire the element and keeps the ratio width/height.</html>");

        waitLayer = new WaitLayerUI();
        JLayer<JPanel> layer = new JLayer<>(panel, waitLayer);
        this.add(layer);
    }

    /**
     * Sets the panel contained by the object.
     * @param bufferedImage The new panel.
     */
    public void setPanelContent(final BufferedImage bufferedImage){
        double rad = Math.toRadians(ge.getRotation());
        //Width and Height of the rectangle containing the rotated bufferedImage
        final double newWidth = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
        final double newHeight = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());
        final int maxWidth = Math.max((int)newWidth, ge.getWidth());
        final int maxHeight = Math.max((int)newHeight, ge.getHeight());

        panel.removeAll();
        //Add the BufferedImage into a JComponent in the CompositionJPanel
        panel.add(new JComponent() {
            //Redefinition of the painComponent method to rotate the component content.
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                g.drawImage(bufferedImage, -(maxWidth - (int) newWidth) / 2, -(maxHeight - (int) newHeight) / 2, null);
            }
        }, BorderLayout.CENTER);
        panel.revalidate();
        this.revalidate();
        //As the buffered image is rotated, change the origin point of the panel to make the center of the image not moving after the rotation.
        //Take account of the border width (2 pixels).
        this.setBounds(ge.getX() + (ge.getWidth() - (int) newWidth) / 2, ge.getY() + (ge.getHeight() - (int) newHeight) / 2, (int) newWidth + 2, (int) newHeight + 2);
        this.setOpaque(false);
        panel.setOpaque(false);
        setBorders();
    }

    /**
     * Draw border if the CompositionJPanel is selected.
     */
    private void setBorders() {
        if(selected)
           this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.ORANGE));
        else
           this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
    }

    public WaitLayerUI getWaitLayer() {
        return waitLayer;
    }

     /**
     * Select or unselect the panel on click.
     * @param me Mouse Event.
     */
    public void mouseClicked(MouseEvent me) {
        if(me.getClickCount()==2)
            uic.showGEProperties(ge);
        if(selected)
            uic.unselectGE(ge);
        else
            uic.selectGE(ge);

        this.moveDirection = MoveDirection.NONE;
        this.moveMode=MoveMode.NONE;
        uic.getMainWindow().getCompositionArea().getOverlay().setMode(CompositionAreaOverlay.Mode.NONE);
    }

    /**
     * Set the moveMod according to the panel border clicked.
     * @param me Mouse Event.
     */
    public void mousePressed(MouseEvent me) {
        //Sets the mouse move mode
        if(me.isShiftDown()) moveMode = MoveMode.SHIFT;
        else if(me.isAltGraphDown()) moveMode = MoveMode.ALTGRAPH;
        else if(me.isControlDown()) moveMode = MoveMode.CTRL;
        else moveMode = MoveMode.NONE;

        //Sets the mouse move direction
        double rad = Math.toRadians(ge.getRotation());
        double newWidth = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
        double newHeight = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());

        startX = me.getLocationOnScreen().x;
        startY = me.getLocationOnScreen().y;

        Point start = new Point(0, 0);

        if(me.getY()<=margin && me.getX()<=margin) {
            moveDirection = MoveDirection.TOP_LEFT;
            start = new Point(this.getX()+this.getWidth()-1, this.getY()+this.getHeight()-1);
        }
        else if(me.getX()<=margin && me.getY()>=newHeight-margin) {
            moveDirection = MoveDirection.BOTTOM_LEFT;
            start = new Point(this.getX()+this.getWidth()-1, this.getY());
        }
        else if(me.getY()>=newHeight-margin && me.getX()>=newWidth-margin) {
            moveDirection = MoveDirection.BOTTOM_RIGHT;
            start = new Point(this.getX(), this.getY());
        }
        else if(me.getX()>=newWidth-margin && me.getY()<=margin) {
            moveDirection = MoveDirection.TOP_RIGHT;
            start = new Point(this.getX(), this.getY()+this.getHeight()-1);
        }
        else if(me.getY()<=margin) {
            moveDirection = MoveDirection.TOP;
            start = new Point(this.getX()+this.getWidth()-1, this.getY()+this.getHeight()-1);
        }
        else if(me.getX()<=margin) {
            moveDirection = MoveDirection.LEFT;
            start = new Point(this.getX()+this.getWidth()-1, this.getY()+this.getHeight()-1);
        }
        else if(me.getY()>=newHeight-margin) {
            moveDirection = MoveDirection.BOTTOM;
            start = new Point(this.getX(), this.getY());
        }
        else if(me.getX()>=newWidth-margin) {
            moveDirection = MoveDirection.RIGHT;
            start = new Point(this.getX(), this.getY());
        }
        else
            moveDirection = MoveDirection.CENTER;

        if(moveDirection != MoveDirection.CENTER){
            uic.getMainWindow().getCompositionArea().getOverlay().setMode(CompositionAreaOverlay.Mode.RESIZE_GE);
            uic.getMainWindow().getCompositionArea().getOverlay().setStart(start);
        }
    }

    /**
    * Called when the mouse is released.
    * It transfer the location of the mouse to the right method according to the mouseMode value;
    * @param p Location on screen of the mouse when it's released.
    */
    public void mouseReleasedHub(Point p){
        //If the mouse didn't moved, it means the user had clicked, so skips the mouse released actions.
        if(p.x==startX && p.y==startY)
            return;

        if(moveDirection==MoveDirection.CENTER) {
            ge.setX(ge.getX() - startX + p.x);
            ge.setY(ge.getY() - startY + p.y);
            this.moveDirection = MoveDirection.NONE;
            this.moveMode = MoveMode.NONE;
        }
        else {
            switch (moveMode) {
                case ALTGRAPH:
                    mouseReleasedALTGRAPH(p);
                    break;
                case CTRL:
                case SHIFT:
                    mouseReleasedSHIFT(p);
                    break;
                case NONE:
                    mouseReleasedNONE(p);
                    break;
            }
            //Set the new bounds of the compositionJPanel before validating it (and redraw it)
            double rad = Math.toRadians(ge.getRotation());
            final double newWidth = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
            final double newHeight = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());
            panel.setBounds(ge.getX() + (ge.getWidth() - (int) newWidth) / 2, ge.getY() + (ge.getHeight() - (int) newHeight) / 2, (int) newWidth + 2, (int) newHeight + 2);
            this.setBounds(ge.getX() + (ge.getWidth() - (int) newWidth) / 2, ge.getY() + (ge.getHeight() - (int) newHeight) / 2, (int) newWidth + 2, (int) newHeight + 2);
            panel.revalidate();

            uic.validateGE(ge);

            this.moveDirection = MoveDirection.NONE;
            this.moveMode=MoveMode.NONE;
        }
        uic.getMainWindow().getCompositionArea().getOverlay().setMode(CompositionAreaOverlay.Mode.NONE);
    }

     /**
      * Move and resize of the GraphicalElement when no key are pressed.
      * Sets the new dimension and position of the ge when the mouse is released.
      * @param p Location on screen of the mouse when it's released.
      */
     private void mouseReleasedNONE(Point p) {
         switch(moveDirection){
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
         }
     }

     /**
      * Move and resize of the GraphicalElement when the key SHIFT is pressed.
      * Resize the GraphicalElement (like in mouseReleasedNONE()) but keep the image width/height ratio
      * @param p Location on screen of the mouse when it's released.
      */
     private void mouseReleasedSHIFT(Point p) {
         float ration = ((float)ge.getHeight())/ge.getWidth();
         switch(moveDirection){
             case TOP:
                 //Set the new height
                 ge.setHeight(Math.abs(startY - p.y + ge.getHeight()));
                 ge.setY(ge.getY() - (startY - p.y));
                 //Adapt the width
                 ge.setX(ge.getX() - (int) (ge.getHeight() / ration - ge.getWidth()));
                 ge.setWidth((int) (ge.getHeight() / ration));
                 break;
             case TOP_LEFT:
                 //test if the new width corresponding to the new height is wider the the new width
                 if(Math.abs(startY - p.y + ge.getHeight())/ration > Math.abs(startX-p.x+ge.getWidth())){
                     //Set the new height
                     ge.setHeight(Math.abs(startY - p.y + ge.getHeight()));
                     ge.setY(ge.getY() - (startY - p.y));
                     //Adapt the width
                     ge.setX(ge.getX() - (int)(ge.getHeight()/ration -ge.getWidth()));
                     ge.setWidth((int) (ge.getHeight() / ration));
                 }
                 else{
                     //Set the new height
                     ge.setWidth(Math.abs(startX - p.x + ge.getWidth()));
                     ge.setX(ge.getX() - (startX - p.x));
                     //Adapt the width
                     ge.setY(ge.getY() - (int) (ge.getWidth() * ration - ge.getHeight()));
                     ge.setHeight((int) (ge.getWidth() * ration));
                 }
                 break;
             case LEFT:
                 //Set the new width
                 ge.setWidth(Math.abs(startX - p.x + ge.getWidth()));
                 ge.setX(ge.getX() - (startX - p.x));
                 //Adapt the height
                 ge.setY(ge.getY() - (int)(ge.getWidth()*ration -ge.getHeight()));
                 ge.setHeight((int) (ge.getWidth() * ration));
                 break;
             case BOTTOM_LEFT:
                 //test if the new width corresponding to the new height is wider the the new width
                 if(Math.abs(-(startY-p.y) + ge.getHeight())/ration > Math.abs(startX-p.x+ge.getWidth())){
                     //Set the new height
                     ge.setHeight(Math.abs(-(startY-p.y)+ge.getHeight()));
                     //Adapt the width
                     ge.setX(ge.getX() - (int)(ge.getHeight()/ration -ge.getWidth()));
                     ge.setWidth((int) (ge.getHeight() / ration));
                 }
                 else{
                     //Set the new width
                     ge.setWidth(Math.abs(startX - p.x + ge.getWidth()));
                     ge.setX(ge.getX() - (startX - p.x));
                     //Adapt the height
                     ge.setHeight((int) (ge.getWidth() * ration));
                 }
                 break;
             case BOTTOM:
                 //Set the new height
                 ge.setHeight(Math.abs(-(startY-p.y)+ge.getHeight()));
                 //Adapt the width
                 ge.setWidth((int) (ge.getHeight() / ration));
                 break;
             case BOTTOM_RIGHT:
                 //test if the new width corresponding to the new height is wider the the new width
                 if(Math.abs(-(startY-p.y) + ge.getHeight())/ration > Math.abs(-(startX - p.x)+ge.getWidth())){
                     //Set the new height
                     ge.setHeight(Math.abs(-(startY-p.y)+ge.getHeight()));
                     //Adapt the width
                     ge.setWidth((int) (ge.getHeight() / ration));
                     break;
                 }
                 else{
                     //Set the new width
                     ge.setWidth(Math.abs(-(startX-p.x)+ge.getWidth()));
                     //Adapt the height
                     ge.setHeight((int) (ge.getWidth() * ration));
                 }
                 break;
             case RIGHT:
                 //Set the new width
                 ge.setWidth(Math.abs(-(startX-p.x)+ge.getWidth()));
                 //Adapt the height
                 ge.setHeight((int) (ge.getWidth() * ration));
                 break;
             case TOP_RIGHT :
                 //test if the new width corresponding to the new height is wider the the new width
                 if(Math.abs(startY - p.y + ge.getHeight())/ration > Math.abs(-(startX - p.x)+ge.getWidth())){
                     //Set the new height
                     ge.setHeight(Math.abs(startY - p.y + ge.getHeight()));
                     ge.setY(ge.getY() - (startY - p.y));
                     //Adapt the width
                     ge.setWidth((int) (ge.getHeight() / ration));
                 }
                 else{
                     //Set the new width
                     ge.setWidth(Math.abs(-(startX - p.x) + ge.getWidth()));
                     //Adapt the height
                     ge.setY(ge.getY() - (int) (ge.getWidth() * ration - ge.getHeight()));
                     ge.setHeight((int) (ge.getWidth() * ration));
                 }
                 break;
         }
     }

     /**
      * Move and resize of the GraphicalElement when the key ALTGRAPH is pressed.
      * When the mouse is released, the CompositionJPanel take the new size and the GraphicalElement is adapted to fit into it.
      * @param p Location on screen of the mouse when it's released.
      */
     private void mouseReleasedALTGRAPH(Point p) {
         Point point;
         switch(moveDirection){
             case TOP:
                 //Convert the new width and height of the resize CompositionJPanel into the corresponding GraphicalElement width and height
                 point = panelToGE(new Point(this.getWidth(), -p.y + startY - 1 + this.getHeight()));
                 //Test if the resize does not stretch too much the panel (GE width or height under twice the margin size)
                 if((point.x>=margin*2) && (point.y>=margin*2)) {
                     //Set the GraphicalElement new width and height taking into account of the border width
                     ge.setWidth(Math.abs(point.x) - 1);
                     ge.setHeight(Math.abs(point.y) - 1);
                     //Move the GraphicalElement to keep the center of the CompositionJPanel at the same position
                     ge.setX((int) this.getBounds().getX() - (Math.abs(point.x) - this.getWidth()) / 2 - 1);
                     ge.setY((int) this.getBounds().getY() - (point.y - (p.y - startY + this.getHeight())) / 2 - 1);
                 }
                 break;
             case TOP_LEFT:
                 //Convert the new width and height of the resize CompositionJPanel into the corresponding GraphicalElement width and height
                 point = panelToGE(new Point(-p.x + startX - 1 + this.getWidth(), -p.y + startY - 1 + this.getHeight()));
                 //Test if the resize does not stretch too much the panel (GE width or height under twice the margin size)
                 if((point.x>=margin*2) && (point.y>=margin*2)) {
                     //Set the GraphicalElement new width and height taking into account of the border width
                     ge.setWidth(Math.abs(point.x) - 1);
                     ge.setHeight(Math.abs(point.y) - 1);
                     //Move the GraphicalElement to keep the center of the CompositionJPanel at the same position
                     ge.setX((int) this.getBounds().getX() - (point.x - (p.x - startX + this.getWidth())) / 2 - 1);
                     ge.setY((int) this.getBounds().getY() - (point.y - (p.y - startY + this.getHeight())) / 2 - 1);
                 }
                 break;
             case LEFT:
                 //Convert the new width and height of the resize CompositionJPanel into the corresponding GraphicalElement width and height
                 point = panelToGE(new Point(-p.x + startX - 1 + this.getWidth(), this.getHeight()));
                 //Test if the resize does not stretch too much the panel (GE width or height under twice the margin size)
                 if((point.x>=margin*2) && (point.y>=margin*2)) {
                     //Set the GraphicalElement new width and height taking into account of the border width
                     ge.setWidth(Math.abs(point.x) - 1);
                     ge.setHeight(Math.abs(point.y) - 1);
                     //Move the GraphicalElement to keep the center of the CompositionJPanel at the same position
                     ge.setX((int) this.getBounds().getX() - (point.x - (p.x - startX + this.getWidth())) / 2 - 1);
                     ge.setY((int) this.getBounds().getY() - (Math.abs(point.y) - this.getHeight()) / 2 - 1);
                 }
                 break;
             case BOTTOM_LEFT:
                 //Convert the new width and height of the resize CompositionJPanel into the corresponding GraphicalElement width and height
                 point = panelToGE(new Point(-p.x + startX - 1 + this.getWidth(), p.y - startY + this.getHeight()));
                 //Test if the resize does not stretch too much the panel (GE width or height under twice the margin size)
                 if((point.x>=margin*2) && (point.y>=margin*2)) {
                     //Set the GraphicalElement new width and height taking into account of the border width
                     ge.setWidth(Math.abs(point.x) - 1);
                     ge.setHeight(Math.abs(point.y) - 1);
                     //Move the GraphicalElement to keep the center of the CompositionJPanel at the same position
                     ge.setX((int) this.getBounds().getX() - (point.x - (p.x - startX + this.getWidth())) / 2 - 1);
                     ge.setY((int) this.getBounds().getY() - (Math.abs(point.y) - (p.y - startY + this.getHeight())) / 2 - 1);
                 }
                 break;
             case BOTTOM:
                 //Convert the new width and height of the resize CompositionJPanel into the corresponding GraphicalElement width and height
                 point = panelToGE(new Point(this.getWidth(), p.y - startY + this.getHeight()));
                 //Test if the resize does not stretch too much the panel (GE width or height under twice the margin size)
                 if((point.x>=margin*2) && (point.y>=margin*2)) {
                     //Set the GraphicalElement new width and height taking into account of the border width
                     ge.setWidth(Math.abs(point.x) - 1);
                     ge.setHeight(Math.abs(point.y) - 1);
                     //Move the GraphicalElement to keep the center of the CompositionJPanel at the same position
                     ge.setX((int) this.getBounds().getX() - (Math.abs(point.x) - this.getWidth()) / 2 - 1);
                     ge.setY((int) this.getBounds().getY() - (Math.abs(point.y) - (p.y - startY + this.getHeight())) / 2 - 1);
                 }
                 break;
             case BOTTOM_RIGHT:
                 //Convert the new width and height of the resize CompositionJPanel into the corresponding GraphicalElement width and height
                 point = panelToGE(new Point(p.x - startX + this.getWidth(), p.y - startY + this.getHeight()));
                 //Test if the resize does not stretch too much the panel (GE width or height under twice the margin size)
                 if((point.x>=margin*2) && (point.y>=margin*2)) {
                     //Set the GraphicalElement new width and height taking into account of the border width
                     ge.setWidth(Math.abs(point.x) - 1);
                     ge.setHeight(Math.abs(point.y) - 1);
                     //Move the GraphicalElement to keep the center of the CompositionJPanel at the same position
                     ge.setX((int) this.getBounds().getX() - (Math.abs(point.x) - (p.x - startX + this.getWidth())) / 2 - 1);
                     ge.setY((int) this.getBounds().getY() - (Math.abs(point.y) - (p.y - startY + this.getHeight())) / 2 - 1);
                 }
                 break;
             case RIGHT:
                 //Convert the new width and height of the resize CompositionJPanel into the corresponding GraphicalElement width and height
                 point = panelToGE(new Point(p.x - startX + this.getWidth(), this.getHeight()));
                 //Test if the resize does not stretch too much the panel (GE width or height under twice the margin size)
                 if((point.x>=margin*2) && (point.y>=margin*2)) {
                     //Set the GraphicalElement new width and height taking into account of the border width
                     ge.setWidth(Math.abs(point.x) - 1);
                     ge.setHeight(Math.abs(point.y) - 1);
                     //Move the GraphicalElement to keep the center of the CompositionJPanel at the same position
                     ge.setX((int) this.getBounds().getX() - (Math.abs(point.x) - (p.x - startX + this.getWidth())) / 2 - 1);
                     ge.setY((int) this.getBounds().getY() - (Math.abs(point.y) - this.getHeight()) / 2 - 1);
                 }
                 break;
             case TOP_RIGHT :
                 //Convert the new width and height of the resize CompositionJPanel into the corresponding GraphicalElement width and height
                 point = panelToGE(new Point(p.x - startX + this.getWidth(), -p.y + startY - 1 + this.getHeight()));
                 //Test if the resize does not stretch too much the panel (GE width or height under twice the margin size)
                 if((point.x>=margin*2) && (point.y>=margin*2)) {
                     //Set the GraphicalElement new width and height taking into account of the border width
                     ge.setWidth(Math.abs(point.x) - 1);
                     ge.setHeight(Math.abs(point.y) - 1);
                     //Move the GraphicalElement to keep the center of the CompositionJPanel at the same position
                     ge.setX((int) this.getBounds().getX() - (Math.abs(point.x) - (p.x - startX + this.getWidth())) / 2 - 1);
                     ge.setY((int) this.getBounds().getY() - (point.y - (p.y - startY + this.getHeight())) / 2 - 1);
                 }
                 break;
         }
     }

    /**
     * Refresh the panel position when the mouse is dragged dragged.
     * @param p Location of the mouse inside the panel.
     */
    public void mouseDragged(Point p) {
        //if the user just want to move the element
        if (moveDirection == MoveDirection.CENTER) {
            double rad = Math.toRadians(ge.getRotation());
            final double newWidth = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
            final double newHeight = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());
            this.setBounds(ge.getX()+(ge.getWidth()-(int)newWidth)/2+p.x-startX, ge.getY()+(ge.getHeight()-(int)newHeight)/2+p.y-startY, this.getWidth(), this.getHeight());
        }
        //If the user is resizing the element
        else{
            //Get the position of the mouse in the COmpositionArea
            Point end = new Point(p.x - uic.getMainWindow().getCompositionArea().getLocationOnScreen().x, p.y - uic.getMainWindow().getCompositionArea().getLocationOnScreen().y);
            //If the user want to resize by saving the element width/height ratio
            if(moveMode==MoveMode.SHIFT){
                float ratio = (float)this.getHeight()/this.getWidth();
                int x = end.x, y = end.y;
                int width, height;
                switch (moveDirection){
                    case TOP :
                        x = (this.getX()+this.getWidth())+(int)((end.y-(this.getY()+this.getHeight()))/ratio);
                        break;
                    case RIGHT:
                        y = (this.getY()+this.getHeight())+(int)((end.x-(this.getX()+this.getWidth()))*ratio);
                        break;
                    case BOTTOM:
                        x = (this.getX()+this.getWidth())+(int)((end.y-(this.getY()+this.getHeight()))/ratio);
                        break;
                    case LEFT:
                        y = (this.getY()+this.getHeight())+(int)((end.x-(this.getX()+this.getWidth()))*ratio);
                        break;
                    case BOTTOM_LEFT:
                        width = Math.abs(end.x-(this.getX()+this.getWidth()));
                        height = Math.abs(end.y-this.getY());
                        if(width*ratio>height) {
                            x = end.x;
                            y = this.getY()+(int)(width*ratio);
                        }
                        else {
                            x = this.getX()+this.getWidth()-(int)(height/ratio);
                            y = end.y;
                        }
                        break;
                    case BOTTOM_RIGHT:
                        width = Math.abs(end.x-this.getX());
                        height = Math.abs(end.y-this.getY());
                        if(width*ratio>height) {
                            x = end.x;
                            y = this.getY()+(int)(width*ratio);
                        }
                        else {
                            x = this.getX()+(int)(height/ratio);
                            y = end.y;
                        }
                        break;
                    case TOP_LEFT:
                        width = Math.abs(end.x-(this.getX()+this.getWidth()));
                        height = Math.abs(end.y-(this.getY()+this.getHeight()));
                        if(width*ratio>height) {
                            x = (this.getX() + this.getWidth()) + (int) ((end.y - (this.getY() + this.getHeight())) / ratio);
                            x = end.x;
                            y = this.getY()+this.getHeight()-(int)(width*ratio);
                        }
                        else {
                            y = (this.getY() + this.getHeight()) + (int) ((end.x - (this.getX() + this.getWidth())) * ratio);
                            x = this.getX() + this.getWidth() - (int) (height / ratio);
                            y = end.y;
                        }
                        break;
                    case TOP_RIGHT:
                        width = Math.abs(end.x-this.getX());
                        height = Math.abs(end.y-(this.getY()+this.getHeight()));
                        if(width*ratio>height) {
                            x = end.x;
                            y = this.getY()+this.getHeight()-(int)(width*ratio);
                        }
                        else {
                            x = this.getX()+(int)(height/ratio);
                            y = end.y;
                        }
                        break;
                }
                end = new Point(x, y);
            }
            //If the element size ratio is not important
            else{
                switch (moveDirection){
                    case TOP :
                        end = new Point(this.getX(), end.y);
                        break;
                    case RIGHT:
                        end = new Point(end.x, this.getY()+this.getHeight()-1);
                        break;
                    case BOTTOM:
                        end = new Point(this.getX()+this.getWidth()-1, end.y);
                        break;
                    case LEFT:
                        end = new Point(end.x, this.getY());
                        break;
                }
            }
            uic.getMainWindow().getCompositionArea().getOverlay().setEnd(end);
        }
    }

    /**
     * Changes the mouse cursor appearence according to the border hovered.
     * @param p Point of the mouse.
     */
    public void mouseMoved(Point p) {
        int x = p.x;
        int y = p.y;
        //Need to recalculate the GE width and height because of the rotation
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
            this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.red));
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
            this.setBorder(null);
    }

     /**
      * Converts point from the CompositionJPanel to the corresponding point in the GraphicalElement.
      * @param p Point from the CompositionJPanel
      * @return Corresponding point in the GraphicalElement
      */
     private Point panelToGE(Point p){
         int x = 0;
         int y = 0;
         double rad = Math.toRadians(ge.getRotation());
         int angle = Math.abs(ge.getRotation());
         if(angle%90==45){
             //If the angle is of 45 cannot resize
         }
         else if(angle>=0 && angle<=90) {
             x = (int) Math.floor((p.x * cos(rad) - p.y * sin(Math.abs(rad))) / (cos(rad) * cos(rad) - sin(rad) * sin(rad)));
             y = (int) Math.floor((p.y * cos(rad) - p.x * sin(Math.abs(rad))) / (cos(rad) * cos(rad) - sin(rad) * sin(rad)));
         }
         else if(angle>=90 && angle<=180) {
             x = -(int) Math.floor((p.x * cos(rad) + p.y * sin(Math.abs(rad))) / (cos(rad) * cos(rad) - sin(rad) * sin(rad)));
             y = -(int) Math.floor((p.y * cos(rad) + p.x * sin(Math.abs(rad))) / (cos(rad) * cos(rad) - sin(rad) * sin(rad)));
         }
         else if(angle>=180 && angle<=270) {
             x = -(int) Math.floor((p.x * cos(rad) - p.y * sin(Math.abs(rad))) / (cos(rad) * cos(rad) - sin(rad) * sin(rad)));
             y = -(int) Math.floor((p.y * cos(rad) - p.x * sin(Math.abs(rad))) / (cos(rad) * cos(rad) - sin(rad) * sin(rad)));
         }
         else if(angle>=270 && angle<=360) {
             x = (int) Math.floor((p.x * cos(rad) + p.y * sin(Math.abs(rad))) / (cos(rad) * cos(rad) - sin(rad) * sin(rad)));
             y = (int) Math.floor((p.y * cos(rad) + p.x * sin(Math.abs(rad))) / (cos(rad) * cos(rad) - sin(rad) * sin(rad)));
         }
         return new Point(x, y);
     }
}
