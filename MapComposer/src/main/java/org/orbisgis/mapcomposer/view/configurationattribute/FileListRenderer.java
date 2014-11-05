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
 */
public class FileListRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        FileListCA filelist = (FileListCA)ca;
        
        pan.add(new JLabel(filelist.getName()));
        JComboBox list = new JComboBox(filelist.getValue().toArray(new String[0]));
        pan.add(list);
        
        return pan;
    }

    @Override
    public void extractValue(JPanel panel, ConfigurationAttribute attribute) {
        FileListCA filelist = (FileListCA)attribute;
        for(Component c : panel.getComponents()){
            if(c instanceof JComboBox){
                filelist.select(new File(((JComboBox)c).getModel().getSelectedItem().toString()));
            }
        }
    }
    
}
