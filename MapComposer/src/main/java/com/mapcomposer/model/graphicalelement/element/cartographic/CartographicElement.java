package com.mapcomposer.model.graphicalelement.element.cartographic;

import java.util.ArrayList;
import java.util.List;
import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Source;
import com.mapcomposer.model.graphicalelement.GraphicalElement;

/**
 * This class is the root class for each cartographic element based on an OWS-Context.
 */
public abstract class CartographicElement extends GraphicalElement{
    /** OWS-Context source.*/
    private final Source source;
    
    /**Main constructor.*/
    public CartographicElement(){
        super();
        this.source = new Source("OWS-Context path");
        this.source.setPropertyValue(".");
    }

    /**
     * Returns the OWS-Context source.
     * @return The OWS-C source string.
     */
    public String getSource() {
        return source.getPropertyValue();
    }
    
    /**
     * Sets the OWS-Context source.
     * @param owsContext New OWS-Context source.
     */
    public void setSource(String owsContext) {
        this.source.setPropertyValue(owsContext);
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.addAll(super.getAllAttributes());
        list.add(source);
        return list;
    }
}
