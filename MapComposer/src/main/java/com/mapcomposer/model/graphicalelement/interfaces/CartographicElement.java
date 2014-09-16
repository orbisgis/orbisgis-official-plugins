package com.mapcomposer.model.graphicalelement.interfaces;

import org.orbisgis.coremap.layerModel.OwsMapContext;

/**
 * Interface for cartographic element based on an OWS-Context.
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
