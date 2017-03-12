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

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;

import java.util.List;

/**
 * Root interface for the GraphicalElement objects.
 * A GraphicalElement is an visual object which data are represented graphically. All it's fields are represented by ConfigurationAttribute object.
 * A basic GraphicalElement contains at least 5 ConfigurationAttributes plus one integer
 *  - The x position on the screen (IntegerCA)
 *  - The y position on the screen (IntegerCA)
 *  - The height (IntegerCA)
 *  - The width (IntegerCA)
 *  - The rotation angle (IntegerCA)
 *  and :
 *  - The z index (int). This index indicate if an object is over or under an other one.
 *
 *  All of those fields are associated to setters and getters.
 *
 * @author Sylvain PALOMINOS
 */
public interface GraphicalElement {

    /** This enumeration permit to identify the basic field of a GraphicalElement. */
    public enum Property{X, Y, WIDTH, HEIGHT, ROTATION;}
    
    /**
     * Sets the x position.
     * @param x The x position of the GE.
     */
    public void setX(int x);

    /**
     * Sets the y position.
     * @param y The y position of the GE.
     */
    public void setY(int y);
    
    /**
     * Sets the z position.
     * Only used on saveProject and loadProject to keep the z position.
     * @param z The z position.
     */
    public void setZ(int z);

    /**
     * Sets the rotation angle.
     * @param rotation The rotation angle of the GE.
     */
    public void setRotation(int rotation);

    /**
     * Sets the height.
     * @param height The height of the GE.
     */
    public void setHeight(int height);

    /**
     * Sets the width.
     * @param width The width of the GE.
     */
    public void setWidth(int width);

    /**
     * Gets the x position.
     * @return The x position of the GE.
     */
    public int getX();

    /**
     * Gets the y position.
     * @return The y position of the GE.
     */
    public int getY();
    
    /**
     * Returns the z position.
     * Only used on saveProject and loadProject to keep the z position.
     * @return The z position.
     */
    public int getZ();

    /**
     * Gets the rotation angle.
     * @return The rotation angle of the GE.
     */
    public int getRotation();

    /**
     * Gets the height.
     * @return The height of the GE.
     */
    public int getHeight();

    /**
     * Gets the width.
     * @return The width of the GE.
     */
    public int getWidth();

    /**
     * Returns the human readable name of the GraphicalElement.
     * This name is only used to be displayed to the user.
     * @return The name of the GraphicalElement.
     */
    public String getGEName();
    
    /**
     * Returns all the attributes that can be configured by the user EXCEPTS the x and y position, the width, the height and the rotation angle which are directly set thank to a tool bar.
     * All the return attributes will be displayed in a window to permit to the user to configure the GraphicalElement.
     * @return List of all the attributes.
     */
    public List<ConfigurationAttribute> getAllAttributes();

    /**
     * If the GraphicalElement have an attribute which is the same as the one given (same name in facts), set it with the data from the one in argument.
     * This method is used on loading in the SaveHandler to set the created GraphicalElement.
     * @param ca ConfigurationAttribute to copy.
     */
    public void setAttribute(ConfigurationAttribute ca);

    /**
     * Returns all the savable attributes of the GE.
     * Only the ConfigurationAttributes returned by this method will be registered on saving the document project. The other one will be lost on close.
     * @return List of all the ConfigurationAttribute to save.
     */
    public List<ConfigurationAttribute> getSavableAttributes();

    /**
     * Return a deep copy of the GraphicalElement itself.
     * @return A deep copy of the GraphicalElement.
     */
    public GraphicalElement deepCopy();
}
