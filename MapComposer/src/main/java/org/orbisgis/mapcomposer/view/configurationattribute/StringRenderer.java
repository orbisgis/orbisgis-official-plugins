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
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA;
import java.awt.FlowLayout;
import java.beans.EventHandler;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 * Renderer associated to the Text ConfigurationAttribute.
 * The JPanel returned by the createJComponentFromCA method look like :
 *  _________________________________________________________________
 * |                                  ____________________________   |
 * | NameOfTheConfigurationAttribute |Some text                   |  |
 * |                                 |____________________________|  |
 * |_________________________________________________________________|
 *
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA
 */
public class StringRenderer implements CARenderer{

    @Override
    public JComponent createJComponentFromCA(ConfigurationAttribute ca) {
    //Create the component
        JComponent component = new JPanel();
        component.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the component all the swing components
        StringCA stringCA = (StringCA)ca;
        
        component.add(new JLabel(stringCA.getName()));
        //Display the StringCA into a JTextArea
        JTextArea area = new JTextArea(stringCA.getValue());
        //"Save" the CA inside the JTextField
        area.getDocument().putProperty("StringCA", stringCA);
        area.getDocument().addDocumentListener(EventHandler.create(DocumentListener.class, this, "saveDocumentText", "document"));
        component.add(area);
        
        return component;
    }

    /**
     * Save the text contained by the Document in the ConfigurationAttribute set as property.
     * @param document
     */
    public void saveDocumentText(Document document){
        try {
            ((StringCA)document.getProperty("StringCA")).setValue(document.getText(0, document.getLength()));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
