package com.mapcomposer.controller;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.view.ui.CompositionArea;
import com.mapcomposer.view.ui.ConfigurationShutter;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 * This class manager the interaction between the user, the UI and the data model.
 */
public class UIController implements MouseListener{
    private static UIController INSTANCE = null;
    
    private UIController(){
        
    }
    
    public static UIController getInstance(){
        if(INSTANCE==null){
            INSTANCE=new UIController();
        }
        return INSTANCE;
    }

    @Override
    public void mouseClicked(MouseEvent me) {
        ConfigurationAttribute ca=null;
        if(me.getSource()==ConfigurationShutter.getInstance()){
            GraphicalElement ge = ConfigurationShutter.getInstance().getSelected();
            for(JPanel panel : ConfigurationShutter.getInstance().getPanels()){
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
        }
        ConfigurationShutter.getInstance().toggle();
        ConfigurationShutter.getInstance().setSelected(null);
    }

    @Override
    public void mousePressed(MouseEvent me) {
        //Unused
    }

    @Override
    public void mouseReleased(MouseEvent me) {
        //Unused
    }

    @Override
    public void mouseEntered(MouseEvent me) {
        //Unused
    }

    @Override
    public void mouseExited(MouseEvent me) {
        //Unused
    }
}
