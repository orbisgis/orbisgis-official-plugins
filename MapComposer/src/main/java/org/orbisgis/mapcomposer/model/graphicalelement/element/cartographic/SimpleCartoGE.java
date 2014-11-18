package org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic;

import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.CartographicElement;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.OwsContextCA;
import org.orbisgis.mapcomposer.model.configurationattribute.utils.CAFactory;
import org.orbisgis.mapcomposer.model.graphicalelement.element.SimpleGE;
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

    @Override public OwsMapContext getOwsMapContext()   {return owsc.getOwsMapContext();}
    @Override public String getOwsPath()                {return owsc.getSelected();}
    @Override public void setOwsContext(String owsContext){this.owsc.select(owsContext);}

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.add(owsc);
        return list;
    }

    @Override
    public List<ConfigurationAttribute> getSavableAttributes() {
        List<ConfigurationAttribute> list = super.getSavableAttributes();
        list.add(owsc);
        return list;
    }

    @Override
    public void setAttribute(ConfigurationAttribute ca) {
        super.setAttribute(ca);
        if(ca.getName().equals(owsc.getName()))
            owsc=(OwsContextCA)ca;
    }
}
