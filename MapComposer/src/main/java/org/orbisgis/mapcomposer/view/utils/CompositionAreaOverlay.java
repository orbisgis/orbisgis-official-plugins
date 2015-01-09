/*
* MapComposer is an OrbisGIS plugin dedicated to the creation of cartographic
* documents based on OrbisGIS results.
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

import org.orbisgis.mapcomposer.controller.UIController;

import javax.swing.*;
import javax.swing.plaf.LayerUI;
import java.awt.*;
import java.awt.event.MouseEvent;

/**
 * This class is used as an overlay to the CompositionArea to permit to draw the bounding box of a new GraphicalElement.
 * It will be enabled only when the user wants to create a new GraphicalElement. So the user can draw the bounding box of the new GraphicalElement.
 * The size will be transfer to the UIController to create the GE.
 */
public class CompositionAreaOverlay extends LayerUI<JComponent>{
    /*Point where the mouse is pressed.*/
    private Point start;
    /*Point where the mouse is released.*/
    private Point end;
    private UIController uiController;

    public enum Mode{NEW_GE, RESIZE_GE, NONE}
    private Mode mode;

    /** This value is the ratio between width and height of the new GraphicalElement (width/height).
     * If it's negative, it means that there is not ratio to respect.
     */
    private float ratio;

    /**
     * Main constructor.
     * @param uiController
     */
    public CompositionAreaOverlay(UIController uiController){
        this.uiController = uiController;
        ratio = -1;
        mode = null;
    }

    /**
     * Set the ratio value to respect on drawing the new GraphicalElement bounding box.
     * @param ratio
     */
    public void setRatio(float ratio) {
        this.ratio = ratio;
    }

    /**
     * Set the start point of the rectangle drawn
     * @param start
     */
    public void setStart(Point start) {
        this.start = start;
    }

    /**
     * Set the end point of the rectangle drawn
     * @param end
     */
    public void setEnd(Point end) {
        this.end = end;
    }

    /**
     * Set the mode of the overlay : NONE if nothing should be drawn, NEW_GE if a new GraphicalElement is drawn by the user, RESIZE_GE if the user is resizing an element.
     * @param mode
     */
    public void setMode(Mode mode) {
        this.mode = mode;
    }

    @Override
    public void paint (Graphics g, JComponent c) {
        super.paint(g, c);
        if (mode == Mode.NEW_GE){
            if (start != null && end != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                float[] dash = {10.0f};
                g2.setStroke(new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f,dash,0.0f));
                int x, y;
                float width, height;
                //If the ratio in negative, there is no need to respect it
                if(ratio<=0) {
                    x = (end.x < start.x) ? end.x : start.x;
                    y = (end.y < start.y) ? end.y : start.y;
                    width = Math.abs(end.x - start.x);
                    height = Math.abs(end.y - start.y);
                }
                //if the ratio is positive, the new GE bounding box have to respect it.
                else{
                    x = (end.x < start.x) ? end.x : start.x;
                    y = (end.y < start.y) ? end.y : start.y;
                    width = (Math.abs(end.x - start.x)>(Math.abs(end.y - start.y)*ratio))?Math.abs(end.x - start.x):Math.abs(end.y - start.y)*ratio;
                    height = width/ratio;

                }
                g2.drawRect(x, y, (int)width, (int)height);
                g2.dispose();
            }
        }
        if (mode == Mode.RESIZE_GE){
            if (start != null && end != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                float[] dash = {10.0f};
                g2.setStroke(new BasicStroke(1.0f, BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER, 10.0f, dash, 0.0f));
                int x, y;
                float width, height;
                //If the ratio in negative, there is no need to respect it
                if(ratio<=0) {
                    x = (end.x < start.x) ? end.x : start.x;
                    y = (end.y < start.y) ? end.y : start.y;
                    width = Math.abs(end.x - start.x);
                    height = Math.abs(end.y - start.y);
                }
                //if the ratio is positive, the new GE bounding box have to respect it.
                else{
                    x = (end.x < start.x) ? end.x : start.x;
                    y = (end.y < start.y) ? end.y : start.y;
                    width = (Math.abs(end.x - start.x)>(Math.abs(end.y - start.y)*ratio))?Math.abs(end.x - start.x):Math.abs(end.y - start.y)*ratio;
                    height = width/ratio;

                }
                g2.drawRect(x, y, (int)width, (int)height);
                g2.dispose();
            }
        }
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e, JLayer<? extends JComponent> l) {
        //If the LayerUI is disable, it doesn't consume the mouse event.
        if(mode == Mode.NONE)
            super.processMouseMotionEvent(e, l);
        else if(mode == Mode.NEW_GE) {
            if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
                end = new Point(e.getLocationOnScreen().x - uiController.getMainWindow().getCompositionArea().getLocationOnScreen().x,
                                e.getLocationOnScreen().y - uiController.getMainWindow().getCompositionArea().getLocationOnScreen().y);
                l.repaint();
            }
            e.consume();
        }
        else if(mode == Mode.RESIZE_GE){
            super.processMouseMotionEvent(e, l);
            l.repaint();
        }
    }

    @Override
    protected void processMouseEvent(MouseEvent e, JLayer<? extends JComponent> l) {
        //If the LayerUI is disable, it doesn't consume the mouse event.
        if(mode == Mode.NONE)
            super.processMouseEvent(e, l);
        else if(mode == Mode.NEW_GE) {
            if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                //Get the click location on the screen an save the location in the compositionArea
                start = new Point(  e.getLocationOnScreen().x - uiController.getMainWindow().getCompositionArea().getLocationOnScreen().x,
                                    e.getLocationOnScreen().y - uiController.getMainWindow().getCompositionArea().getLocationOnScreen().y);
                if(!e.isShiftDown())
                    ratio=-1;
            }
            if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                int x, y;
                float width, height;
                //If the ratio in negative, there is no need to respect it
                if(ratio<=0) {
                    x = (end.x < start.x) ? end.x : start.x;
                    y = (end.y < start.y) ? end.y : start.y;
                    width = Math.abs(end.x - start.x);
                    height = Math.abs(end.y - start.y);
                }
                //if the ratio is positive, the new GE bounding box have to respect it.
                else{
                    x = (end.x < start.x) ? end.x : start.x;
                    y = (end.y < start.y) ? end.y : start.y;
                    width = (Math.abs(end.x - start.x)>(Math.abs(end.y - start.y)*ratio))?Math.abs(end.x - start.x):Math.abs(end.y - start.y)*ratio;
                    height = width/ratio;

                }
                uiController.setNewGE(x, y, (int)width, (int)height);
                start=null;
                end=null;
                ratio=-1;
            }
            e.consume();
        }
        else if(mode == Mode.RESIZE_GE){
            super.processMouseEvent(e, l);
            if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                start = null;
                end = null;
                ratio = -1;
                l.repaint();
            }
        }
    }

    @Override
    public void installUI(JComponent c) {
        super.installUI(c);
        JLayer jlayer = (JLayer)c;
        jlayer.setLayerEventMask( AWTEvent.MOUSE_EVENT_MASK | AWTEvent.MOUSE_MOTION_EVENT_MASK );
    }

    @Override
    public void uninstallUI(JComponent c) {
        JLayer jlayer = (JLayer)c;
        jlayer.setLayerEventMask(0);
        super.uninstallUI(c);
    }
}
