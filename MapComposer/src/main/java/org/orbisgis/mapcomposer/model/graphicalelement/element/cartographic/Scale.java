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

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.MapImageListCA;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import java.util.List;

/**
 * This class represent the scale of a map. It extends the SimpleCartoGE interface.
 *
 * @author Sylvain PALOMINOS
 */
public class Scale extends SimpleCartoGE{
    
    /**Link to the MapImage*/
    private MapImageListCA milka;

    /** Object for the translation*/
    private static final I18n i18n = I18nFactory.getI18n(Scale.class);

    /**Displayed name of the MapImageList ConfigurationAttribute*/
    private static final String sMILKA = I18n.marktr("Link to MapImage");

    /**
     * Main constructor.
     */

    public Scale() {
        super();
        milka = new MapImageListCA(sMILKA, false);
    }

    @Override
    public String getGEName(){
        return i18n.tr("Scale");
    }
    
    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> ret = super.getAllAttributes();
        ret.add(milka);
        return ret;
    }

    @Override
    public List<ConfigurationAttribute> getSavableAttributes() {
        List<ConfigurationAttribute> list = super.getSavableAttributes();
        list.add(milka);
        return list;
    }
    
    /** 
     * Returns the map scale if there is a link to a mapImage, else return 1.
     */
    public double getMapScale(){
        if(milka.getSelected()!=null)
            return milka.getSelected().getMapTransform().getScaleDenominator();
        return 1;
    }

    @Override
    public void setAttribute(ConfigurationAttribute ca) {
        super.setAttribute(ca);
        if(ca.getName().equals(milka.getName()))
            milka=(MapImageListCA)ca;
    }

    @Override
    public GraphicalElement deepCopy() {
        Scale copy = (Scale) super.deepCopy();
        copy.milka = (MapImageListCA)milka.deepCopy();
        return copy;
    }
}
