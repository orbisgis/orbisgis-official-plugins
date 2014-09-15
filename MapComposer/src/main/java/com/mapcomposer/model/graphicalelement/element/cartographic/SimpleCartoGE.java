package com.mapcomposer.model.graphicalelement.element.cartographic;

import com.mapcomposer.model.graphicalelement.interfaces.CartographicElement;
import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.OwsContextCA;
import com.mapcomposer.model.graphicalelement.element.SimpleGE;
import java.util.ArrayList;
import java.util.List;
import org.orbisgis.coremap.layerModel.OwsMapContext;

/**
 * Simple implementation of the CartographicElement.
 */
public class SimpleCartoGE extends SimpleGE implements CartographicElement{
    /** OWS-Context source.*/
    private final OwsContextCA owsc;
    
    /**Main constructor.*/
    public SimpleCartoGE(){
        super();
        this.owsc = new OwsContextCA();
        this.owsc.setName("OWS-Context path");
    }

    /**
     * Returns the OwsMapContext.
     * @return The OwsMapContext.
     */
    @Override
    public OwsMapContext getOwsMapContext() {
        return owsc.getOwsMapContext();
    }

    /**
     * Returns the OwsContextCA path.
     * @return The OwsContextCA path.
     */
    @Override
    public String getOwsPath() {
        return owsc.getSelected();
    }
    
    /**
     * Sets the OwsContextCA path.
     * @param owsContext New OwsContextCA path.
     */
    @Override
    public void setOwsContext(String owsContext) {
        this.owsc.select(owsContext);
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.addAll(super.getAllAttributes());
        list.add(owsc);
        return list;
    }
}
