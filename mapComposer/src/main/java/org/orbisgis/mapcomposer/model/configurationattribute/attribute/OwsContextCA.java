/*
* MapComposer is an OrbisGIS plugin dedicated to the creation of cartographic
* documents.
*
* This plugin was firstly developed  at French IRSTV institute as part of the MApUCE project,
* funded by the French Agence Nationale de la Recherche (ANR) under contract ANR-13-VBDU-0004.
* 
* Since 2015, MapComposer is developed and maintened by the GIS group of the DECIDE team of the 
* Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
*
* The GIS group of the DECIDE team is located at :
*
* Laboratoire Lab-STICC – CNRS UMR 6285
* Equipe DECIDE
* UNIVERSITÉ DE BRETAGNE-SUD
* Institut Universitaire de Technologie de Vannes
* 8, Rue Montaigne - BP 561 56017 Vannes Cedex
*
* Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
* Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
*
* The MapComposer plugin is distributed under GPL 3 license. 
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

package org.orbisgis.mapcomposer.model.configurationattribute.attribute;

import org.orbisgis.commons.progress.NullProgressMonitor;
import org.orbisgis.corejdbc.DataManager;
import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.RefreshCA;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.layerModel.OwsMapContext;
import org.orbisgis.mapeditorapi.MapEditorExtension;
import org.orbisgis.wkguiapi.ViewWorkspace;
import org.slf4j.LoggerFactory;

/**
 * This class represent a list of path to OwsMapContext files created by the user in OrbisGIS.
 * The OwsMapContexts are saved in the workspace. The class use the OrbisGIS functions (thanks to the LinkToOrbisGIS class) to get and load them.
 * It contains all the information of the map and permit to get the map rendering, the scale value ...
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.BaseListCA
 *
 * @author Sylvain PALOMINOS
 */
public class OwsContextCA extends BaseListCA<String> implements RefreshCA{
    /** Index of the value selected.*/
    private int index;

    /** Property itself */
    private List<String> list;

    /** OwsMapContext corresponding to the path selected in the list.*/
    private OwsMapContext omc;

    private DataManager dataManager;
    private ViewWorkspace viewWorkspace;
    private MapEditorExtension mapEditorExtension;

    /**
     * Empty constructor used in the SaveHandler, on loading a project save.
     */
    public OwsContextCA(){
        super("void", false);
        this.list  = new ArrayList<>();
        dataManager = null;
        viewWorkspace = null;
    }

    /**
     * Default constructor for the OwsContextCA.
     * @param name Name of the ConfigurationAttribute
     * @param readOnly Boolean to enable or not the modification of the CA
     */
    public OwsContextCA(String name, boolean readOnly){
        super(name, readOnly);
        list = new ArrayList<>();
        index = 0;
        omc = null;
        dataManager = null;
        viewWorkspace = null;
    }

    /**
     * Return the OwsMapContext contained represented by the selected path in the list.
     * @return The OwsMapContext selected
     */
    public OwsMapContext getOwsMapContext(){return omc;}

    @Override
    public void refresh(MainController mainController) {
        if(mainController!= null) {
            if (dataManager == null)
                dataManager = mainController.getDataManager();
            if (viewWorkspace == null)
                viewWorkspace = mainController.getViewWorkspace();
            if (mapEditorExtension == null)
                mapEditorExtension = mainController.getMapEditorExtension();
        }
        if(dataManager == null || viewWorkspace == null || mapEditorExtension == null)
            return;
        //Refresh of the file list
        loadListFiles();
        //Refresh of the selected file
        if(omc==null)
            omc = new OwsMapContext(dataManager);
        reloadSelectedOMC();
    }
    
    /**
     * Reload the OWS-Context corresponding to the value of ConfigurationAttribute. 
     * @throws FileNotFoundException 
     */
    public void reloadSelectedOMC(){
        if (index != -1) {
            try {
                if (omc.isOpen())
                    omc.close(new NullProgressMonitor());
                omc.read(new FileInputStream(new File(getSelected())));
                omc.open(new NullProgressMonitor());
            } catch (LayerException|FileNotFoundException ex) {
                LoggerFactory.getLogger(OwsContextCA.class).error(ex.getMessage());
            }
        }
    }
    
    /**
     * Go to the workspace and add to the list all the .ows files.
     */
    private void loadListFiles(){
        File workspace = new File(viewWorkspace.getCoreWorkspace().getWorkspaceFolder()+"/maps/");
        //save the selected path
        String s = getSelected();
        list=new ArrayList<>();
        //add all the files in the maps folder of the workspace
        list.addAll(getOwsInFolder(workspace));
        //add all the map contains in the MapsManager folders
        for(String path : mapEditorExtension.getMapsManagerData().getMapCatalogFolderList()){
            list.addAll(getOwsInFolder(new File(path)));
        }
        //try to reselect the path saved
        select(s);
    }

    public List<String> getOwsInFolder(File folder){
        List<String> listOws = new ArrayList<>();
        //Test if the folder is not null and if it exists
        if(folder == null || !folder.exists())
            return listOws;
        for(File f : folder.listFiles()){
            if(f.isDirectory()){
                listOws.addAll(getOwsInFolder(f));
            }
            if(f.isFile()){
                if(f.getName().toLowerCase().endsWith(".ows")){
                    listOws.add(f.getAbsolutePath());
                }
            }
        }
        return listOws;
    }

    @Override
    public void add(String value) {list.add(value);}

    @Override
    public String getSelected() {
        if(index>=0 && index<list.size())
            return list.get(index);
        else
            return null;
    }

    @Override
    public void select(String choice) {index=list.indexOf(choice);}

    @Override
    public boolean remove(String value) {return list.remove(value);}

    @Override
    public void setValue(List<String> value) {this.list=value;}

    @Override
    public List<String> getValue() {return list;}

    @Override public boolean isSameValue(ConfigurationAttribute configurationAttribute) {
        if(configurationAttribute instanceof ListCA){
            if(getSelected()!=null)
                return getSelected().equals(((ListCA) configurationAttribute).getSelected());
            else
                return (getSelected()==null && ((ListCA) configurationAttribute).getSelected()==null);
        }
        return false;
    }

    @Override
    public void setField(String name, String value) {
        super.setField(name, value);
        if(name.equals("list"))
            list= Arrays.asList(value.split(","));
        if(name.equals("index"))
            index=Integer.parseInt(value);
    }

    @Override
    public Map<String, Object> getAllFields() {
        Map ret = super.getAllFields();
        ret.put("index", index);
        String s="";
        for(String str : list)
            s+=str+",";
        if(list.size()>0)s=s.substring(0, s.length()-1);
        ret.put("list", s);
        return ret;
    }

    @Override
    public ConfigurationAttribute deepCopy() {
        OwsContextCA copy = new OwsContextCA();
        List<String> list = new ArrayList<>();
        for(String s : this.getValue())
            list.add(s);
        copy.setValue(list);
        copy.setReadOnly(this.getReadOnly());
        copy.setName(this.getName());
        copy.select(this.getSelected());
        copy.viewWorkspace = viewWorkspace;
        copy.dataManager = dataManager;
        copy.refresh(null);

        return copy;
    }
}
