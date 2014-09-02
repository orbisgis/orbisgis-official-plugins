package com.mapcomposer.view.configurationattribute;

import com.mapcomposer.controller.UIController;
import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.LinkToMapImage;
import com.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        
        final LinkToMapImage ltmi = (LinkToMapImage)ca;
        
        pan.add(new JLabel(ltmi.getName()));
        final JComboBox list = new JComboBox(ltmi.getValue().toArray(new MapImage[0]));
        list.addActionListener(new ActionListener() {
            @Override public void actionPerformed(ActionEvent ae) {
                System.out.println("action : "+ae.getActionCommand());
                UIController.getInstance().getGEMap().get(list.getModel().getSelectedItem()).hightlight();
            }
        });
        pan.add(list);
        return pan;
    }
        

    @Override
    public void extractValue(JPanel panel, ConfigurationAttribute attribute) {
        LinkToMapImage ltmi = (LinkToMapImage)attribute;
        for(Component c : panel.getComponents()){
            if(c instanceof JComboBox){
                int i=1;
                for(Object ge : UIController.getInstance().getGEMap().keySet().toArray()){
                    System.out.println(ge);
                    if(ge instanceof MapImage){
                        System.out.println("mapImage");
                        if(i==((JComboBox)c).getItemCount()){
                            ltmi.select(((MapImage)ge));
                            System.out.println("setted");
                            break;
                        }
                        i++;
                    }
                }
            }
        }
    }
    
}
