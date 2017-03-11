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

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the ConfigurationAttribute (CA) interface and is a base for the natives CA.
 * All the methods implemented are common for each native CA.
 * It can also be used as a base to develop custom CA.
 *
 * @author Sylvain PALOMINOS
 */
public abstract class BaseCA<T> implements ConfigurationAttribute<T>{

    /** Name of the property. */
    private String name;

    /** Read-only field. It tells if the value can be modified or not. */
    private boolean readOnly;

    /**
     * Default constructor setting the name and the readOnly mode.
     * @param name Name of the ConfigurationAttribute
     * @param readOnly ReadOnly mode
     */
    public BaseCA(String name, boolean readOnly){
        this.name = name;
        this.readOnly = readOnly;
    }

    @Override public void setReadOnly(boolean readOnly) {this.readOnly = readOnly;}
    @Override public boolean getReadOnly()              {return readOnly;}

    @Override public String getName()           {return name;}
    @Override public void setName(String name)  {this.name = name;}
    
    @Override public boolean isSameName(ConfigurationAttribute configurationAttribute){
        return this.name.equals(configurationAttribute.getName());
    }

    @Override
    public void setField(String name, String value) {
        if(name.equals("name"))
            this.name=value;
        else if(name.equals("readOnly"))
            this.readOnly = Boolean.parseBoolean(value);
    }

    @Override
    public Map<String, Object> getAllFields() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("readOnly", readOnly);
        return map;
    }
}
