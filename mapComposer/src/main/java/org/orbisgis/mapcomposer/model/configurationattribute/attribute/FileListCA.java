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

import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.RefreshCA;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

/**
 * This class represent a list of different files and extends the BaseListCA abstract class.
 * As a file can change (file moved, renamed, deleted ...) if also implements the RefreshCA interface to verify if the files contained still exist.
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.BaseListCA
 *
 * @author Sylvain PALOMINOS
 */
public class FileListCA extends BaseListCA<File> implements RefreshCA{

    /** Index of the value selected.*/
    private int index;

    /** Property itself. */
    private List<File> list;

    /**
     * Empty constructor used in the SaveHandler, on loading a project save.
     */
    public FileListCA(){
        super("void", false);
        this.list  = new ArrayList<>();
    }

    /**
     * Default constructor for the FileListCA.
     * @param name Name of the ConfigurationAttribute
     * @param readOnly Boolean to enable or not the modification of the CA
     */
    public FileListCA(String name, boolean readOnly){
        super(name, readOnly);
        this.index = -1;
        this.list  = new ArrayList<>();
    }

    @Override public void setValue(List<File> value) {
        list=new ArrayList<>();
        list.addAll(value);
    }

    @Override public List<File> getValue() {
        return list;
    }

    @Override public boolean isSameValue(ConfigurationAttribute configurationAttribute) {
        //If the two CA have the same selected value, return true.
        if(configurationAttribute instanceof ListCA){
            if(getSelected()==null)
                return false;
            return getSelected().equals(((ListCA) configurationAttribute).getSelected());
        }
        return false;
    }

    @Override public void       add(File value)       {list.add(value);}
    @Override public boolean    remove(File value)    {return this.list.remove(value);}

    @Override public void       select(File choice)   {index=list.indexOf(choice);}
    @Override public File       getSelected()         {
        //Verify if the index is correct. If not, return null.
        if(index>=0 && index<list.size())
            return list.get(index);
        else
            return null;
    }

    /**
     * Verify if the files path still right.
     * If the file doesn't exist, it's path is removed from the list.
     */
    @Override
    public void refresh(MainController uic) {
        List<File> listT = new ArrayList<>();
        //Add to a list all the wrong path
        for(File f : this.getValue()){
            if(!f.exists()){
                listT.add(f);
            }
        }
        //Remove the files path
        for(File file : listT){
            this.remove(file);
        }
    }

    @Override
    public void setField(String name, String value) {
        super.setField(name, value);
        if(name.equals("list")) {
            //Reconstruction of the list of Fle from the list of path.
            List<String> listString = Arrays.asList(value.split(","));
            for(String str : listString)
                this.list.add(new File(str));
        }
        else if(name.equals("index"))
            this.index=Integer.parseInt(value);
    }

    @Override
    public Map<String, Object> getAllFields() {
        Map ret = super.getAllFields();

        ret.put("index", index);
        //All the path are save as one unique string coma separated : "path1,path2,path3"
        String s="";
        for(File file : list)
            s+=file.getAbsolutePath()+",";
        if(list.size()>0)s=s.substring(0, s.length()-1);
        ret.put("list", s);

        return ret;
    }

    @Override
    public ConfigurationAttribute deepCopy() {
        FileListCA copy = new FileListCA();
        List<File> list = new ArrayList<>();
        for(File f : this.getValue())
            list.add(new File(f.getPath()));
        copy.setValue(list);
        copy.setName(this.getName());
        copy.setReadOnly(this.getReadOnly());
        copy.select(this.getSelected());
        return copy;
    }
}
