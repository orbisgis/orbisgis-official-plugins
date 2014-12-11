package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.IntegerCA;
import java.awt.FlowLayout;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.EventHandler;
import javax.swing.*;
import javax.swing.event.ChangeListener;

/**
 * Renderer associated to the IntegerCA ConfigurationAttribute.
 * The JComponent returned by the createJComponentFromCA method look like :
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
    public JComponent createJComponentFromCA(ConfigurationAttribute ca) {
    //Create the component
        JComponent component = new JPanel();
        component.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the panel all the swing components
        IntegerCA integerCA = (IntegerCA)ca;
        
        component.add(new JLabel(integerCA.getName()));
        SpinnerModel model;
        //Display the IntegerCA into a JSpinner
        if(integerCA.getLimits())
            model =new SpinnerNumberModel((int)integerCA.getValue(), integerCA.getMin(), integerCA.getMax(), 1);
        else
            model =new SpinnerNumberModel((int)integerCA.getValue(), Integer.MIN_VALUE, Integer.MAX_VALUE, 1);
        JSpinner spinner = new JSpinner(model);
        spinner.addChangeListener(EventHandler.create(ChangeListener.class, integerCA, "setValue", "source.value"));
        spinner.addMouseWheelListener(EventHandler.create(MouseWheelListener.class, this, "mouseWheel", ""));
        component.add(spinner);
        return component;
    }

    /**
     * Action done when the mouse wheel is used in the JComboBox of the rendered IntegerCA.
     * @param mwe MouseWheelEvent
     */
    public void mouseWheel(MouseWheelEvent mwe){
        JSpinner source = (JSpinner) mwe.getSource();
        SpinnerNumberModel spinMod = (SpinnerNumberModel)source.getModel();
        int value = (int)source.getValue()-mwe.getWheelRotation();
        System.out.println(spinMod.getMaximum()+", "+value+", "+spinMod.getMinimum());
        if(value<=(int)spinMod.getMaximum() && value>=(int)spinMod.getMinimum())
            source.setValue(value);
    }
}
