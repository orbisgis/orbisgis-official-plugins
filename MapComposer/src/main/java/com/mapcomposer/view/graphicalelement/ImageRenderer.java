package com.mapcomposer.view.graphicalelement;

import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.illustration.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;

/**
 * Renderer associated to the Image GraphicalElement.
 */
public class ImageRenderer extends GERenderer{

    @Override
    public BufferedImage getcontentImage(GraphicalElement ge) {
        // Draw in a BufferedImage the image file
        File f = new File(((Image)ge).getPath());
        if(f.exists() && f.isFile()){
            try {
                return ImageIO.read(f);
            } catch (IOException ex) {
                Logger.getLogger(ImageRenderer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        return null;
    }
}
