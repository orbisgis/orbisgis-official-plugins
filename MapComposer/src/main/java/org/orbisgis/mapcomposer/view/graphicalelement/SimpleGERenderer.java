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

import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.sif.UIPanel;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Base renderer for GraphicalElement.
 * This abstract class contain a function applying to the bufferedImage the rotation angle of the GraphicalElement.
 * In fact, after the rotation, the bounding box of the image will be bigger. So if the original bounding box is kept, the image will be cut.
 * That's why a new image is created, with the new Bounding box and the original image is drawn inside. After the rotation the image won't be cut.
 */
public abstract class SimpleGERenderer implements GERenderer {

    protected BufferedImage applyRotationToBufferedImage(BufferedImage bi, GraphicalElement ge){
        //Calculate the size of the bufferedImage according to the rotation of the ge.
        double rad = Math.toRadians(ge.getRotation());
        double newHeight = Math.abs(sin(rad)*ge.getWidth())+Math.abs(cos(rad)*ge.getHeight());
        double newWidth = Math.abs(sin(rad)*ge.getHeight())+Math.abs(cos(rad)*ge.getWidth());

        int maxWidth = Math.max((int)newWidth, ge.getWidth());
        int maxHeight = Math.max((int)newHeight, ge.getHeight());

        //Create a new BufferedImage with the new size (size after rotation)
        BufferedImage bufferedImage = new BufferedImage(maxWidth, maxHeight, bi.getType());
        Graphics2D graph = bufferedImage.createGraphics();

        //Draw the BufferedImage bi into the bigger BufferedImage
        graph.drawImage(bi,
                (maxWidth-ge.getWidth())/2, (maxHeight-ge.getHeight())/2,
                maxWidth-((maxWidth-ge.getWidth() )/ 2), maxHeight-((maxHeight-ge.getHeight() )/ 2),
                0, 0,
                ge.getWidth(), ge.getHeight(), null);
        graph.dispose();
        bi = bufferedImage;

        AffineTransform affineTransform;
        AffineTransformOp affineTransformOp;

        //Create the rotation transform fo the buffered image
        affineTransform= new AffineTransform();
        affineTransform.rotate(Math.toRadians(ge.getRotation()), maxWidth / 2, maxHeight / 2);

        //Apply the transform to the bufferedImage an return it
        affineTransformOp = new AffineTransformOp(affineTransform, AffineTransformOp.TYPE_BILINEAR);
        return affineTransformOp.filter(bi, null);
    }

    @Override
    public UIPanel createConfigurationPanel(GraphicalElement ge, UIController uic) {
        return null;
    }
}
