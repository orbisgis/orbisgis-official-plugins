package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import javax.swing.JPanel;

/**
 * This interface defines the getRenderer function associated with a ConfigurationATtribute (CA).
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
    
    /**
     * Extracts the value contained by the JPanel and set the attribute.
     * @param panel JPanel of the ConfigurationShutter containing the value.
     * @param attribute ConfigurationAttribute to set.
     */
    public void extractValue(JPanel panel, ConfigurationAttribute attribute);
}
