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

package org.orbisgis.mapcomposer;

import org.orbisgis.corejdbc.DataManager;
import org.orbisgis.mainframe.api.MainFrameAction;
import org.orbisgis.mapcomposer.view.ui.MainWindow;
import org.orbisgis.mapcomposer.view.utils.MapComposerIcon;
import org.orbisgis.sif.components.actions.DefaultAction;
import org.orbisgis.wkguiapi.ViewWorkspace;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.LoggerFactory;

import javax.swing.Action;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * @author Sylvain PALOMINOS
 */

@Component
public class Activator implements MainFrameAction {

    public static final String MENU_MAPCOMPOSER = "MapComposer";

    /** Main frame of the MapComposer*/
    private MainWindow mainWindow;

    /** ConfigurationAdmin instance */
    private ConfigurationAdmin configurationAdmin;

    private DataManager dataManager;
    private ViewWorkspace viewWorkspace;

    private Dictionary<String, Object> properties;

    @Override
    public List<Action> createActions(org.orbisgis.mainframe.api.MainWindow target) {
        List<Action> actions = new ArrayList<>();
        actions.add(new DefaultAction(MENU_MAPCOMPOSER, "M&ap Composer",
                        MapComposerIcon.getIcon("map_composer"),
                        EventHandler.create(ActionListener.class, this, "showMapComposer")
                ).setParent(MENU_TOOLS));
        return actions;
    }

    @Override
    public void disposeActions(org.orbisgis.mainframe.api.MainWindow target, List<Action> actions) {
    }


    @Reference
    protected void setDataManager(DataManager dataManager) {
        this.dataManager = dataManager;
    }

    @Reference
    protected void setConfigurationAdmin(ConfigurationAdmin configurationAdmin){
        this.configurationAdmin = configurationAdmin;
        try {
            properties = configurationAdmin.getConfiguration(Activator.class.getName()).getProperties();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Reference
    protected void setViewWorkspace(ViewWorkspace viewWorkspace) {
        this.viewWorkspace = viewWorkspace;
    }

    protected void unsetDataManager(DataManager dataManager) {
        this.mainWindow.setDataManager(null);
    }

    protected void unsetViewWorkspace(ViewWorkspace viewWorkspace) {
        this.mainWindow.setViewWorkspace(null);
    }

    protected void unsetConfigurationAdmin(ConfigurationAdmin configurationAdmin){
        this.mainWindow.setConfigurationAdmin(null);
    }

    @Deactivate
    public void close(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        mainWindow.writeLayoutInByteArrayOutputStream(byteArrayOutputStream);
        try {
            Configuration configuration = configurationAdmin.getConfiguration(Activator.class.getName());
            Dictionary<String, Object> props = configuration.getProperties();
            if (props == null) {
                props = new Hashtable<>();
            }
            props.put("window_width", mainWindow.getWidth());
            props.put("window_height", mainWindow.getHeight());
            props.put("window_x", mainWindow.getX());
            props.put("window_y", mainWindow.getY());
            props.put("unit", mainWindow.getCompositionArea().getUnit());
            props.put("layout", byteArrayOutputStream.toByteArray());
            configuration.update(props);
        } catch (IOException e) {
            LoggerFactory.getLogger(Activator.class).error(e.getMessage());
        }
        mainWindow.close();
    }

    public void showMapComposer(){
        //If the mainWindow hasn't been instantiate, do it and configure it.
        if(mainWindow == null){
            mainWindow = new MainWindow();
            mainWindow.setDataManager(this.dataManager);
            mainWindow.setViewWorkspace(this.viewWorkspace);
            mainWindow.setConfigurationAdmin(this.configurationAdmin);
            mainWindow.constructUI();
        }
        mainWindow.setVisible(true);
        //Load the DockingFrames configuration
        if(properties != null) {
            mainWindow.setBounds((Integer) properties.get("window_x"), (Integer) properties.get("window_y"), (Integer) properties.get("window_width"), (Integer) properties.get("window_height"));
            mainWindow.getCompositionArea().configure((Integer) properties.get("unit"));
            mainWindow.configure((byte[]) properties.get("layout"));
        }
    }
}
