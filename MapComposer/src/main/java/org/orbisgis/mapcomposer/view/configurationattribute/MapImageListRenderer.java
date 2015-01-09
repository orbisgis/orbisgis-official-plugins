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

package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.MapImageListCA;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;

import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.util.ArrayList;
import javax.swing.*;

/**
 * Renderer associated to the LinkToMap CA
 * The JPanel returned by the createJComponentFromCA method look like :
 *  _________________________________________________________________
 * |                                  ____________________________   |
 * | NameOfTheConfigurationAttribute |selected value          | v |  |
 * |                                 |________________________|___|  |
 * |_________________________________________________________________|
 *
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.MapImageListCA
 */
public class MapImageListRenderer implements CARenderer{

    @Override
    public JComponent createJComponentFromCA(ConfigurationAttribute ca) {
    //Create the component
        JComponent component = new JPanel();
        component.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the component all the swing components
        final MapImageListCA milka = (MapImageListCA)ca;
        
        //component.add(new JLabel(milka.getName()));
        ArrayList<String> names = new ArrayList<>();
        //Display the MapImageListCA into a JComboBox
        for(MapImage mi : milka.getValue())
            names.add(mi.toString());
        JComboBox<String> jcb = new JComboBox(names.toArray());
        jcb.addActionListener(EventHandler.create(ActionListener.class, milka, "select", "source.selectedItem"));
        jcb.setSelectedItem(ca.getValue());
        component.add(jcb);
        return component;
    }
}
