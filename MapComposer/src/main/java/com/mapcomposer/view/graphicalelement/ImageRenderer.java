package com.mapcomposer.view.graphicalelement;

import com.mapcomposer.Configuration;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.illustration.Image;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

/**
 * Renderer associated to the Image GraphicalElement.
 */
public class ImageRenderer extends GERenderer{

    @Override
    public BufferedImage getcontentImage(GraphicalElement ge) {
        // Draw in a BufferedImage the image file
        File f = new File(((Image)ge).getPath());
        if(f.exists() && f.isFile()) {
            try {
                return ImageIO.read(f);
            } catch (IOException ex) {
                Logger.getLogger(ImageRenderer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            //Use ImageIcon to convert the file URL into a buffered image
            ImageIcon icon = new ImageIcon(Configuration.defaultImageURL);
            BufferedImage bi = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            icon.paintIcon(null, g, 0,0);
            g.dispose();
            return bi;
        }
        return null;
    }
}
