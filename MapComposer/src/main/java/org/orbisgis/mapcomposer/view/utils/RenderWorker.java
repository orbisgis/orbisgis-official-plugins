package org.orbisgis.mapcomposer.view.utils;

import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.view.graphicalelement.GERenderer;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * This class extends the SwingWorker and is used to do the rendering of a GraphicalElement without freezing the MapComposer.
 */
public class RenderWorker extends SwingWorker{

    private CompositionJPanel compPanel;
    private GERenderer geRenderer;
    private GraphicalElement ge;

    /**
     * Main Constructor
     * @param compPanel CompositionPanel where the GraphicalElement should be rendered.
     * @param geRenderer Renderer to use to render the GraphicalElement.
     * @param ge GraphicalElement to render
     */
    public RenderWorker(CompositionJPanel compPanel, GERenderer geRenderer, GraphicalElement ge){
        this.compPanel = compPanel;
        this.geRenderer = geRenderer;
        this.ge = ge;
    }

    @Override
    protected Object doInBackground() throws Exception {
        //Display the wait layer in the CompositionJPanel
        compPanel.setEnabled(false);
        compPanel.getWaitLayer().start();
        BufferedImage bi = geRenderer.createImageFromGE(ge);
        compPanel.redraw(ge.getX(), ge.getY(), ge.getWidth(), ge.getHeight(), ge.getRotation(), bi);
        compPanel.getWaitLayer().stop();
        compPanel.setEnabled(true);
        return null;
    }
}

