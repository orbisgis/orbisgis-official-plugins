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

package org.orbisgis.mapcomposer.model.utils;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.orbisgis.corejdbc.DataManager;
import org.orbisgis.viewapi.workspace.ViewWorkspace;
import org.orbisgis.viewapi.edition.EditorManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;

/**
 * Class giving a link to OrbisGIS.
 * It give an access to usefull classes instance of OrbisGIS.
 */
public class LinkToOrbisGIS {
    /** Unique instance of the class*/
    private static LinkToOrbisGIS INSTANCE = null;
    /** BundleContext given to the plugin in the activator*/
    private static BundleContext bc;
    
    /**
     * Main constructor
     */
    private LinkToOrbisGIS(){
    }
    
    /**
     * Sets the BundleContext used to get the acess to the services registrered by OrbisGIS.
     * @param bc BundleContext given to the plugin in the Activator
     */
    public static void setBundleContext(BundleContext bc){
        LinkToOrbisGIS.bc = bc;
    }
    
    /**
     * Returns the unique instance of the Object.
     * If it doesn't existe, it's created.
     * @return Class unique instance.
     */
    public static LinkToOrbisGIS getInstance(){
        if(INSTANCE==null){
            INSTANCE = new LinkToOrbisGIS();
        }
        return INSTANCE;
    }
    
    /**
     * Return the DataManager Registered by OrbisGIS.
     * @return OrbisGIS DataManager.
     */
    public DataManager getDataManager(){
        return bc.getService(bc.getServiceReference(DataManager.class));
    }
    
    /**
     * Return the ViewWorkspace Registered by OrbisGIS.
     * @return OrbisGIS ViewWorkspace.
     */
    public ViewWorkspace getViewWorkspace(){
        return bc.getService(bc.getServiceReference(ViewWorkspace.class));
    }
    
    /**
     * Return the EditorManager Registered by OrbisGIS.
     * @return OrbisGIS EditorManager.
     */
    public EditorManager getEditorManager(){
        return bc.getService(bc.getServiceReference(EditorManager.class));
    }
}
