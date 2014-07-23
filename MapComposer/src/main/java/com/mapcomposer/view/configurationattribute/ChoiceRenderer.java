package com.mapcomposer.view.configurationattribute;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Choice;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Renderer associated to the Choice ConfigurationAttribute.
 */
public class ChoiceRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout());
        
        Choice choice = (Choice)ca;
        
        pan.add(new JLabel(choice.getName()));
        JComboBox list = new JComboBox(choice.getValue().toArray(new String[0]));
        pan.add(list);
        
        return pan;
    }
    
}
