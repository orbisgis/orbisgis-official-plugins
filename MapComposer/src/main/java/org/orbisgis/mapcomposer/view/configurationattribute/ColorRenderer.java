package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.ColorCA;
import org.orbisgis.mapcomposer.view.utils.ColorChooser;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.beans.PropertyChangeListener;
import javax.swing.*;

/**
 * Renderer associated to the ColorCA ConfigurationAttribute.
 * The JComponent returned by the createJComponentFromCA method look like :
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
    public JComponent createJComponentFromCA(ConfigurationAttribute ca) {
    //Create the component
        JComponent component = new JPanel();
        component.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the component all the swing components
        final ColorCA colorCA = (ColorCA)ca;
        //Add the name of the ConfigurationAttribute
        component.add(new JLabel(colorCA.getName()));

        JButton button = new JButton("Text demo");
        //Display the color in the button background
        button.setBackground(colorCA.getValue());
        //On clicking on the button, open a color chooser
        button.addActionListener(EventHandler.create(ActionListener.class, this, "open", "source"));
        button.addPropertyChangeListener(EventHandler.create(PropertyChangeListener.class, colorCA, "setValue", "source.background"));
        //Add the JButton
        component.add(button);
        return component;
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
