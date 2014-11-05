package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.IntegerCA;
import java.awt.Component;
import java.awt.FlowLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;

/**
 * Renderer associated to the IntegerCA ConfigurationAttribute.
 */
public class NumericRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        IntegerCA num = (IntegerCA)ca;
        
        pan.add(new JLabel(num.getName()));
        SpinnerModel model;
        if(num.getLimits())
            model =new SpinnerNumberModel((int)num.getValue(), num.getMin(), num.getMax(), 1);
        else
            model =new SpinnerNumberModel((int)num.getValue(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        pan.add(new JSpinner(model));
        return pan;
    }

    @Override
    public void extractValue(JPanel panel, ConfigurationAttribute attribute) {
        IntegerCA num = (IntegerCA)attribute;
        for(Component c : panel.getComponents()){
            if(c instanceof JSpinner){
                num.setValue(((Integer)((JSpinner)c).getValue()));
            }
        }
    }
    
}
