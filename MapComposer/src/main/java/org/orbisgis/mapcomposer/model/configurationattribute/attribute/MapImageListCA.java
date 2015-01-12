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

package org.orbisgis.mapcomposer.model.configurationattribute.attribute;

import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.RefreshCA;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represent a list of different MapImage GraphicalElement and extends the BaseListCA interface.
 * As the list of MapImage can change, it implements the RefreshCA interface to permit to verify the list.
 * @see org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.BaseListCA
 *
 * @author Sylvain PALOMINOS
 */
public class MapImageListCA extends BaseListCA<MapImage> implements RefreshCA{

    /** Index of the value selected.*/
    private int index;

    /** Property itself. */
    private List<MapImage> list;

    /**
     * Empty constructor used in the SaveHandler, on loading a project save.
     */
    public MapImageListCA(){
        super("void", false);
        this.list  = new ArrayList<>();
    }

    /**
     * Default constructor for the MapImageListCA
     * @param name Name of the ConfigurationAttribute
     * @param readOnly Boolean to enable or not the modification of the CA
     */
    public MapImageListCA(String name, boolean readOnly){
        super(name, readOnly);
        index = -1;
        list = new ArrayList<>();
    }
        
    @Override public void setValue(List<MapImage> value) {this.list=value;}
    @Override public List<MapImage> getValue() {return list;}

    @Override public boolean isSameValue(ConfigurationAttribute configurationAttribute) {
        if(configurationAttribute instanceof ListCA){
            if(getSelected()==null)
                return false;
            return getSelected().equals(((ListCA) configurationAttribute).getSelected());
        }
        return false;
    }

    @Override public void       add(MapImage value)       {list.add(value);}
    @Override public boolean    remove(MapImage value)    {return this.list.remove(value);}

    @Override public void       select(MapImage choice)   {index=list.indexOf(choice);}
    @Override public MapImage getSelected(){
        //Verify if the index is correct. If not, return null.
        if(index>=0 && index<list.size())
            return list.get(index);
        else
            return null;
    }

    /**
     * Verify if the MapImages in the list still exist in the GraphicalElement list in the UIController.
     * If the MapImages don't exist, they are removed from the list.
     */
    @Override
    public void refresh(UIController uic) {
        //For each MapImage in the list, test if it still present in the UIController.
        //If not, it's removed
        for(MapImage mi : list){
            boolean flag=false;
            for(GraphicalElement ge : uic.getGEList())
                if(ge.equals(mi))
                    flag = true;
            if(!flag)
                this.remove(mi);
        }
        
        for(GraphicalElement ge : uic.getGEList()){
            if(ge instanceof MapImage){
                if(!list.contains(ge)){
                    this.add((MapImage)ge);
                }
            }
        }
        
    }

    @Override
    public void setField(String name, String value) {
        super.setField(name, value);
        if(name.equals("list"))
            //list= Arrays.asList(value.split(","));
        if(name.equals("index"))
            index=Integer.parseInt(value);
    }

    @Override
    public Map<String, Object> getAllFields() {
        Map ret = super.getAllFields();
        ret.put("index", index);
        /*String s="";
        for(String str : list)
            s+=str+",";
        if(list.size()>0)s=s.substring(0, s.length()-1);
        ret.put("list", s);*/
        return ret;
    }
}
