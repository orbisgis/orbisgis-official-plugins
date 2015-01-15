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

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.CartographicElement;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.OwsContextCA;
import org.orbisgis.mapcomposer.model.graphicalelement.element.SimpleGE;
import org.orbisgis.coremap.layerModel.OwsMapContext;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple implementation of the CartographicElement interface.
 *
 * @author Sylvain PALOMINOS
 */
public abstract class SimpleCartoGE extends SimpleGE implements CartographicElement{

    /** OWS-Context source.*/
    private OwsContextCA owsc;

    public static final String sOWSC = "OWS-Context path";
    
    /**Main constructor.*/
    public SimpleCartoGE(){
        super();
        this.owsc = new OwsContextCA(sOWSC, false);
    }

    @Override public OwsMapContext getOwsMapContext()   {return owsc.getOwsMapContext();}
    @Override public String getOwsPath()                {return owsc.getSelected();}
    @Override public void setOwsContext(String owsContext){this.owsc.select(owsContext);}

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.add(owsc);
        return list;
    }

    @Override
    public List<ConfigurationAttribute> getSavableAttributes() {
        List<ConfigurationAttribute> list = super.getSavableAttributes();
        list.add(owsc);
        return list;
    }

    @Override
    public void setAttribute(ConfigurationAttribute ca) {
        super.setAttribute(ca);
        if(ca.getName().equals(sOWSC))
            owsc=(OwsContextCA)ca;
    }
}
