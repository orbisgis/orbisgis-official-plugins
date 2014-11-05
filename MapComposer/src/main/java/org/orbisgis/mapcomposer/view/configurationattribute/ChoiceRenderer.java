package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
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
        
        SourceListCA choice = (SourceListCA)ca;
        
        pan.add(new JLabel(choice.getName()));
        JComboBox jcb = new JComboBox(choice.getValue().toArray(new String[0]));
        jcb.setSelectedItem(choice.getSelected());
        pan.add(jcb);
        
        return pan;
    }

    @Override
    public void extractValue(JPanel panel, ConfigurationAttribute attribute) {
        SourceListCA choice = (SourceListCA)attribute;
        for(Component c : panel.getComponents()){
            if(c instanceof JComboBox){
                choice.select(((JComboBox)c).getModel().getSelectedItem().toString());
            }
        }
    }
    
}
