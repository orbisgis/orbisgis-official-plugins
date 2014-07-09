package com.mapcomposer.model.configurationattribute.utils;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import javax.swing.JPanel;

/**
 * This interface defines the render function associated with a ConfigurationATtribute (CA).
 * The rendering will be used to display the CAs of a GraphicalElement in the ConfigurationShutter.
 * The link between the CA and its Renderer will be done by the CAManager.
 */
public interface CARenderer {
    /**
     * Render method defines how the attribute should be displayed into the configuration shutter.
     * @param ca Instance of the attribute to render.
     * @return JPanel with the representation of the attribute.
     */
    public JPanel render(ConfigurationAttribute ca);
}
