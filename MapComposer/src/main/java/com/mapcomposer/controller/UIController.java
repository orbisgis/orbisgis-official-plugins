package com.mapcomposer.controller;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import com.mapcomposer.model.graphicalelement.utils.GEManager;
import com.mapcomposer.view.ui.CompositionArea;
import com.mapcomposer.view.ui.ConfigurationShutter;
import com.mapcomposer.view.utils.CompositionJPanel;
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
    /**Unique instance of the class*/
    private static UIController INSTANCE = null;
    /**Map doing the link between GraphicalElements and their CompositionJPanel*/
    private static Map<GraphicalElement, CompositionJPanel> map;
    
    /**
     * Main constructor.
     */
    private UIController(){
        map = new HashMap<>();
        //as example
        MapImage mi = new MapImage();
        //map.put(mi, new JPanel(new BorderLayout()));
        map.put(mi, new CompositionJPanel(mi));
        CompositionArea.getInstance().addGE(getPanel(mi));
        ConfigurationShutter.getInstance().setSelected(mi);
    }
    
    /**
     * Returns the CompositionPanel corresponding to a GraphicalElement.
     * @param ge GraphicalElement.
     * @return The CompositionPanel corresponding to the GraphicalElement.
     */
    public static CompositionJPanel getPanel(GraphicalElement ge){
        return map.get(ge);
    }
    
    /**
     * Returns the unique instance of the class.
     * @return The unique instance of the class.
     */
    public static UIController getInstance(){
        if(INSTANCE==null){
            INSTANCE=new UIController();
        }
        return INSTANCE;
    }

    /**
     * Read a List of JPanels to set the GraphicalElement ConfigurationAttribute.
     * This action done when the button validate of the ConfigurationShutter is clicked. 
     * @param panels List of panel to read.
     * @param ge GraphicalElement to set.
     */
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
        ConfigurationShutter.getInstance().close();
        map.get(ge).setPanel(GEManager.getInstance().render(ge.getClass()).render(ge));
    }
}
