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
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.OwsContextCA;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import javax.swing.*;

/**
 * Renderer associated to the OwsContextCA ConfigurationAttribute.
 * The JPanel returned by the createJComponentFromCA method look like :
 *  _________________________________________________________________
 * |                                  ____________________________   |
 * | NameOfTheConfigurationAttribute |selected value          | v |  |
 * |                                 |________________________|___|  |
 * |_________________________________________________________________|
 *
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.OwsContextCA
 */
public class OwsContextRenderer implements CARenderer{

    @Override
    public JComponent createJComponentFromCA(ConfigurationAttribute ca) {
    //Create the component
        JComponent component = new JPanel();
        component.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the component all the swing components
        OwsContextCA owsContextCA = (OwsContextCA)ca;
        component.add(new JLabel(owsContextCA.getName()));

        //Display the OwsContextCA into a JComboBox
        JComboBox<String> jcb = new JComboBox(owsContextCA.getValue().toArray());
        //Adds an empty ows-context
        jcb.addItem("< none >");
        //Try to select the selected value of the OwsContextCA in the JComboBox
        if(owsContextCA.getSelected()!=null)
            jcb.setSelectedItem(owsContextCA.getSelected());
        else
            jcb.setSelectedIndex(jcb.getItemCount() - 1);
        //Add an action listener to set the ConfigurationAttribute with the selected value of the JComboBox
        jcb.addActionListener(EventHandler.create(ActionListener.class, owsContextCA, "select", "source.selectedItem"));
        component.add(jcb);
        return component;
    }
}
