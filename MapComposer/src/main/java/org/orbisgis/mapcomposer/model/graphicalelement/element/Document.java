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

package org.orbisgis.mapcomposer.model.graphicalelement.element;

import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GEProperties;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.xnap.commons.i18n.I18n;

import java.awt.Dimension;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * This GraphicalElement represents the document itself. it is display as a blank page, always behind all the other GraphicalElement.
 * The user can set its orientation (portrait or landscape), it format (A4, A3 or custom) and its name.
 *
 * @author Sylvain PALOMINOS
 */
public class Document extends SimpleGE implements GEProperties {

    /**Document orientation (portrait or landscape)*/
    private SourceListCA orientation;

    /**Size of the document*/
    private SourceListCA format;

    /**Unit (millimeters or inch) of the document*/
    private SourceListCA unit;

    /**Name of the document*/
    private StringCA name;

    /** Displayed name of the orientation*/
    public static final String sOrientation = I18n.marktr("Orientation");

    /** Displayed name of the format*/
    public static final String sFormat = I18n.marktr("Format");

    /** Displayed name of the unit*/
    public static final String sUnit = I18n.marktr("Unit");

    /**Displayed name of the name*/
    public static final String sName = I18n.marktr("Name");

    /**Displayed name of the title*/
    private static final String sDefaultName = I18n.marktr("Document title");

    @Override
    public boolean isDocumentNeeded() {
        return false;
    }

    @Override
    public boolean isAlwaysOnTop() {
        return false;
    }

    @Override
    public boolean isAlwaysOnBack() {
        return true;
    }

    @Override
    public boolean isDrawnByUser() {
        return false;
    }

    @Override
    public boolean isAlwaysRefreshed() { return false; }

    @Override
    public boolean isAlwaysCentered() {
        return true;
    }

    @Override
    public boolean isEditedByMouse() {
        return false;
    }

    /** Enumeration of the orientation possibilities : portrait or landscape.*/
    public enum Orientation{
        PORTRAIT(I18n.marktr("Portrait")),
        LANDSCAPE(I18n.marktr("Landscape"));

        /** Orientation name */
        private String name;

        /**
         * Main constructor.
         * @param name Name of the orientation.
         */
        private Orientation(String name){
            this.name = name;
        }

        /**
         * Returns the name of the orientation.
         * @return The name of the orientation.
         */
        public String getName(){
            return name;
        }
    }
    
    /**
     * Main constructor.
     */
    public Document(){
        //ConfigurationAttribute instantiation
        orientation= new SourceListCA(sOrientation, false);
        format= new SourceListCA(sFormat, false);
        unit= new SourceListCA(sUnit, false);
        name= new StringCA(sName, false, sDefaultName);
        //Sets the orientation CA
        orientation.add(Orientation.LANDSCAPE.getName());
        orientation.add(Orientation.PORTRAIT.getName());
        orientation.select(Orientation.LANDSCAPE.getName());
        //Sets the format CA
        format.add(Format.A3.getName());
        format.add(Format.A4.getName());
        format.add(Format.CUSTOM.getName());
        setFormat(Format.A4);
        //Sets the unit CA
        unit.add(Unit.MM.name());
        unit.add(Unit.IN.name());
        unit.add(Unit.PIXEL.name());
        unit.select(Unit.MM.name());
    }
    
    /**
     * Sets the document name.
     * @param name New name of the document.
     */
    public void setName(String name){this.name.setValue(name);}
    
    /**
     * Returns the document name.
     * @return The document name.
     */
    public String getName(){return name.getValue();}
    
    /**
     * Sets the format of the document.
     * @param f New format of the document.
     */
    public void setFormat(Format f){
        format.select(f.getName());
        if(!f.equals(Format.CUSTOM)) {
            this.setWidth(f.getPixelWidth());
            this.setHeight(f.getPixelHeight());
        }
    }

    /**
     * Return the format of the document.
     * @return Format of the document.
     */
    public Format getFormat(){
        return Format.valueOf(format.getSelected().toUpperCase());
    }

    /**
     * Return the unit of the document.
     * @return Unit of the document.
     */
    public Unit getUnit(){
        return Unit.valueOf(unit.getSelected());
    }
    
    /**
     * Sets the orientation of the document.
     * @param o New document orientation.
     */
    public void setOrientation(Orientation o){orientation.select(o.getName());}

    /**
     * Return the dimension of the document according to the format and to the orientation of the document.
     * @return The dimension of the document.
     */
    public Dimension getDimension(){
        return new Dimension(this.getWidth(), this.getHeight());
    }

    public void refresh() {
        /*if(this.format.getSelected().equals(Format.CUSTOM.getName())) {
            this.width.setValue((int) (this.width.getValue() * Unit.valueOf(unit.getSelected()).getConv()));
            this.height.setValue((int) (this.height.getValue() * Unit.valueOf(unit.getSelected()).getConv()));
        }*/
        if(orientation.getSelected().equals(Orientation.LANDSCAPE.getName())){
            int width = this.getWidth();
            this.setWidth(this.getHeight());
            this.setHeight(width);
        }
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.add(width);
        list.add(height);
        list.add(unit);
        list.add(format);
        list.add(orientation);
        list.add(name);
        return list;
    }

    @Override
    public List<ConfigurationAttribute> getSavableAttributes() {
        List<ConfigurationAttribute> list = super.getSavableAttributes();
        list.add(format);
        list.add(unit);
        list.add(orientation);
        list.add(name);
        return list;
    }

    @Override
    public GraphicalElement deepCopy() {
        Document copy = (Document) super.deepCopy();
        copy.orientation = (SourceListCA) this.orientation.deepCopy();
        copy.format = (SourceListCA) this.format.deepCopy();
        copy.name = (StringCA) this.name.deepCopy();
        copy.unit = (SourceListCA) this.unit.deepCopy();

        return copy;
    }

    @Override
    public void setAttribute(ConfigurationAttribute ca) {
        super.setAttribute(ca);
        if(ca.getName().equals(orientation.getName()))
            orientation=(SourceListCA)ca;
        if(ca.getName().equals(format.getName()))
            format=(SourceListCA)ca;
        if(ca.getName().equals(unit.getName()))
            unit=(SourceListCA)ca;
        if(ca.getName().equals(name.getName()))
            name=(StringCA)ca;
    }

    /**
     * Enumeration of the different Documents formats.
     * To each format the corresponding dimensions are associated.
     */
    public static enum Format{
        A4(210, 297, I18n.marktr("A4")),
        A3(297, 420, I18n.marktr("A3")),
        CUSTOM(0, 0, I18n.marktr("Custom"));
        /**Width of the format*/
        private int w;
        /**Height of the format*/
        private int h;
        /**Name of the format*/
        private String name;

        /**DPI of the screen. As java don't detect well the dpi, it is set manually.*/
        private int dpi;

        /**
         * Main constructor.
         * @param w Width of the format.
         * @param h Height of the format.
         */
        private Format(int w, int h, String name) {
            this.w = w;
            this.h = h;
            this.name = name;

            //To be run on a server (no GUI), try to get the default Toolkit.
            //If it is not possible use a default value for the screen resolution.
            try {
                //Get the method getDefaultToolkit
                Method m = Class.forName("java.awt.Toolkit").getDeclaredMethod("getDefaultToolkit", null);
                //Get the ToolKit return by the previous method
                Object o = m.invoke(null, null);
                //Get the screen resolution
                dpi = (Integer)o.getClass().getDeclaredMethod("getScreenResolution").invoke(o);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
                dpi = 96;
            }
        }

        /**
         * Return the height of the document in pixels.
         * @return The height of the document in pixels.
         */
        public int getPixelHeight(){
            return (int)(dpi*h/25.4);
        }
        /**
         * Return the width of the document in pixels.
         * @return The width of the document in pixels.
         */
        public int getPixelWidth(){
            return (int)(dpi*w/25.4);
        }

        /**
         * Returns the name of the format.
         * @return The name of the format.
         */
        public String getName(){
            return this.name;
        }
    }

    public static enum Unit {
        MM((double)1/25.4, I18n.marktr("mm")),
        IN((double)1, I18n.marktr("in")),
        PIXEL(1, I18n.marktr("pixel"));
        /**
         * Width of the format
         */
        private double conv;

        /**
         * DPI of the screen. As java don't detect well the dpi, it is set manually.
         */
        private double dpi;

        /**
         * Main constructor.
         */
        private Unit(double conv, String name) {
            //To be run on a server (no GUI), try to get the default Toolkit.
            //If it is not possible use a default value for the screen resolution.
            try {
                //Get the method getDefaultToolkit
                Method m = Class.forName("java.awt.Toolkit").getDeclaredMethod("getDefaultToolkit", null);
                //Get the ToolKit return by the previous method
                Object o = m.invoke(null, null);
                //Get the screen resolution
                dpi = (double) (Integer) o.getClass().getDeclaredMethod("getScreenResolution").invoke(o);
            } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException | ClassNotFoundException e) {
                dpi = 96;
            }
            if (!name.equals("pixel")) {
                this.conv = conv * dpi;
            } else {
                this.conv = conv;
            }
        }

        public double getConv(){
            return conv;
        }
    }
}
