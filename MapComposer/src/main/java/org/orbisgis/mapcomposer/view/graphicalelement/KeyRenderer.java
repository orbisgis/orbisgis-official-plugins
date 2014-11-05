package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import java.awt.Color;
import java.awt.image.BufferedImage;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Renderer associated to the amp key.
 */
public class KeyRenderer extends GERenderer{
    
    @Override
    public JPanel render(GraphicalElement ge) {
        //Gets back the panel rendered by the mother class
        JPanel panel = super.render(ge);
        panel.add(new JLabel());
        panel.setBackground(Color.DARK_GRAY);
        return panel;
    }

    @Override
    public BufferedImage getcontentImage(GraphicalElement ge) {
        return null;
    }
}
