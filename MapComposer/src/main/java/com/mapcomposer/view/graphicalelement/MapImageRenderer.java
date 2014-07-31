package com.mapcomposer.view.graphicalelement;

import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
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
