package com.mapcomposer.view.ui;

import com.mapcomposer.controller.UIController;
import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.utils.CAManager;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import java.awt.Component;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

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
        //this.dispalyConfiguration(new MapImage());
    }
    
    /** 
     * Displays in the configurationShutter JPanel all the UI element for the configuration of he differents CA
     * @param ge GaphicalElement to configure.
     */
    public void dispalyConfiguration(GraphicalElement ge){
        this.selectedGE = ge;
        pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
        for(ConfigurationAttribute ca : ge.getAllAttributes()){
            pan.add(CAManager.getInstance().getRenderer(ca).render(ca));
        }
        pan.add(validate);
        JScrollPane listScroller = new JScrollPane(pan);
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
    
    /**
     * Returns the GE Selected
     * @param ge The selected GE
     */
    public void setSelected(GraphicalElement ge){
        selectedGE=ge;
    }

    private List<JPanel> getPanels() {
        ArrayList<JPanel> list = new ArrayList<>();
        for(Component comp : pan.getComponents()){
            //Verify if it's a JPanel
            if(comp instanceof JPanel){
                list.add((JPanel)comp);
            }
        }
        return list;
    }
    
    
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if(e.getSource()==validate){
            UIController.getInstance().validate(getPanels(), selectedGE);
        }
    }
}
