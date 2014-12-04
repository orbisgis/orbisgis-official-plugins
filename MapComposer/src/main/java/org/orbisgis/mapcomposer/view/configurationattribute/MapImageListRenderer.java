package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.MapImageListCA;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.util.ArrayList;
import javax.swing.*;

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
    public JComponent createJComponentFromCA(ConfigurationAttribute ca) {
    //Create the component
        JComponent component = new JPanel();
        component.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the component all the swing components
        final MapImageListCA milka = (MapImageListCA)ca;
        
        component.add(new JLabel(milka.getName()));
        ArrayList<String> names = new ArrayList<>();
        //Display the MapImageListCA into a JComboBox
        for(MapImage mi : milka.getValue())
            names.add(mi.toString());
        JComboBox<String> jcb = new JComboBox(names.toArray());
        jcb.addActionListener(EventHandler.create(ActionListener.class, milka, "select", "source.selectedItem"));
        jcb.setSelectedItem(ca.getValue());
        component.add(jcb);
        return component;
    }
}
