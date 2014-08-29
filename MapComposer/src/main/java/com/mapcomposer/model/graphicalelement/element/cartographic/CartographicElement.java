package com.mapcomposer.model.graphicalelement.element.cartographic;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.OwsContext;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import java.util.ArrayList;
import java.util.List;
import org.orbisgis.coremap.layerModel.OwsMapContext;

/**
 * This class is the root class for each cartographic element based on an OWS-Context.
 */
public class CartographicElement extends GraphicalElement{
    /** OWS-Context source.*/
    private final OwsContext owsc;
    
    /**Main constructor.*/
    public CartographicElement(){
        super();
        this.owsc = new OwsContext("OWS-Context path");
        setDefaultValue();
    }

    /**
     * Returns the OwsMapContext.
     * @return The OwsMapContext.
     */
    public OwsMapContext getOwsMapContext() {
        return owsc.getOwsContext();
    }

    /**
     * Returns the OwsContext path.
     * @return The OwsContext path.
     */
    public String getOwsPath() {
        return owsc.getValue();
    }
    
    /**
     * Sets the OwsContext path.
     * @param owsContext New OwsContext path.
     */
    public void setOwsContext(String owsContext) {
        this.owsc.setValue(owsContext);
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.addAll(super.getAllAttributes());
        list.add(owsc);
        return list;
    }
    
    private void setDefaultValue(){
        //Nothing because the OWSContext initiate itself
    }
    
    @Override
    public void setDefaultElementShutter(){
        super.setDefaultElementShutter();
        //Nothing because the OWSContext initiate itself
    }
}
