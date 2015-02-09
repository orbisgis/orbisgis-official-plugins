/*
* MapComposer is an OrbisGIS plugin dedicated to the creation of cartographic
* documents based on OrbisGIS results.
*
* This plugin is developed at French IRSTV institute as part of the MApUCE project,
* funded by the French Agence Nationale de la Recherche (ANR) under contract ANR-13-VBDU-0004.
*
* The MapComposer plugin is distributed under GPL 3 license. It is produced by the "Atelier SIG"
* team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
*
* Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
*
* This file is part of the MapComposer plugin.
*
* The MapComposer plugin is free software: you can redistribute it and/or modify it under the
* terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
*
* The MapComposer plugin is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
* A PARTICULAR PURPOSE. See the GNU General Public License for more details <http://www.gnu.org/licenses/>.
*/

package org.orbisgis.mapcomposer.view.utils;

import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GEProperties;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import java.awt.*;
import java.awt.Image;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.beans.EventHandler;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import javax.swing.*;

/**
 * This panel, which extends JPanel, contains the representation of a GraphicalElement and all the method used to manage this representation.
 * A Composition JPanel is structured this way :
 *     CompositionJPanel <- JLayer <-+- JPanel <- JComponent
 *                                   |
 *                                   |_ WaitLayer
 *
 * The JComponent contains the BufferedImage get from the GraphicalElement rendering.
 * The WaitLayer is used to display an animation to indicate to the user that an action (like rendering) is in process.
 *
 * @author Sylvain PALOMINOS
 */
public class CompositionJPanel extends JPanel{

    /**GraphicalElement displayed. */
    private final GraphicalElement ge;

    /**Select state of the panel. */
    private boolean selected=false;

    /** Initial point where the user clicked to modify the CompositionJPanel. */
    private Point startPoint;

    /**Type of move the user want to do. */
    private enum MoveDirection {TOP_LEFT, TOP, TOP_RIGHT, RIGHT, BOTTOM_RIGHT, BOTTOM, BOTTOM_LEFT, LEFT, CENTER, NONE}

    /**Id of the move that the user is doing. */
    private MoveDirection moveDirection = MoveDirection.NONE;

    /**Type of move the user want to do. */
    private enum MoveMode {NONE, SHIFT, ALTGRAPH, CTRL}

    /**Id of the move that the user is doing. */
    private MoveMode moveMode = MoveMode.NONE;

    /** Reference to the UIController. */
    private final MainController mainController;

    /** Size of the margin of the border for resizing. */
    private static final int margin = 5;

    private WaitLayerUI waitLayer;
    private JPanel panel;

    /** Last BufferedImage rendered used for this CompositionJPanel */
    private BufferedImage contentImage;

     /** Object for the translation*/
    private static final I18n i18n = I18nFactory.getI18n(CompositionJPanel.class);

    /**
     * Main constructor.
     * @param ge GraphicalElement to display.
     * @param mainController UIController of the application.
     */
    public CompositionJPanel(GraphicalElement ge, MainController mainController){
        super(new BorderLayout());
        this.setSize(ge.getWidth(), ge.getHeight());
        this.mainController = mainController;
        this.ge=ge;
        //Disable mouse listeners if the GraphicalElement can't be edited by the mouse.
        if(ge instanceof GEProperties && !((GEProperties)ge).isEditedByMouse())
            this.setEnabled(false);
        else{
            this.addMouseListener(EventHandler.create(MouseListener.class, this, "mouseClicked", "", "mouseClicked"));
            this.addMouseListener(EventHandler.create(MouseListener.class, this, "mousePressed", "", "mousePressed"));
            this.addMouseListener(EventHandler.create(MouseListener.class, this, "mouseReleasedHub", "getLocationOnScreen", "mouseReleased"));
            this.addMouseMotionListener(EventHandler.create(MouseMotionListener.class, this, "mouseDragged", "getLocationOnScreen", "mouseDragged"));
            this.addMouseMotionListener(EventHandler.create(MouseMotionListener.class, this, "mouseMoved", "getPoint", "mouseMoved"));
            this.setToolTipText(i18n.tr("<html>Holding <strong>Alt Gr</strong> : resize the representation of the element.<br/>" +
                    "Holding <strong>Shift</strong> : resire the element and keeps the ratio width/height.</html>"));
        }

        panel = new JPanel(new BorderLayout());
        waitLayer = new WaitLayerUI();
        JLayer<JPanel> layer = new JLayer<>(panel, waitLayer);
        this.add(layer);
        this.setOpaque(false);
    }

    /**
     * Transform the given BufferedImage to get the unrotated equivalent and store it as contentImage.
     * The stored image will be used to be drawn in the CompositionJPanel without rendering the GraphicalElement.
     * @param renderedBufferedImage BufferedImage to store.
     */
     private void storeLastRenderedImage(BufferedImage renderedBufferedImage){
         double rad = Math.toRadians(ge.getRotation());
         //Width and Height of the rectangle containing the rotated bufferedImage
         final double newWidth = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
         final double newHeight = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());
         final int maxWidth = Math.max((int)newWidth, ge.getWidth());
         final int maxHeight = Math.max((int)newHeight, ge.getHeight());

         //create a copy of the given BufferedImage and recover it's form without rotation
         AffineTransform affineTransform= new AffineTransform();
         affineTransform.rotate(-Math.toRadians(ge.getRotation()), maxWidth / 2, maxHeight / 2);
         AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
         BufferedImage unrotatedBI = affineTransformOp.filter(renderedBufferedImage, null);

         //Crop it to the good size
         BufferedImage croppedBI = new BufferedImage(ge.getWidth(), ge.getHeight(), BufferedImage.TYPE_INT_ARGB);
         Graphics graph = croppedBI.createGraphics();
         graph.drawImage(unrotatedBI,
                 0, 0,
                 ge.getWidth(), ge.getHeight(),
                 (maxWidth - ge.getWidth()) / 2, (maxHeight - ge.getHeight()) / 2,
                 maxWidth - ((maxWidth - ge.getWidth()) / 2), maxHeight - ((maxHeight - ge.getHeight()) / 2), null);
         graph.dispose();

         //Store the copy image as the contentImage
         contentImage = croppedBI;
     }

    /**
     * Returns the contentImage after resizing it and rotating it to fit the GraphicalElement properties (width, height, rotation).
     * @return The ready to draw BufferedImage of the GraphicalElement.
     */
     private BufferedImage getContentImage(){
         double rad = Math.toRadians(ge.getRotation());
         //Width and Height of the rectangle containing the rotated bufferedImage
         final double newWidth = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
         final double newHeight = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());
         final int maxWidth = Math.max((int)newWidth, ge.getWidth());
         final int maxHeight = Math.max((int)newHeight, ge.getHeight());

         //First scale the contentImage to the new size of the GraphicalELement
         Image image = contentImage.getScaledInstance(ge.getWidth(), ge.getHeight(), BufferedImage.SCALE_FAST);
         BufferedImage bi = new BufferedImage(ge.getWidth(), ge.getHeight(), contentImage.getType());
         Graphics2D graph = bi.createGraphics();
         graph.drawImage(image, 0, 0, null);
         graph.dispose();

         //Draw the BufferedImage bi into the bigger BufferedImage to prepare it for the rotation (and to avoid cropping the image).
         BufferedImage bufferedImage = new BufferedImage(maxWidth, maxHeight, bi.getType());
         graph = bufferedImage.createGraphics();
         graph.drawImage(bi,
                 (maxWidth - ge.getWidth()) / 2, (maxHeight - ge.getHeight()) / 2,
                 maxWidth - ((maxWidth - ge.getWidth()) / 2), maxHeight - ((maxHeight - ge.getHeight()) / 2),
                 0, 0,
                 ge.getWidth(), ge.getHeight(), null);
         graph.dispose();

         //Create the rotation transform fo the buffered image
         AffineTransform affineTransform = new AffineTransform();
         affineTransform.rotate(Math.toRadians(ge.getRotation()), maxWidth / 2, maxHeight / 2);

         //Apply the transform to the bufferedImage and draw it
         AffineTransformOp affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
         return affineTransformOp.filter(bufferedImage, null);
     }

    /**
     * Redraw the GraphicalElement with the given BufferedImage if the argument is not null or with the stored one if the argument is null.
     *
     * @param bufferedImage New graphical representation of the GE or null.
     */
    public void refresh(final BufferedImage bufferedImage){
        double rad = Math.toRadians(ge.getRotation());
        //Width and Height of the rectangle containing the rotated bufferedImage
        final double newWidth = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
        final double newHeight = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());
        final int maxWidth = Math.max((int)newWidth, ge.getWidth());
        final int maxHeight = Math.max((int)newHeight, ge.getHeight());

        //Draw the bufferedImage
        panel.removeAll();
        //Add the BufferedImage into a JComponent in the CompositionJPanel
        panel.add(new JComponent() {
            //Redefinition of the painComponent method to rotate the component content.
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (bufferedImage != null) {
                    g.drawImage(bufferedImage, -(maxWidth - (int) newWidth) / 2, -(maxHeight - (int) newHeight) / 2, null);
                    storeLastRenderedImage(bufferedImage);
                } else if (contentImage != null)
                    g.drawImage(getContentImage(), -(maxWidth - (int) newWidth) / 2, -(maxHeight - (int) newHeight) / 2, null);
            }
        }, BorderLayout.CENTER);
        panel.revalidate();
        //Take account of the border width (2 pixels).
        if(ge instanceof GEProperties && ((GEProperties)ge).isAlwaysCentered()){
            this.setPreferredSize(new Dimension((int) newWidth + 2, (int) newHeight + 2));
        }
        else {
            Point p = new Point(ge.getX() + (ge.getWidth() - (int) newWidth) / 2, ge.getY() + (ge.getHeight() - (int) newHeight) / 2);
            p = mainController.getMainWindow().getCompositionArea().documentPointToScreenPoint(p);
            this.setBounds(p.x, p.y, (int) newWidth + 2, (int) newHeight + 2);
        }
        panel.setOpaque(false);
        setBorders();
    }

    /**
     * Draw border if the CompositionJPanel is selected.
     */
    private void setBorders() {
        if(!(ge instanceof Document)) {
            if (selected)
                this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.ORANGE));
            else
                this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }
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
            mainController.getUIController().showGEProperties(ge);
        if(selected)
            mainController.unselectGE(ge);
        else
            mainController.selectGE(ge);

        this.moveDirection = MoveDirection.NONE;
        this.moveMode=MoveMode.NONE;
        mainController.getMainWindow().getCompositionArea().getOverlay().setMode(CompositionAreaOverlay.Mode.NONE);
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

        startPoint = me.getLocationOnScreen();

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
            mainController.getMainWindow().getCompositionArea().getOverlay().setMode(CompositionAreaOverlay.Mode.RESIZE_GE);
            mainController.getMainWindow().getCompositionArea().getOverlay().setStart(start);
        }
    }

    /**
    * Called when the mouse is released.
    * It transfer the location of the mouse to the right method according to the mouseMode value;
    * @param p Location on screen of the mouse when it's released.
    */
    public void mouseReleasedHub(Point p){
        GraphicalElement ge = this.ge.deepCopy();
        //If the mouse didn't moved, it means the user had clicked, so skips the mouse released actions.
        if(p.x==startPoint.x && p.y==startPoint.y)
            return;

        if(moveDirection==MoveDirection.CENTER) {
            ge.setX(ge.getX() - startPoint.x + p.x);
            ge.setY(ge.getY() - startPoint.y + p.y);
            mainController.modifyGE(this.ge, ge);
            this.moveDirection = MoveDirection.NONE;
            this.moveMode = MoveMode.NONE;
        }
        else {
            switch (moveMode) {
                case ALTGRAPH:
                    mouseReleasedALTGRAPH(p, ge);
                    break;
                case CTRL:
                case SHIFT:
                    mouseReleasedSHIFT(p, ge);
                    break;
                case NONE:
                    mouseReleasedNONE(p, ge);
                    break;
            }
            //Set the new bounds of the compositionJPanel before validating it (and redraw it)
            double rad = Math.toRadians(ge.getRotation());
            final double newWidth = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
            final double newHeight = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());
            Point point = mainController.getMainWindow().getCompositionArea().documentPointToScreenPoint(new Point(
                    ge.getX() + (ge.getWidth() - (int) newWidth) / 2,
                    ge.getY() + (ge.getHeight() - (int) newHeight) / 2));
            panel.setBounds(point.x, point.y, (int) newWidth + 2, (int) newHeight + 2);
            this.setBounds(point.x, point.y, (int) newWidth + 2, (int) newHeight + 2);
            panel.revalidate();

            mainController.modifyGE(this.ge, ge);

            this.moveDirection = MoveDirection.NONE;
            this.moveMode=MoveMode.NONE;
        }
        mainController.getMainWindow().getCompositionArea().getOverlay().setMode(CompositionAreaOverlay.Mode.NONE);
        mainController.getUIController().refreshSpin();
    }

     /**
      * Move and resize of the GraphicalElement when no key are pressed.
      * Sets the new dimension and position of the ge when the mouse is released.
      * @param p Location on screen of the mouse when it's released.
      */
     private void mouseReleasedNONE(Point p, GraphicalElement ge) {
         switch(moveDirection){
             case TOP:
                 ge.setHeight(Math.abs(startPoint.y-p.y+ge.getHeight()));
                 ge.setY(ge.getY()-(startPoint.y-p.y));
                 break;
             case TOP_LEFT:
                 ge.setHeight(Math.abs(startPoint.y-p.y+ge.getHeight()));
                 ge.setY(ge.getY()-(startPoint.y-p.y));
                 ge.setWidth(Math.abs(startPoint.x-p.x+ge.getWidth()));
                 ge.setX(ge.getX()-(startPoint.x-p.x));
                 break;
             case LEFT:
                 ge.setWidth(Math.abs(startPoint.x-p.x+ge.getWidth()));
                 ge.setX(ge.getX()-(startPoint.x-p.x));
                 break;
             case BOTTOM_LEFT:
                 ge.setWidth(Math.abs(startPoint.x-p.x+ge.getWidth()));
                 ge.setX(ge.getX()-(startPoint.x-p.x));
                 ge.setHeight(Math.abs(-(startPoint.y-p.y)+ge.getHeight()));
                 break;
             case BOTTOM:
                 ge.setHeight(Math.abs(-(startPoint.y-p.y)+ge.getHeight()));
                 break;
             case BOTTOM_RIGHT:
                 ge.setHeight(Math.abs(-(startPoint.y-p.y)+ge.getHeight()));
                 ge.setWidth(Math.abs(-(startPoint.x-p.x)+ge.getWidth()));
                 break;
             case RIGHT:
                 ge.setWidth(Math.abs(-(startPoint.x-p.x)+ge.getWidth()));
                 break;
             case TOP_RIGHT :
                 ge.setWidth(Math.abs(-(startPoint.x-p.x)+ge.getWidth()));
                 ge.setHeight(Math.abs(startPoint.y-p.y+ge.getHeight()));
                 ge.setY(ge.getY()-(startPoint.y-p.y));
                 break;
         }
     }

     /**
      * Move and resize of the GraphicalElement when the key SHIFT is pressed.
      * Resize the GraphicalElement (like in mouseReleasedNONE()) but keep the image width/height ratio
      * @param p Location on screen of the mouse when it's released.
      */
     private void mouseReleasedSHIFT(Point p, GraphicalElement ge) {
         float ration = ((float)ge.getHeight())/ge.getWidth();
         switch(moveDirection){
             case TOP:
                 //Set the new height
                 ge.setHeight(Math.abs(startPoint.y - p.y + ge.getHeight()));
                 ge.setY(ge.getY() - (startPoint.y - p.y));
                 //Adapt the width
                 ge.setX(ge.getX() - (int) (ge.getHeight() / ration - ge.getWidth()));
                 ge.setWidth((int) (ge.getHeight() / ration));
                 break;
             case TOP_LEFT:
                 //test if the new width corresponding to the new height is wider the the new width
                 if(Math.abs(startPoint.y - p.y + ge.getHeight())/ration > Math.abs(startPoint.x-p.x+ge.getWidth())){
                     //Set the new height
                     ge.setHeight(Math.abs(startPoint.y - p.y + ge.getHeight()));
                     ge.setY(ge.getY() - (startPoint.y - p.y));
                     //Adapt the width
                     ge.setX(ge.getX() - (int)(ge.getHeight()/ration -ge.getWidth()));
                     ge.setWidth((int) (ge.getHeight() / ration));
                 }
                 else{
                     //Set the new height
                     ge.setWidth(Math.abs(startPoint.x - p.x + ge.getWidth()));
                     ge.setX(ge.getX() - (startPoint.x - p.x));
                     //Adapt the width
                     ge.setY(ge.getY() - (int) (ge.getWidth() * ration - ge.getHeight()));
                     ge.setHeight((int) (ge.getWidth() * ration));
                 }
                 break;
             case LEFT:
                 //Set the new width
                 ge.setWidth(Math.abs(startPoint.x - p.x + ge.getWidth()));
                 ge.setX(ge.getX() - (startPoint.x - p.x));
                 //Adapt the height
                 ge.setY(ge.getY() - (int)(ge.getWidth()*ration -ge.getHeight()));
                 ge.setHeight((int) (ge.getWidth() * ration));
                 break;
             case BOTTOM_LEFT:
                 //test if the new width corresponding to the new height is wider the the new width
                 if(Math.abs(-(startPoint.y-p.y) + ge.getHeight())/ration > Math.abs(startPoint.x-p.x+ge.getWidth())){
                     //Set the new height
                     ge.setHeight(Math.abs(-(startPoint.y-p.y)+ge.getHeight()));
                     //Adapt the width
                     ge.setX(ge.getX() - (int)(ge.getHeight()/ration -ge.getWidth()));
                     ge.setWidth((int) (ge.getHeight() / ration));
                 }
                 else{
                     //Set the new width
                     ge.setWidth(Math.abs(startPoint.x - p.x + ge.getWidth()));
                     ge.setX(ge.getX() - (startPoint.x - p.x));
                     //Adapt the height
                     ge.setHeight((int) (ge.getWidth() * ration));
                 }
                 break;
             case BOTTOM:
                 //Set the new height
                 ge.setHeight(Math.abs(-(startPoint.y-p.y)+ge.getHeight()));
                 //Adapt the width
                 ge.setWidth((int) (ge.getHeight() / ration));
                 break;
             case BOTTOM_RIGHT:
                 //test if the new width corresponding to the new height is wider the the new width
                 if(Math.abs(-(startPoint.y-p.y) + ge.getHeight())/ration > Math.abs(-(startPoint.x - p.x)+ge.getWidth())){
                     //Set the new height
                     ge.setHeight(Math.abs(-(startPoint.y-p.y)+ge.getHeight()));
                     //Adapt the width
                     ge.setWidth((int) (ge.getHeight() / ration));
                     break;
                 }
                 else{
                     //Set the new width
                     ge.setWidth(Math.abs(-(startPoint.x-p.x)+ge.getWidth()));
                     //Adapt the height
                     ge.setHeight((int) (ge.getWidth() * ration));
                 }
                 break;
             case RIGHT:
                 //Set the new width
                 ge.setWidth(Math.abs(-(startPoint.x-p.x)+ge.getWidth()));
                 //Adapt the height
                 ge.setHeight((int) (ge.getWidth() * ration));
                 break;
             case TOP_RIGHT :
                 //test if the new width corresponding to the new height is wider the the new width
                 if(Math.abs(startPoint.y - p.y + ge.getHeight())/ration > Math.abs(-(startPoint.x - p.x)+ge.getWidth())){
                     //Set the new height
                     ge.setHeight(Math.abs(startPoint.y - p.y + ge.getHeight()));
                     ge.setY(ge.getY() - (startPoint.y - p.y));
                     //Adapt the width
                     ge.setWidth((int) (ge.getHeight() / ration));
                 }
                 else{
                     //Set the new width
                     ge.setWidth(Math.abs(-(startPoint.x - p.x) + ge.getWidth()));
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
     private void mouseReleasedALTGRAPH(Point p, GraphicalElement ge) {
         Point point;
         switch(moveDirection){
             case TOP:
                 //Convert the new width and height of the resize CompositionJPanel into the corresponding GraphicalElement width and height
                 point = panelToGE(new Point(this.getWidth(), -p.y + startPoint.y - 1 + this.getHeight()));
                 //Test if the resize does not stretch too much the panel (GE width or height under twice the margin size)
                 if((point.x>=margin*2) && (point.y>=margin*2)) {
                     //Set the GraphicalElement new width and height taking into account of the border width
                     ge.setWidth(Math.abs(point.x) - 1);
                     ge.setHeight(Math.abs(point.y) - 1);
                     //Move the GraphicalElement to keep the center of the CompositionJPanel at the same position
                     Point geCoord = mainController.getMainWindow().getCompositionArea().screenPointToDocumentPoint(new Point(
                             (int) this.getBounds().getX() - (Math.abs(point.x) - this.getWidth()) / 2 - 1,
                             (int) this.getBounds().getY() - (point.y - (p.y - startPoint.y + this.getHeight())) / 2 - 1
                     ));
                     ge.setX(geCoord.x);
                     ge.setY(geCoord.y);
                 }
                 break;
             case TOP_LEFT:
                 //Convert the new width and height of the resize CompositionJPanel into the corresponding GraphicalElement width and height
                 point = panelToGE(new Point(-p.x + startPoint.x - 1 + this.getWidth(), -p.y + startPoint.y - 1 + this.getHeight()));
                 //Test if the resize does not stretch too much the panel (GE width or height under twice the margin size)
                 if((point.x>=margin*2) && (point.y>=margin*2)) {
                     //Set the GraphicalElement new width and height taking into account of the border width
                     ge.setWidth(Math.abs(point.x) - 1);
                     ge.setHeight(Math.abs(point.y) - 1);
                     //Move the GraphicalElement to keep the center of the CompositionJPanel at the same position
                     Point geCoord = mainController.getMainWindow().getCompositionArea().screenPointToDocumentPoint(new Point(
                             (int) this.getBounds().getX() - (point.x - (p.x - startPoint.x + this.getWidth())) / 2 - 1,
                             (int) this.getBounds().getY() - (point.y - (p.y - startPoint.y + this.getHeight())) / 2 - 1
                     ));
                     ge.setX(geCoord.x);
                     ge.setY(geCoord.y);
                 }
                 break;
             case LEFT:
                 //Convert the new width and height of the resize CompositionJPanel into the corresponding GraphicalElement width and height
                 point = panelToGE(new Point(-p.x + startPoint.x - 1 + this.getWidth(), this.getHeight()));
                 //Test if the resize does not stretch too much the panel (GE width or height under twice the margin size)
                 if((point.x>=margin*2) && (point.y>=margin*2)) {
                     //Set the GraphicalElement new width and height taking into account of the border width
                     ge.setWidth(Math.abs(point.x) - 1);
                     ge.setHeight(Math.abs(point.y) - 1);
                     //Move the GraphicalElement to keep the center of the CompositionJPanel at the same position
                     Point geCoord = mainController.getMainWindow().getCompositionArea().screenPointToDocumentPoint(new Point(
                             (int) this.getBounds().getX() - (point.x - (p.x - startPoint.x + this.getWidth())) / 2 - 1,
                             (int) this.getBounds().getY() - (Math.abs(point.y) - this.getHeight()) / 2 - 1
                     ));
                     ge.setX(geCoord.x);
                     ge.setY(geCoord.y);
                 }
                 break;
             case BOTTOM_LEFT:
                 //Convert the new width and height of the resize CompositionJPanel into the corresponding GraphicalElement width and height
                 point = panelToGE(new Point(-p.x + startPoint.x - 1 + this.getWidth(), p.y - startPoint.y + this.getHeight()));
                 //Test if the resize does not stretch too much the panel (GE width or height under twice the margin size)
                 if((point.x>=margin*2) && (point.y>=margin*2)) {
                     //Set the GraphicalElement new width and height taking into account of the border width
                     ge.setWidth(Math.abs(point.x) - 1);
                     ge.setHeight(Math.abs(point.y) - 1);
                     //Move the GraphicalElement to keep the center of the CompositionJPanel at the same position
                     Point geCoord = mainController.getMainWindow().getCompositionArea().screenPointToDocumentPoint(new Point(
                             (int) this.getBounds().getX() - (point.x - (p.x - startPoint.x + this.getWidth())) / 2 - 1,
                             (int) this.getBounds().getY() - (Math.abs(point.y) - this.getHeight()) / 2 - 1
                     ));
                     ge.setX(geCoord.x);
                     ge.setY(geCoord.y);
                 }
                 break;
             case BOTTOM:
                 //Convert the new width and height of the resize CompositionJPanel into the corresponding GraphicalElement width and height
                 point = panelToGE(new Point(this.getWidth(), p.y - startPoint.y + this.getHeight()));
                 //Test if the resize does not stretch too much the panel (GE width or height under twice the margin size)
                 if((point.x>=margin*2) && (point.y>=margin*2)) {
                     //Set the GraphicalElement new width and height taking into account of the border width
                     ge.setWidth(Math.abs(point.x) - 1);
                     ge.setHeight(Math.abs(point.y) - 1);
                     //Move the GraphicalElement to keep the center of the CompositionJPanel at the same position
                     Point geCoord = mainController.getMainWindow().getCompositionArea().screenPointToDocumentPoint(new Point(
                             (int) this.getBounds().getX() - (Math.abs(point.x) - this.getWidth()) / 2 - 1,
                             (int) this.getBounds().getY() - (Math.abs(point.y) - (p.y - startPoint.y + this.getHeight())) / 2 - 1
                     ));
                     ge.setX(geCoord.x);
                     ge.setY(geCoord.y);
                 }
                 break;
             case BOTTOM_RIGHT:
                 //Convert the new width and height of the resize CompositionJPanel into the corresponding GraphicalElement width and height
                 point = panelToGE(new Point(p.x - startPoint.x + this.getWidth(), p.y - startPoint.y + this.getHeight()));
                 //Test if the resize does not stretch too much the panel (GE width or height under twice the margin size)
                 if((point.x>=margin*2) && (point.y>=margin*2)) {
                     //Set the GraphicalElement new width and height taking into account of the border width
                     ge.setWidth(Math.abs(point.x) - 1);
                     ge.setHeight(Math.abs(point.y) - 1);
                     //Move the GraphicalElement to keep the center of the CompositionJPanel at the same position
                     Point geCoord = mainController.getMainWindow().getCompositionArea().screenPointToDocumentPoint(new Point(
                             (int) this.getBounds().getX() - (Math.abs(point.x) - (p.x - startPoint.x + this.getWidth())) / 2 - 1,
                             (int) this.getBounds().getY() - (Math.abs(point.y) - (p.y - startPoint.y + this.getHeight())) / 2 - 1
                     ));
                     ge.setX(geCoord.x);
                     ge.setY(geCoord.y);
                 }
                 break;
             case RIGHT:
                 //Convert the new width and height of the resize CompositionJPanel into the corresponding GraphicalElement width and height
                 point = panelToGE(new Point(p.x - startPoint.x + this.getWidth(), this.getHeight()));
                 //Test if the resize does not stretch too much the panel (GE width or height under twice the margin size)
                 if((point.x>=margin*2) && (point.y>=margin*2)) {
                     //Set the GraphicalElement new width and height taking into account of the border width
                     ge.setWidth(Math.abs(point.x) - 1);
                     ge.setHeight(Math.abs(point.y) - 1);
                     //Move the GraphicalElement to keep the center of the CompositionJPanel at the same position
                     Point geCoord = mainController.getMainWindow().getCompositionArea().screenPointToDocumentPoint(new Point(
                             (int) this.getBounds().getX() - (Math.abs(point.x) - (p.x - startPoint.x + this.getWidth())) / 2 - 1,
                             (int) this.getBounds().getY() - (Math.abs(point.y) - this.getHeight()) / 2 - 1
                     ));
                     ge.setX(geCoord.x);
                     ge.setY(geCoord.y);
                 }
                 break;
             case TOP_RIGHT :
                 //Convert the new width and height of the resize CompositionJPanel into the corresponding GraphicalElement width and height
                 point = panelToGE(new Point(p.x - startPoint.x + this.getWidth(), -p.y + startPoint.y - 1 + this.getHeight()));
                 //Test if the resize does not stretch too much the panel (GE width or height under twice the margin size)
                 if((point.x>=margin*2) && (point.y>=margin*2)) {
                     //Set the GraphicalElement new width and height taking into account of the border width
                     ge.setWidth(Math.abs(point.x) - 1);
                     ge.setHeight(Math.abs(point.y) - 1);
                     //Move the GraphicalElement to keep the center of the CompositionJPanel at the same position
                     Point geCoord = mainController.getMainWindow().getCompositionArea().screenPointToDocumentPoint(new Point(
                             (int) this.getBounds().getX() - (Math.abs(point.x) - (p.x - startPoint.x + this.getWidth())) / 2 - 1,
                             (int) this.getBounds().getY() - (point.y - (p.y - startPoint.y + this.getHeight())) / 2 - 1
                     ));
                     ge.setX(geCoord.x);
                     ge.setY(geCoord.y);
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
            Point point = mainController.getMainWindow().getCompositionArea().documentPointToScreenPoint(new Point(
                    ge.getX() + (ge.getWidth() - (int) newWidth) / 2 + p.x - startPoint.x,
                    ge.getY() + (ge.getHeight() - (int) newHeight) / 2 + p.y - startPoint.y));
            this.setBounds(point.x, point.y, this.getWidth(), this.getHeight());
        }
        //If the user is resizing the element
        else{
            //Get the position of the mouse in the CompositionArea
            Point end = new Point(p.x - mainController.getMainWindow().getCompositionArea().getLocationOnScreen().x-50, p.y - mainController.getMainWindow().getCompositionArea().getLocationOnScreen().y-50);
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
                            x = end.x;
                            y = this.getY()+this.getHeight()-(int)(width*ratio);
                        }
                        else {
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
            mainController.getMainWindow().getCompositionArea().getOverlay().setEnd(end);
        }
    }

    /**
     * Changes the mouse cursor appearance according to the border hovered.
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
             return new Point(x, y);
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

     /**
      * Returns the GraphicalElement represented by this CompositionJPanel.
      * @return The GraphicalElement represented
      */
     public GraphicalElement getGE(){
         return  ge;
     }
}
