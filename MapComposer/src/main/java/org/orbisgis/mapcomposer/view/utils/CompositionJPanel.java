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

    /**Type of move the user want to do. It uses binary value to cumulate two direction (i.e. TOP and RIGHT = 0x0011)*/
    private static final int TOP = 0b0001;
    private static final int RIGHT = 0b0010;
    private static final int BOTTOM = 0b0100;
    private static final int LEFT = 0b1000;
    private static final int NONE = 0b0000;
    private static final int CENTER = 0b10000;

    /**Value of the move direction that the user is doing. */
    private int moveDirection = NONE;

    /**Type of move the user want to do. */
    private enum MoveMode {NONE, SHIFT, ALTGRAPH, CTRL}

    /**Id of the move that the user is doing. */
    private MoveMode moveMode = MoveMode.NONE;

    /** Reference to the UIController. */
    private final MainController mainController;

    /** Size of the margin of the border for resizing. */
    private static final int margin = 5;

    /** Layer used to draw an animation to indicate that something is processing.*/
    private WaitLayerUI waitLayer;

    /** Body panel of the CompositionJPanel where is drawn the GraphicalElement.*/
    private JPanel body;

    /** Last BufferedImage rendered used for this CompositionJPanel */
    private BufferedImage contentImage;

    /** Object for the translation*/
    private static final I18n i18n = I18nFactory.getI18n(CompositionJPanel.class);

    /** Distance dragged by the mouse */
    private Dimension drag;

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
            this.addMouseListener(EventHandler.create(MouseListener.class, this, "mouseReleasedHub", null, "mouseReleased"));
            this.addMouseMotionListener(EventHandler.create(MouseMotionListener.class, this, "mouseDragged", "", "mouseDragged"));
            this.addMouseMotionListener(EventHandler.create(MouseMotionListener.class, this, "mouseMoved", "getPoint", "mouseMoved"));
            this.setToolTipText(i18n.tr("<html>Holding <strong>Alt Gr</strong> : resize the representation of the element.<br/>" +
                    "Holding <strong>Shift</strong> : resize the element and keeps the ratio width/height.</html>"));
        }
        //Adds the layer and the body panel
        body = new JPanel(new BorderLayout());
        waitLayer = new WaitLayerUI();
        JLayer<JPanel> layer = new JLayer<>(body, waitLayer);
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
        body.removeAll();
        //Add the BufferedImage into a JComponent in the CompositionJPanel
        body.add(new JComponent() {
            //Redefinition of the painComponent method to rotate the component content.
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                //If the BufferedImage in argument is not null, use it.
                if (bufferedImage != null) {
                    g.drawImage(bufferedImage, -(maxWidth - (int) newWidth) / 2, -(maxHeight - (int) newHeight) / 2, null);
                    storeLastRenderedImage(bufferedImage);
                }
                else if(contentImage != null) {
                    g.drawImage(getContentImage(), -(maxWidth - (int) newWidth) / 2, -(maxHeight - (int) newHeight) / 2, null);
                }
            }
        }, BorderLayout.CENTER);
        body.revalidate();
        if(ge instanceof GEProperties && ((GEProperties)ge).isAlwaysCentered()){
            //Take account of the border width (2 pixels).
            this.setPreferredSize(new Dimension((int) newWidth + 2, (int) newHeight + 2));
        }
        else {
            //Take account of the border width (2 pixels).
            Point p = new Point(ge.getX() + (ge.getWidth() - (int) newWidth) / 2, ge.getY() + (ge.getHeight() - (int) newHeight) / 2);
            p = mainController.getMainWindow().getCompositionArea().documentPointToLayeredPanePoint(p);
            //Take account of the border width (2 pixels).
            this.setBounds(p.x, p.y, (int) newWidth + 2, (int) newHeight + 2);
        }
        body.setOpaque(false);
        setBorders();
    }

    /**
     * Draw borders if the CompositionJPanel is selected.
     */
    private void setBorders() {
        if(!(ge instanceof Document)) {
            if (selected)
                this.setBorder(BorderFactory.createMatteBorder(2, 2, 2, 2, Color.ORANGE));
            else
                this.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        }
    }

    /**
     * Return the WaitLayerUI used to display animation over the CompositionJPanel.
     * @return The WaitLayerUI of the CompositionJPanel.
     */
    public WaitLayerUI getWaitLayer() {
        return waitLayer;
    }

    /**
     * Select or unselect the panel on click.
     * @param me Mouse Event.
     */
    public void mouseClicked(MouseEvent me) {
        //If double click
        if(me.getClickCount()==2)
            mainController.getUIController().showGEProperties(ge);
        //If simple click
        if(selected)
            mainController.unselectGE(ge);
        else
            mainController.selectGE(ge);

        this.moveDirection = NONE;
        this.moveMode=MoveMode.NONE;
        mainController.getMainWindow().getCompositionArea().getOverlay().setMode(CompositionAreaOverlay.Mode.NONE);
    }

    /**
     * Set the move mode of the .action and the start point where the mouse is pressed.
     * @param me Mouse Event.
     */
    public void mousePressed(MouseEvent me) {
        drag = new Dimension(0, 0);
        //Sets the mouse move mode
        if(me.isShiftDown()) moveMode = MoveMode.SHIFT;
        else if(me.isAltGraphDown()) moveMode = MoveMode.ALTGRAPH;
        else if(me.isControlDown()) moveMode = MoveMode.CTRL;
        else moveMode = MoveMode.NONE;

        double rad = Math.toRadians(ge.getRotation());
        double newWidth = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
        double newHeight = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());

        //Gets the start point of the mouse movement on the screen and store it
        startPoint = me.getLocationOnScreen();

        //Sets the mouse move direction and the start corner of the action (for the overlay)
        Point start = new Point(this.getLocationOnScreen());
        if(me.getY() <= margin) {
            moveDirection |= TOP;
            start.x=this.getLocationOnScreen().x+this.getWidth()-1;
            start.y=this.getLocationOnScreen().y+this.getHeight()-1;
        }
        if(me.getX() <= margin) {
            moveDirection |= LEFT;
            start.x=this.getLocationOnScreen().x+this.getWidth() -1;
            start.y=this.getLocationOnScreen().y+this.getHeight()-1;
        }
        if(me.getX() >= newWidth - margin) {
            moveDirection |= RIGHT;
            start.x = this.getLocationOnScreen().x;
        }
        if(me.getY() >= newHeight-margin) {
            moveDirection |= BOTTOM;
            start.y = this.getLocationOnScreen().y;
        }
        if(moveDirection == NONE)
            moveDirection = CENTER;

        if(moveDirection != CENTER){
            start = mainController.getMainWindow().getCompositionArea().screenPointToOverlayPoint(start);
            mainController.getMainWindow().getCompositionArea().getOverlay().setMode(CompositionAreaOverlay.Mode.RESIZE_GE);
            mainController.getMainWindow().getCompositionArea().getOverlay().setStart(start);
        }
    }

    /**
     * Called when the mouse is released.
     * It transfer the location of the mouse to the right method according to the mouseMode value;
     */
    public void mouseReleasedHub(){
        GraphicalElement ge = this.ge.deepCopy();
        //If the mouse didn't moved, it means the user had clicked, so skips the mouse released actions.
        if(drag.width==0 && drag.height == 0)
            return;
        if(moveDirection==CENTER) {
            ge.setX(ge.getX() + drag.width);
            ge.setY(ge.getY() + drag.height);
        }
        else {
            switch (moveMode) {
                case ALTGRAPH:
                    mouseReleasedALTGRAPH(ge);
                    break;
                case CTRL:
                case SHIFT:
                    mouseReleasedSHIFT(ge);
                    break;
                case NONE:
                    mouseReleasedNONE(ge);
                    break;
            }
        }
        this.moveDirection = NONE;
        this.moveMode = MoveMode.NONE;
        mainController.modifyGE(this.ge, ge);
        mainController.getMainWindow().getCompositionArea().getOverlay().setMode(CompositionAreaOverlay.Mode.NONE);
        mainController.getUIController().refreshSpin();
    }

    /**
     * Move and resize of the GraphicalElement when no key are pressed.
     * Sets the new dimension and position of the ge when the mouse is released.
     */
    private void mouseReleasedNONE(GraphicalElement ge) {
        if((moveDirection&TOP) != 0) {
            ge.setHeight(Math.abs(-drag.height + ge.getHeight()));
            ge.setY(ge.getY() + drag.height);
        }
        if((moveDirection&LEFT) != 0) {
            ge.setWidth(Math.abs(-drag.width + ge.getWidth()));
            ge.setX(ge.getX() + drag.width);
        }
        if((moveDirection&BOTTOM) != 0) {
            ge.setHeight(Math.abs(-(- drag.height) + ge.getHeight()));
        }
        if((moveDirection&RIGHT) != 0) {
            ge.setWidth(Math.abs(-(-drag.width) + ge.getWidth()));
        }
    }

    /**
     * Move and resize of the GraphicalElement when the key SHIFT is pressed.
     * Resize the GraphicalElement (like in mouseReleasedNONE()) but keep the image width/height ratio
     */
    private void mouseReleasedSHIFT(GraphicalElement ge) {
        float ratio = ((float)ge.getHeight())/ge.getWidth();
        switch(moveDirection){
            case TOP:
                //Set the new height
                ge.setHeight(Math.abs(-drag.height + ge.getHeight()));
                ge.setY(ge.getY() + drag.height);
                //Adapt the width
                ge.setX(ge.getX() - (int) (ge.getHeight() / ratio - ge.getWidth()));
                ge.setWidth((int) (ge.getHeight() / ratio));
                break;
            case TOP|LEFT:
                //test if the new width corresponding to the new height is wider the the new width
                if(Math.abs(-drag.height + ge.getHeight())/ratio > Math.abs(-drag.width+ge.getWidth())){
                    //Set the new height
                    ge.setHeight(Math.abs(-drag.height + ge.getHeight()));
                    ge.setY(ge.getY() + drag.height);
                    //Adapt the width
                    ge.setX(ge.getX() - (int)(ge.getHeight()/ratio -ge.getWidth()));
                    ge.setWidth((int) (ge.getHeight() / ratio));
                }
                else{
                    //Set the new height
                    ge.setWidth(Math.abs(-drag.width + ge.getWidth()));
                    ge.setX(ge.getX() + drag.width);
                    //Adapt the width
                    ge.setY(ge.getY() - (int) (ge.getWidth() * ratio - ge.getHeight()));
                    ge.setHeight((int) (ge.getWidth() * ratio));
                }
                break;
            case LEFT:
                //Set the new width
                ge.setWidth(Math.abs(-drag.width + ge.getWidth()));
                ge.setX(ge.getX() + drag.width);
                //Adapt the height
                ge.setY(ge.getY() - (int)(ge.getWidth()*ratio -ge.getHeight()));
                ge.setHeight((int) (ge.getWidth() * ratio));
                break;
            case BOTTOM|LEFT:
                //test if the new width corresponding to the new height is wider the the new width
                if(Math.abs(drag.getHeight() + ge.getHeight())/ratio > Math.abs(-drag.width+ge.getWidth())){
                    //Set the new height
                    ge.setHeight((int)Math.abs(drag.getHeight()+ge.getHeight()));
                    //Adapt the width
                    ge.setX(ge.getX() - (int)(ge.getHeight()/ratio -ge.getWidth()));
                    ge.setWidth((int) (ge.getHeight() / ratio));
                }
                else{
                    //Set the new width
                    ge.setWidth(Math.abs(-drag.width + ge.getWidth()));
                    ge.setX(ge.getX() + drag.width);
                    //Adapt the height
                    ge.setHeight((int) (ge.getWidth() * ratio));
                }
                break;
            case BOTTOM:
                //Set the new height
                ge.setHeight((int)Math.abs(drag.getHeight()+ge.getHeight()));
                //Adapt the width
                ge.setWidth((int) (ge.getHeight() / ratio));
                break;
            case BOTTOM|RIGHT:
                //test if the new width corresponding to the new height is wider the the new width
                if(Math.abs(drag.getHeight() + ge.getHeight())/ratio > Math.abs(-(-drag.width)+ge.getWidth())){
                    //Set the new height
                    ge.setHeight((int)Math.abs(drag.getHeight()+ge.getHeight()));
                    //Adapt the width
                    ge.setWidth((int) (ge.getHeight() / ratio));
                    break;
                }
                else{
                    //Set the new width
                    ge.setWidth(Math.abs(drag.width+ge.getWidth()));
                    //Adapt the height
                    ge.setHeight((int) (ge.getWidth() * ratio));
                }
                break;
            case RIGHT:
                //Set the new width
                ge.setWidth(Math.abs(drag.width+ge.getWidth()));
                //Adapt the height
                ge.setHeight((int) (ge.getWidth() * ratio));
                break;
            case TOP|RIGHT :
                //test if the new width corresponding to the new height is wider the the new width
                if(Math.abs(-drag.height + ge.getHeight())/ratio > Math.abs(drag.width+ge.getWidth())){
                    //Set the new height
                    ge.setHeight(Math.abs(-drag.height + ge.getHeight()));
                    ge.setY(ge.getY() + drag.height);
                    //Adapt the width
                    ge.setWidth((int) (ge.getHeight() / ratio));
                }
                else{
                    //Set the new width
                    ge.setWidth(Math.abs(drag.width + ge.getWidth()));
                    //Adapt the height
                    ge.setY(ge.getY() - (int) (ge.getWidth() * ratio - ge.getHeight()));
                    ge.setHeight((int) (ge.getWidth() * ratio));
                }
                break;
        }
    }

    /**
     * Move and resize of the GraphicalElement when the key ALTGRAPH is pressed.
     */
    private void mouseReleasedALTGRAPH(GraphicalElement ge) {
        Point point = new Point(this.getWidth(), this.getHeight());

        //Get the dimension of the actual margin between the GE size and the CompositionJPanel size
        Dimension actualMargin = new Dimension((ge.getWidth() - this.getWidth()) / 2, (ge.getHeight() - this.getHeight()) / 2);

        //Store into point the modification done
        if((moveDirection&TOP) !=  0)
            point.y = -drag.height + this.getHeight();
        if((moveDirection&LEFT) !=  0)
            point.x = -drag.width + this.getWidth();
        if((moveDirection&BOTTOM) !=  0)
            point.y = drag.height + this.getHeight();
        if((moveDirection&RIGHT) !=  0)
            point.x = drag.width + this.getWidth();

        //Convert the new width and height of the resize CompositionJPanel into the corresponding GraphicalElement width and height
        point = panelToGE(point);
        //Test if the resize does not stretch too much the body (GE width or height under twice the margin size)
        if((point.x>=margin*2) && (point.y>=margin*2)) {
            //Set the GraphicalElement new width and height taking into account of the border width
            ge.setWidth(Math.abs(point.x));
            ge.setHeight(Math.abs(point.y));
        }

        //Get the dimension of the future (after resizing) margin between the GE size and the CompositionJPanel size
        double rad = Math.toRadians(ge.getRotation());
        //Width and Height of the rectangle containing the rotated bufferedImage
        double newWidth = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
        double newHeight = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());
        Dimension futureMargin = new Dimension((ge.getWidth() - (int) newWidth) / 2, (ge.getHeight() - (int) newHeight) / 2);

        //Adds to the position the variation of margin to ensure that the CompositionJPanel won't move.
        ge.setY(ge.getY() + actualMargin.height - futureMargin.height);
        ge.setX(ge.getX() + actualMargin.width - futureMargin.width);
        //If it's needed, move the panel to follow the mouse.
        if((moveDirection&TOP) !=  0)
            ge.setY(ge.getY() + ((int) (this.getHeight() - newHeight)));
        if((moveDirection&LEFT) !=  0)
            ge.setX(ge.getX()+((int)(this.getWidth()-newWidth)));
    }

    /**
     * Refresh the panel position when the mouse is dragged dragged.
     * @param mouseEvent MouseEvent
     */
    public void mouseDragged(MouseEvent mouseEvent) {
        Point p = mouseEvent.getLocationOnScreen();

        if(mouseEvent.isShiftDown()) moveMode = MoveMode.SHIFT;
        else if(mouseEvent.isAltGraphDown()) moveMode = MoveMode.ALTGRAPH;
        else if(mouseEvent.isControlDown()) moveMode = MoveMode.CTRL;
        else moveMode = MoveMode.NONE;

        //if the user just want to move the element
        if (moveDirection == CENTER) {
            //Move the panel to its new location
            if(this.selected) {
                for (GraphicalElement ge : mainController.getGEController().getSelectedGE()) {
                    JComponent component = mainController.getCompositionAreaController().getCompositionJPanel(ge);
                    component.setLocation(component.getX() + p.x - startPoint.x, component.getY() + p.y - startPoint.y);
                }
            }
            else {
                this.setLocation(this.getX() + p.x - startPoint.x, this.getY() + p.y - startPoint.y);
            }
        }
        //If the user is resizing the element
        else{
            Point end =p;
            //If the user want to resize by saving the element width/height ratio
            if(moveMode==MoveMode.SHIFT){
                float ratio = (float)this.getHeight()/this.getWidth();
                int x = end.x, y = end.y;
                int width, height;
                switch (moveDirection){
                    case BOTTOM:
                    case TOP :
                        x = (this.getLocationOnScreen().x+this.getWidth())+(int)((end.y-(this.getLocationOnScreen().y+this.getHeight()))/ratio);
                        break;
                    case RIGHT:
                    case LEFT:
                        y = (this.getLocationOnScreen().y+this.getHeight())+(int)((end.x-(this.getLocationOnScreen().x+this.getWidth()))*ratio);
                        break;
                    case BOTTOM|LEFT:
                        width = Math.abs(end.x-(this.getLocationOnScreen().x+this.getWidth()));
                        height = Math.abs(end.y-this.getLocationOnScreen().y);
                        if(width*ratio>height) {
                            x = end.x;
                            y = this.getLocationOnScreen().y+(int)(width*ratio);
                        }
                        else {
                            x = this.getLocationOnScreen().x+this.getWidth()-(int)(height/ratio);
                            y = end.y;
                        }
                        break;
                    case BOTTOM|RIGHT:
                        width = Math.abs(end.x-this.getLocationOnScreen().x);
                        height = Math.abs(end.y-this.getLocationOnScreen().y);
                        if(width*ratio>height) {
                            x = end.x;
                            y = this.getLocationOnScreen().y+(int)(width*ratio);
                        }
                        else {
                            x = this.getLocationOnScreen().x+(int)(height/ratio);
                            y = end.y;
                        }
                        break;
                    case TOP|LEFT:
                        width = Math.abs(end.x-(this.getLocationOnScreen().x+this.getWidth()));
                        height = Math.abs(end.y-(this.getLocationOnScreen().y+this.getHeight()));
                        if(width*ratio>height) {
                            x = end.x;
                            y = this.getLocationOnScreen().y+this.getHeight()-(int)(width*ratio);
                        }
                        else {
                            x = this.getLocationOnScreen().x + this.getWidth() - (int) (height / ratio);
                            y = end.y;
                        }
                        break;
                    case TOP|RIGHT:
                        width = Math.abs(end.x-this.getLocationOnScreen().x);
                        height = Math.abs(end.y-(this.getLocationOnScreen().y+this.getHeight()));
                        if(width*ratio>height) {
                            x = end.x;
                            y = this.getLocationOnScreen().y+this.getHeight()-(int)(width*ratio);
                        }
                        else {
                            x = this.getLocationOnScreen().x+(int)(height/ratio);
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
                        end.x = this.getLocationOnScreen().x;
                        break;
                    case RIGHT:
                        end.y = this.getLocationOnScreen().y+this.getHeight()-1;
                        break;
                    case BOTTOM:
                        end.x = getLocationOnScreen().x+this.getWidth()-1;
                        break;
                    case LEFT:
                        end.y = this.getLocationOnScreen().y;
                        break;
                }
            }
            end = mainController.getMainWindow().getCompositionArea().screenPointToOverlayPoint(end);

            mainController.getMainWindow().getCompositionArea().getOverlay().setEnd(end);
        }

        //Store the mouse point as new start point to avoid redoing a movement.
        drag.width+=p.x - startPoint.x;
        drag.height+=p.y - startPoint.y;
        startPoint=p;
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

    /** Unselect the CompositionJPanel (remove the orange borders). */
    public void unselect(){ this.selected=false; setBorders(); }

    /** Select the CompositionJPanel. */
    public void select(){ this.selected=true; setBorders(); }

    /**
     * Converts point from the CompositionJPanel to the corresponding point in the GraphicalElement (in fact apply the reverse transformation that was applyed to the GE).
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
