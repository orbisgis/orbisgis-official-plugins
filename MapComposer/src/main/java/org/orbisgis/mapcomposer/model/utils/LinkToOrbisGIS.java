/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
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
