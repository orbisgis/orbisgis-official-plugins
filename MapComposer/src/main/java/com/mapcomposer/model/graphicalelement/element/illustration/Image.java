package com.mapcomposer.model.graphicalelement.element.illustration;

import com.mapcomposer.Configuration;
import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import java.util.List;

/**
 * Graphical element representing a picture, an image ...
 */
public final class Image extends IllustrationElement {

    public Image() {
        super();
    }
    
    /**
     * Clone constructor.
     * @param ge
     */
    public Image(Image ge){
        super(ge);
    }
    
    
    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        return super.getAllAttributes();
    }
    
    @Override
    public Class<? extends GraphicalElement> getCommonClass(Class<? extends GraphicalElement> c) {
        if(c.isAssignableFrom(this.getClass()))
            return c;
        else if(c.isAssignableFrom(IllustrationElement.class))
            return IllustrationElement.class;
        else
            return GraphicalElement.class;
    }
}
