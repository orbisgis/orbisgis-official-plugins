package com.mapcomposer.controller;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import com.mapcomposer.model.graphicalelement.utils.GEManager;
import com.mapcomposer.view.ui.CompositionArea;
import com.mapcomposer.view.ui.ConfigurationShutter;
import java.awt.BorderLayout;
import java.awt.Component;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 * This class manager the interaction between the user, the UI and the data model.
 */
public class UIController{
    private static UIController INSTANCE = null;
    private static Map<GraphicalElement, JPanel> map;
    
    private UIController(){
        map = new HashMap<>();
        //as example
        MapImage mi = new MapImage();
        map.put(mi, new JPanel(new BorderLayout()));
        CompositionArea.getInstance().addGE(getPanel(mi));
        ConfigurationShutter.getInstance().dispalyConfiguration(mi);
    }
    
    public static JPanel getPanel(GraphicalElement ge){
        return map.get(ge);
    }
    
    public static UIController getInstance(){
        if(INSTANCE==null){
            INSTANCE=new UIController();
        }
        return INSTANCE;
    }

    public void validate(List<JPanel> panels, GraphicalElement ge) {
        ConfigurationAttribute ca=null;
            for(JPanel panel : panels){
                //Takes every components ofthe JPanel
                for(Component c : panel.getComponents()){
                    //Test if it's a JLabel containing the name of the field
                    if(c instanceof JLabel){
                        for(ConfigurationAttribute conf : ge.getAllAttributes()){
                            //The text of the label is compared to the CA of the GE selected
                            if(conf.getName().equals(((JLabel)c).getText())){
                                //Save the CA to set
                                ca = conf;
                                break;
                            }
                        }
                    }
                    //If the CA was fineded before
                    if(ca!=null){
                        //Test if to know where the new CA value should be fined
                        if(c instanceof JComboBox){
                            ca.setValue(((JComboBox)c).getModel().getSelectedItem().toString());
                            ca=null;
                            break;
                        }
                        if(c instanceof JSpinner){
                            ca.setValue(((JSpinner)c).getValue());
                            ca=null;
                            break;
                        }
                        if(c instanceof JTextField){
                            ca.setValue(((JTextField)c).getText());
                            ca=null;
                            break;
                        }
                    }
                }
            }
        ConfigurationShutter.getInstance().toggle();
        ConfigurationShutter.getInstance().setSelected(null);
        CompositionArea.getInstance().addGE(GEManager.getInstance().render(ge.getClass()).render(ge));
    }
}
