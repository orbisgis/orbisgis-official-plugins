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

    /** MapImageId of the value selected.*/
    private String mapImageId;

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
        mapImageId = "";
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

    @Override public void       select(MapImage choice)   {
        if(choice == null) {
            mapImageId = "";
        }
        else {
            mapImageId = choice.getIdentifier();
        }
    }
    @Override public MapImage getSelected(){
        //Verify if the mapImageId is correct. If not, return null.
        for(MapImage mi : list){
            if(mapImageId == mi.getIdentifier()){
                return mi;
            }
        }
        return null;
    }

    /**
     * Verify if the MapImages in the list still exist in the GraphicalElement list in the UIController.
     * If the MapImages don't exist, they are removed from the list.
     */
    @Override
    public void refresh(MainController uic) {
        list = new ArrayList<>();
        MapImage choice = null;
        for(GraphicalElement ge : uic.getGEList()){
            if(ge instanceof MapImage){
                this.add((MapImage)ge);
                if(((MapImage)ge).getIdentifier().equals(mapImageId)) {
                    choice = (MapImage) ge;
                }
            }
        }
        this.select(choice);
    }

    @Override
    public void setField(String name, String value) {
        super.setField(name, value);
        if(name.equals("mapImageId"))
            mapImageId = value;
    }

    @Override
    public Map<String, Object> getAllFields() {
        Map ret = super.getAllFields();
        ret.put("mapImageId", mapImageId);
        return ret;
    }

    @Override
    public ConfigurationAttribute deepCopy() {
        MapImageListCA copy = new MapImageListCA();
        List<MapImage> list = new ArrayList<>();
        for(MapImage mapImage : this.getValue())
            list.add((MapImage)mapImage.deepCopy());
        copy.setValue(list);
        copy.setReadOnly(this.getReadOnly());
        copy.setName(this.getName());
        copy.select(this.getSelected());

        return copy;
    }
}
