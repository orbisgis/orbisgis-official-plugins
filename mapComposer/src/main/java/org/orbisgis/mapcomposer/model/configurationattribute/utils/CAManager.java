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

package org.orbisgis.mapcomposer.model.configurationattribute.utils;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;

import java.util.*;

import org.orbisgis.mapcomposer.model.configurationattribute.attribute.ColorCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.FileListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.IntegerCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.MapImageListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.OwsContextCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA;
import org.orbisgis.mapcomposer.view.configurationattribute.CARenderer;
import org.orbisgis.mapcomposer.view.configurationattribute.SourceListRenderer;
import org.orbisgis.mapcomposer.view.configurationattribute.ColorRenderer;
import org.orbisgis.mapcomposer.view.configurationattribute.FileListRenderer;
import org.orbisgis.mapcomposer.view.configurationattribute.MapImageListRenderer;
import org.orbisgis.mapcomposer.view.configurationattribute.IntegerRenderer;
import org.orbisgis.mapcomposer.view.configurationattribute.OwsContextRenderer;
import org.orbisgis.mapcomposer.view.configurationattribute.SourceRenderer;
import org.orbisgis.mapcomposer.view.configurationattribute.StringRenderer;

/**
* The class manages the link between the ConfigurationAttribute (CA) and their Renderer.
* When a CA need to be displayed, the Renderer will be get via this class.
* So to be used, both should be registered in the CAManager.
*/

/**
 * This class contain all the ConfigurationAttribute used in the MapComposer. It also manages the link between the ConfigurationAttributes and their Renderer.
 * To be used, a ConfigurationAttribute should be register with its renderer into this class to be recognise by the composer.
 * Each GraphicalElement or ConfigurationAttribute which is not register won't be used.
 *
 * @author Sylvain PALOMINOS
 */
public class CAManager {

    /** HashMap linking the CA to its Renderer */
    private static Map<Class<? extends ConfigurationAttribute>, CARenderer> map;
    
    /**
    * Main constructor.
    */
    public CAManager(){
        map = new HashMap<>();
        //Adding the original CA and their Renderer
        map.put(IntegerCA.class, new IntegerRenderer());
        map.put(SourceListCA.class, new SourceListRenderer());
        map.put(FileListCA.class, new FileListRenderer());
        map.put(SourceCA.class, new SourceRenderer());
        map.put(StringCA.class, new StringRenderer());
        map.put(OwsContextCA.class, new OwsContextRenderer());
        map.put(ColorCA.class, new ColorRenderer());
        map.put(MapImageListCA.class, new MapImageListRenderer());
    }
    
    /**
    * Register in the Map a CA and it's Renderer. This step is essential because a CA
    * of a graphical element can't be displayed if it isn't added with its Renderer this way.
    * @param caClass Class of the CA.
    * @param rendererClass Renderer class associated with the previous class.
    * @return True if the values are successfully added, false otherwise.
    */
    public boolean registerCA(Class<? extends ConfigurationAttribute> caClass , CARenderer rendererClass){
        if(caClass != null && rendererClass != null){
            map.put(caClass, rendererClass);
            return true;
        }else{
            return false;
        }
    }
    
    /**
    * Give back the Renderer corresponding to the CA given as parameter.
    * @param ca ConfigurationAttribute to render.
    * @return The Renderer of the ConfigurationAttribute.
    */
    public CARenderer getRenderer(ConfigurationAttribute ca){
        return map.get(ca.getClass());
    }



    /**
     * Return the list of all the previously registered GraphicalElement classes.
     * @return List of the GE.
     */
    public List<Class<? extends ConfigurationAttribute>> getRegisteredGEClasses(){
        List<Class<? extends ConfigurationAttribute>> list = new ArrayList<>();
        Iterator<Map.Entry<Class<? extends ConfigurationAttribute>, CARenderer>> it = map.entrySet().iterator();
        while(it.hasNext())
            list.add(it.next().getKey());
        return list;
    }
}