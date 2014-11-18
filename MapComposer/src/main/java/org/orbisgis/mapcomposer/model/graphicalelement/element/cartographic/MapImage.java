package org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic;

import com.vividsolutions.jts.geom.Envelope;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.IntegerCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GERefresh;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.progress.NullProgressMonitor;
import org.slf4j.LoggerFactory;

/**
 * Map image generated from an OWS-Context.
 */


public class MapImage extends SimpleCartoGE implements GERefresh {
    /** MapTransform*/
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
                mapTransform.setImage(new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB));
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
