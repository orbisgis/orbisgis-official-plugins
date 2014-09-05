package com.mapcomposer.view.configurationattribute;

import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Choice;
import java.awt.Component;
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
        pan.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        Choice choice = (Choice)ca;
        
        pan.add(new JLabel(choice.getName()));
        JComboBox jcb = new JComboBox(choice.getValue().toArray(new String[0]));
        jcb.setSelectedItem(choice.getSelected());
        pan.add(jcb);
        
        return pan;
    }

    @Override
    public void extractValue(JPanel panel, ConfigurationAttribute attribute) {
        Choice choice = (Choice)attribute;
        for(Component c : panel.getComponents()){
            if(c instanceof JComboBox){
                choice.select(((JComboBox)c).getModel().getSelectedItem().toString());
            }
        }
    }
    
}
