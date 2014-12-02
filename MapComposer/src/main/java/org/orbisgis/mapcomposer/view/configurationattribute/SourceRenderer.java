package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceCA;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import org.orbisgis.mapcomposer.view.utils.MouseListenerBrowse;
import java.awt.Component;

/**
 * Renderer associated to the Source ConfigurationAttribute.
 * The JPanel returned by the render method look like :
 *  _____________________________________________________________________________________
 * |                                  ____________________________        _____________  |
 * | NameOfTheConfigurationAttribute | text field with the path   |      |Button browse| |
 * |                                 |____________________________|      |_____________| |
 * |_____________________________________________________________________________________|
 *
 * A button open a JFileChooser to permit to the user to find the source file.
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceCA
 */
public class SourceRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
    //Create the panel
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the panel all the swing components
        final SourceCA source = (SourceCA)ca;
        
        pan.add(new JLabel(source.getName()));
        //Display the SourceCA into a JTextField
        JTextField jtf = new JTextField(source.getValue());
        pan.add(jtf);
        JButton button = new JButton("Browse");
        button.addMouseListener(new MouseListenerBrowse(jtf));
        
        pan.add(button);
        return pan;
    }

    @Override
    public void extractValue(JPanel panel, ConfigurationAttribute attribute) {
        SourceCA source = (SourceCA)attribute;
        //As the source is in the JTextField, find it and extract the value.
        for(Component c : panel.getComponents()){
            if(c instanceof JTextField){
                source.setValue(((JTextField)c).getText());
            }
        }
    }
    
}
