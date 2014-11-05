package org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.MapImageListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.utils.CAFactory;
import java.util.List;

/**
 * SimpleScaleGE of the map. 
 */
public class SimpleScaleGE extends SimpleCartoGE{
    
    /**Link to the MapImage*/
    private MapImageListCA milka;

    /**
     * Main constructor.
     */
    public SimpleScaleGE() {
        super();
        milka = CAFactory.createMapImageListCA("Link to MapImage");
    }

    /**
     * Copy constructor.
     * @param ssge SimpleScaleGE to copy.
     */
    public SimpleScaleGE(SimpleScaleGE ssge) {
        super(ssge);
        milka = ssge.milka;
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
