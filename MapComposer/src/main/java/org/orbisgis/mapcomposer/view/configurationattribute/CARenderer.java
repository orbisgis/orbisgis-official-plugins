package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import javax.swing.JPanel;

/**
 * This interface defines the render function associated to a ConfigurationAttribute (CA).
 * The render function return a JPanel containing all swing components (JLabel, JButton, JSpinner ...) necessary to the user to configure the attribute.
 * Thanks to this method, all the ConfigurationAttributes of a GraphicalElement are display into the configuration window.
 * The link between a CA and its Renderer will be done by the CAManager.
 */
public interface CARenderer {
    /**
     * The Render method defines how the attribute should be displayed into the configuration window.
     * @param ca Instance of the attribute to render.
     * @return JPanel with the representation of the attribute.
     */
    public JPanel render(ConfigurationAttribute ca);
    
    /**
     * Extracts the value contained by the JPanel and set the attribute.
     * On validation the configuration, this method receive the panel given by the render(ConfigurationAttribute) method and extract the value set by the user.
     * The value is saved into the attribute given in argument.
     * @param panel JPanel of the configuration window containing the value.
     * @param attribute ConfigurationAttribute to set with the extracted value.
     */
    public void extractValueFromPanel(JPanel panel, ConfigurationAttribute attribute);
}
