package com.mapcomposer.view.graphicalelement;

import com.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.Document;
import java.awt.Color;
import javax.swing.JPanel;

/**
 * Renderer associated to teh Document
 */
public class DocumentRenderer extends GERenderer{
    
    @Override
    public JPanel render(final GraphicalElement ge){
        JPanel panel = new JPanel();
        panel.setOpaque(true);
        panel.setLayout(null);
        panel.setBackground(Color.yellow);
        System.out.println(((Document)ge).getFormat().getPixelDimension());
        panel.setPreferredSize(((Document)ge).getFormat().getPixelDimension());
        
        return panel;
    }
}
