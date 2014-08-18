package com.mapcomposer.view.configurationattribute;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Choice;
import com.mapcomposer.model.configurationattribute.attribute.Text;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Renderer associated to the Text ConfigurationAttribute.
 */
public class TextRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        Text text = (Text)ca;
        
        pan.add(new JLabel(text.getName()));
        pan.add(new JTextField(text.getValue()));
        
        return pan;
    }

    @Override
    public void extractValue(JPanel panel, ConfigurationAttribute attribute) {
        Text text = (Text)attribute;
        for(Component c : panel.getComponents()){
            if(c instanceof JTextField){
                text.setValue(((JTextField)c).getText());
            }
        }
    }
    
}
