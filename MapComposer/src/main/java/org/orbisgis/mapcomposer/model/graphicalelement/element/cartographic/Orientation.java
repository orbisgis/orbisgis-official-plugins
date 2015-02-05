/*
* MapComposer is an OrbisGIS plugin dedicated to the creation of cartographic
* documents based on OrbisGIS results.
*
* This plugin is developed at French IRSTV institute as part of the MApUCE project,
* funded by the French Agence Nationale de la Recherche (ANR) under contract ANR-13-VBDU-0004.
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

package org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic;

import java.util.ArrayList;
import java.util.List;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceCA;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

/**
 * This class represent the arrow giving the orientation of the map.
 * The user can specify the picture to use as icon.
 *
 * @author Sylvain PALOMINOS
 */
public class Orientation extends SimpleCartoGE{
    
    /** Icon of the orientation*/
    private SourceCA icon;

    /** Object for the translation*/
    private static final I18n i18n = I18nFactory.getI18n(Orientation.class);

    /** Displayed ame of the path to the icon*/
    private static final String sIcon = i18n.tr("Path");

    /**Main constructor*/
    public Orientation(){
        super();
        icon = new SourceCA(sIcon, false);
    }

    /**
     * Returns the icon source path.
     * @return The icon source path.
     */
    public String getIconPath() {return icon.getValue();}
    
    /**
     * Sets the icon source path.
     * @param path New path of the icon.
     */
    public void setIconPath(String path) {
        icon.setValue(path);
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.addAll(super.getAllAttributes());
        list.add(icon);
        return list;
    }

    @Override
    public List<ConfigurationAttribute> getSavableAttributes() {
        List<ConfigurationAttribute> list = super.getSavableAttributes();
        list.addAll(super.getAllAttributes());
        list.add(icon);
        return list;
    }

    @Override
    public void setAttribute(ConfigurationAttribute ca) {
        super.setAttribute(ca);
        if(ca.getName().equals(icon.getName()))
            icon = (SourceCA) ca;
    }

    @Override
    public GraphicalElement deepCopy() {
        Orientation copy = (Orientation)super.deepCopy();
        copy.icon = (SourceCA) this.icon.deepCopy();
        return copy;
    }
}
