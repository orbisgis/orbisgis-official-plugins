package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.IntegerCA;
import java.awt.Component;
import java.awt.FlowLayout;
import java.beans.EventHandler;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.SpinnerModel;
import javax.swing.SpinnerNumberModel;
import javax.swing.event.ChangeListener;

/**
 * Renderer associated to the IntegerCA ConfigurationAttribute.
 * The JPanel returned by the createJComponentFromCA method look like :
 *  ________________________________________________________________
 *                                   ____________________________
 * NameOfTheConfigurationAttribute  |integer value           | ^ |
 *                                  |________________________|_v_|
 * |________________________________________________________________|
 *
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.IntegerCA
 */
public class IntegerRenderer implements CARenderer{

    @Override
    public JPanel createJComponentFromCA(ConfigurationAttribute ca) {
    //Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the panel all the swing components
        IntegerCA integerCA = (IntegerCA)ca;
        
        panel.add(new JLabel(integerCA.getName()));
        SpinnerModel model;
        //Display the IntegerCA into a JSpinner
        if(integerCA.getLimits())
            model =new SpinnerNumberModel((int)integerCA.getValue(), integerCA.getMin(), integerCA.getMax(), 1);
        else
            model =new SpinnerNumberModel((int)integerCA.getValue(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        JSpinner spinner = new JSpinner(model);
        spinner.addChangeListener(EventHandler.create(ChangeListener.class, integerCA, "setValue", "source.value"));
        panel.add(spinner);
        return panel;
    }
}
