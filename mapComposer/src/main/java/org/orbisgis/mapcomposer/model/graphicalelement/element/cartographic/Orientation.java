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
    private static final String sIcon = I18n.marktr("Path");

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

    @Override
    public String getGEName(){
        return i18n.tr("Orientation");
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
