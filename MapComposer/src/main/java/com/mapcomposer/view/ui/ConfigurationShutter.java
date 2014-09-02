package com.mapcomposer.view.ui;

import com.mapcomposer.controller.UIController;
import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.utils.CAManager;
import java.awt.Component;
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

/**
 * Lateral shutter containing the configuration elements.
 */
public class ConfigurationShutter extends Shutter implements MouseListener{
    
    /**Unique instance of the class*/
    private static ConfigurationShutter INSTANCE = null;
    
    /** Validation button*/
    private final JButton validate;
    /** Remove button*/
    private final JButton remove;
    /** To the front button*/
    private JButton toFront;
    /** Front button*/
    private JButton front;
    /** To the back button*/
    private JButton toBack;
    /** Back button*/
    private JButton back;
    
    /** JPanel of the configuration elements */
    private JPanel pan;
    
    /** List of ConfPanel displayed */
    private List<ConfPanel> listPanels;
    
    /**Private constructor*/
    private ConfigurationShutter(){
        super();
        listPanels = new ArrayList<>();
        validate = new JButton("Validate");
        validate.addMouseListener(this);
        remove = new JButton("Remove");
        remove.addMouseListener(this);
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
        //ZIndex panel
        JPanel zindex = new JPanel(new FlowLayout());
        toFront = new JButton("To the front");
        toFront.addMouseListener(this);
        front = new JButton("Front");
        front.addMouseListener(this);
        toBack = new JButton("To the back");
        toBack.addMouseListener(this);
        back = new JButton("Back");
        back.addMouseListener(this);
        zindex.add(toFront);
        zindex.add(front);
        zindex.add(toBack);
        zindex.add(back);
        zindex.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        pan.add(zindex);
        //Button panel
        JPanel button = new JPanel(new FlowLayout());
        button.add(remove);
        button.add(validate);
        button.setAlignmentX(JPanel.LEFT_ALIGNMENT);
        pan.add(button);
        this.setBodyPanel(pan);
        this.open();
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
            eraseConfiguration();
        }
        if(e.getSource()==remove){
            UIController.getInstance().remove();
            eraseConfiguration();
        }
        if(e.getSource()==toFront){
            UIController.getInstance().zindexChange(2);
        }
        if(e.getSource()==front){
            UIController.getInstance().zindexChange(1);
        }
        if(e.getSource()==toBack){
            UIController.getInstance().zindexChange(-2);
        }
        if(e.getSource()==back){
            UIController.getInstance().zindexChange(-1);
        }
    }
    
    /**
     * Erases all the element displayed in the shutter and close it.
     */
    public void eraseConfiguration(){
        pan = new JPanel();
        listPanels = new ArrayList<>();
        this.setBodyPanel(pan);
        this.close();
    }

    @Override
    public void mousePressed(MouseEvent me) {}

    @Override
    public void mouseReleased(MouseEvent me) {}

    @Override
    public void mouseEntered(MouseEvent me) {}

    @Override
    public void mouseExited(MouseEvent me) {}
    
    
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
                pan.disable();
                for(Component c : pan.getComponents())
                    c.disable();
            }
        }
        
        public ConfigurationAttribute getCA(){
            CAManager.getInstance().getRenderer(ca).extractValue(pan, ca);
            return ca;
        }

        @Override
        public void itemStateChanged(ItemEvent ie) {
            if(!((JCheckBox)ie.getSource()).isSelected()){
                pan.enable();
                for(Component c : pan.getComponents())
                    c.enable();
                ca.unlock();
            }
            if(((JCheckBox)ie.getSource()).isSelected()){
                pan.disable();
                for(Component c : pan.getComponents())
                    c.disable();
                ca.lock();
            }
            this.repaint();
            this.revalidate();
        }
    }
}
