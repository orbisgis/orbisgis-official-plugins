package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;

import javax.swing.*;

/**
 * This interface defines the createJComponentFromCA function associated to a ConfigurationAttribute (CA).
 * The createJComponentFromCA function return a JComponent containing all swing components (JLabel, JButton, JSpinner ...) necessary to the user to configure the attribute.
 * Thanks to this method, all the ConfigurationAttributes of a GraphicalElement are display into the configuration window.
 * The link between a CA and its Renderer will be done by the CAManager.
 */
public interface CARenderer {
    /**
     * This method defines how the attribute should be displayed into the configuration window.
     * The ConfigurationAttribute should be displayed into swing component and should permit the user to configure the ConfigurationAttribute.
     * The method need to implement a way to get back the value configured by the user.
     * As example, it can use an ActionListener to se the ConfigurationAttribute with the value when the swing component is modified.
     * @param ca Instance of the attribute to createJComponentFromCA.
     * @return JComponent with the representation of the attribute.
     */
    public JComponent createJComponentFromCA(ConfigurationAttribute ca);
}
