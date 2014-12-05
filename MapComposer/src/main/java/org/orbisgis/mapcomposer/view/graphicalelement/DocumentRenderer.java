package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Renderer associated to the Document GraphicalElement.
 */
public class DocumentRenderer extends SimpleGERenderer {

    @Override
    public BufferedImage createImageFromGE(GraphicalElement ge) {
        //Returns a white rectangle without applying any rotation
        BufferedImage bi = new BufferedImage(ge.getWidth(), ge.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics = bi.createGraphics();
        graphics.setPaint(new Color(255, 255, 255));
        graphics.fillRect(0, 0, bi.getWidth(), bi.getHeight());
        return bi;
    }
}
