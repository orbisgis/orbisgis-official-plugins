package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.ColorCA;
import org.orbisgis.mapcomposer.view.utils.ColorChooser;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import javax.swing.*;

/**
 * Renderer associated to the ColorCA ConfigurationAttribute.
 * The JPanel returned by the render method look like :
 *  _______________________________________________
 * |                                  _________    |
 * | NameOfTheConfigurationAttribute |Button   |   |
 * |                                 |_________|   |
 * |_______________________________________________|
 *
 * The color is chosen by clicking on a button. It opens a ColorChooser and the color is saved in the background of the button
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.ColorCA
 */
public class ColorRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
    //Create the panel
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the panel all the swing components
        final ColorCA color = (ColorCA)ca;
        //Add the name of the ConfigurationAttribute
        pan.add(new JLabel(color.getName()));

        JLabel label = new JLabel("Text demo");
        JButton button = new JButton();
        //Display the color in the button background
        button.setBackground(color.getValue());
        button.add(label);
        //On clicking on the button, open a color chooser
        button.addActionListener(EventHandler.create(ActionListener.class, this, "open", "source"));
        //Add the JButton
        pan.add(button);
        return pan;
    }

    @Override
    public void extractValue(JPanel panel, ConfigurationAttribute attribute) {
        ColorCA color = (ColorCA)attribute;
        //As the color is in the JButton background, find it and extract the value.
        for(Component c : panel.getComponents()){
            if(c instanceof JButton){
                color.setValue(c.getBackground());
            }
        }
    }

    /**
     * Open a ColorChooser and show it.
     * @param component The chosen color will be saved in the background of the component
     */
    public void open(JComponent component){
        ColorChooser cc = new ColorChooser(component);
        cc.setVisible(true);
    }
    
}
