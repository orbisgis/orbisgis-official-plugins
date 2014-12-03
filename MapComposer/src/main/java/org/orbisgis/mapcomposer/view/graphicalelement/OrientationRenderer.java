package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import org.orbisgis.mapcomposer.view.ui.MainWindow;
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
public class OrientationRenderer extends SimpleGERenderer {

    @Override
    public BufferedImage getContentImage(GraphicalElement ge) {
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
            //Return the icon of the Orientation as BufferedImage
            ImageIcon icon = new ImageIcon(MainWindow.class.getResource("compass.png"));
            BufferedImage bi = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            icon.paintIcon(null, g, 0,0);
            g.dispose();
            return bi;
        }
        return null;
    }
}
