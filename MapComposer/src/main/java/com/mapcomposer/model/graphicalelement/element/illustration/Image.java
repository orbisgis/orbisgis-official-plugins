package com.mapcomposer.model.graphicalelement.element.illustration;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import java.util.List;

/**
 * Graphical element representing a picture, an image ...
 */
public class Image extends IllustrationElement {

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
