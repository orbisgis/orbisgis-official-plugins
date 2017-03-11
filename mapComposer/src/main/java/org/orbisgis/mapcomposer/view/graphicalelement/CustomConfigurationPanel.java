/*
* MapComposer is an OrbisGIS plugin dedicated to the creation of cartographic
* documents.
*
* This plugin was firstly developed  at French IRSTV institute as part of the MApUCE project,
* funded by the French Agence Nationale de la Recherche (ANR) under contract ANR-13-VBDU-0004.
* 
* Since 2015, MapComposer is developed and maintened by the GIS group of the DECIDE team of the 
* Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
*
* The GIS group of the DECIDE team is located at :
*
* Laboratoire Lab-STICC – CNRS UMR 6285
* Equipe DECIDE
* UNIVERSITÉ DE BRETAGNE-SUD
* Institut Universitaire de Technologie de Vannes
* 8, Rue Montaigne - BP 561 56017 Vannes Cedex
*
* Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
* Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
*
* The MapComposer plugin is distributed under GPL 3 license. 
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

import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.sif.UIPanel;

import java.util.List;

/**
 * This interface for the GERenderers allow to customize the configuration panel displayed on configuration a GraphicalElement.
 *
 * @author Sylvain PALOMINOS
 */

public interface CustomConfigurationPanel {

    /**
     * This methods allow to create a custom UIPanel displayed in the configuration dialog.
     *
     * @param caList List of ConfigurationAttribute to configure.
     * @param uic UIController.
     * @param enableLock If true, checkboxes enabling and disabling the ConfigurationAttributes configuration are displayed. They aren't if false;
     * @return Null to use the default UIPanel constitution, not null to use a custom one.
     */
    public UIPanel createConfigurationPanel(List<ConfigurationAttribute> caList, MainController uic, boolean enableLock);
}
