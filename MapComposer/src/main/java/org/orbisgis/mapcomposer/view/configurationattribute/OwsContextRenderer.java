package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.OwsContextCA;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Renderer associated to the OwsContextCA ConfigurationAttribute.
 * The JPanel returned by the createJComponentFromCA method look like :
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
    public JPanel createJComponentFromCA(ConfigurationAttribute ca) {
    //Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the panel all the swing components
        OwsContextCA owsContextCA = (OwsContextCA)ca;
        panel.add(new JLabel(owsContextCA.getName()));

        //Display the OwsContextCA into a JComboBox
        JComboBox jcb = new JComboBox(owsContextCA.getValue().toArray());
        jcb.addActionListener(EventHandler.create(ActionListener.class, owsContextCA, "select", "source.selectedItem"));
        jcb.setSelectedItem(ca.getValue());
        panel.add(jcb);
        return panel;
    }
}
