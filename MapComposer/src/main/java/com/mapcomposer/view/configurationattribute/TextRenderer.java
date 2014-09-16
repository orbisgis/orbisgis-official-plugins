package com.mapcomposer.view.configurationattribute;

import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Text;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;

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
        JTextArea area = new JTextArea(text.getValue());
        pan.add(area);
        
        return pan;
    }

    @Override
    public void extractValue(JPanel panel, ConfigurationAttribute attribute) {
        Text text = (Text)attribute;
        for(Component c : panel.getComponents()){
            if(c instanceof JTextArea){
                text.setValue(((JTextArea)c).getText());
            }
        }
    }
    
}
