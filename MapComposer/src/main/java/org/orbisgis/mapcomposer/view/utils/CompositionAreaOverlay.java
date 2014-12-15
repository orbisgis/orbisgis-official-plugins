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
    private boolean enable;

    /**
     * Main constructor.
     * @param uiController
     */
    public CompositionAreaOverlay(UIController uiController){
        this.uiController = uiController;
        enable = false;
    }

    /**
     * Enable or disable itself.
     * @param enable
     */
    public void setEnable(boolean enable) {
        System.out.println(enable);
        this.enable = enable;
    }


    @Override
    public void paint (Graphics g, JComponent c) {
        super.paint(g, c);
        if (enable){
            if (start != null && end != null) {
                Graphics2D g2 = (Graphics2D) g.create();
                float[] dash = {10.0f};
                g2.setStroke(new BasicStroke(1.0f,BasicStroke.CAP_BUTT,BasicStroke.JOIN_MITER,10.0f,dash,0.0f));
                int x = (end.x < start.x) ? end.x : start.x;
                int y = (end.y < start.y) ? end.y : start.y;
                g2.drawRect(x, y, Math.abs(end.x - start.x), Math.abs(end.y - start.y));
                g2.dispose();
            }
        }
    }

    @Override
    protected void processMouseMotionEvent(MouseEvent e, JLayer<? extends JComponent> l) {
        //If the LayerUI is disable, it doesn't consume the mouse event.
        if(!enable)
            super.processMouseMotionEvent(e, l);
        else {
            if (e.getID() == MouseEvent.MOUSE_DRAGGED) {
                end = new Point(e.getLocationOnScreen().x - uiController.getMainWindow().getCompositionArea().getLocationOnScreen().x,
                                e.getLocationOnScreen().y - uiController.getMainWindow().getCompositionArea().getLocationOnScreen().y);
                l.repaint();
            }
            e.consume();
        }
    }

    @Override
    protected void processMouseEvent(MouseEvent e, JLayer<? extends JComponent> l) {
        //If the LayerUI is disable, it doesn't consume the mouse event.
        if(!enable)
            super.processMouseEvent(e, l);
        else {
            if (e.getID() == MouseEvent.MOUSE_PRESSED) {
                //Get the click location on the screen an save the location in the compositionArea
                start = new Point(  e.getLocationOnScreen().x - uiController.getMainWindow().getCompositionArea().getLocationOnScreen().x,
                                    e.getLocationOnScreen().y - uiController.getMainWindow().getCompositionArea().getLocationOnScreen().y);
                l.repaint();
            }
            if (e.getID() == MouseEvent.MOUSE_RELEASED) {
                int x = (end.x < start.x) ? end.x : start.x;
                int y = (end.y < start.y) ? end.y : start.y;
                uiController.createGE(x, y, Math.abs(end.x - start.x), Math.abs(end.y - start.y));
                l.repaint();
                start=null;
                end=null;
            }
            e.consume();
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
