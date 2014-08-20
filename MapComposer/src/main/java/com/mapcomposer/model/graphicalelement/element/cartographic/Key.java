package com.mapcomposer.model.graphicalelement.element.cartographic;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import java.util.List;

/**
 * Key of a map. Actually not supported because the informations aren't well defined into the OWS-C.
 */
public final class Key extends CartographicElement{

    public Key(){
        super();
    }
    
    /**
     * Clone constructor.
     * @param ge
     */
    public Key(Key ge){
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
        else if(c.isAssignableFrom(CartographicElement.class))
            return CartographicElement.class;
        else
            return GraphicalElement.class;
    }
    
}
