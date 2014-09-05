package com.mapcomposer.view.configurationattribute;

import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.OwsContext;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Renderer associated to the OwsContext ConfigurationAttribute.
 */
public class OwsContextRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        OwsContext source = (OwsContext)ca;
        pan.add(new JLabel(source.getName()));
        
        JComboBox list = new JComboBox(source.getList().toArray());
        list.setSelectedItem(ca.getValue());
        pan.add(list);
        return pan;
    }

    @Override
    public void extractValue(JPanel panel, ConfigurationAttribute attribute) {
        OwsContext source = (OwsContext)attribute;
        for(Component c : panel.getComponents()){
            if(c instanceof JComboBox){
                source.setValue(((JComboBox)c).getModel().getSelectedItem().toString());
            }
        }
    }
    
}
