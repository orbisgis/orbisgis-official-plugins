package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.MapImageListCA;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;

import java.awt.Component;
import java.awt.FlowLayout;
import java.util.ArrayList;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Renderer associated to the LinkToMap CA
 * The JPanel returned by the render method look like :
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
    public JPanel render(ConfigurationAttribute ca) {
    //Create the panel
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the panel all the swing components
        final MapImageListCA milka = (MapImageListCA)ca;
        
        pan.add(new JLabel(milka.getName()));
        ArrayList<String> names = new ArrayList<>();
        //Display the MapImageListCA into a JComboBox
        for(MapImage mi : milka.getValue())
            names.add(mi.toString());
        final JComboBox list = new JComboBox(names.toArray());
        pan.add(list);
        return pan;
    }
        

    @Override
    public void extractValue(JPanel panel, ConfigurationAttribute attribute) {
        MapImageListCA milka = (MapImageListCA)attribute;
        //As the MapImage list is in the JComboBox, find it and extract the value.
        for(Component c : panel.getComponents()){
            if(c instanceof JComboBox){
                for(MapImage ge : milka.getValue()){
                    if(ge.equals(((JComboBox)c).getSelectedItem()))
                        milka.select(ge);
                }
            }
        }
    }
}
