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

package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.commons.progress.ProgressMonitor;
import org.orbisgis.mapcomposer.model.graphicalelement.element.shape.OvalGE;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.image.BufferedImage;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Circle renderer.
 *
 * @author Sylvain PALOMINOS
 */

public class CircleRenderer implements GERenderer, RendererRaster, RendererVector {
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

        OvalGE rectangle = (OvalGE)ge;

        //First draw the shape
        Color c = rectangle.getShapeColor();
        graphics2D.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), rectangle.getShapeAlpha()*255/100));
        graphics2D.fillOval(x - ge.getWidth()/2,
                y - ge.getHeight()/2,
                ge.getWidth(),
                ge.getHeight());

        //Then if there is a border, draw it.
        if(rectangle.getBorderStyle().equals(OvalGE.EmptyBorder))
            return;

        c = rectangle.getBorderColor();
        graphics2D.setColor(new Color(c.getRed(), c.getGreen(), c.getBlue(), rectangle.getBorderAlpha()*255/100));
        Stroke stroke = null;
        if(rectangle.getBorderStyle().equals(OvalGE.LineBorder)){
            stroke = new BasicStroke(rectangle.getBorderWidth(),
                    BasicStroke.CAP_BUTT,
                    BasicStroke.JOIN_MITER,
                    1.0f, null, 0f);
        }
        graphics2D.setStroke(stroke);
        graphics2D.drawOval(x - ge.getWidth()/2 + rectangle.getBorderWidth()/2,
                y - ge.getHeight()/2 + rectangle.getBorderWidth()/2,
                ge.getWidth() - rectangle.getBorderWidth(),
                ge.getHeight() - rectangle.getBorderWidth());

        graphics2D.dispose();
    }
}
