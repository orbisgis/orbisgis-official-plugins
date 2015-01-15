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

package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.FileListCA;
import org.orbisgis.sif.common.ContainerItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.io.File;
import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Renderer associated to the FileListCA ConfigurationAttribute.
 * The JComponent returned by the createJComponentFromCA method look like :
 *  ____________________________
 * |selected value          | v |
 * |________________________|___|
 *
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.FileListCA
 *
 * @author Sylvain PALOMINOS
 */
public class FileListRenderer implements CARenderer{

    @Override
    public JComponent createJComponentFromCA(ConfigurationAttribute ca) {
        FileListCA fileListCA = (FileListCA)ca;

        //Display the FileListCA into a JComboBox
        List<ContainerItem<File>> listContainer = new ArrayList<>();
        for(File f : fileListCA.getValue()){
            listContainer.add(new ContainerItem<File>(f, f.getName()));
        }
        JComboBox<ContainerItem<File>> jcb = new JComboBox<ContainerItem<File>>(listContainer.toArray(new ContainerItem[0]));
        jcb.putClientProperty("ca", fileListCA);
        jcb.setSelectedItem(ca.getValue());
        //Adds a listener to set the ConfigurationAttribute represented to the JComboBox selected value.
        jcb.addActionListener(EventHandler.create(ActionListener.class, this, "setCA", ""));
        
        return jcb;
    }

    public void setCA(ActionEvent actionEvent){
        if((actionEvent.getSource() instanceof JComboBox)){
            FileListCA fileListCA = (FileListCA)((JComboBox) actionEvent.getSource()).getClientProperty("ca");
            ContainerItem<File> container = (ContainerItem)((JComboBox) actionEvent.getSource()).getSelectedItem();
            fileListCA.select(container.getKey());
        }
    }
}
