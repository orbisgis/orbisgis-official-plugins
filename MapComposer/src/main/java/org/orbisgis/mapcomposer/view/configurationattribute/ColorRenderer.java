package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.ColorCA;
import org.orbisgis.mapcomposer.view.utils.ColorChooser;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Renderer associated to the Source ConfigurationAttribute.
 */
public class ColorRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        final ColorCA color = (ColorCA)ca;
        
        pan.add(new JLabel(color.getName()));
        JLabel label = new JLabel("Text demo");
        JButton button = new JButton();
        button.setBackground(color.getValue());
        button.add(label);
        button.addActionListener(EventHandler.create(ActionListener.class, this, "open", "source"));
        
        pan.add(button);
        return pan;
    }

    @Override
    public void extractValue(JPanel panel, ConfigurationAttribute attribute) {
        ColorCA color = (ColorCA)attribute;
        for(Component c : panel.getComponents()){
            if(c instanceof JButton){
                color.setValue(c.getBackground());
            }
        }
    }
    
    public void open(JButton button){
        ColorChooser cc = new ColorChooser(button);
        cc.setVisible(true);
    }
    
}
