package com.mapcomposer.model.graphicalelement.element.illustration;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import java.util.List;

/**
 * Element displaying data such as tables.
 */
public class Data extends IllustrationElement{

    public Data() {
        super();
    }
    
    /**
     * Clone constructor.
     * @param ge
     */
    public Data(Data ge){
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
