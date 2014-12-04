package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.MapImageListCA;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;

import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Renderer associated to the LinkToMap CA
 * The JPanel returned by the createJComponentFromCA method look like :
 *  _________________________________________________________________
 * |                                  ____________________________   |
 * | NameOfTheConfigurationAttribute |selected value          | v |  |
 * |                                 |________________________|___|  |
 * |_________________________________________________________________|
 *
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.MapImageListCA
 */
public class MapImageListRenderer implements CARenderer{

    @Override
    public JPanel createJComponentFromCA(ConfigurationAttribute ca) {
    //Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the panel all the swing components
        final MapImageListCA milka = (MapImageListCA)ca;
        
        panel.add(new JLabel(milka.getName()));
        ArrayList<String> names = new ArrayList<>();
        //Display the MapImageListCA into a JComboBox
        for(MapImage mi : milka.getValue())
            names.add(mi.toString());
        JComboBox jcb = new JComboBox(names.toArray());
        jcb.addActionListener(EventHandler.create(ActionListener.class, milka, "select", "source.selectedItem"));
        jcb.setSelectedItem(ca.getValue());
        panel.add(jcb);
        return panel;
    }
}
