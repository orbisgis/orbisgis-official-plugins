package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.element.illustration.Image;
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
 * Renderer associated to the Image GraphicalElement.
 */
public class ImageRenderer extends SimpleGERenderer {

    @Override
    public BufferedImage createImageFromGE(GraphicalElement ge) {
        // Draw in a BufferedImage the image file
        File file = new File(((Image)ge).getPath());
        if(file.exists() && file.isFile()) {
            try {
                return ImageIO.read(file);
            } catch (IOException ex) {
                Logger.getLogger(ImageRenderer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        else{
            //Return the icon of the Image as BufferedImage
            ImageIcon icon = new ImageIcon(MainWindow.class.getResource("add_picture.png"));
            BufferedImage bi = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            icon.paintIcon(null, g, 0,0);
            g.dispose();
            return applyRotationToBufferedImage(bi, ge);
        }
        return null;
    }
}
