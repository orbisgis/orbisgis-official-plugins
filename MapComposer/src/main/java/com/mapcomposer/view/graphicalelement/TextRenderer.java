package com.mapcomposer.view.graphicalelement;

import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.text.TextElement;
import java.awt.BorderLayout;
import java.awt.Font;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Renderer associated to the scale GraphicalElement.
 */
public class TextRenderer extends GERenderer{
    
    @Override
    public JPanel render(GraphicalElement ge){
        JPanel panel = super.render(ge);
        
        TextElement te = ((TextElement)ge);
        
        JLabel text = new JLabel();
        
        //Font settings
        //text.setFont();
        //Text alignment
        //Text Color
        text.setText(te.getText());
        
        panel.setLayout(new BorderLayout());
        panel.add(text, BorderLayout.CENTER);
        return panel;
    }
}
