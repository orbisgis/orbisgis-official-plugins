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
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceCA;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.orbisgis.sif.UIFactory;
import org.orbisgis.sif.components.OpenFilePanel;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.io.File;

/**
 * Renderer associated to the Source ConfigurationAttribute.
 * The JPanel returned by the createJComponentFromCA method look like :
 *  _____________________________________________________________________________________
 * |                                  ____________________________        _____________  |
 * | NameOfTheConfigurationAttribute | text field with the path   |      |Button browse| |
 * |                                 |____________________________|      |_____________| |
 * |_____________________________________________________________________________________|
 *
 * A button open a JFileChooser to permit to the user to find the source file.
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceCA
 *
 * @author Sylvain PALOMINOS
 */
public class SourceRenderer implements CARenderer{

    /** Object for the translation*/
    private static final I18n i18n = I18nFactory.getI18n(SourceRenderer.class);

    @Override
    public JComponent createJComponentFromCA(ConfigurationAttribute ca) {
    //Create the component
        JComponent component = new JPanel();
        component.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the component all the swing components
        final SourceCA sourceCA = (SourceCA)ca;
        
        //component.add(new JLabel(sourceCA.getName()));
        //Display the SourceCA into a JTextField
        JTextField jtf = new JTextField();
        jtf.setColumns(25);
        //"Save" the CA inside the JTextField
        jtf.getDocument().putProperty("SourceCA", sourceCA);
        //add the listener for the text changes in the JTextField
        jtf.getDocument().addDocumentListener(EventHandler.create(DocumentListener.class, this, "saveDocumentText", "document"));

        if(sourceCA.getValue()!="")
            jtf.setText(new File(sourceCA.getValue()).getName());
        else {
            //Load the last path use in a sourceCA
            OpenFilePanel openFilePanel = new OpenFilePanel("ConfigurationAttribute.SourceCA", i18n.tr
                    ("Select " +
                    "source"));
            openFilePanel.addFilter(new String[]{"*"}, i18n.tr("All files"));
            openFilePanel.loadState();
            jtf.setText(openFilePanel.getCurrentDirectory().getAbsolutePath());
        }

        component.add(jtf);
        //Create the button Browse
        JButton button = new JButton(i18n.tr("Browse"));
        //"Save" the sourceCA and the JTextField in the button
        button.putClientProperty("SourceCA", sourceCA);
        button.putClientProperty("JTextField", jtf);
        //Add the listener for the click on the button
        button.addActionListener(EventHandler.create(ActionListener.class, this, "openLoadPanel", ""));
        
        component.add(button);
        return component;
    }

    /**
     * Opens an LoadPanel to permit to the user to select the file to load.
     * @param event
     */
    public void openLoadPanel(ActionEvent event){
        OpenFilePanel openFilePanel = new OpenFilePanel("ConfigurationAttribute.SourceCA", i18n.tr("Select " +
                "source"));
        openFilePanel.addFilter(new String[]{"*"}, i18n.tr("All files"));
        openFilePanel.loadState();
        if (UIFactory.showDialog(openFilePanel, true, true)) {
            JButton source = (JButton)event.getSource();
            SourceCA sourceCA = (SourceCA)source.getClientProperty("SourceCA");
            sourceCA.setValue(openFilePanel.getSelectedFile().getAbsolutePath());
            JTextField textField = (JTextField)source.getClientProperty("JTextField");
            textField.setText(openFilePanel.getSelectedFile().getName());
        }
    }

    /**
     * Save the text contained by the Document in the ConfigurationAttribute set as property.
     * @param document
     */
    public void saveDocumentText(Document document){
        try {
            SourceCA sourceCA = (SourceCA)document.getProperty("SourceCA");
            String name = document.getText(0, document.getLength());
            if(new File(name).exists())
                sourceCA.setValue(name);
        } catch (BadLocationException e) {
            LoggerFactory.getLogger(MainController.class).error(e.getMessage());
        }
    }
}
