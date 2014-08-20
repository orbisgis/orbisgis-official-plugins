package com.mapcomposer.model.graphicalelement.element.cartographic;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.model.graphicalelement.utils.GERefresh;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.progress.NullProgressMonitor;

/**
 * Scale of the map. 
 */
public class Scale extends CartographicElement implements GERefresh{
    
    private MapTransform mapTransform;

    public Scale() {
        super();
        mapTransform = new MapTransform();
    }
    
    /**
     * Clone constructor.
     * @param ge
     */
    public Scale(Scale ge){
        super(ge);
        mapTransform = ge.mapTransform;
    }
    
    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        return super.getAllAttributes();
    }

    @Override
    public void refresh() {
        try {
            mapTransform.setExtent(this.getOwsMapContext().getBoundingBox());
            BufferedImage outImage = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_RGB);
            mapTransform.setImage(outImage);
            if(!this.getOwsMapContext().isOpen())
                this.getOwsMapContext().open(new NullProgressMonitor());
            this.getOwsMapContext().draw(mapTransform, new NullProgressMonitor());
        } catch (LayerException ex) {
            Logger.getLogger(Scale.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public double getMapScale(){
        return mapTransform.getScaleDenominator();
    }
    
    
    @Override
    public Class<? extends GraphicalElement> getCommonClass(Class<? extends GraphicalElement> c) {
        if(c.isAssignableFrom(this.getClass()))
            return c;
        else if(c.isAssignableFrom(CartographicElement.class))
            return CartographicElement.class;
        else
            return GraphicalElement.class;
    }
}
