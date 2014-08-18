package com.mapcomposer.view.configurationattribute;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Choice;
import com.mapcomposer.model.configurationattribute.attribute.Numeric;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 * Renderer associated to the Numeric ConfigurationAttribute.
 */
public class NumericRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        Numeric num = (Numeric)ca;
        
        pan.add(new JLabel(num.getName()));
        SpinnerModel model =new SpinnerNumberModel((int)num.getValue(), -99999, 99999, 1);
        pan.add(new JSpinner(model));
        return pan;
    }

    @Override
    public void extractValue(JPanel panel, ConfigurationAttribute attribute) {
        Numeric num = (Numeric)attribute;
        for(Component c : panel.getComponents()){
            if(c instanceof JSpinner){
                num.setValue(((Integer)((JSpinner)c).getValue()));
            }
        }
    }
    
}
