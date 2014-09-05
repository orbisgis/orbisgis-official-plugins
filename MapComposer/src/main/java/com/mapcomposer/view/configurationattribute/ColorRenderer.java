package com.mapcomposer.view.configurationattribute;

import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.ColorCA;
import com.mapcomposer.view.utils.ColorChooser;
import java.awt.Component;
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
        label.setForeground(color.getValue());
        pan.add(label);
        JButton button = new JButton("Color Chooser");
        button.addMouseListener(new ColorChooser(label));
        
        pan.add(button);
        return pan;
    }

    @Override
    public void extractValue(JPanel panel, ConfigurationAttribute attribute) {
        ColorCA color = (ColorCA)attribute;
        for(Component c : panel.getComponents()){
            if(c instanceof JLabel){
                JLabel label = (JLabel)c;
                if(label.getText().equals("Text demo")){
                    color.setValue(label.getForeground());
                }
            }
        }
    }
    
}
