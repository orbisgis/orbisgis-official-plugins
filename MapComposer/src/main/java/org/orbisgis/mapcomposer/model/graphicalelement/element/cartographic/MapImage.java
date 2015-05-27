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

package org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic;

import org.orbisgis.commons.progress.NullProgressMonitor;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GEIdentifier;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GERefresh;
import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;

import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import java.awt.image.BufferedImage;

/**
 * This class represent the graphical representation (the image) of the map.
 *
 * @author Sylvain PALOMINOS
 */
public class MapImage extends SimpleCartoGE implements GERefresh, GEIdentifier {

    /** MapTransform used to generate the map image.
     * This class come from OrbisGIS
     */
    private MapTransform mapTransform;

    /**
     * Unique id of the MapImage
     */
    private String id;

    /** Object for the translation*/
    private static final I18n i18n = I18nFactory.getI18n(MapImage.class);
    
    /**
     * Main constructor.
     */
    public MapImage(){
        super();
        mapTransform = new MapTransform();
        id = this.toString();
    }

    @Override
    public String getGEName(){
        return i18n.tr("Map");
    }
    
    @Override
    public void refresh() {
        if(getOwsMapContext()!=null && getOwsMapContext().getBoundingBox()!=null) {
            try {
                owsc.refresh(null);
                mapTransform.setExtent(this.getOwsMapContext().getBoundingBox());
                mapTransform.setImage(new BufferedImage(this.getWidth(), this.getHeight(), BufferedImage.TYPE_INT_ARGB));
                if (!this.getOwsMapContext().isOpen())
                    this.getOwsMapContext().open(new NullProgressMonitor());
            } catch (LayerException ex) {
                LoggerFactory.getLogger(MapImage.class).error(ex.getMessage());
            }
        }
        else
            mapTransform.setImage(null);
    }
    
    /**
     * Returns the MapTransform of the map.
     * @return The MapTransform of the map.
     */
    public MapTransform getMapTransform(){return mapTransform;}

    @Override
    public GraphicalElement deepCopy() {
        MapImage mapImage = (MapImage)super.deepCopy();
        mapImage.id = this.id;
        mapImage.refresh();
        return mapImage;
    }

    @Override
    public void setIdentifier(String id) {
        this.id = id;
    }

    @Override
    public String getIdentifier() {
        return id;
    }
}
