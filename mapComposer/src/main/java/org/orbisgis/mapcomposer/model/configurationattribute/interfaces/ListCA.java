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

package org.orbisgis.mapcomposer.model.configurationattribute.interfaces;

import java.util.List;

/**
 * This interface is an extension of the ConfigurationAttribute interface. But instead of representing the property by a single object, the property is represented by a list.
 * It permits to manage the list of values with basic function such as add, remove, select ...
 * The getValue() method inherited from the ConfigurationAttribute interface returns the list and the getSelected() method returns the object of the list that was selected by the select(T) method.
 * An implementation of this interface should contain at least 4 fields :
 *  - The 3 fields from the ConfigurationAttribute interface (property value, property name, read-only)
 *  - An index to know which value from the list is selected
 * @param <T> Type of the values contained in the list.
 * @see org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute
 *
 * @author Sylvain PALOMINOS
 */
public interface ListCA<T> extends ConfigurationAttribute<List<T>> {
    /**
     * Appends the specified value to the end of the value list. 
     * @param value Value to appended to the list.
     */
    public void add(T value);
    
    /**
     * Returns the the selected value. If the index isn't right (out of range, negative), it returns NULL.
     * @return The value selected or null.
     */
    public T getSelected();
    
    /**
     * Selects the value given in parameter and sets the index to the value position in the list.
     * If the value given isn't in the list, the index should be set to -1.
     * @param choice Choice to select.
     */
    public void select(T choice);
    
    
    /**
     * Removes the first occurrence of the specified value from this list, if it is present.
     * If the list does not contain the value, it is unchanged.
     * Returns true if this list contained the specified value (or equivalently, if this list changed as a result of the call). 
     * @param value Value to remove from the list.
     * @return True if this list contained the specified value, false otherwise.
     */
    public boolean remove(T value);
}
