package com.mapcomposer.model.graphicalelement.element.cartographic;

import com.mapcomposer.model.graphicalelement.interfaces.CartographicElement;
import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.OwsContext;
import com.mapcomposer.model.graphicalelement.element.SimpleGE;
import java.util.ArrayList;
import java.util.List;
import org.orbisgis.coremap.layerModel.OwsMapContext;

/**
 * Simple implementation of the CartographicElement.
 */
public class SimpleCartoGE extends SimpleGE implements CartographicElement{
    /** OWS-Context source.*/
    private final OwsContext owsc;
    
    /**Main constructor.*/
    public SimpleCartoGE(){
        super();
        this.owsc = new OwsContext("OWS-Context path");
    }

    /**
     * Returns the OwsMapContext.
     * @return The OwsMapContext.
     */
    @Override
    public OwsMapContext getOwsMapContext() {
        return owsc.getOwsContext();
    }

    /**
     * Returns the OwsContext path.
     * @return The OwsContext path.
     */
    @Override
    public String getOwsPath() {
        return owsc.getValue();
    }
    
    /**
     * Sets the OwsContext path.
     * @param owsContext New OwsContext path.
     */
    @Override
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
}
