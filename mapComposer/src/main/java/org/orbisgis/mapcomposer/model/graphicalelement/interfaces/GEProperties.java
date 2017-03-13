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

package org.orbisgis.mapcomposer.model.graphicalelement.interfaces;

/**
 * This interface allow to define custom behavior of the GraphicalElement
 *
 * @author Sylvain PALOMINOS
 */
public interface GEProperties {

    /**
     * This method specify if the GraphicalElement needs the previous creation of a Document GE or not.
     * @return True if it need a Document GE, false otherwise.
     */
    public boolean isDocumentNeeded();

    /**
     * This methods specify if the GraphicalElement should be always drawn over all the others.
     * @return True if it will always be on the top, false otherwise.
     */
    public boolean isAlwaysOnTop();

    /**
     * This methods specify if the GraphicalElement should be always drawn under all the others.
     * @return True if it will always be on the back, false otherwise.
     */
    public boolean isAlwaysOnBack();

    /**
     * This method specify if this GraphicalElement should be draw by the user or if its dimension are automatically set.
     * @return True if the user need to draw it, false otherwise.
     */
    public boolean isDrawnByUser();

    /**
     * This method specify if this GraphicalElement should be refreshed (redrawn) after each modification and not just after clicking on the refresh button.
     * @return True if it should be redrawn after each modification.
     */
    public boolean isAlwaysRefreshed();

    /**
     * This method specify if this GraphicalElement should be placed at the center of the CompositionArea.
     * @return True if this element is always placed at the center of the CompositionArea.
     */
    public boolean isAlwaysCentered();

    /**
     * This method specify if this GraphicalElement can be edited by the mouse (moving, resizing, configured) or not.
     * @return True if it can be edited by mouse, false otherwise.
     */
    public boolean isEditedByMouse();
}
