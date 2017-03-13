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

import java.util.Map;

/**
 * The Source attribute contain the path to a specified data source like data, image ...
 * It extends the BaseCA abstract class.
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.BaseCA
 *
 * @author Sylvain PALOMINOS
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

    @Override
    public ConfigurationAttribute deepCopy() {
        SourceCA copy = new SourceCA();
        copy.setValue(this.getValue());
        copy.setName(this.getName());
        copy.setReadOnly(this.getReadOnly());

        return copy;
    }
}
