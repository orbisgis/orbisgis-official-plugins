package com.mapcomposer.model.graphicalelement.element.cartographic;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.graphicalelement.utils.GERefresh;
import java.awt.image.BufferedImage;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.progress.NullProgressMonitor;

/**
 * Map image generated from an OWS-Context.
 */
public final class MapImage extends CartographicElement implements GERefresh{
    
    private BufferedImage image = null;
    private MapTransform mapTransform;

    public MapImage() {
        super();
        mapTransform = new MapTransform();
    }
    
    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        return super.getAllAttributes();
    }

    @Override
    public void refresh() {
        try {
            mapTransform = new MapTransform();
            mapTransform.setExtent(this.getOwsMapContext().getBoundingBox());
            image = new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB);
            mapTransform.setImage(image);
            if(!this.getOwsMapContext().isOpen())
                this.getOwsMapContext().open(new NullProgressMonitor());
            this.getOwsMapContext().draw(mapTransform, new NullProgressMonitor());
        } catch (LayerException ex) {
            Logger.getLogger(MapImage.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public BufferedImage getImage(){
        return image;
    }
    
    public MapTransform getMapTransform(){
        return mapTransform;
    }
}
