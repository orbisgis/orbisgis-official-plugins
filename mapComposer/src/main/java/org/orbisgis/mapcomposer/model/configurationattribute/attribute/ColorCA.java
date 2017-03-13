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

package org.orbisgis.mapcomposer.model.configurationattribute.attribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import java.awt.Color;
import java.util.Map;

/**
 * This CA represent a color field (like text color or background color) and extends the abstract class BaseCA.
 * On saving, only string type can be used, so the color is saved in a String containing 4 int : red, green, blue, alpha.
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.BaseCA
 *
 * @author Sylvain PALOMINOS
 */
public class ColorCA extends BaseCA<Color> {

    /** Property itself.*/
    private Color color;

    /**
     * Empty constructor used in the SaveHandler, on loading a project save.
     */
    public ColorCA(){
        super("void", false);
        color = Color.GRAY;
    }

    /**
     * Default constructor of the ColorCA.
     * @param name Name of the ConfigurationAttribute
     * @param readOnly Boolean to enable or not the modification of the CA
     * @param color
     */
    public ColorCA(String name, boolean readOnly, Color color){
        super(name, readOnly);
        this.color = color;
    }

    @Override public void setValue(Color value) {
        this.color = value;
    }

    @Override public Color getValue() {return color;}

    @Override public boolean isSameValue(ConfigurationAttribute configurationAttribute) {
        return configurationAttribute.getValue().equals(this.getValue());
    }


    @Override
    public void setField(String name, String value) {
        super.setField(name, value);
        if(name.equals("color")) {
            //Decode the string value of the color from String[] to Color
            String[] s = value.split(",");
            this.color = new Color(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]));
        }
    }

    @Override
    public Map<String, Object> getAllFields() {
        Map ret = super.getAllFields();
        //Save the color field as a String like "r,g,b,a" corresponding ot the red,green,blue,alpha value of the color
        ret.put("color", color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "," + color.getAlpha());
        return ret;
    }

    @Override
    public ConfigurationAttribute deepCopy() {
        ColorCA copy = new ColorCA();
        copy.setValue(new Color(this.getValue().getRed(), this.getValue().getGreen(), this.getValue().getBlue()));
        copy.setName(this.getName());
        copy.setReadOnly(this.getReadOnly());
        return copy;
    }

}
