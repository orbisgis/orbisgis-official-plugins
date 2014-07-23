package com.mapcomposer.model.graphicalelement.element.cartographic;

import java.util.ArrayList;
import java.util.List;
import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.OwsContext;
import com.mapcomposer.model.configurationattribute.attribute.Source;
import com.mapcomposer.model.graphicalelement.GraphicalElement;

/**
 * This class is the root class for each cartographic element based on an OWS-Context.
 */
public abstract class CartographicElement extends GraphicalElement{
    /** OWS-Context source.*/
    private final OwsContext owsc;
    
    /**Main constructor.*/
    public CartographicElement(){
        super();
        this.owsc = new OwsContext("OWS-Context path");
        this.owsc.setValue(".");
    }

    /**
     * Returns the OWS-Context source.
     * @return The OWS-C source string.
     */
    public String getSource() {
        return owsc.getValue();
    }
    
    /**
     * Sets the OWS-Context source.
     * @param owsContext New OWS-Context source.
     */
    public void setSource(String owsContext) {
        this.owsc.setValue(owsContext);
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.addAll(super.getAllAttributes());
        list.add(owsc);
        return list;
    }
}
