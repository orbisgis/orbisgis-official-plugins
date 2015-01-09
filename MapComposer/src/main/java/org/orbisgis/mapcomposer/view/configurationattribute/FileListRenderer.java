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
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.FileListCA;
import java.awt.FlowLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.io.File;
import javax.swing.*;

/**
 * Renderer associated to the FileListCA ConfigurationAttribute.
 * The JComponent returned by the createJComponentFromCA method look like :
 *  ________________________________________________________________
 * |                                  ____________________________  |
 * | NameOfTheConfigurationAttribute |selected value          | v | |
 * |                                 |________________________|___| |
 * |________________________________________________________________|
 *
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.FileListCA
 */
public class FileListRenderer implements CARenderer{

    @Override
    public JComponent createJComponentFromCA(ConfigurationAttribute ca) {
    //Create the component
        JComponent component = new JPanel();
        component.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the panel all the swing components
        FileListCA fileListCA = (FileListCA)ca;
        
        //component.add(new JLabel(fileListCA.getName()));
        //Display the FileListCA into a JComboBox
        JComboBox<File> jcb = new JComboBox(fileListCA.getValue().toArray(new String[0]));
        jcb.addActionListener(EventHandler.create(ActionListener.class, fileListCA, "select", "source.selectedItem"));
        jcb.setSelectedItem(ca.getValue());
        component.add(jcb);
        
        return component;
    }
}
