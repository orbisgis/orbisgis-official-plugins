/**
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information.
 *
 * OrbisGIS is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
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