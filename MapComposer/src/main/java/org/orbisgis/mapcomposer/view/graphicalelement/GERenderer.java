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

package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.sif.UIPanel;

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

    /**
     * This methods allow to create a custom UIPanel displayed in the configuration dialog.
     * By default this method must return null to indicate to the application the it should use the default UIPanel.
     * @param ge GraphicalElement taht configuration is displayed.
     * @return Null to use the default UIPanel constitution, not null to use a custom one.
     */
    public UIPanel createConfigurationPanel(GraphicalElement ge, UIController uic);
}
