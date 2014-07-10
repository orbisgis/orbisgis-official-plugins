package com.mapcomposer.model.graphicalelement.element.illustration;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import java.util.List;

/**
 * Element displaying data such as tables.
 */
public class Data extends IllustrationElement{

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
