package org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.MapImageListCA;

import java.util.List;

/**
 * This class represent the scale of a map. It extends the SimpleCartoGE interface.
 */
public class Scale extends SimpleCartoGE{
    
    /**Link to the MapImage*/
    private MapImageListCA milka;

    /**
     * Main constructor.
     */
    public Scale() {
        super();
        milka = new MapImageListCA("Link to MapImage", false);
    }
    
    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> ret = super.getAllAttributes();
        ret.add(milka);
        return ret;
    }

    @Override
    public List<ConfigurationAttribute> getSavableAttributes() {
        List<ConfigurationAttribute> list = super.getSavableAttributes();
        list.add(milka);
        return list;
    }
    
    /** 
     * Returns the map scale if there is a link to a mapImage, else return 1.
     */
    public double getMapScale(){
        if(milka.getSelected()!=null)
            return milka.getSelected().getMapTransform().getScaleDenominator();
        return 1;
    }

    @Override
    public void setAttribute(ConfigurationAttribute ca) {
        super.setAttribute(ca);
        if(ca.getName().equals(milka.getName()))
            milka=(MapImageListCA)ca;
    }
}
