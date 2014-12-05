package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.FileListCA;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.io.File;
import javax.swing.*;

/**
 * Renderer associated to the FileListCA ConfigurationAttribute.
 * The JComponent returned by the createJComponentFromCA method look like :
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
    public JComponent createJComponentFromCA(ConfigurationAttribute ca) {
    //Create the component
        JComponent component = new JPanel();
        component.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the panel all the swing components
        FileListCA fileListCA = (FileListCA)ca;
        
        component.add(new JLabel(fileListCA.getName()));
        //Display the FileListCA into a JComboBox
        JComboBox<File> jcb = new JComboBox(fileListCA.getValue().toArray(new String[0]));
        jcb.addActionListener(EventHandler.create(ActionListener.class, fileListCA, "select", "source.selectedItem"));
        jcb.setSelectedItem(ca.getValue());
        component.add(jcb);
        
        return component;
    }
}
