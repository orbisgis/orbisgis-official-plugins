/*
* MapComposer is an OrbisGIS plugin dedicated to the creation of cartographic
* documents based on OrbisGIS results.
*
* The MapComposer plugin is distributed under GPL 3 license. It is produced by the "Atelier SIG"
* team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
*
* Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
*
* This file is part of the MapComposer plugin.
*
* The MapComposer plugin is free software: you can redistribute it and/or modify it under the
* terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
*
* The MapComposer plugin is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
* A PARTICULAR PURPOSE. See the GNU General Public License for more details <http://www.gnu.org/licenses/>.
*/

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
