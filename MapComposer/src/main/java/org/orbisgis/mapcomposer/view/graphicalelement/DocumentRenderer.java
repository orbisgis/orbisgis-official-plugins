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

package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.IntegerCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.OwsContextCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.element.SimpleGE;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.view.utils.UIDialogProperties;
import org.orbisgis.sif.UIPanel;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.SpinnerNumberModel;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.event.ItemListener;
import java.awt.image.BufferedImage;
import java.beans.EventHandler;
import java.util.List;

/**
 * Renderer associated to the Document GraphicalElement.
 *
 * @author Sylvain PALOMINOS
 */
public class DocumentRenderer implements RendererRaster, RendererVector, CustomConfigurationPanel {

    /** Object for the translation*/
    private static final I18n i18n = I18nFactory.getI18n(DocumentRenderer.class);

    @Override
    public void drawGE(Graphics2D graphics2D, GraphicalElement ge) {
        //Returns a white rectangle without applying any rotation
        graphics2D.setPaint(new Color(255, 255, 255));
        graphics2D.fillRect(0, 0, ge.getWidth(), ge.getHeight());
    }

    @Override
    public BufferedImage createGEImage(GraphicalElement ge) {
        BufferedImage bi = new BufferedImage(ge.getWidth(), ge.getHeight(), BufferedImage.TYPE_INT_ARGB);
        drawGE(bi.createGraphics(), ge);
        return bi;
    }

    @Override
    public UIPanel createConfigurationPanel(List<ConfigurationAttribute> caList, MainController mainController, boolean enableLock){

        //Create the UIDialogProperties that will be returned
        UIDialogProperties uid = new UIDialogProperties(mainController, enableLock);

        //Get the ConfigurationAttribute of the width and its JComponent
        IntegerCA widthCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(SimpleGE.sWidth))
                widthCA = (IntegerCA)ca;

        JLabel widthLabel = new JLabel(i18n.tr("width"));
        JSpinner widthSpinner = (JSpinner)mainController.getCAManager().getRenderer(widthCA).createJComponentFromCA(widthCA);

        widthLabel.setEnabled(false);
        widthSpinner.setEnabled(false);

        //Get the ConfigurationAttribute of the height and its JComponent
        IntegerCA heightCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(SimpleGE.sHeight))
                heightCA = (IntegerCA)ca;

        JLabel heightLabel = new JLabel(i18n.tr("height"));
        JSpinner heightSpinner = (JSpinner)mainController.getCAManager().getRenderer(widthCA).createJComponentFromCA(heightCA);

        heightLabel.setEnabled(false);
        heightSpinner.setEnabled(false);

        //Get the ConfigurationAttribute of the unit and its JComponent
        SourceListCA unitCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(Document.sUnit))
                unitCA = (SourceListCA)ca;

        JComboBox unitBox = (JComboBox)mainController.getCAManager().getRenderer(unitCA).createJComponentFromCA(unitCA);

        //Get the ConfigurationAttribute of the format and its JComponent
        SourceListCA formatCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(Document.sFormat))
                formatCA = (SourceListCA)ca;

        JLabel formatName = new JLabel(formatCA.getName());
        uid.addComponent(formatName, formatCA, 0, 0, 1, 1);

        //Adds an action listener to enable or disable the width and height spinner if the CUSTOM format is selected
        JComboBox formatBox = (JComboBox)mainController.getCAManager().getRenderer(formatCA).createJComponentFromCA(formatCA);
        formatBox.putClientProperty("widthLabel", widthLabel);
        formatBox.putClientProperty("widthSpinner", widthSpinner);
        formatBox.putClientProperty("heightLabel", heightLabel);
        formatBox.putClientProperty("heightSpinner", heightSpinner);
        formatBox.putClientProperty("mainController", mainController);
        formatBox.putClientProperty("formatCA", formatCA);
        formatBox.addActionListener(EventHandler.create(ActionListener.class, this, "selectItem", "source"));

        //Adds all the previous elements to the UIDialogProperties
        uid.addComponent(formatBox, formatCA, 1, 0, 2, 1);
        uid.addComponent(unitBox, unitCA, 0, 1, 2, 1);

        uid.addComponent(widthLabel, widthCA, 1, 1, 1, 1);
        uid.addComponent(widthSpinner, widthCA, 2, 1, 1, 1);

        uid.addComponent(heightLabel, heightCA, 1, 2, 1, 1);
        uid.addComponent(heightSpinner, heightCA, 2, 2, 1, 1);

        //Find the OwsContext ConfigurationAttribute
        SourceListCA orientationCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(Document.sOrientation))
                orientationCA = (SourceListCA)ca;

        JLabel orientationName = new JLabel(orientationCA.getName());
        uid.addComponent(orientationName, orientationCA, 0, 4, 1, 1);

        JComboBox orientationBox = (JComboBox)mainController.getCAManager().getRenderer(orientationCA).createJComponentFromCA(orientationCA);
        uid.addComponent(orientationBox, orientationCA, 1, 4, 2, 1);

        //Find the OwsContext ConfigurationAttribute
        StringCA nameCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(Document.sName))
                nameCA = (StringCA)ca;

        JLabel nameName = new JLabel(nameCA.getName());
        uid.addComponent(nameName, nameCA, 0, 6, 1, 1);

        JTextArea nameArea = (JTextArea)mainController.getCAManager().getRenderer(nameCA).createJComponentFromCA(nameCA);
        uid.addComponent(nameArea, nameCA, 0, 7, 4, 4);

        return uid;
    }

    public void selectItem(Object item){
        JComboBox formatBox = (JComboBox) item;
        if(formatBox.getSelectedItem().equals(Document.Format.CUSTOM.getName())) {
            JLabel widthLabel = (JLabel) formatBox.getClientProperty("widthLabel");
            widthLabel.setEnabled(true);
            JSpinner widthSpinner = (JSpinner) formatBox.getClientProperty("widthSpinner");
            widthSpinner.setEnabled(true);
            JLabel heightLabel = (JLabel) formatBox.getClientProperty("heightLabel");
            heightLabel.setEnabled(true);
            JSpinner heightSpinner = (JSpinner) formatBox.getClientProperty("heightSpinner");
            heightSpinner.setEnabled(true);

            widthSpinner.setValue(50);
            heightSpinner.setValue(50);
        }
        else{
            JLabel widthLabel = (JLabel) formatBox.getClientProperty("widthLabel");
            widthLabel.setEnabled(false);
            JSpinner widthSpinner = (JSpinner) formatBox.getClientProperty("widthSpinner");
            widthSpinner.setEnabled(false);
            JLabel heightLabel = (JLabel) formatBox.getClientProperty("heightLabel");
            heightLabel.setEnabled(false);
            JSpinner heightSpinner = (JSpinner) formatBox.getClientProperty("heightSpinner");
            heightSpinner.setEnabled(false);

            widthSpinner.setValue(Document.Format.valueOf((String) formatBox.getSelectedItem()).getPixelWidth());
            heightSpinner.setValue(Document.Format.valueOf((String) formatBox.getSelectedItem()).getPixelHeight());
        }
    }
}
