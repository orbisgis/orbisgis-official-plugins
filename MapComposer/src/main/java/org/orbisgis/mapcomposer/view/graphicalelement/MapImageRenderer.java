package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import org.orbisgis.mapcomposer.view.ui.MainWindow;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Renderer associated to the Cartographic GraphicalElement.
 */
public class MapImageRenderer extends SimpleGERenderer {
    
    @Override
    public BufferedImage createImageFromGE(GraphicalElement ge) {
        BufferedImage bi;
        // Draw in a BufferedImage the map image
        if(((MapImage)ge).getImage()!=null){
            bi = ((MapImage)ge).getImage();
        }
        else{
            //Return the icon of the MapImage as BufferedImage
            ImageIcon icon = new ImageIcon(MainWindow.class.getResource("add_map.png"));
            bi = new BufferedImage(icon.getIconWidth(),icon.getIconHeight(),BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            icon.paintIcon(null, g, 0,0);
            g.dispose();
        }

        //Scale the bufferedImage to the GraphicalElement size
        java.awt.Image image = bi.getScaledInstance(ge.getWidth(), ge.getHeight(), java.awt.Image.SCALE_FAST);
        BufferedImage resize = new BufferedImage(ge.getWidth(), ge.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graph = resize.createGraphics();
        graph.drawImage(image, 0, 0, null);
        graph.dispose();

        return applyRotationToBufferedImage(resize, ge);
    }
}
