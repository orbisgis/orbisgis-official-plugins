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
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.element.SimpleGE;
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
import javax.swing.event.ChangeListener;
import java.awt.Color;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
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
    public UIPanel createConfigurationPanel(List<ConfigurationAttribute> caList, MainController mainController,
                                            boolean enableLock){

        //Create the UIDialogProperties that will be returned
        UIDialogProperties uid = new UIDialogProperties(mainController, enableLock);

        //Get the ConfigurationAttribute of the unit and its JComponent
        SourceListCA unitCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(Document.sUnit))
                unitCA = (SourceListCA)ca;

        JComboBox unitBox = (JComboBox)mainController.getCAManager().getRenderer(unitCA).createJComponentFromCA(unitCA);
        unitBox.addActionListener(EventHandler.create(ActionListener.class, this, "onUnitChange", "source"));
        unitBox.putClientProperty("last", unitBox.getSelectedItem());

        //Get the ConfigurationAttribute of the width and its JComponent
        IntegerCA widthCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(SimpleGE.sWidth))
                widthCA = (IntegerCA)ca;

        JLabel widthLabel = new JLabel(i18n.tr("width"));
        JSpinner widthSpinner = new JSpinner(new SpinnerNumberModel((int)widthCA.getValue(), 1, Integer.MAX_VALUE, 1));
        //Adds the listener and the client properties needed.
        widthSpinner.putClientProperty("unit", unitBox);
        widthSpinner.putClientProperty("ca", widthCA);
        widthSpinner.addChangeListener(EventHandler.create(ChangeListener.class, this, "onValueChange", "source"));

        widthLabel.setEnabled(false);
        widthSpinner.setEnabled(false);

        //Get the ConfigurationAttribute of the height and its JComponent
        IntegerCA heightCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(SimpleGE.sHeight))
                heightCA = (IntegerCA)ca;

        JLabel heightLabel = new JLabel(i18n.tr("height"));
        JSpinner heightSpinner = new JSpinner(new SpinnerNumberModel((int)heightCA.getValue(), 1, Integer.MAX_VALUE,1));
        //Adds the listener and the client properties needed.
        heightSpinner.putClientProperty("unit", unitBox);
        heightSpinner.putClientProperty("ca", heightCA);
        heightSpinner.addChangeListener(EventHandler.create(ChangeListener.class, this, "onValueChange", "source"));

        heightLabel.setEnabled(false);
        heightSpinner.setEnabled(false);

        unitBox.putClientProperty("widthSpinner", widthSpinner);
        unitBox.putClientProperty("heightSpinner", heightSpinner);

        //Get the ConfigurationAttribute of the format and its JComponent
        SourceListCA formatCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(Document.sFormat))
                formatCA = (SourceListCA)ca;

        JLabel formatName = new JLabel(formatCA.getName());
        uid.addComponent(formatName, formatCA, 0, 0, 1, 1);

        //Adds the listener and the client properties needed.
        JComboBox formatBox = (JComboBox)mainController.getCAManager().getRenderer(formatCA)
                .createJComponentFromCA(formatCA);
        formatBox.putClientProperty("widthLabel", widthLabel);
        formatBox.putClientProperty("widthSpinner", widthSpinner);
        formatBox.putClientProperty("heightLabel", heightLabel);
        formatBox.putClientProperty("heightSpinner", heightSpinner);
        formatBox.putClientProperty("unitBox", unitBox);
        formatBox.putClientProperty("formatCA", formatCA);
        formatBox.addActionListener(EventHandler.create(ActionListener.class, this, "onFormatChange", "source"));

        //Adds all the previous elements to the UIDialogProperties
        uid.addComponent(formatBox, formatCA, 1, 0, 2, 1);
        uid.addComponent(unitBox, unitCA, 0, 1, 2, 1);

        uid.addComponent(widthLabel, widthCA, 1, 1, 1, 1);
        uid.addComponent(widthSpinner, widthCA, 2, 1, 1, 1);

        uid.addComponent(heightLabel, heightCA, 1, 2, 1, 1);
        uid.addComponent(heightSpinner, heightCA, 2, 2, 1, 1);

        //Find the orientation ConfigurationAttribute
        SourceListCA orientationCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(Document.sOrientation))
                orientationCA = (SourceListCA)ca;

        JLabel orientationName = new JLabel(orientationCA.getName());
        uid.addComponent(orientationName, orientationCA, 0, 4, 1, 1);

        //Adds the listener and the client properties needed.
        JComboBox orientationBox = (JComboBox)mainController.getCAManager().getRenderer(orientationCA)
                .createJComponentFromCA(orientationCA);
        uid.addComponent(orientationBox, orientationCA, 1, 4, 2, 1);
        orientationBox.putClientProperty("last", orientationBox.getSelectedItem());
        orientationBox.putClientProperty("widthSpinner", widthSpinner);
        orientationBox.putClientProperty("heightSpinner", heightSpinner);
        orientationBox.addActionListener(EventHandler.create(
                ActionListener.class, this,"onOrientationChange", "source"));

        formatBox.putClientProperty("orientationBox", orientationBox);

        //Find the name ConfigurationAttribute
        StringCA nameCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(Document.sName))
                nameCA = (StringCA)ca;

        JLabel nameName = new JLabel(nameCA.getName());
        uid.addComponent(nameName, nameCA, 0, 6, 1, 1);

        JTextArea nameArea = (JTextArea)mainController.getCAManager().getRenderer(nameCA)
                .createJComponentFromCA(nameCA);
        uid.addComponent(nameArea, nameCA, 0, 7, 4, 4);

        //Sets the different comboBoxes
        formatBox.setSelectedItem(formatBox.getSelectedItem());
        unitBox.setSelectedItem(unitBox.getSelectedItem());
        orientationBox.setSelectedItem(orientationBox.getSelectedItem());

        return uid;
    }

    /**
     * When the value of the document format is changed, set the width and height value with the one from the format.
     * @param formatSpinner Spinner representing the format.
     */
    public void onFormatChange(Object formatSpinner){
        JComboBox formatBox = (JComboBox) formatSpinner;

        JComboBox unitBox = (JComboBox) ((JComboBox) formatSpinner).getClientProperty("unitBox");
        Document.Unit unit = Document.Unit.valueOf((String)unitBox.getSelectedItem());
        JComboBox orientationBox = (JComboBox) ((JComboBox) formatSpinner).getClientProperty("orientationBox");
        JLabel widthLabel = (JLabel) formatBox.getClientProperty("widthLabel");
        JSpinner widthSpinner = (JSpinner) formatBox.getClientProperty("widthSpinner");
        JLabel heightLabel = (JLabel) formatBox.getClientProperty("heightLabel");
        JSpinner heightSpinner = (JSpinner) formatBox.getClientProperty("heightSpinner");

        //If the format selected is CUSTOM, enable the spinners
        boolean isCustomSelected  = formatBox.getSelectedItem().equals(Document.Format.CUSTOM.getName());
        widthLabel.setEnabled(isCustomSelected);
        widthSpinner.setEnabled(isCustomSelected);
        heightLabel.setEnabled(isCustomSelected);
        heightSpinner.setEnabled(isCustomSelected);

        double width;
        double height;

        //Sets the width and height according to the format
        if(formatBox.getSelectedItem().equals(Document.Format.CUSTOM.getName())) {
            //If the format is custom, keep the previous width and height value
            width = Double.parseDouble(widthSpinner.getValue().toString());
            height = Double.parseDouble(heightSpinner.getValue().toString());
        }
        else {
            if (orientationBox.getSelectedItem().equals(Document.Orientation.PORTRAIT.getName())){
                width = Document.Format.valueOf((String) formatBox.getSelectedItem()).getPixelWidth() / unit.getConv();
                height = Document.Format.valueOf((String)formatBox.getSelectedItem()).getPixelHeight() / unit.getConv();
            }
            else{
                height = Document.Format.valueOf((String) formatBox.getSelectedItem()).getPixelWidth() / unit.getConv();
                width = Document.Format.valueOf((String) formatBox.getSelectedItem()).getPixelHeight() / unit.getConv();
            }
        }

        //Correct the values (round for millimeter, and two digits for inch)
        if(unit.equals(Document.Unit.IN)){
            widthSpinner.setValue( ((double) ((int)(width*100)) )/100 );
            heightSpinner.setValue( ((double) ((int)(height * 100))) / 100);
        }
        if(unit.equals(Document.Unit.MM)){
            widthSpinner.setValue((int)Math.round(width));
            heightSpinner.setValue((int)Math.round(height));
        }
        unitBox.setSelectedItem(unitBox.getSelectedItem());
    }

    /**
     * When the value of the document orientation is changed, invert width and height values.
     * @param orientationSpinner Spinner representing the format.
     */
    public void onOrientationChange(Object orientationSpinner){
        JComboBox orientation = (JComboBox) orientationSpinner;
        //Verify if the selected orientation is different from the last selected one
        if(!orientation.getSelectedItem().equals(orientation.getClientProperty("last"))) {
            JSpinner widthSpinner = (JSpinner) orientation.getClientProperty("widthSpinner");
            JSpinner heightSpinner = (JSpinner) orientation.getClientProperty("heightSpinner");
            //Invert the width and the height
            Object temp = widthSpinner.getValue();
            widthSpinner.setValue(heightSpinner.getValue());
            heightSpinner.setValue(temp);
            //Store the new orientation
            orientation.putClientProperty("last", orientation.getSelectedItem());
        }
    }

    /**
     * When the value of the document unit is changed, convert the width and height values.
     * @param unitSpinner Spinner representing the format.
     */
    public void onUnitChange(Object unitSpinner){
        JComboBox unitBox = (JComboBox) unitSpinner;
        Document.Unit unitBefore = Document.Unit.valueOf((String) unitBox.getClientProperty("last"));
        Document.Unit unitNow = Document.Unit.valueOf((String) unitBox.getSelectedItem());

        JSpinner widthSpinner = (JSpinner) unitBox.getClientProperty("widthSpinner");
        JSpinner heightSpinner = (JSpinner) unitBox.getClientProperty("heightSpinner");

        //Get the actual width and height value
        double width = 1;
        if(widthSpinner.getValue() instanceof Double){
            width = (double)widthSpinner.getValue();
        }
        if(widthSpinner.getValue() instanceof Integer){
            width = (int)widthSpinner.getValue();
        }
        double height = 1;
        if(heightSpinner.getValue() instanceof Double){
            height = (double)heightSpinner.getValue();
        }
        if(heightSpinner.getValue() instanceof Integer){
            height = (int)heightSpinner.getValue();
        }

        //Convert the value to the pixel unit
        width *= unitBefore.getConv();
        height *= unitBefore.getConv();

        //Convert the pixel value to the new unit
        width /= unitNow.getConv();
        height /= unitNow.getConv();

        //According to the unit set the width and height spinner model and store the new unit value
        switch(unitNow){
            case PIXEL:
                width = (int)Math.round(width);
                height = (int)Math.round(height);
                widthSpinner.setModel(new SpinnerNumberModel(width, 1, Integer.MAX_VALUE, 1));
                heightSpinner.setModel(new SpinnerNumberModel(height, 1, Integer.MAX_VALUE, 1));
                unitBox.putClientProperty("last", Document.Unit.PIXEL.name());
                break;
            case MM:
                width = (int)Math.round(width);
                height = (int)Math.round(height);
                widthSpinner.setModel(new SpinnerNumberModel(width, 1, Integer.MAX_VALUE, 1));
                heightSpinner.setModel(new SpinnerNumberModel(height, 1, Integer.MAX_VALUE, 1));
                unitBox.putClientProperty("last", Document.Unit.MM.name());
                break;
            case IN:
                width = ((double) ((int)(width*100)) )/100;
                height = ((double) ((int)(height * 100))) / 100;
                widthSpinner.setModel(new SpinnerNumberModel(width, 1, Integer.MAX_VALUE, 0.1));
                heightSpinner.setModel(new SpinnerNumberModel(height, 1, Integer.MAX_VALUE, 0.1));
                unitBox.putClientProperty("last", Document.Unit.IN.name());
                break;
        }
    }

    /**
     * When the value of the width or height spinner is changed, set the corresponding IntegerCA value.
     * @param spinner Width or height spinner.
     */
    public void onValueChange(Object spinner){
        JSpinner unitSpinner = (JSpinner) spinner;
        String unit = (String) ((JComboBox) unitSpinner.getClientProperty("unit")).getSelectedItem();
        double value = 1;
        //Get the value which can be Doudle or Integer
        if(unitSpinner.getValue() instanceof Double){
            value = (double)unitSpinner.getValue();
        }
        if(unitSpinner.getValue() instanceof Integer){
            value = (int)unitSpinner.getValue();
        }
        IntegerCA ca = (IntegerCA) unitSpinner.getClientProperty("ca");
        ca.setValue((int)(value * Document.Unit.valueOf(unit).getConv()));
    }
}
