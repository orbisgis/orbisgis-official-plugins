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

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;

import java.util.Map;

/**
 * This class represent an integer which can have limits to ensure that its value won't be too high or too low.
 * It extends the BaseCA abstract class.
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.BaseCA
 *
 * @author Sylvain PALOMINOS
 */
public class IntegerCA extends BaseCA<Integer> {

    /** Property itself. */
    private Integer value;

    /** Minimum value. */
    private int min;

    /** Maximum value. */
    private int max;

    /** Boolean to enable or not the max and min value. */
    private boolean limits;

    /**
     * Empty constructor used in the SaveHandler, on loading a project save.
     */
    public IntegerCA(){
        super("void", false);
        value=0;
        min=Integer.MIN_VALUE;
        max=Integer.MAX_VALUE;
    }

    /**
     * Constructor with limits values.
     * The limits will apply only if the limits boolean is to true anf if min<=value<=max.
     * @param name Name of the ConfigurationAttribute
     * @param readOnly Boolean to enable or not the modification of the CA
     * @param value Value of the integer represented
     * @param limits Enable or not the limits of the integer value
     * @param min Minimum value of the integer
     * @param max Maximum value of the integer
     */
    public IntegerCA(String name, boolean readOnly, int value, boolean limits, int min, int max){
        super(name, readOnly);
        this.limits=limits;
        this.value = value;
        //verify if min<=value<=max
        if(min <= value && value <= max){
            this.min = min;
            this.max = max;
        }
        else{
            this.min = Integer.MIN_VALUE;
            this.max = Integer.MAX_VALUE;
            this.limits=false;
        }
    }

    /**
     * This constructor disable the limits for the integer value.
     * @param name Name of the ConfigurationAttribute
     * @param readOnly Boolean to enable or not the modification of the CA
     * @param value Value of the integer represented
     */
    public IntegerCA(String name, boolean readOnly, int value){
        super(name, readOnly);
        this.limits=false;
        this.min = Integer.MIN_VALUE;
        this.value = value;
        this.max = Integer.MAX_VALUE;
    }
    
    @Override public void setValue(Integer value) {
        if(min<=value && value<=max || !limits)
            this.value=value;
    }

    @Override public Integer getValue() {return value;}

    @Override public boolean isSameValue(ConfigurationAttribute configurationAttribute) {
        return configurationAttribute.getValue().equals(value);
    }
    
    /**
     * Returns the minimum value.
     * @return The minimum value.
     */
    public int getMin(){return min;}
    
    /**
     * Sets the minimum value.
     * @param min The minimum value.
     */
    public void setMin(int min){this.min=min;}
    
    /**
     * Returns the maximum value.
     * @return The maximum value.
     */
    public int getMax(){return max;}
    
    /**
     * Sets the maximum value.
     * @param max The maximum value.
     */
    public void setMax(int max){
        this.max=max;
    }
    
    /**
     * Returns the limits boolean. True if the integer value has limits, false otherwise.
     * @return The limits boolean.
     */
    public boolean getLimits(){
        return limits;
    }
    
    /**
     * Sets the limits value.
     * @param limits The limits value.
     */
    public void setLimits(boolean limits){
        this.limits=limits;
    }

    @Override
    public void setField(String name, String value) {
        super.setField(name, value);
        if(name.equals("value"))
            this.value=Integer.parseInt(value);
        else if(name.equals("min"))
            this.min = Integer.parseInt(value);
        else if(name.equals("max"))
            this.max = Integer.parseInt(value);
        else if(name.equals("limits"))
            this.limits = Boolean.parseBoolean(value);
    }

    @Override
    public Map<String, Object> getAllFields() {
        Map ret = super.getAllFields();
        ret.put("value", value);
        ret.put("min", min);
        ret.put("max", max);
        ret.put("limits", limits);
        return ret;
    }

    @Override
    public ConfigurationAttribute deepCopy() {
        IntegerCA copy = new IntegerCA();
        copy.setValue(this.getValue());
        copy.setReadOnly(this.getReadOnly());
        copy.setName(this.getName());
        copy.setLimits(this.getLimits());
        copy.setMax(this.getMax());
        copy.setMin(this.getMin());
        return copy;
    }
}
