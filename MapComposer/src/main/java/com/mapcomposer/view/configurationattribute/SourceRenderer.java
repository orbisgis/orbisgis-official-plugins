package com.mapcomposer.view.configurationattribute;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Source;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import com.mapcomposer.view.utils.MouseListenerBrowse;

/**
 * Renderer associated to the Source ConfigurationAttribute.
 */
public class SourceRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        final Source source = (Source)ca;
        
        pan.add(new JLabel(source.getName()));
        JTextField jtf = new JTextField(source.getValue());
        pan.add(jtf);
        JButton button = new JButton("Browse");
        button.addMouseListener(new MouseListenerBrowse(jtf));
        
        pan.add(button);
        return pan;
    }
    
}
