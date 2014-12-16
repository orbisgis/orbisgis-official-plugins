package org.orbisgis.mapcomposer.view.utils;

import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.beans.EventHandler;
import java.util.List;
import javax.swing.*;

import net.miginfocom.swing.MigLayout;

/**
 * Dialog windows displaying the GraphicalElements attributes.
 */
public class DialogProperties extends JFrame{
    
    /** JPanel containing the body of the dialog. */
    private final JPanel body;
    
    /** Validation button. */
    private final JButton validate;
    
    /** Cancel button. */
    private final JButton cancel;
    
    /** JPanel of the configuration elements. */
    private JPanel panel;

    private List<ConfigurationAttribute> caList;
    
    /** UIController. */
    private UIController uic;
    
    /** Main constructor.
     * @param list List of ConfigurationAttributes to display.
     * @param uic UIController.
     */
    public DialogProperties(List<ConfigurationAttribute> list, UIController uic, boolean enableLock){
        body = new JPanel(new MigLayout("wrap 2"));
        this.caList = list;
        this.uic=uic;
        this.setLayout(new BorderLayout());
        this.add(body, BorderLayout.CENTER);
        
        //Adds to a panel the ConfigurationAttribute
        panel = new JPanel();
        panel.setLayout(new MigLayout("wrap 1"));
        for(ConfigurationAttribute ca : list){
            ConfPanel cp = new ConfPanel(uic.getCAManager().getRenderer(ca).createJComponentFromCA(ca), ca, enableLock);
            this.panel.add(cp, "wrap");
        }
        body.add(panel, "wrap");
        
        
        //Adds the ok and cancel buttons
        validate = new JButton("Validate");
        validate.addActionListener(EventHandler.create(ActionListener.class, this, "validation"));
        cancel = new JButton("Cancel");
        cancel.addActionListener(EventHandler.create(ActionListener.class, this, "clearAndHide"));
        
        body.add(validate);
        body.add(cancel);

        this.setAlwaysOnTop(true);
        this.pack();
    }
    
    /**
     * Action executed when the ok button is clicked.
     */
    public void validation() {
        uic.validateCAList(caList);
        clearAndHide();
    }

    /** 
     * Clears the dialog and hides it.
     */
    public void clearAndHide() {
        panel = new JPanel();
        this.setContentPane(panel);
        setVisible(false);
    }
    
    /**
     * Extension of the JPanel used to display the ConfigurationAttributes.
     * It also permit to lock and unlock the fields.
     */
    private class ConfPanel extends JPanel implements ItemListener{
        private final JComponent component;
        private final ConfigurationAttribute ca;
        public ConfPanel(JComponent component, ConfigurationAttribute ca, boolean enableLock){
            super();
            this.component =component;
            this.ca = ca;
            this.setLayout(new FlowLayout(FlowLayout.LEFT));
            if(enableLock){
                JCheckBox box = new JCheckBox();
                box.addItemListener(this);
                box.setSelected(ca.getReadOnly());
                this.add(box);
            }
            this.add(component);
            if(ca.getReadOnly()){
                component.setEnabled(false);
                for(Component c : component.getComponents())
                    c.setEnabled(false);
            }
        }

        @Override
        public void itemStateChanged(ItemEvent ie) {
            boolean b = ((JCheckBox)ie.getSource()).isSelected();
            component.setEnabled(!b);
            for(Component c : component.getComponents())
                c.setEnabled(!b);
            ca.setReadOnly(b);
        }
    }
}
