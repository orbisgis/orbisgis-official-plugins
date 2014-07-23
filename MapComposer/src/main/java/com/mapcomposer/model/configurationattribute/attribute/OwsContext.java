/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.utils.interfaces.CARefresh;
import org.orbisgis.coremap.layerModel.OwsMapContext;

/**
 * ConfigurationAttribute representing a specified OwsMapContext given by OrbisGIS.
 */
public class OwsContext extends ConfigurationAttribute<OwsMapContext> implements CARefresh{

    public OwsContext(String name) {
        super(name);
    }

    @Override
    public void refresh() {
        //TODO : implement this method
    }
    
}
