package com.mapcomposer.view.configurationattribute;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Numeric;
import java.awt.FlowLayout;
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
        pan.setLayout(new FlowLayout());
        
        Numeric num = (Numeric)ca;
        
        pan.add(new JLabel(num.getPropertyName()));
        SpinnerModel model =new SpinnerNumberModel((int)num.getPropertyValue(), -99999, 99999, 1);
        pan.add(new JSpinner(model));
        return pan;
    }
    
}
