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
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the panel all the swing components
        FileListCA filelist = (FileListCA)ca;
        
        pan.add(new JLabel(filelist.getName()));
        //Display the FileListCA into a JComboBox
        JComboBox list = new JComboBox(filelist.getValue().toArray(new String[0]));
        pan.add(list);
        
        return pan;
    }

    @Override
    public void extractValue(JPanel panel, ConfigurationAttribute attribute) {
        FileListCA filelist = (FileListCA)attribute;
        //As the file list is in the JComboBox, find it and extract the value.
        for(Component c : panel.getComponents()){
            if(c instanceof JComboBox){
                filelist.select(new File(((JComboBox)c).getModel().getSelectedItem().toString()));
            }
        }
    }
    
}
