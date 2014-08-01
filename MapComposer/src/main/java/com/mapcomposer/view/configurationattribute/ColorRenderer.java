package com.mapcomposer.view.configurationattribute;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.ColorCA;
import com.mapcomposer.view.utils.ColorChooser;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Renderer associated to the Source ConfigurationAttribute.
 */
public class ColorRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        final ColorCA color = (ColorCA)ca;
        
        pan.add(new JLabel(color.getName()));
        JLabel label = new JLabel("Text demo");
        pan.add(label);
        JButton button = new JButton("Color Chooser");
        button.addMouseListener(new ColorChooser(label));
        
        pan.add(button);
        return pan;
    }
    
}
