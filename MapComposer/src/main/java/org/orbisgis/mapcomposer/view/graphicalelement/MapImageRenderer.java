package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import java.awt.image.BufferedImage;

/**
 * Renderer associated to the Cartographic GraphicalElement.
 */
public class MapImageRenderer extends GERenderer {
    
    @Override
    public BufferedImage getcontentImage(GraphicalElement ge) {
        // Draw in a BufferedImage the map image
        if(((MapImage)ge).getImage()!=null){
        return ((MapImage)ge).getImage();
        }
        else{
            return null;
        }
    }
    
}
