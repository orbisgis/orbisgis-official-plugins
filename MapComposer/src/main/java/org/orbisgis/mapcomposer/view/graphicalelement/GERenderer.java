package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;

/**
 * Base interface for the implementation of a render class associated to a GraphicalElement.
 * It's composed of two methods :
 *  - JPanel render(final GraphicalElement) apply the GraphicalElement properties to a BufferedImage (the one from the getContentImage() methd) like the rotation or the dimension and return a JPanel.
 *  - BufferedImage getContentImage(GraphicalElement) which create the visual render of the GraphicalElement (the GraphicalElement image).
 */
public interface  GERenderer {
    /**
     * Transform the BufferedImage from the getContentImage() method , draw it into a panel, size it following the properties of the GraphicalElement.
     * At the end the panel is returned.
     * @param ge GraphicalElement to render.
     * @return JPanel of the GraphicalElement.
     */
    public JPanel render(final GraphicalElement ge);

    /**
     * This method returns the the graphical representation, the image of the GraphicalElement.
     * It doesn't take account of the physical properties of the GraphicalElement like the size, the position ...
     * @param ge GraphicalElement to display
     * @return The buffered image corresponding to the GraphicalElement
     */
    public BufferedImage getContentImage(GraphicalElement ge);
}
