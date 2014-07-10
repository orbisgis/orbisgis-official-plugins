package com.mapcomposer.model.graphicalelement.element.cartographic;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import java.util.List;

/**
 * Scale of the map. 
 */
public class Scale extends CartographicElement{

    @Override
    protected void initialisation() {
        this.setHeight(50);
        this.setWidth(50);
    }
    
    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        return super.getAllAttributes();
    }
}
