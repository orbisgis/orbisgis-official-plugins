package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import javax.swing.*;

/**
 * Renderer associated to the SourceListCA ConfigurationAttribute.
 * The JPanel returned by the createJComponentFromCA method look like :
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
    public JComponent createJComponentFromCA(ConfigurationAttribute ca) {
    //Create the component
        JComponent component = new JPanel();
        component.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the component all the swing components
        SourceListCA sourceListCA = (SourceListCA)ca;
        //Add the name of the ConfigurationAttribute
        component.add(new JLabel(sourceListCA.getName()));

        JComboBox<String> jcb = new JComboBox(sourceListCA.getValue().toArray(new String[0]));
        jcb.addActionListener(EventHandler.create(ActionListener.class, sourceListCA, "select", "source.selectedItem"));
        //Display the SourceListCA into a JComboBox
        jcb.setSelectedItem(sourceListCA.getSelected());
        //Add the JComboBox
        component.add(jcb);
        
        return component;
    }
}
