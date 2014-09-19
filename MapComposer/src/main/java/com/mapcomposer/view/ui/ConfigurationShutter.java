package com.mapcomposer.view.ui;

import com.mapcomposer.Configuration;
import com.mapcomposer.controller.UIController;
import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.utils.CAManager;
import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.ArrayList;
import java.util.List;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Lateral shutter containing the configuration of the selected GraphicalElements.
 */
public class ConfigurationShutter extends JPanel implements MouseListener{
    
    /**Unique instance of the class*/
    private static ConfigurationShutter INSTANCE = null;
    
    /** JPanel containing the body ofthe shutter */
    private final JPanel body;
    
    /** Validation button*/
    private final JButton validate;
    
    /** JPanel of the configuration elements */
    private JPanel pan;
    
    /** List of ConfPanel displayed */
    private List<ConfPanel> listPanels;
    
    /**Private constructor*/
    private ConfigurationShutter(){
        body = new JPanel(new BorderLayout());
        
        //Shutter creation.
        this.setMinimumSize(new Dimension(Configuration.defaultShutterSize, 0));
        this.setMaximumSize(new Dimension(Configuration.defaultShutterSize, 0));
        
        //Positionning the shutter
        this.setLayout(new BorderLayout());
        this.add(body, BorderLayout.CENTER);
        
        listPanels = new ArrayList<>();
        validate = new JButton("Validate");
        validate.addMouseListener(this);
        pan = new JPanel();
    }
    
    /** 
     * Displays in the configurationShutter JPanel all the UI element for the configuration of he differents CA
     * @param list List of ConfigurationAttributes to display.
     */
    public void dispalyConfiguration(List<ConfigurationAttribute> list){
        listPanels = new ArrayList<>();
        pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
        for(ConfigurationAttribute ca : list){
            JPanel panel = CAManager.getInstance().getRenderer(ca).render(ca);
            //It align the button to le left, but why ?
            panel.setAlignmentX(JPanel.TOP_ALIGNMENT);
            ConfPanel cp = new ConfPanel(panel, ca);
            listPanels.add(cp);
            pan.add(cp);
        }
        //Button panel
        JPanel button = new JPanel(new FlowLayout());
        button.add(validate);
        button.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        pan.add(button);
        this.setBodyPanel(pan);
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
    
    
    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource()==validate){
            List<ConfigurationAttribute> listca = new ArrayList<>();
            for(ConfPanel cp : listPanels){
                listca.add(cp.getCA());
            }
            UIController.getInstance().validate(listca);
        }
    }
    
    /**
     * Erases all the element displayed in the shutter and close it.
     */
    public void eraseConfiguration(){
        pan = new JPanel();
        listPanels = new ArrayList<>();
        this.setBodyPanel(pan);
    }

    @Override
    public void mousePressed(MouseEvent me) {}

    @Override
    public void mouseReleased(MouseEvent me) {}

    @Override
    public void mouseEntered(MouseEvent me) {}

    @Override
    public void mouseExited(MouseEvent me) {}
    
    /**
     * Extenson of the JPanel used to display the ConfigurationAttributes.
     * It also permite to lock and unlock the fields. 
     */
    private class ConfPanel extends JPanel implements ItemListener{
        private final JPanel pan;
        private final JCheckBox box;
        private final ConfigurationAttribute ca;
        public ConfPanel(JPanel pan, ConfigurationAttribute ca){
            super();
            this.pan=pan;
            this.ca = ca;
            this.setLayout(new BoxLayout(this, BoxLayout.PAGE_AXIS));
            box = new JCheckBox();
            box.addItemListener(this);
            box.setSelected(ca.isLocked());
            this.add(box);
            this.add(pan);
            if(ca.isLocked()){
                pan.setEnabled(false);
                for(Component c : pan.getComponents())
                    c.setEnabled(false);
            }
        }
        
        public ConfigurationAttribute getCA(){
            CAManager.getInstance().getRenderer(ca).extractValue(pan, ca);
            return ca;
        }

        @Override
        public void itemStateChanged(ItemEvent ie) {
            boolean b = ((JCheckBox)ie.getSource()).isSelected();
            pan.setEnabled(!b);
            for(Component c : pan.getComponents())
                c.setEnabled(!b);
            ca.setLock(b);
            this.repaint();
            this.revalidate();
        }
    }
    
    /**
     * Sets the JPanel of the body of the Shutter.
     * @param panel Body JPanel.
     */
    public void setBodyPanel(JPanel panel){
        body.removeAll();
        body.revalidate();
        body.repaint();
        JScrollPane pane = new JScrollPane(panel);
        body.add(pane);
    }
}
