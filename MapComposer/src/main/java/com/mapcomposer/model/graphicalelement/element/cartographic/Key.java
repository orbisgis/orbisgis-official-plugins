package com.mapcomposer.model.graphicalelement.element.cartographic;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import java.util.List;

/**
 * Key of a map. Actually not supported because the informations aren't well defined into the OWS-C.
 */
public final class Key extends CartographicElement{

    /**Main constructor*/
    public Key(){
        super();
    }
    
    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        return super.getAllAttributes();
    }
}
