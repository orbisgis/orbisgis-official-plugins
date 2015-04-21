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
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.MapImageListCA;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import org.orbisgis.sif.common.ContainerItem;

import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.util.ArrayList;
import javax.swing.*;
import java.util.List;

/**
 * Renderer associated to the LinkToMap CA
 * The JPanel returned by the createJComponentFromCA method look like :
 *  ____________________________
 * |selected value          | v |
 * |________________________|___|
 *
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.MapImageListCA
 *
 * @author Sylvain PALOMINOS
 */
public class MapImageListRenderer implements CARenderer{

    @Override
    public JComponent createJComponentFromCA(ConfigurationAttribute ca) {
        final MapImageListCA milka = (MapImageListCA)ca;

        //Create a MapImage list
        List<ContainerItem<MapImage>> listContainer = new ArrayList<>();
        for(MapImage mapImage : milka.getValue()){
            if(mapImage != null) {
                listContainer.add(new ContainerItem<>(mapImage, mapImage.getOwsMapContext().getTitle()));
            }
        }
        //Adds a null MapImage
        listContainer.add(new ContainerItem<MapImage>(null, "<none>"));

        //Display the MapImage list into a JComboBox
        JComboBox<ContainerItem<MapImage>> jcb = new JComboBox<ContainerItem<MapImage>>(listContainer.toArray(new ContainerItem[0]));
        jcb.putClientProperty("ca", milka);
        if(milka.getSelected()!=null)
            jcb.setSelectedItem(listContainer.get(milka.getValue().indexOf(milka.getSelected())));
        else
            jcb.setSelectedIndex(jcb.getItemCount() - 1);
        //Adds a listener to set the ConfigurationAttribute represented to the JComboBox selected value.
        jcb.addActionListener(EventHandler.create(ActionListener.class, this, "setCA", ""));

        return jcb;
    }

    public void setCA(ActionEvent actionEvent){
        if((actionEvent.getSource() instanceof JComboBox)){
            MapImageListCA milka = (MapImageListCA)((JComboBox) actionEvent.getSource()).getClientProperty("ca");
            ContainerItem<MapImage> container = (ContainerItem)((JComboBox) actionEvent.getSource()).getSelectedItem();
            milka.select(container.getKey());
        }
    }
}
