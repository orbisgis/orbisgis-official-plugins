package org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic;

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GERefresh;
import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.progress.NullProgressMonitor;

import org.slf4j.LoggerFactory;

import java.awt.image.BufferedImage;

/**
 * This class represent the graphical representation (the image) of the map.
 */
public class MapImage extends SimpleCartoGE implements GERefresh {

    /** MapTransform used to generate the map image.
     * This class come from OrbisGIS
     */
    private final MapTransform mapTransform;
    
    /**
     * Main constructor.
     */
    public MapImage(){
        super();
        mapTransform = new MapTransform();
    }
    
    @Override
    public void refresh() {
        if(getOwsMapContext()!=null && getOwsMapContext().getBoundingBox()!=null)
            try {
                mapTransform.setExtent(this.getOwsMapContext().getBoundingBox());
                mapTransform.setImage(new BufferedImage((int)this.getOwsMapContext().getBoundingBox().getWidth(), (int)this.getOwsMapContext().getBoundingBox().getHeight(), BufferedImage.TYPE_INT_ARGB));
                if(!this.getOwsMapContext().isOpen())
                    this.getOwsMapContext().open(new NullProgressMonitor());
                this.getOwsMapContext().draw(mapTransform, new NullProgressMonitor());
            } catch (LayerException ex) {
                LoggerFactory.getLogger(MapImage.class).error(ex.getMessage());
            }
    }
    
    /**
     * Returns the BufferedImage of the map.
     * @return The bufferedImage of the map.
     */
    public BufferedImage getImage(){return mapTransform.getImage(); }
    
    /**
     * Returns the MapTransform of the map.
     * @return The MapTransform of the map.
     */
    public MapTransform getMapTransform(){return mapTransform;}
}
