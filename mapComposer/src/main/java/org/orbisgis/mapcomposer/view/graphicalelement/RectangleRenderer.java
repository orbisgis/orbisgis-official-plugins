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

package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.commons.progress.ProgressMonitor;
import org.orbisgis.mapcomposer.model.graphicalelement.element.shape.RectangleGE;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Rectangle renderer.
 *
 * @author Sylvain PALOMINOS
 */

public class RectangleRenderer implements GERenderer, RendererRaster, RendererVector{
    @Override
    public BufferedImage createGEImage(GraphicalElement ge, ProgressMonitor pm) {

        double rad = Math.toRadians(ge.getRotation());
        double newHeight = Math.abs(sin(rad)*ge.getWidth())+Math.abs(cos(rad)*ge.getHeight());
        double newWidth = Math.abs(sin(rad)*ge.getHeight())+Math.abs(cos(rad)*ge.getWidth());

        int maxWidth = Math.max((int)newWidth, ge.getWidth());
        int maxHeight = Math.max((int)newHeight, ge.getHeight());

        BufferedImage bi = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
        drawGE(bi.createGraphics(), ge);
        return bi;
    }

    @Override
    public void drawGE(Graphics2D graphics2D, GraphicalElement ge) {

        double rad = Math.toRadians(ge.getRotation());
        double newHeight = Math.abs(sin(rad)*ge.getWidth())+Math.abs(cos(rad)*ge.getHeight());
        double newWidth = Math.abs(sin(rad)*ge.getHeight())+Math.abs(cos(rad)*ge.getWidth());

        int x = Math.max((int)newWidth, ge.getWidth())/2;
        int y = Math.max((int)newHeight, ge.getHeight())/2;
        graphics2D.rotate(rad, x, y);

        RectangleGE rectangle = (RectangleGE)ge;

        //First draw the shape
        Color c = rectangle.getShapeColor();
        graphics2D.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), rectangle.getShapeAlpha()*255/100));
        graphics2D.fillRect(x - ge.getWidth()/2,
                y - ge.getHeight()/2,
                ge.getWidth(),
                ge.getHeight());

        if(rectangle.getBorderStyle().equals(RectangleGE.EmptyBorder))
            return;

        //Then if there is a border, draw it.
        c = rectangle.getBorderColor();
        graphics2D.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), rectangle.getBorderAlpha()*255/100));
        Stroke stroke = null;
        if(rectangle.getBorderStyle().equals(RectangleGE.LineBorder)){
             stroke = new BasicStroke(rectangle.getBorderWidth(),
                     BasicStroke.CAP_BUTT, BasicStroke.JOIN_MITER,
                     1.0f, null, 0f);
        }
        graphics2D.setStroke(stroke);
        graphics2D.drawRect(x - ge.getWidth()/2 + rectangle.getBorderWidth()/2,
                y - ge.getHeight()/2 + rectangle.getBorderWidth()/2,
                ge.getWidth() - rectangle.getBorderWidth(),
                ge.getHeight() - rectangle.getBorderWidth());

        graphics2D.dispose();
    }
}
