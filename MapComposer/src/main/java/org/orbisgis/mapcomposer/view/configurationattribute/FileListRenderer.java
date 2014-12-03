package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.FileListCA;
import java.awt.Component;
import java.awt.FlowLayout;
import java.io.File;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Renderer associated to the FileListCA ConfigurationAttribute.
 * The JPanel returned by the render method look like :
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
    public JPanel render(ConfigurationAttribute ca) {
    //Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the panel all the swing components
        FileListCA fileListCA = (FileListCA)ca;
        
        panel.add(new JLabel(fileListCA.getName()));
        //Display the FileListCA into a JComboBox
        JComboBox list = new JComboBox(fileListCA.getValue().toArray(new String[0]));
        panel.add(list);
        
        return panel;
    }

    @Override
    public void extractValueFromPanel(JPanel panel, ConfigurationAttribute attribute) {
        FileListCA filelist = (FileListCA)attribute;
        //As the file list is in the JComboBox, find it and extract the value.
        for(Component c : panel.getComponents()){
            if(c instanceof JComboBox){
                filelist.select(new File(((JComboBox)c).getModel().getSelectedItem().toString()));
            }
        }
    }
    
}
