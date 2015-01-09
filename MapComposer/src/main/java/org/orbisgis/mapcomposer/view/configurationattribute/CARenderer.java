/*
* MapComposer is an OrbisGIS plugin dedicated to the creation of cartographic
* documents based on OrbisGIS results.
*
* The MapComposer plugin is distributed under GPL 3 license. It is produced by the "Atelier SIG"
* team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
*
* Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
*
* This file is part of the MapComposer plugin.
*
* The MapComposer plugin is free software: you can redistribute it and/or modify it under the
* terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
*
* The MapComposer plugin is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
* A PARTICULAR PURPOSE. See the GNU General Public License for more details <http://www.gnu.org/licenses/>.
*/

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
