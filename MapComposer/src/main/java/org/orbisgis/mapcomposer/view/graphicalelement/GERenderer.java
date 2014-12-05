package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Base interface for the implementation of a render class associated to a GraphicalElement.
 * It contain a function returning the graphical representation of a GraphicalElement.
 */
public interface  GERenderer {

    /**
     * This method creates and return the the graphical representation, the image of the GraphicalElement.
     * The image created is set with the GraphicalElement attribute to set its size, its rotation and its aspect.
     * @param ge GraphicalElement to display
     * @return The buffered image corresponding to the GraphicalElement
     */
    public BufferedImage createImageFromGE(GraphicalElement ge);
}
