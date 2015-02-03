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

import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA;
import org.slf4j.LoggerFactory;

import java.awt.*;
import java.beans.EventHandler;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Renderer associated to the Text ConfigurationAttribute.
 * The JPanel returned by the createJComponentFromCA method look like :
 *  __________________________________________________
 * |Some text                                         |
 * |                                                  |
 * |__________________________________________________|
 *
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA
 *
 * @author Sylvain PALOMINOS
 */
public class StringRenderer implements CARenderer{

    @Override
    public JComponent createJComponentFromCA(ConfigurationAttribute ca) {
        StringCA stringCA = (StringCA)ca;

        //Display the StringCA into a JTextArea
        JTextArea area = new JTextArea(stringCA.getValue());
        area.setAlignmentX(Component.LEFT_ALIGNMENT);
        area.setColumns(35);
        area.setRows(7);
        area.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        //"Save" the CA inside the JTextField
        area.getDocument().putProperty("StringCA", stringCA);
        area.getDocument().addDocumentListener(EventHandler.create(DocumentListener.class, this, "saveDocumentText", "document"));
        
        return area;
    }

    /**
     * Save the text contained by the Document in the ConfigurationAttribute set as property.
     * @param document
     */
    public void saveDocumentText(Document document){
        try {
            ((StringCA)document.getProperty("StringCA")).setValue(document.getText(0, document.getLength()));
        } catch (BadLocationException e) {
            LoggerFactory.getLogger(MainController.class).error(e.getMessage());
        }
    }
}
