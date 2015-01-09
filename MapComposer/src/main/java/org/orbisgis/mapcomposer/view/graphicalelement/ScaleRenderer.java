/*
* MapComposer is an OrbisGIS plugin dedicated to the creation of cartographic
* documents based on OrbisGIS results.
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

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.Scale;

import java.awt.*;
import java.awt.image.BufferedImage;

/**
 * Renderer associated to the scale GraphicalElement.
 */
public class ScaleRenderer extends SimpleGERenderer {
    /**Dot per millimeter screen resolution. */
    private double dpmm;

    @Override
    public BufferedImage createImageFromGE(GraphicalElement ge) {

        int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        this.dpmm = (((double)dpi)/25.4);
        
        int resolution=-1;
        
        //Get the map scale
        double mapScalemmR = ((Scale)ge).getMapScale();
        //Calculate the real distance in milimeter that the Scale panel width represent.
        double panelWidthmmR = (ge.getWidth()/dpmm)*mapScalemmR;
        
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
        resolution*=dpmm;
        
        //Draw the BufferedImage
        BufferedImage bi = new BufferedImage(ge.getWidth(), ge.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D g = bi.createGraphics();
        g.setBackground(Color.black);
        g.setColor(Color.black);
        
        boolean updown = false;
        int i=0;
        int width = ge.getWidth();
        while(width>=resolution){
            if(updown){
                g.drawRect(i * resolution, 0, resolution, ge.getHeight()/2-1);
                g.fillRect(i * resolution, ge.getHeight() / 2, resolution, ge.getHeight() -ge.getHeight()/2);
            }
            else{
                g.fillRect(i * resolution, 0, resolution, ge.getHeight()/2-1);
                g.drawRect(i * resolution, (ge.getHeight() - 1) / 2, resolution, ge.getHeight() -ge.getHeight()/2);
            }
            updown=!updown;
            width-=resolution;
            i++;
        }
        if(updown){
            g.drawRect(i * resolution, 0, width - 1, ge.getHeight()/2-1);
            g.fillRect(i * resolution, ge.getHeight() / 2, width, ge.getHeight() -ge.getHeight()/2);
        }
        else{
            g.fillRect(i * resolution, 0, width, ge.getHeight()/2-1);
            g.drawRect(i * resolution, (ge.getHeight() - 1) / 2, width - 1, ge.getHeight() -ge.getHeight()/2);
        }
        g.dispose();
        return applyRotationToBufferedImage(bi, ge);
    }
}
