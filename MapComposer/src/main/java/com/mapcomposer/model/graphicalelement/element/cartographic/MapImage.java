package com.mapcomposer.model.graphicalelement.element.cartographic;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import java.util.List;

/**
 * Map image generated from an OWS-Context.
 */
public class MapImage extends CartographicElement {

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
