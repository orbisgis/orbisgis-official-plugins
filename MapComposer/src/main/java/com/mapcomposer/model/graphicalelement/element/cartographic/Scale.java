package com.mapcomposer.model.graphicalelement.element.cartographic;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.LinkToMapImage;
import com.mapcomposer.model.graphicalelement.utils.GERefresh;
import java.util.List;
import org.orbisgis.progress.NullProgressMonitor;

/**
 * Scale of the map. 
 */
public final class Scale extends CartographicElement{
    
    private final LinkToMapImage ltmi;

    public Scale() {
        super();
        ltmi = new LinkToMapImage("Link to MapImage");
    }
    
    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> ret = super.getAllAttributes();
        ret.add(ltmi);
        return ret;
    }
    
    public double getMapScale(){
        if(ltmi.getSelected()!=null)
            return ltmi.getSelected().getMapTransform().getScaleDenominator();
        else
            return 1;
    }
}
