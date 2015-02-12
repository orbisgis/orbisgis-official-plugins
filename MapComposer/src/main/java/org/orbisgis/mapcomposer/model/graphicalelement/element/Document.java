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
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GERefresh;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This GraphicalElement represents the document itself. it is display as a blank page, always behind all the other GraphicalElement.
 * The user can set its orientation (portrait or landscape), it format (A4, A3 or custom) and its name.
 *
 * @author Sylvain PALOMINOS
 */
public class Document extends SimpleGE implements GERefresh, GEProperties {

    /**Document orientation (portrait or landscape)*/
    private SourceListCA orientation;

    /**Size of the document*/
    private SourceListCA format;

    /**Name of the document*/
    private StringCA name;

    /** Object for the translation*/
    private static final I18n i18n = I18nFactory.getI18n(Document.class);

    /** Displayed name of the orientation*/
    private static final String sOrientation = i18n.tr("Orientation");

    /** Displayed name of the format*/
    private static final String sFormat = i18n.tr("Format");

    /**Displayed name of the name*/
    private static final String sName = i18n.tr("Name");

    /**Displayed name of the title*/
    private static final String sDefaultName = i18n.tr("Document title");

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
    public enum Orientation{PORTRAIT, LANDSCAPE}
    
    /**
     * Main constructor.
     */
    public Document(){
        //ConfigurationAttribute instantiation
        orientation= new SourceListCA(sOrientation, false);
        format= new SourceListCA(sFormat, false);
        name= new StringCA(sName, false, sDefaultName);
        //Sets the orientation CA
        orientation.add(Orientation.LANDSCAPE.name());
        orientation.add(Orientation.PORTRAIT.name());
        orientation.select(Orientation.LANDSCAPE.name());
        //Sets the format CA
        format.add(Format.A3.name());
        format.add(Format.A4.name());
        format.add(Format.CUSTOM.name());
        setFormat(Format.A4);
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
        format.select(f.name());
        this.setWidth(Format.valueOf(format.getSelected()).getPixelWidth());
        this.setHeight(Format.valueOf(format.getSelected()).getPixelHeight());
    }
    
    /**
     * Return the format of the document.
     * @return Format of the document.
     */
    public Format getFormat(){return Format.valueOf(format.getSelected());}
    
    /**
     * Sets the orientation of the document.
     * @param o New document orientation.
     */
    public void setOrientation(Orientation o){orientation.select(o.name());}

    /**
     * Return the dimension of the document according to the format and to the orientation of the document.
     * @return The dimension of the document.
     */
    public Dimension getDimension(){
        Dimension dim;
        if(orientation.getSelected().equals(Orientation.PORTRAIT.name()))
            dim = new Dimension(Format.valueOf(format.getSelected()).getPixelWidth(), Format.valueOf(format.getSelected()).getPixelHeight());
        else
            dim = new Dimension(Format.valueOf(format.getSelected()).getPixelHeight(), Format.valueOf(format.getSelected()).getPixelWidth());
        return dim;
    }

    @Override
    public void refresh() {
        //If the document format isn't CUSTOM, the set the specified width and height. (If the format is CUSTOM keep the set height and width)
        if(!getFormat().name().equals(Format.CUSTOM.name())){
            this.setWidth(getFormat().getPixelWidth());
            this.setHeight(getFormat().getPixelHeight());
        }
        //If the orientation is landscape, invert the height and width.
        if(orientation.getSelected().equals(Orientation.LANDSCAPE.name())){
            this.setHeight(Format.valueOf(format.getSelected()).getPixelWidth());
            this.setWidth(Format.valueOf(format.getSelected()).getPixelHeight());
        }
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.add(format);
        list.add(orientation);
        list.add(name);
        return list;
    }

    @Override
    public List<ConfigurationAttribute> getSavableAttributes() {
        List<ConfigurationAttribute> list = super.getSavableAttributes();
        list.add(format);
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

        return copy;
    }

    @Override
    public void setAttribute(ConfigurationAttribute ca) {
        super.setAttribute(ca);
        if(ca.getName().equals(orientation.getName()))
            orientation=(SourceListCA)ca;
        if(ca.getName().equals(format.getName()))
            format=(SourceListCA)ca;
        if(ca.getName().equals(name.getName()))
            name=(StringCA)ca;
    }

    /**
     * Enumeration of the different Documents formats.
     * To each format the corresponding dimensions are associated.
     */
    public static enum Format{
        A4(210, 297),
        A3(297, 420),
        CUSTOM(0, 0);
        /**Width of the format*/
        private final int w;
        /**Height of the format*/
        private final int h;

        /**DPI of the screen. As java don't detect well the dpi, it is set manually.*/
        private final int dpi = Toolkit.getDefaultToolkit().getScreenResolution();

        /**
         * Main constructor.
         * @param w Width of the format.
         * @param h Height of the format.
         */
        private Format(int w, int h){
            this.w=w;
            this.h=h;
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
    }

}
