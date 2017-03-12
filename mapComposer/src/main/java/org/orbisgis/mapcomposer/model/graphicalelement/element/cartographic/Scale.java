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
