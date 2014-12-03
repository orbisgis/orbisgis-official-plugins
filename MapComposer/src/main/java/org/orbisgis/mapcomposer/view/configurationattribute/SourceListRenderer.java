package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Renderer associated to the SourceListCA ConfigurationAttribute.
 * The JPanel returned by the render method look like :
 *  ________________________________________________________________
 * |                                  ____________________________  |
 * | NameOfTheConfigurationAttribute |selected value          | v | |
 * |                                 |________________________|___| |
 * |________________________________________________________________|
 *
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA
 */
public class SourceListRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
    //Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the panel all the swing components
        SourceListCA sourceListCA = (SourceListCA)ca;
        //Add the name of the ConfigurationAttribute
        panel.add(new JLabel(sourceListCA.getName()));

        JComboBox jcb = new JComboBox(sourceListCA.getValue().toArray(new String[0]));
        //Display the SourceListCA into a JComboBox
        jcb.setSelectedItem(sourceListCA.getSelected());
        //Add the JComboBox
        panel.add(jcb);
        
        return panel;
    }

    @Override
    public void extractValueFromPanel(JPanel panel, ConfigurationAttribute attribute) {
        SourceListCA choice = (SourceListCA)attribute;
        //As value is inside a JComboBox, if find a component which is a JComboBox an set the attribute
        for(Component c : panel.getComponents()){
            if(c instanceof JComboBox){
                choice.select(((JComboBox)c).getModel().getSelectedItem().toString());
            }
        }
    }
    
}
