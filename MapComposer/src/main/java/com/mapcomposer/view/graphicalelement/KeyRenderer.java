package com.mapcomposer.view.graphicalelement;

import com.mapcomposer.model.graphicalelement.GraphicalElement;
import java.awt.Color;
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
}
