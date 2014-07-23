package com.mapcomposer.view.configurationattribute;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Source;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Renderer associated to the Source ConfigurationAttribute.
 */
public class SourceRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout());
        
        Source source = (Source)ca;
        
        pan.add(new JLabel(source.getName()));
        pan.add(new JTextField(source.getValue()));
        JButton button = new JButton("Browse");
        
        pan.add(button);
        return pan;
    }
    
}
