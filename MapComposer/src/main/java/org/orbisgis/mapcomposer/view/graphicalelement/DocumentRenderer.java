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
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.view.utils.UIDialogProperties;
import org.orbisgis.sif.UIPanel;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.beans.EventHandler;
import java.util.List;

/**
 * Renderer associated to the Document GraphicalElement.
 *
 * @author Sylvain PALOMINOS
 */
public class DocumentRenderer implements RendererRaster, RendererVector, CustomConfigurationPanel {

    @Override
    public void drawGE(Graphics2D graphics2D, GraphicalElement ge) {
        //Returns a white rectangle without applying any rotation
        graphics2D.setPaint(new Color(255, 255, 255));
        graphics2D.fillRect(0, 0, ge.getWidth(), ge.getHeight());
    }

    @Override
    public BufferedImage createGEImage(GraphicalElement ge) {
        BufferedImage bi = new BufferedImage(ge.getWidth(), ge.getHeight(), BufferedImage.TYPE_INT_ARGB);
        drawGE(bi.createGraphics(), ge);
        return bi;
    }

    @Override
    public UIPanel createConfigurationPanel(List<ConfigurationAttribute> caList, MainController uic, boolean enableLock){

        //Create the UIDialogProperties that will be returned
        UIDialogProperties uid = new UIDialogProperties(uic, enableLock);

        //Find the OwsContext ConfigurationAttribute
        SourceListCA formatCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(Document.sOrientation))
                formatCA = (SourceListCA)ca;

        JLabel formatName = new JLabel(formatCA.getName());
        uid.addComponent(formatName, formatCA, 0, 1, 1, 1);

        JComboBox formatBox = (JComboBox)uic.getCAManager().getRenderer(formatCA).createJComponentFromCA(formatCA);
        uid.addComponent(formatBox, formatCA, 1, 1, 2, 1);

        //Find the OwsContext ConfigurationAttribute
        SourceListCA orientationCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(Document.sOrientation))
                orientationCA = (SourceListCA)ca;

        JLabel orientationName = new JLabel(orientationCA.getName());
        uid.addComponent(orientationName, orientationCA, 0, 1, 1, 1);

        JComboBox orientationBox = (JComboBox)uic.getCAManager().getRenderer(orientationCA).createJComponentFromCA(orientationCA);
        uid.addComponent(orientationBox, orientationCA, 1, 1, 2, 1);
        orientationBox.addActionListener(EventHandler.create(ActionListener.class, this, "selectItem", "source" +
                ".selectedItem"));

        return uid;
    }

    public void selectItem(Object item){
        if(item.equals(Document.Format.CUSTOM.getName())){
            unitBox.isVisible(true);
            widthBox.isVisible(true);
            heightBox.isVisible(true);
        }
    }
}
