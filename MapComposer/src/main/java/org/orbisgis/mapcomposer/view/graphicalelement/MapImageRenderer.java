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

import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.OwsContextCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import org.orbisgis.mapcomposer.view.utils.Graphics2DRenderer;
import org.orbisgis.mapcomposer.view.utils.MapComposerIcon;
import org.orbisgis.mapcomposer.view.utils.UIDialogProperties;
import org.orbisgis.sif.UIPanel;

import javax.swing.Box;
import javax.swing.ImageIcon;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * This renderer is used to get the vector and raster representation of a MapImage object and to get the configuration UIPanel associated.
 *
 * @author Sylvain PALOMINOS
 */
public class MapImageRenderer implements RendererRaster, RendererVector, CustomConfigurationPanel {

    @Override
    public void drawGE(Graphics2D graphics2D, GraphicalElement ge) {
        //Get the MapImageObject
        MapImage mi = (MapImage)ge;

        //Calculate the size of the GraphicalElement after rotation
        double rad = Math.toRadians(ge.getRotation());
        double newHeight = Math.abs(sin(rad) * ge.getWidth())+Math.abs(cos(rad)*ge.getHeight());
        double newWidth = Math.abs(sin(rad) * ge.getHeight())+Math.abs(cos(rad)*ge.getWidth());

        //If the MapImage object contain a valid OwsMapContext, render it with the Graphics2DRenderer
        if(mi.getOwsPath() != null) {
            //Translate the Graphics2D to draw the map at its center
            graphics2D.translate((newWidth - ge.getWidth()) / 2, (newHeight - ge.getHeight()) / 2);
            //Apply the rotation
            graphics2D.rotate(rad, ge.getWidth() / 2, ge.getHeight() / 2);
            //Render the map and draw it in the Graphics2D
            Graphics2DRenderer renderer = new Graphics2DRenderer(graphics2D, ge.getWidth(), ge.getHeight());
            renderer.draw(graphics2D, ge.getWidth(), ge.getHeight(), mi.getMapTransform().getExtent(), mi.getOwsMapContext().getLayerModel(), null);
        }
        //Else, draw the MapImage icon
        else{
            //Get the image to draw
            ImageIcon icon = MapComposerIcon.getIcon("add_map");
            //Draw it in the Graphics2D
            graphics2D.drawImage(icon.getImage(), 0, 0, ge.getWidth(), ge.getHeight(), null);
        }
    }

    @Override
    public BufferedImage createGEImage(GraphicalElement ge) {
        //Calculate the size of the GraphicalElement after rotation
        double rad = Math.toRadians(ge.getRotation());
        double newHeight = Math.abs(sin(rad)*ge.getWidth())+Math.abs(cos(rad)*ge.getHeight());
        double newWidth = Math.abs(sin(rad)*ge.getHeight())+Math.abs(cos(rad)*ge.getWidth());
        //Calculate the bounding box of the GraphicalElement after rotation
        int maxWidth = Math.max((int) newWidth, ge.getWidth());
        int maxHeight = Math.max((int)newHeight, ge.getHeight());

        //Create the BufferedImage which will contain the GraphicalElement representation.
        BufferedImage bi = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D graphics2D = bi.createGraphics();

        //Get the MapImageObject
        MapImage mi = (MapImage)ge;

        //If the MapImage object contain a valid OwsMapContext, render it with the Graphics2DRenderer
        if(mi.getOwsPath() != null) {
            //Translate the Graphics2D to draw the map at its center
            graphics2D.translate((newWidth - ge.getWidth()) / 2, (newHeight - ge.getHeight()) / 2);
            //Apply the rotation
            graphics2D.rotate(rad, ge.getWidth() / 2, ge.getHeight() / 2);
            //Render the map and draw it in the Graphics2D
            Graphics2DRenderer renderer = new Graphics2DRenderer(graphics2D, ge.getWidth(), ge.getHeight());
            renderer.draw(graphics2D, ge.getWidth(), ge.getHeight(), mi.getMapTransform().getExtent(), mi.getOwsMapContext().getLayerModel(), null);
        }
        //Else, draw the MapImage icon
        else{
            //Get the image to draw
            ImageIcon icon = MapComposerIcon.getIcon("add_map");
            //Draw it in the Graphics2D
            graphics2D.drawImage(icon.getImage(), 0, 0, ge.getWidth(), ge.getHeight(), null);
        }

        return bi;
    }

    @Override
    public UIPanel createConfigurationPanel(java.util.List<ConfigurationAttribute> caList, MainController uic, boolean enableLock){
        //Create the UIDialogProperties that will be returned
        UIDialogProperties uid = new UIDialogProperties(uic);

        //Add the text configuration elements
        //Find the OwsContext ConfigurationAttribute
        OwsContextCA owscCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(MapImage.sOWSC))
                owscCA = (OwsContextCA)ca;
        //Create the list of component composing the ConfigurationAttribute representation.
        List<Component> owsc = new ArrayList<>();
        JLabel owscName = new JLabel(owscCA.getName());
        owsc.add(owscName);
        //Create a kind of tabulation to align elements from different ConfigurationAttribute
        owsc.add(Box.createHorizontalStrut(150 - owscName.getFontMetrics(owscName.getFont()).stringWidth(owscName.getText())));
        JComboBox owscBox = (JComboBox)uic.getCAManager().getRenderer(owscCA).createJComponentFromCA(owscCA);
        //Limits the size of the component
        owscBox.setPreferredSize(new Dimension(200, (int) owscBox.getPreferredSize().getHeight()));
        owsc.add(owscBox);
        //Add the ConfigurationAttribute and its representation to the UIDialogProperties
        uid.addComponent(owsc, owscCA, enableLock);

        return uid;
    }
}
