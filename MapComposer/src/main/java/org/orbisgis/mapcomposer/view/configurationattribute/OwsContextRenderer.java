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
 * The JPanel returned by the render method look like :
 *  _________________________________________________________________
 * |                                  ____________________________   |
 * | NameOfTheConfigurationAttribute |selected value          | v |  |
 * |                                 |________________________|___|  |
 * |_________________________________________________________________|
 *
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.OwsContextCA
 */
public class OwsContextRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
    //Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the panel all the swing components
        OwsContextCA owsContextCA = (OwsContextCA)ca;
        panel.add(new JLabel(owsContextCA.getName()));

        //Display the OwsContextCA into a JComboBox
        JComboBox list = new JComboBox(owsContextCA.getValue().toArray());
        list.setSelectedItem(ca.getValue());
        panel.add(list);
        return panel;
    }

    @Override
    public void extractValueFromPanel(JPanel panel, ConfigurationAttribute attribute) {
        OwsContextCA source = (OwsContextCA)attribute;
        //As the OwsContext list is in the JComboBox, find it and extract the value.
        for(Component c : panel.getComponents()){
            if(c instanceof JComboBox){
                source.select(((JComboBox)c).getModel().getSelectedItem().toString());
            }
        }
    }
    
}
