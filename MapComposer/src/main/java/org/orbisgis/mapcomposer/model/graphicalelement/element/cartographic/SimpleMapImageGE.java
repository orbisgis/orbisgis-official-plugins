package org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic;

import org.orbisgis.mapcomposer.model.configurationattribute.attribute.IntegerCA;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GERefresh;
import java.awt.image.BufferedImage;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.progress.NullProgressMonitor;

/**
 * Map image generated from an OWS-Context.
 */


public class SimpleMapImageGE extends SimpleCartoGE implements GERefresh {
    /** MapTransform*/
    private final MapTransform mapTransform;
    private IntegerCA integer;
    
    /**
     * Main constructor.
     */
    public SimpleMapImageGE(){
        super();
        mapTransform = new MapTransform();
    }
    
    /**
     * Copy constructor.
     * @param smige SimpleMapImageGE to copy.
     */
    public SimpleMapImageGE(SimpleMapImageGE smige){
        super(smige);
        mapTransform = smige.mapTransform;
    }
    
    @Override
    public void refresh() {
        try {
            mapTransform.setExtent(this.getOwsMapContext().getBoundingBox());
            mapTransform.setImage(new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB));
            if(!this.getOwsMapContext().isOpen())
                this.getOwsMapContext().open(new NullProgressMonitor());
            this.getOwsMapContext().draw(mapTransform, new NullProgressMonitor());
        } catch (LayerException ex) {
            Logger.getLogger(SimpleMapImageGE.class.getName()).log(Level.SEVERE, null, ex);
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
