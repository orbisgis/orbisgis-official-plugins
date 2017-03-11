/*
* MapComposer is an OrbisGIS plugin dedicated to the creation of cartographic
* documents.
*
* This plugin was firstly developed  at French IRSTV institute as part of the MApUCE project,
* funded by the French Agence Nationale de la Recherche (ANR) under contract ANR-13-VBDU-0004.
* 
* Since 2015, MapComposer is developed and maintened by the GIS group of the DECIDE team of the 
* Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
*
* The GIS group of the DECIDE team is located at :
*
* Laboratoire Lab-STICC – CNRS UMR 6285
* Equipe DECIDE
* UNIVERSITÉ DE BRETAGNE-SUD
* Institut Universitaire de Technologie de Vannes
* 8, Rue Montaigne - BP 561 56017 Vannes Cedex
*
* Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
* Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
*
* The MapComposer plugin is distributed under GPL 3 license. 
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
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.ColorCA;
import org.orbisgis.mapcomposer.view.utils.ColorChooser;
import org.orbisgis.sif.UIFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.beans.PropertyChangeListener;
import javax.swing.*;

/**
 * Renderer associated to the ColorCA ConfigurationAttribute.
 * The JComponent returned by the createJComponentFromCA method look like :
 *  _________
 * |  Button |
 * |_________|
 *
 * The color is chosen by clicking on a button. It opens a ColorChooser and the color is saved in the background of the button
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.ColorCA
 *
 * @author Sylvain PALOMINOS
 */
public class ColorRenderer implements CARenderer{

    /** Translation*/
    private static final I18n i18n = I18nFactory.getI18n(ColorRenderer.class);

    @Override
    public JComponent createJComponentFromCA(ConfigurationAttribute ca) {
        final ColorCA colorCA = (ColorCA)ca;

        JButton button = new JButton(i18n.tr("Text demo"));
        //Display the color in the button background
        button.setBackground(colorCA.getValue());
        //On clicking on the button, open a color chooser
        button.addActionListener(EventHandler.create(ActionListener.class, this, "open", "source"));
        button.addPropertyChangeListener(EventHandler.create(PropertyChangeListener.class, colorCA, "setValue", "source.background"));

        return button;
    }

    /**
     * Open a ColorChooser and show it.
     * @param component The chosen color will be saved in the background of the component
     */
    public void open(JComponent component){
        ColorChooser colorChooser = new ColorChooser(component);
        UIFactory.showDialog(colorChooser, true, true);
    }

}
