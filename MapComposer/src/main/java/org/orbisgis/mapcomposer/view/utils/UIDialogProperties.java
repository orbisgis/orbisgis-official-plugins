package org.orbisgis.mapcomposer.view.utils;

import net.miginfocom.swing.MigLayout;
import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.sif.UIPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.util.List;

public class UIDialogProperties implements UIPanel {

    /** JPanel of the configuration elements. */
    private JComponent panel;

    private List<ConfigurationAttribute> caList;

    /** UIController. */
    private UIController uic;

    public UIDialogProperties(java.util.List<ConfigurationAttribute> list, UIController uic, boolean enableLock) {
        this.caList = list;
        this.uic=uic;

        //Adds to a panel the ConfigurationAttribute
        panel = new JPanel();
        panel.setLayout(new MigLayout("wrap 1"));
        for(ConfigurationAttribute ca : list){
            ConfPanel cp = new ConfPanel(uic.getCAManager().getRenderer(ca).createJComponentFromCA(ca), ca, enableLock);
            this.panel.add(cp, "wrap");
        }
    }

    @Override
    public URL getIconURL() {
        return null;
    }

    @Override
    public String getTitle() {
        return "Configuration";
    }

    @Override
    public String validateInput() {
        uic.validateCAList(caList);
        return null;
    }

    @Override
    public Component getComponent() {
        return panel;
    }


    /**
     * Extension of the JPanel used to display the ConfigurationAttributes.
     * It also permit to lock and unlock the fields.
     */
    private static class ConfPanel extends JPanel implements ItemListener {
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
