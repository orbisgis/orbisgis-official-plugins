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

import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.*;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.graphicalelement.element.text.TextElement;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import org.orbisgis.mapcomposer.view.ui.MainWindow;
import org.orbisgis.mapcomposer.view.utils.UIDialogProperties;
import org.orbisgis.sif.UIPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;

/**
 * Renderer associated to the Cartographic GraphicalElement.
 *
 * @author Sylvain PALOMINOS
 */
public class MapImageRenderer extends SimpleGERenderer {

    @Override
    public BufferedImage createImageFromGE(GraphicalElement ge) {
        BufferedImage bi;
        // Draw in a BufferedImage the map image
        if (((MapImage) ge).getImage() != null) {
            bi = ((MapImage) ge).getImage();
        } else {
            //Return the icon of the MapImage as BufferedImage
            ImageIcon icon = new ImageIcon(MainWindow.class.getResource("add_map.png"));
            bi = new BufferedImage(icon.getIconWidth(), icon.getIconHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics g = bi.createGraphics();
            icon.paintIcon(null, g, 0, 0);
            g.dispose();
        }

        //Scale the bufferedImage to the GraphicalElement size
        java.awt.Image image = bi.getScaledInstance(ge.getWidth(), ge.getHeight(), Image.SCALE_SMOOTH);
        BufferedImage resize = new BufferedImage(ge.getWidth(), ge.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics graph = resize.createGraphics();
        graph.drawImage(image, 0, 0, null);
        graph.dispose();

        return applyRotationToBufferedImage(resize, ge);
    }

    public UIPanel createConfigurationPanel(java.util.List<ConfigurationAttribute> caList, UIController uic, boolean enableLock){
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
        owscBox.setPreferredSize(new Dimension(150, (int) owscBox.getPreferredSize().getHeight()));
        owsc.add(owscBox);
        //Add the ConfigurationAttribute and its representation to the UIDialogProperties
        uid.addComponent(owsc, owscCA, enableLock);

        return uid;
    }
}
