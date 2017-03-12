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
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.Scale;

import java.awt.*;
import java.awt.image.BufferedImage;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Renderer associated to the scale GraphicalElement.
 *
 * @author Sylvain PALOMINOS
 */
public class ScaleRenderer implements RendererRaster, RendererVector {

    @Override
    public void drawGE(Graphics2D graphics2D, GraphicalElement ge) {

        double rad = Math.toRadians(ge.getRotation());
        double newHeight = Math.abs(sin(rad)*ge.getWidth())+Math.abs(cos(rad)*ge.getHeight());
        double newWidth = Math.abs(sin(rad)*ge.getHeight())+Math.abs(cos(rad)*ge.getWidth());

        int x = Math.max((int)newWidth, ge.getWidth())/2;
        int y = Math.max((int)newHeight, ge.getHeight())/2;
        graphics2D.rotate(rad, x, y);

        int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        /*Dot per millimeter screen resolution. */
        double dpmm = (((double) dpi) / 25.4);

        int resolution=-1;

        //Get the map scale
        double mapScalemmR = ((Scale)ge).getMapScale();
        //Calculate the real distance in milimeter that the Scale panel width represent.
        double panelWidthmmR = (ge.getWidth()/ dpmm)*mapScalemmR;

        //Scale resolution. It gives the better size for black and white rectangles of the scale
        for(int i=0; resolution==-1; i++){
            if(panelWidthmmR/Math.pow(10, i)<10){
                resolution=(int)(1*Math.pow(10, i));
            }
            else if(panelWidthmmR/Math.pow(10, i)<50){
                resolution=(int)(5*Math.pow(10, i));
            }
        }
        //Convert the resolution from millimeters per rectangles to pixel per block
        resolution/=mapScalemmR;
        resolution*= dpmm;
        //Draw the BufferedImage
        graphics2D.setBackground(Color.black);
        graphics2D.setColor(Color.black);

        boolean updown = false;
        int i=0;
        int width = ge.getWidth();
        while(width>=resolution){
            if(updown){
                graphics2D.drawRect(x-ge.getWidth()/2+i * resolution,
                        y-ge.getHeight()/2,
                        resolution,
                        ge.getHeight()/2-1);
                graphics2D.fillRect(x-ge.getWidth()/2+i * resolution,
                        y-1,
                        resolution,
                        ge.getHeight()/2+2);
            }
            else{
                graphics2D.fillRect(x-ge.getWidth()/2+i * resolution,
                        y-ge.getHeight()/2,
                        resolution,
                        ge.getHeight()/2-1);
                graphics2D.drawRect(x-ge.getWidth()/2+i * resolution,
                        y-1,
                        resolution,
                        ge.getHeight()/2+1);
            }
            updown=!updown;
            width-=resolution;
            i++;
        }
        if(updown){
            graphics2D.drawRect(x-ge.getWidth()/2+i * resolution,
                    y-ge.getHeight()/2,
                    width - 1,
                    ge.getHeight()/2-1);
            graphics2D.fillRect(x-ge.getWidth()/2+i * resolution,
                    y-1,
                    width,
                    ge.getHeight()/2+2);
        }
        else{
            graphics2D.fillRect(x-ge.getWidth()/2+i * resolution,
                    y-ge.getHeight()/2,
                    width,
                    ge.getHeight()/2-1);
            graphics2D.drawRect(x-ge.getWidth()/2+i * resolution,
                    y-1,
                    width,
                    ge.getHeight()/2+1);
        }

        graphics2D.setFont(graphics2D.getFont().deriveFont(Font.PLAIN, ge.getHeight() / 2 - 1));
        graphics2D.drawString("1:" + ((int) mapScalemmR/(i+1)), x-ge.getWidth()/2, y+ge.getHeight()/2-1);
        graphics2D.dispose();
    }

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
}
