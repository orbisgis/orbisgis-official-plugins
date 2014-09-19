package com.mapcomposer.model.graphicalelement.element.cartographic;

import com.mapcomposer.model.graphicalelement.interfaces.CartographicElement;
import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.OwsContextCA;
import com.mapcomposer.model.configurationattribute.utils.CAFactory;
import com.mapcomposer.model.graphicalelement.element.SimpleGE;
import java.util.ArrayList;
import java.util.List;
import org.orbisgis.coremap.layerModel.OwsMapContext;

/**
 * Simple implementation of the CartographicElement.
 */
public class SimpleCartoGE extends SimpleGE implements CartographicElement{
    /** OWS-Context source.*/
    private OwsContextCA owsc;
    
    /**Main constructor.*/
    public SimpleCartoGE(){
        super();
        this.owsc = CAFactory.createOwsContextCA("OWS-Context path");
    }
    
    /**
     * Copy constructor.
     * @param scge SimpleCartoGE to copy.
     */
    public SimpleCartoGE(SimpleCartoGE scge){
        super(scge);
        this.owsc = scge.owsc;
    }

    @Override public OwsMapContext getOwsMapContext()   {return owsc.getOwsMapContext();}
    @Override public String getOwsPath()                {return owsc.getSelected();}
    @Override public void setOwsContext(String owsContext){this.owsc.select(owsContext);}

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.add(owsc);
        return list;
    }
}
