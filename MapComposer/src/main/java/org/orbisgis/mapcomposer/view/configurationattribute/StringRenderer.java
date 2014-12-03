package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

/**
 * Renderer associated to the Text ConfigurationAttribute.
 * The JPanel returned by the render method look like :
 *  _________________________________________________________________
 * |                                  ____________________________   |
 * | NameOfTheConfigurationAttribute |Some text                   |  |
 * |                                 |____________________________|  |
 * |_________________________________________________________________|
 *
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA
 */
public class StringRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
    //Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the panel all the swing components
        StringCA stringCA = (StringCA)ca;
        
        panel.add(new JLabel(stringCA.getName()));
        //Display the StringCA into a JTextArea
        JTextArea area = new JTextArea(stringCA.getValue());
        panel.add(area);
        
        return panel;
    }

    @Override
    public void extractValueFromPanel(JPanel panel, ConfigurationAttribute attribute) {
        StringCA text = (StringCA)attribute;
        //As the string is in the JTextArea, find it and extract the value.
        for(Component c : panel.getComponents()){
            if(c instanceof JTextArea){
                text.setValue(((JTextArea)c).getText());
            }
        }
    }
    
}
