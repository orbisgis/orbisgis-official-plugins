package com.mapcomposer.model.graphicalelement.element.cartographic;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import java.util.List;

/**
 * Key of a map. Actually not supported because the informations aren't well defined into the OWS-C.
 */
public class Key extends CartographicElement{

    public Key(){
        super();
    }
    
    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        return super.getAllAttributes();
    }
    
}
