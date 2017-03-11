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

package org.orbisgis.mapcomposer.model.graphicalelement.element.shape;

import org.orbisgis.mapcomposer.model.configurationattribute.attribute.ColorCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.IntegerCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.graphicalelement.element.SimpleGE;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import java.awt.Color;
import java.util.ArrayList;
import java.util.List;

/**
 * Abstract GraphicalElement containing the basic implementation of a shape.
 *
 * @author Sylvain PALOMINOS
 */

public abstract class SimpleShapeGE extends SimpleGE {

    /**
     * Name of the line border.
     */
    public static final String LineBorder = I18n.marktr("Line");

    /**
     * Name of the empty border.
     */
    public static final String EmptyBorder = I18n.marktr("Empty");

    /**
     * Name of the shape color.
     */
    public static final String sShapeColor = I18n.marktr("Shape color");

    /**
     * Name of the shape alpha.
     */
    public static final String sShapeAlpha = I18n.marktr("Shape alpha");

    /**
     * Name of the border width.
     */
    public static final String sBorderWidth = I18n.marktr("Border width");

    /**
     * Name of the border alpha.
     */
    public static final String sBorderAlpha = I18n.marktr("Border alpha");

    /**
     * Name of the border color.
     */
    public static final String sBorderColor = I18n.marktr("Border color");

    /**
     * Name of the border style.
     */
    public static final String sBorderStyle = I18n.marktr("Border style");

    /**
     * Configuration attribute of the shape color.
     */
    private ColorCA shapeColor;
    /**
     * Configuration attribute of the shape alpha.
     */
    private IntegerCA shapeAlpha;
    /**
     * Configuration attribute of the border width.
     */
    private IntegerCA borderWidth;
    /**
     * Configuration attribute of the border alpha.
     */
    private IntegerCA borderAlpha;
    /**
     * Configuration attribute of the border color.
     */
    private ColorCA borderColor;
    /**
     * Configuration attribute of the border style.
     */
    private SourceListCA borderStyle;

    /**
     * Main constructor
     */
    public SimpleShapeGE() {
        super();
        shapeColor = new ColorCA(I18n.marktr(sShapeColor), false, Color.GRAY);
        shapeAlpha = new IntegerCA(I18n.marktr(sShapeAlpha), false, 100, true, 0, 100);
        borderWidth = new IntegerCA(I18n.marktr(sBorderWidth), false, 5);
        borderAlpha = new IntegerCA(I18n.marktr(sBorderAlpha), false, 100, true, 0, 100);
        borderColor = new ColorCA(I18n.marktr(sBorderColor), false, Color.BLACK);
        borderStyle = new SourceListCA(I18n.marktr(sBorderStyle), false);
        borderStyle.add(I18n.marktr(LineBorder));
        borderStyle.add(I18n.marktr(EmptyBorder));
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.add(shapeColor);
        list.add(shapeAlpha);
        list.add(borderWidth);
        list.add(borderAlpha);
        list.add(borderColor);
        list.add(borderStyle);
        return list;
    }

    @Override
    public List<ConfigurationAttribute> getSavableAttributes() {
        List<ConfigurationAttribute> list = super.getSavableAttributes();
        list.add(shapeColor);
        list.add(shapeAlpha);
        list.add(borderWidth);
        list.add(borderAlpha);
        list.add(borderColor);
        list.add(borderStyle);
        return list;
    }

    @Override
    public void setAttribute(ConfigurationAttribute ca) {
        super.setAttribute(ca);
        if(ca.getName().equals(shapeColor.getName()))
            shapeColor = (ColorCA) ca;
        if(ca.getName().equals(shapeAlpha.getName()))
            shapeAlpha = (IntegerCA) ca;
        if(ca.getName().equals(borderWidth.getName()))
            borderWidth = (IntegerCA) ca;
        if(ca.getName().equals(borderAlpha.getName()))
            borderAlpha = (IntegerCA) ca;
        if(ca.getName().equals(borderColor.getName()))
            borderColor = (ColorCA) ca;
        if(ca.getName().equals(borderStyle.getName()))
            borderStyle = (SourceListCA) ca;
    }

    @Override
    public GraphicalElement deepCopy() {
        SimpleShapeGE copy = (SimpleShapeGE) super.deepCopy();
        copy.shapeColor = (ColorCA) this.shapeColor.deepCopy();
        copy.shapeAlpha = (IntegerCA) this.shapeAlpha.deepCopy();
        copy.borderWidth = (IntegerCA) this.borderWidth.deepCopy();
        copy.borderAlpha = (IntegerCA) this.borderAlpha.deepCopy();
        copy.borderColor = (ColorCA) this.borderColor.deepCopy();
        copy.borderStyle = (SourceListCA) this.borderStyle.deepCopy();

        return copy;
    }

    /**
     * Returns the shape color.
     * @return The shape Color.
     */
    public Color getShapeColor() {
        return shapeColor.getValue();
    }

    /**
     * Returns the shape alpha.
     * @return The shape alpha.
     */
    public int getShapeAlpha() {
        return shapeAlpha.getValue();
    }

    /**
     * Returns the border width.
     * @return The border width.
     */
    public int getBorderWidth() {
        return borderWidth.getValue();
    }

    /**
     * Returns the border alpha.
     * @return The border alpha.
     */
    public int getBorderAlpha() {
        return borderAlpha.getValue();
    }

    /**
     * Returns the border color.
     * @return The border color.
     */
    public Color getBorderColor() {
        return borderColor.getValue();
    }

    /**
     * Returns the border style.
     * @return The border style.
     */
    public String getBorderStyle() {
        return borderStyle.getSelected();
    }
}
