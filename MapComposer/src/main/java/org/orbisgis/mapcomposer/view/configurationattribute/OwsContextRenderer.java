package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.OwsContextCA;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Renderer associated to the OwsContextCA ConfigurationAttribute.
 */
public class OwsContextRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        OwsContextCA source = (OwsContextCA)ca;
        pan.add(new JLabel(source.getName()));
        
        JComboBox list = new JComboBox(source.getValue().toArray());
        list.setSelectedItem(ca.getValue());
        pan.add(list);
        return pan;
    }

    @Override
    public void extractValue(JPanel panel, ConfigurationAttribute attribute) {
        OwsContextCA source = (OwsContextCA)attribute;
        for(Component c : panel.getComponents()){
            if(c instanceof JComboBox){
                source.select(((JComboBox)c).getModel().getSelectedItem().toString());
            }
        }
    }
    
}
