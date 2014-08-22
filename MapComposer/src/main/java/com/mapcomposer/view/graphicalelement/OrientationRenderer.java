package com.mapcomposer.view.graphicalelement;

import com.mapcomposer.Configuration;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Renderer associated to the Orientation GraphicalElement.
 */
public class OrientationRenderer extends GERenderer{

    @Override
    public BufferedImage getcontentImage(GraphicalElement ge) {
        // Draw in a BufferedImage the orientation icon.
        File f = new File(((Orientation)ge).getIconPath());
        if(f.exists() && f.isFile()) {
            try {
                return ImageIO.read(f);
            } catch (IOException ex) {
                Logger.getLogger(ImageRenderer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            //Use ImageIcon to convert the file URL into a buffered image
            ImageIcon icon = new ImageIcon(Configuration.defaultOrientationURL);
            BufferedImage bi = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            icon.paintIcon(null, g, 0,0);
            g.dispose();
            return bi;
        }
        return null;
    }
}
