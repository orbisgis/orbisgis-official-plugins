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
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.OwsContextCA;
import org.orbisgis.sif.common.ContainerItem;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.io.File;
import java.util.*;
import javax.swing.*;

/**
 * Renderer associated to the OwsContextCA ConfigurationAttribute.
 * The JPanel returned by the createJComponentFromCA method look like :
 *  ____________________________
 * |selected value          | v |
 * |________________________|___|
 *
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.OwsContextCA
 *
 * @author Sylvain PALOMINOS
 */
public class OwsContextRenderer implements CARenderer{

    @Override
    public JComponent createJComponentFromCA(ConfigurationAttribute ca) {
        OwsContextCA owsContextCA = (OwsContextCA)ca;

        //Display the OwsContextCA into a JComboBox
        List<ContainerItem<String>> listContainer = new ArrayList<>();
        for(String string : owsContextCA.getValue()){
            listContainer.add(new ContainerItem<>(string, (new File(string.toString()).getName())));
        }
        JComboBox<ContainerItem<String>> jcb = new JComboBox<ContainerItem<String>>(listContainer.toArray(new ContainerItem[0]));
        jcb.addItem(new ContainerItem<String>("", "< none >"));
        jcb.putClientProperty("ca", owsContextCA);
        //Try to select the selected value of the OwsContextCA in the JComboBox
        if(owsContextCA.getSelected()!=null)
            jcb.setSelectedItem(listContainer.get(owsContextCA.getValue().indexOf(owsContextCA.getSelected())));
        else
            jcb.setSelectedIndex(jcb.getItemCount() - 1);
        //Adds a listener to set the ConfigurationAttribute represented to the JComboBox selected value.
        jcb.addActionListener(EventHandler.create(ActionListener.class, this, "setCA", ""));

        return jcb;
    }

    public void setCA(ActionEvent actionEvent){
        if((actionEvent.getSource() instanceof JComboBox)){
            OwsContextCA owsContextCA = (OwsContextCA)((JComboBox) actionEvent.getSource()).getClientProperty("ca");
            ContainerItem<String> container = (ContainerItem)((JComboBox) actionEvent.getSource()).getSelectedItem();
            owsContextCA.select(container.getKey());
        }
    }
}
