package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.geom.AffineTransform;
import java.awt.image.BufferedImage;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import javax.swing.JComponent;
import javax.swing.JPanel;

/**
 * Base renderer for GraphicalElement.
 * Every extention of the renderer sould call super.render(ge) to get the panel where the element is displayed.
 */
public abstract class GERenderer {
    /**
     * Renders the GrapgicalElement given.
     * @param ge GraphicalElement to render.
     * @return JPanel of the GraphicalElement.
     */
    public JPanel render(final GraphicalElement ge){
        JPanel panel = UIController.getPanel(ge);
        //Calculate the required size of the panel to contain the full rotated rectangle image.
        double rad = Math.toRadians(ge.getRotation());
        double newHeight = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
        double newWidth = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());
        //Sets the panel absolute position
        panel.setBounds(ge.getX(), ge.getY(), (int)newHeight, (int)newWidth);
        panel.setPreferredSize(new Dimension((int)newHeight, (int)newWidth));
        
        //Empty the panel to redisplay the element.
        panel.removeAll();
        
        //Get back the element to display and rotate it
        final BufferedImage bi = getcontentImage(ge);
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
        panel.setOpaque(false);
        return panel;
    }
    
    /**
     * This method return the content of the panel as an image.
     * @param ge GraphicalElement
     * @return The buffered image corresponding to the GraphicalElement
     */
    public abstract BufferedImage getcontentImage(GraphicalElement ge);
}
