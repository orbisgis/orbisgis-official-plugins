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
package org.orbisgis.mapcomposer.view.utils;

import java.awt.Graphics2D;
import java.sql.ResultSet;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.coremap.renderer.Renderer;
import org.orbisgis.coremap.renderer.se.Symbolizer;
/**
 * This renderer is used to generate rendered-layers in a Graphics2D
 * Adapted from the Renderer class from OrbisGIS written by Maxence Laurent
 *
 * @see org.orbisgis.coremap.renderer.Renderer
 * @author Sylvain Palominos
 */
public class Graphics2DRenderer extends Renderer {
    private int width;
    private int height;
    private Map<Integer, Graphics2D> g2Levels;
    private Graphics2D baseG2;
    public Graphics2DRenderer(Graphics2D graphics2D, int width, int height) {
        super();
        g2Levels = null;
        baseG2 = graphics2D;
        this.width = width;
        this.height = height;
    }
    @Override
    protected Graphics2D getGraphics2D(Symbolizer s) {
        Graphics2D get = g2Levels.get(s.getLevel());
        return get;
    }
    @Override
    protected void initGraphics2D(List<Symbolizer> symbs, Graphics2D g2, MapTransform mt) {
        g2Levels = new HashMap<>();
        List<Integer> levels = new LinkedList<>();
        for (Symbolizer s : symbs) {
            if (!levels.contains(s.getLevel())) {
                levels.add(s.getLevel());
            }
        }
        Collections.sort(levels);
        for (Integer level : levels) {
            Graphics2D sg2 = (Graphics2D) baseG2.create(0, 0, width, height);
            sg2.addRenderingHints(mt.getRenderingHints());
            g2Levels.put(level, sg2);
        }
    }
    @Override
    public void disposeLayer(Graphics2D g2) {
        g2Levels.clear();
    }
    @Override
    protected void releaseGraphics2D(Graphics2D g2) {
    }
    @Override
    public void beginLayer(String name) {
    }
    @Override
    public void endLayer(String name) {
    }
    @Override
    protected void beginFeature(long id, ResultSet rs) {
    }
    @Override
    protected void endFeature(long id, ResultSet rs) {
    }
}