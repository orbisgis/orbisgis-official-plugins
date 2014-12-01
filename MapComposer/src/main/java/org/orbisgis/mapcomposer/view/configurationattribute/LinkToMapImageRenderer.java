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
 */
public class LinkToMapImageRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout(FlowLayout.LEFT));
        
        final MapImageListCA milka = (MapImageListCA)ca;
        
        pan.add(new JLabel(milka.getName()));
        ArrayList<String> names = new ArrayList<>();
        for(MapImage mi : milka.getValue())
            names.add(mi.toString());
        final JComboBox list = new JComboBox(names.toArray());
        pan.add(list);
        return pan;
    }
        

    @Override
    public void extractValue(JPanel panel, ConfigurationAttribute attribute) {
        MapImageListCA milka = (MapImageListCA)attribute;
        for(Component c : panel.getComponents()){
            if(c instanceof JComboBox){
                int i=1;
                for(MapImage ge : milka.getValue()){
                    if(ge.equals(((JComboBox)c).getSelectedItem()))
                        milka.select(ge);
                }
            }
        }
    }
    
}
