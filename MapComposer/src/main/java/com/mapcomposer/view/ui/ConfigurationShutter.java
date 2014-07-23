package com.mapcomposer.view.ui;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.utils.CAManager;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextField;

/**
 * Lateral shutter containing the configuration elements.
 */
public class ConfigurationShutter extends Shutter implements MouseListener{
    
    /**Unique instance of the class*/
    private static ConfigurationShutter INSTANCE = null;
    
    /**GraphicalElement selected in the UI */
    private GraphicalElement selectedGE;
    
    /** Validation button*/
    private final JButton validate;
    
    /** JPanel of the configuration elements */
    private JPanel pan;
    
    /**Private constructor*/
    private ConfigurationShutter(){
        super(300, Shutter.LEFT_SHUTTER);
        validate = new JButton("Validate");
        validate.addMouseListener(this);
        pan = new JPanel();
        //Just as example
        this.dispalyConfiguration(new Orientation());
    }
    
    /** 
     * Displays in the configurationShutter JPanel all the UI element for the configuration of he differents CA
     * @param ge GaphicalElement to configure.
     */
    public void dispalyConfiguration(GraphicalElement ge){
        this.selectedGE = ge;
        pan = new JPanel();
        pan.setName("body");
        pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
        for(ConfigurationAttribute ca : ge.getAllAttributes()){
            pan.add(CAManager.getInstance().getRenderer(ca).render(ca));
        }
        pan.add(validate);
        JScrollPane listScroller = new JScrollPane(pan);
        listScroller.setName("scroll");
        this.getBodyPanel().add(listScroller);
    }
    
    /**
     * Returns the unique instace of the class.
     * @return The unique instance of te class.
     */
    public static ConfigurationShutter getInstance(){
        if(INSTANCE==null){
            INSTANCE = new ConfigurationShutter();
        }
        return INSTANCE;
    }
    
    /**
     * Returns the GE Selected
     * @return The selected GE
     */
    public GraphicalElement getSelected(){
        return selectedGE;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if(e.getSource()==validate){
            this.validateConfiguration();
        }
    }
    
    /**
     * Validates the configuration and register it into the GE.
     * @TODO change the access to the values of the CA displayed.
     */
    private void validateConfiguration(){
        ConfigurationAttribute ca = null;
        //Takes every components of the pan object
        for(Component comp : pan.getComponents()){
            //Verify if it's a JPanel
            if(comp instanceof JPanel){
                //Takes every components ofthe JPanel
                for(Component c : ((JPanel)comp).getComponents()){
                    //Test if it's a JLabel containing the name of the field
                    if(c instanceof JLabel){
                        for(ConfigurationAttribute conf : selectedGE.getAllAttributes()){
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
                            ca.setValue(((JComboBox)c).getModel().getSelectedItem());
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
        //Close the Shutter
        this.toggle();
        selectedGE=null;
    }
}
