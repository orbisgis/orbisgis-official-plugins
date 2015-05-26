/*
* MapComposer is an OrbisGIS plugin dedicated to the creation of cartographic
* documents based on OrbisGIS results.
*
* This plugin is developed at French IRSTV institute as part of the MApUCE project,
* funded by the French Agence Nationale de la Recherche (ANR) under contract ANR-13-VBDU-0004.
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
import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.RefreshCA;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GERefresh;
import org.orbisgis.sif.UIPanel;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * This class extends the UIPanel class and it is used to display a popup window allowing the user to configure GraphicalElements.
 * It can be constructed by two ways :
 *
 * - The UI can be constructed automatically with the list of ConfigurationAttributes to display
 *      @see UIDialogProperties#UIDialogProperties(java.util.List, org.orbisgis.mapcomposer.controller.MainController, boolean)
 *
 * - The user can only create the UIDialogProperties and then add each component of the UI specificating their size and position.
 *      @see UIDialogProperties#UIDialogProperties(org.orbisgis.mapcomposer.controller.MainController, boolean)
 *      @see UIDialogProperties#addComponent(java.awt.Component, org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute, int, int, int, int)
 *
 * @author Sylvain PALOMINOS
 */
public class UIDialogProperties implements UIPanel {

    /** JComponent that will be displayed. */
    private JComponent panel;

    /** List of the ConfigurationAttributes displayed in the panel. */
    private List<ConfigurationAttribute> caList;

    /** MainController. */
    private MainController mainController;

    /** Boolean that specify if the UI should display the EnableCheckBox. */
    private boolean enableLock;

    /** List of EnableCheckBox.*/
    private List<EnableCheckBox> listEnableCheckBox;

    /** Object for the translation*/
    private static final I18n i18n = I18nFactory.getI18n(UIDialogProperties.class);

    /**
     * Main constructor that build itself the UI from the given list of ConfigurationAttributes.
     * @param caList List of ConfigurationAttributes to display.
     * @param mainController UIController.
     * @param enableLock If true, checkboxes enabling and disabling the ConfigurationAttributes configuration are displayed. They aren't if false;
     */
    public UIDialogProperties(List<ConfigurationAttribute> caList, MainController mainController, boolean enableLock) {
        this.caList = caList;
        this.mainController = mainController;
        this.panel = new JPanel();
        this.enableLock = enableLock;
        this.listEnableCheckBox = new ArrayList<>();

        //There is only 2 elements per line : the ConfigurationAttribute name and its representation
        int colNum = 2;
        //There is 3 element per line if the EnableCheckBox should be displayed.
        if(enableLock){
            colNum++;
        }
        panel.setLayout(new MigLayout());

        for(int i=0; i<caList.size(); i++){
            if(caList.get(i) instanceof RefreshCA)
                ((RefreshCA)caList.get(i)).refresh(mainController);
            JLabel name = new JLabel(i18n.tr(caList.get(i).getName()));
            JComponent component = mainController.getCAManager().getRenderer(caList.get(i)).createJComponentFromCA(caList.get(i));
            if(enableLock) {
                EnableCheckBox checkBox = new EnableCheckBox(caList.get(i));
                listEnableCheckBox.add(checkBox);
                panel.add(checkBox, "cell 0 "+i+" 1 1");
                checkBox.addComponent(name);
                checkBox.addComponent(component);
            }
            //Adds the elements at the row i and at the good column
            panel.add(name, "cell "+(colNum-2)+" "+i+" 1 1");
            panel.add(component, "cell "+(colNum-2)+" "+i+" 1 1");
        }
    }

    /**
     * This constructor must be followed by several calls to the method addComponent(ConfigurationAttribute, Component, int, int, int, int) to construct the panel.
     * @param mainController
     * @param enableLock If true, checkboxes enabling and disabling the ConfigurationAttributes configuration are displayed. They aren't if false;
     */
    public UIDialogProperties(MainController mainController, boolean enableLock){
        this.caList = new ArrayList<>();
        this.mainController = mainController;
        this.panel = new JPanel();
        this.enableLock = enableLock;
        this.listEnableCheckBox = new ArrayList<>();
        panel.setLayout(new MigLayout());
    }

    /**
     * This methods add the given Component (associated to the given ConfigurationAttribute) at the position x,y with the size width,height.
     * @param component Component to add.
     * @param ca ConfigurationAttribute associated to the Component
     * @param x X position of the component
     * @param y Y position of the component
     * @param width Width of the component
     * @param height Height of the component
     */
    public void addComponent(Component component, ConfigurationAttribute ca, int x, int y, int width, int height){
        if(!caList.contains(ca)){
            caList.add(ca);
        }
        if(enableLock){
            x++;
            //Check if the ConfigurationAttribute already has an EnableCheckBox
            boolean isCheckBox = false;
            for(EnableCheckBox checkBox : listEnableCheckBox) {
                if(checkBox.getCA() == ca) {
                    isCheckBox = true;
                }
            }
            //If not, create one
            if(!isCheckBox){
                EnableCheckBox enableCheckBox = new EnableCheckBox(ca);
                listEnableCheckBox.add(enableCheckBox);
                panel.add(enableCheckBox, "cell 0 " + y + " 1 1");
            }
        }
        panel.add(component, "cell "+x+" "+y+" "+width+" "+height);
        //If the ConfigurationAttribute has an EnableCheckBox, add the component to it.
        for(EnableCheckBox checkBox : listEnableCheckBox){
            if(checkBox.getCA() == ca){
                checkBox.addComponent(component);
            }
        }
    }

    @Override
    public URL getIconURL() {
        return null;
    }

    @Override
    public String getTitle() {
        return i18n.tr("Configuration");
    }

    @Override
    public String validateInput() {
        mainController.validateCAList(caList);
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
    private static class EnableCheckBox extends JCheckBox implements ItemListener {
        private final List<Component> componentList;
        private final ConfigurationAttribute ca;

        public EnableCheckBox(ConfigurationAttribute ca){
            super();
            this.componentList = new ArrayList<>();
            this.ca = ca;
            this.addItemListener(this);
            this.setSelected(ca.getReadOnly());
            this.setIcon(MapComposerIcon.getIcon("lock_open"));
            this.setSelectedIcon(MapComposerIcon.getIcon("lock_close"));
        }

        /**
         * Adds a Component to this EnableCheckBox to enable or disable it.
         * @param component
         */
        public void addComponent(Component component){
            if(!componentList.contains(component)){
                componentList.add(component);
                enableAll(!this.isSelected(), component);
            }
        }

        /**
         * Returns the ConfigurationAttribute associated.
         * @return the ConfigurationAttribute associated.
         */
        public ConfigurationAttribute getCA(){
            return ca;
        }

        @Override
        public void itemStateChanged(ItemEvent ie) {
            boolean b = this.isSelected();
            for(Component c : componentList)
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
