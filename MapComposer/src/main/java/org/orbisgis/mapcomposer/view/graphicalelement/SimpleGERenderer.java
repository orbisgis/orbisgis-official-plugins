package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Base renderer for GraphicalElement.
 * It create a JPanel, size and move it, rotate it and draw the bufferedImage from the getContentImage() method.
 * Every extension of the SimpleRenderer class should call super.getRenderer(ge) to get the panel where the element is displayed.
 */
public abstract class SimpleGERenderer implements GERenderer {

    @Override
    public JPanel render(final GraphicalElement ge){
        JPanel panel = new JPanel(new BorderLayout());
        panel.add(new JPanel());
        //Calculate the required size of the panel to contain the full rotated image.
        double rad = Math.toRadians(ge.getRotation());
        double newHeight = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
        double newWidth = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());
        //Sets the panel bounds
        panel.setBounds(ge.getX(), ge.getY(), (int)newHeight, (int)newWidth);
        
        //Get back the BufferedImage of the GraphicalElement to display and rotate it
        final BufferedImage bi = getContentImage(ge);
        if(bi!=null){
            panel.add(new JComponent() {
                //Redefinition of the painComponent method to rotate panel content.
                @Override protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    AffineTransform at = new AffineTransform();
                    at.translate(getWidth() / 2, getHeight() / 2);
                    at.rotate(Math.toRadians(ge.getRotation()));
                    at.translate(-bi.getWidth()/2, -bi.getHeight()/2);
                    ((Graphics2D) g).drawImage(bi, at, null);
                }
            });
        }
        //Make the panel transparent
        panel.setOpaque(false);
        return panel;
    }
}
