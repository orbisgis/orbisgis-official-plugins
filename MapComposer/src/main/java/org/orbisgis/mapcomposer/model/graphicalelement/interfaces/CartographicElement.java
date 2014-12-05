package org.orbisgis.mapcomposer.model.graphicalelement.interfaces;

import org.orbisgis.coremap.layerModel.OwsMapContext;

/**
 * This interface extends the GraphicalElement interface and represent a cartographic element based on a OwsMapContext file.
 * It contain the basics filed of a GraphicalElement plus an OwsMapContext (OwsContextCA) which contain all the data from the map created in OrbisGIS.
 * To simply the use of this interface, the OwsMapContext is set by giving its path.
 */
public interface CartographicElement extends GraphicalElement{

    /**
     * Returns the OwsMapContext.
     * @return The OwsMapContext.
     */
    public OwsMapContext getOwsMapContext();

    /**
     * Returns the OwsContext path.
     * @return The OwsContext path.
     */
    public String getOwsPath();
    
    /**
     * Sets the OwsContext path.
     * @param owsContext New OwsContext path.
     */
    public void setOwsContext(String owsContext);
}
