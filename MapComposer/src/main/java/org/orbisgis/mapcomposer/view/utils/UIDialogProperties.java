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

import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.sif.UIPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class extends the UIPanel class an is used to display a popup windows allowing the user to configure GraphicalElements.
 * It can be constructed in two ways :
 * - By giving it the list of ConfigurationAttributes to configure and the class construct itself the displayed UI.
 * - Adding one by one the different Components and ConfigurationAttributes corresponding to set.
 */
public class UIDialogProperties implements UIPanel {

    /** JComponent that will be displayed. */
    private JComponent panel;

    /** List of the ConfigurationAttributes displayed in the panel. */
    private List<ConfigurationAttribute> caList;

    /** UIController. */
    private UIController uic;

    /**
     * Main constructor that build itself the Component to display from the caList of ConfigurationAttribute given
     * @param caList List of ConfigurationAttributes to set.
     * @param uic UIController.
     * @param enableLock If true, checkboxes enabling and disabling the ConfigurationAttributes configuration are displayed. They aren't if false;
     */
    public UIDialogProperties(java.util.List<ConfigurationAttribute> caList, UIController uic, boolean enableLock) {
        this.caList = caList;
        this.uic=uic;

        //Adds to a panel the ConfigurationAttribute
        panel = new JPanel();
        panel.setLayout(new GridBagLayout());
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx=1;
        for(ConfigurationAttribute ca : caList){
            List<Component> caComponent = new ArrayList<>();
            caComponent.add(new JLabel(ca.getName()));
            caComponent.add(uic.getCAManager().getRenderer(ca).createJComponentFromCA(ca));
            ConfPanel cp = new ConfPanel(caComponent, ca, enableLock);
            constraints.gridy=this.panel.getComponentCount();
            this.panel.add(cp, constraints);
        }
    }

    /**
     * This constructor must be followed by several calls to the method addComponent(List<>, ConfigurationAttribute, boolean) to constrcut the panel.
     * @param uic
     */
    public UIDialogProperties(UIController uic){
        this.caList = new ArrayList<>();
        this.uic = uic;
        this.panel = new JPanel();
        panel.setLayout(new GridBagLayout());
    }

    /**
     * Add the given ConfigurationAttributes and its representation to the UIDialogProperties.
     * @param component Component that will be displayed.
     * @param ca ConfigurationAttribute to configure.
     * @param enableLock If true, checkboxes enabling and disabling the ConfigurationAttributes configuration are displayed. They aren't if false;
     */
    public void addComponent(List<Component> component, ConfigurationAttribute ca, boolean enableLock){
        caList.add(ca);
        GridBagConstraints constraints = new GridBagConstraints();
        constraints.fill = GridBagConstraints.HORIZONTAL;
        constraints.weightx=1;
        constraints.gridy=this.panel.getComponentCount();
        panel.add(new ConfPanel(component, ca, enableLock), constraints);
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
        private final List<Component> component;
        private final ConfigurationAttribute ca;
        public ConfPanel(List<Component> component, ConfigurationAttribute ca, boolean enableLock){
            super();
            this.component = component;
            this.ca = ca;
            this.setLayout(new FlowLayout(FlowLayout.LEFT));
            if(enableLock){
                JCheckBox box = new JCheckBox();
                box.addItemListener(this);
                box.setSelected(ca.getReadOnly());
                this.add(box);
            }
            for(Component comp : component){
                this.add(comp);
            }
            if(ca.getReadOnly()){
                this.setEnabled(false);
                for(Component c : component)
                    c.setEnabled(false);
            }
        }

        @Override
        public void itemStateChanged(ItemEvent ie) {
            boolean b = ((JCheckBox)ie.getSource()).isSelected();
            this.setEnabled(!b);
            for(Component c : component)
                enableAll(!b, c);
            ca.setReadOnly(b);
        }

        /**
         * Recursive call to enable or not all th components contained by a JComponent.
         * @param enable
         * @param component
         */
        private void enableAll(boolean enable, Component component){
            component.setEnabled(enable);
            if(component instanceof JComponent)
            for(Component comp : ((JComponent)component).getComponents())
                enableAll(enable, comp);
        }
    }
}
