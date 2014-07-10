package com.mapcomposer.model.graphicalelement.utils;

import javax.swing.JPanel;
import com.mapcomposer.model.graphicalelement.GraphicalElement;

/**
 * This interface corresponds to a GraphicalElement (GE) and define it's rendering method.
 */
public interface GERenderer {
    /**
     * Render method defines how the GE should be displayed.
     * @param ge Instance of the GE to render.
     * @return JPanel with the representation of the GE.
     */
    public JPanel render(GraphicalElement ge);
}
