package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.element.illustration.Image;
import org.orbisgis.mapcomposer.view.ui.MainWindow;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;

/**
 * Renderer associated to the Image GraphicalElement.
 */
public class ImageRenderer extends SimpleGERenderer {

    @Override
    public BufferedImage createImageFromGE(GraphicalElement ge) {
        //Get the image to draw
        File file = new File(((Image)ge).getPath());
        ImageIcon icon;
        if(file.exists() && file.isFile())
            icon = new ImageIcon(((Image)ge).getPath());
        else
            icon = new ImageIcon(MainWindow.class.getResource("add_picture.png"));

        //Get the bufferedImage from the image
        BufferedImage bi = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        icon.paintIcon(null, g, 0,0);
        g.dispose();

        //Scale the bufferedImage to the GraphicalElement size
        java.awt.Image image = bi.getScaledInstance(ge.getWidth(), ge.getHeight(), java.awt.Image.SCALE_FAST);
        BufferedImage resize = new BufferedImage(ge.getWidth(), ge.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graph = resize.createGraphics();
        graph.drawImage(image, 0, 0, null);
        graph.dispose();

        return applyRotationToBufferedImage(resize, ge);
    }
}
