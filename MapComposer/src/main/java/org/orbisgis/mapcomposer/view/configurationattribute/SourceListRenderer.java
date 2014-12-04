package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

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
    public JPanel createJComponentFromCA(ConfigurationAttribute ca) {
    //Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the panel all the swing components
        SourceListCA sourceListCA = (SourceListCA)ca;
        //Add the name of the ConfigurationAttribute
        panel.add(new JLabel(sourceListCA.getName()));

        JComboBox jcb = new JComboBox(sourceListCA.getValue().toArray(new String[0]));
        jcb.addActionListener(EventHandler.create(ActionListener.class, sourceListCA, "select", "source.selectedItem"));
        //Display the SourceListCA into a JComboBox
        jcb.setSelectedItem(sourceListCA.getSelected());
        //Add the JComboBox
        panel.add(jcb);
        
        return panel;
    }
}
