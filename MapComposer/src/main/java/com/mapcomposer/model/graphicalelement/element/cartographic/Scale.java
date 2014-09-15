package com.mapcomposer.model.graphicalelement.element.cartographic;

import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.MapImageListCA;
import com.mapcomposer.model.configurationattribute.utils.CAFactory;
import java.util.List;

/**
 * Scale of the map. 
 */
public final class Scale extends SimpleCartoGE{
    
    private final MapImageListCA milka;

    public Scale() {
        super();
        milka = CAFactory.createMapImageListCA("Link to MapImage");
    }
    
    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> ret = super.getAllAttributes();
        ret.add(milka);
        return ret;
    }
    
    /** 
     * Returns the map scale if there is a link to a mapImage, else return 1.
     */
    public double getMapScale(){
        if(milka.getSelected()!=null)
            return milka.getSelected().getMapTransform().getScaleDenominator();
        else
            return 1;
    }
}
