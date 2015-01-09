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

package org.orbisgis.mapcomposer.model.configurationattribute.attribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;

import java.util.Map;

/**
 * The Source attribute contain the path to a specified data source like data, image ...
 * It extends the BaseCA abstract class.
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.BaseCA
 */
public class SourceCA extends BaseCA<String>{
    /** Property itself */
    private String value;

    /**
     * Empty constructor used in the SaveHandler, on loading a project save.
     */
    public SourceCA(){
        super("void", false);
        value = "";
    }

    /**
     * Default constructor for the SourceCA.
     * @param name Name of the ConfigurationAttribute
     * @param readOnly Boolean to enable or not the modification of the CA
     */
    public SourceCA(String name, boolean readOnly){
        super(name, readOnly);
        value = "";
    }
    
    @Override public void setValue(String value) {this.value=value;}

    @Override public String getValue() {return value;}

    @Override public boolean isSameValue(ConfigurationAttribute configurationAttribute) {
        return configurationAttribute.getValue().equals(value);
    }

    @Override
    public void setField(String name, String value) {
        super.setField(name, value);
        if(name.equals("value"))
            this.value=value;
    }

    @Override
    public Map<String, Object> getAllFields() {
        Map ret = super.getAllFields();
        ret.put("value", value);
        return ret;
    }
}
