package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.FileListCA;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.io.File;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Renderer associated to the FileListCA ConfigurationAttribute.
 * The JPanel returned by the createJComponentFromCA method look like :
 *  ________________________________________________________________
 * |                                  ____________________________  |
 * | NameOfTheConfigurationAttribute |selected value          | v | |
 * |                                 |________________________|___| |
 * |________________________________________________________________|
 *
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.FileListCA
 */
public class FileListRenderer implements CARenderer{

    @Override
    public JPanel createJComponentFromCA(ConfigurationAttribute ca) {
    //Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the panel all the swing components
        FileListCA fileListCA = (FileListCA)ca;
        
        panel.add(new JLabel(fileListCA.getName()));
        //Display the FileListCA into a JComboBox
        JComboBox jcb = new JComboBox(fileListCA.getValue().toArray(new String[0]));
        jcb.addActionListener(EventHandler.create(ActionListener.class, fileListCA, "select", "source.selectedItem"));
        jcb.setSelectedItem(ca.getValue());
        panel.add(jcb);
        
        return panel;
    }
}
