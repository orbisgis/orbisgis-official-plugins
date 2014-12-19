package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import org.orbisgis.mapcomposer.view.ui.MainWindow;

import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.ImageIcon;

/**
 * Renderer associated to the Orientation GraphicalElement.
 */
public class OrientationRenderer extends SimpleGERenderer {

    @Override
    public BufferedImage createImageFromGE(GraphicalElement ge) {
        // Draw in a BufferedImage the orientation icon.
        File file = new File(((Orientation)ge).getIconPath());
        ImageIcon icon;
        if(file.exists() && file.isFile())
            icon = new ImageIcon(((Orientation)ge).getIconPath());
        else
            icon = new ImageIcon(MainWindow.class.getResource("compass.png"));

        //Get the bufferedImage from the image
        BufferedImage bi = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        icon.paintIcon(null, g, 0,0);
        g.dispose();

        //Scale the bufferedImage to the GraphicalElement size
        java.awt.Image image = bi.getScaledInstance(ge.getWidth(), ge.getHeight(), java.awt.Image.SCALE_SMOOTH);
        BufferedImage resize = new BufferedImage(ge.getWidth(), ge.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graph = resize.createGraphics();
        graph.drawImage(image, 0, 0, null);
        graph.dispose();

        return applyRotationToBufferedImage(resize, ge);
    }
}
