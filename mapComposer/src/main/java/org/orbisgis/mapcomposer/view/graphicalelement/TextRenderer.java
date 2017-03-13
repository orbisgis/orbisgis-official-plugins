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

package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.commons.progress.ProgressMonitor;
import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.ColorCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.IntegerCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.element.text.TextElement;
import org.orbisgis.mapcomposer.view.utils.UIDialogProperties;
import org.orbisgis.sif.UIPanel;
import org.orbisgis.sif.common.ContainerItem;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.beans.EventHandler;
import java.text.AttributedString;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * Renderer associated to the scale GraphicalElement.
 *
 * @author Sylvain PALOMINOS
 */
public class TextRenderer implements RendererRaster, RendererVector, CustomConfigurationPanel {

    /** Object for the translation*/
    private static final I18n i18n = I18nFactory.getI18n(TextRenderer.class);

    @Override
    public void drawGE(Graphics2D graphics2D, GraphicalElement ge){

        double rad = Math.toRadians(ge.getRotation());
        double newHeight = Math.abs(sin(rad)*ge.getWidth())+Math.abs(cos(rad)*ge.getHeight());
        double newWidth = Math.abs(sin(rad)*ge.getHeight())+Math.abs(cos(rad)*ge.getWidth());

        int x = Math.max((int)newWidth, ge.getWidth())/2;
        int y = Math.max((int)newHeight, ge.getHeight())/2;
        graphics2D.rotate(rad, x, y);

        TextElement te = ((TextElement)ge);
        //Drawing on a BufferedImage the text.
        graphics2D.setColor(new Color(te.getColorBack().getRed(), te.getColorBack().getGreen(), te.getColorBack().getBlue(), te.getAlpha()*255/100));
        graphics2D.fillRect(x - ge.getWidth()/2, y - ge.getHeight()/2, te.getWidth(), te.getHeight());
        //graph = bi.createGraphics();

        //Draw the string and make it fit to the TextElement bounds
        int textY = y - ge.getHeight()/2;
        AttributedString attributedString;
        LineBreakMeasurer measurer;
        //Split the text to draw it line after line
        for(String s : te.getText().split("\n")) {
            //Replace the empty string by "\n"
            if(s.equals("")) {
                attributedString = new AttributedString("\n");
            }
            else {
                attributedString = new AttributedString(s);
            }
            attributedString.addAttribute(TextAttribute.FONT, new Font(te.getFont(), te.getStyle(), te.getFontSize
                    ()));
            attributedString.addAttribute(TextAttribute.FOREGROUND, te.getColorText());
            //Cut the text if it's too wide for the BufferedImage width
            int textX = x - ge.getWidth()/2;
            measurer = new LineBreakMeasurer(attributedString.getIterator(), graphics2D.getFontRenderContext());
            while (measurer.getPosition() < attributedString.getIterator().getEndIndex()) {
                TextLayout textLayout = measurer.nextLayout(te.getWidth());
                textY += textLayout.getAscent();
                switch (te.getAlignment()) {
                    case LEFT:
                        textX = x - ge.getWidth()/2 + 0;
                        break;
                    case CENTER:
                        textX = x - ge.getWidth()/2 + (int) ((te.getWidth() - textLayout.getBounds().getWidth()) / 2);
                        break;
                    case RIGHT:
                        textX = x - ge.getWidth()/2 + (int) (te.getWidth() - textLayout.getBounds().getWidth());
                        break;
                }
                textLayout.draw(graphics2D, textX, textY);
                textY += textLayout.getDescent() + textLayout.getLeading();
            }
        }
    }

    @Override
    public BufferedImage createGEImage(GraphicalElement ge, ProgressMonitor pm) {

        double rad = Math.toRadians(ge.getRotation());
        double newHeight = Math.abs(sin(rad)*ge.getWidth())+Math.abs(cos(rad)*ge.getHeight());
        double newWidth = Math.abs(sin(rad)*ge.getHeight())+Math.abs(cos(rad)*ge.getWidth());

        int maxWidth = Math.max((int)newWidth, ge.getWidth());
        int maxHeight = Math.max((int)newHeight, ge.getHeight());

        BufferedImage bi = new BufferedImage(maxWidth, maxHeight, BufferedImage.TYPE_INT_ARGB);
        drawGE(bi.createGraphics(), ge);
        return bi;
    }

    @Override
    public UIPanel createConfigurationPanel(List<ConfigurationAttribute> caList, MainController uic, boolean enableLock) {


        //Create the UIDialogProperties that will be returned
        UIDialogProperties uid = new UIDialogProperties(uic, enableLock);
        JLabel name;
        JComponent component;

        //Add the text configuration elements
        //Find the Text ConfigurationAttribute
        StringCA textCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(TextElement.sText))
                textCA = (StringCA)ca;
        //Create the list of component composing the ConfigurationAttribute representation.
        name = new JLabel(i18n.tr(textCA.getName()));
        uid.addComponent(name, textCA, 0, 0, 1, 1);

        component = new JScrollPane(uic.getCAManager().getRenderer(textCA).createJComponentFromCA(textCA));
        uid.addComponent(component, textCA, 0, 1, 4, 3);

        //Find the Font ConfigurationAttribute
        SourceListCA fontCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(TextElement.sFont))
                fontCA = (SourceListCA)ca;
        //Create the list of component composing the ConfigurationAttribute representation.
        name = new JLabel(i18n.tr(fontCA.getName()));
        uid.addComponent(name, fontCA, 0, 4, 1, 1);

        component = uic.getCAManager().getRenderer(fontCA).createJComponentFromCA(fontCA);
        uid.addComponent(component, fontCA, 1, 4, 3, 1);

        //Find the Font ConfigurationAttribute
        IntegerCA fontSizeCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(TextElement.sFontSize))
                fontSizeCA = (IntegerCA)ca;
        //Create the list of component composing the ConfigurationAttribute representation.
        name = new JLabel(i18n.tr(fontSizeCA.getName()));
        uid.addComponent(name, fontSizeCA, 0, 5, 2, 1);

        component = uic.getCAManager().getRenderer(fontSizeCA).createJComponentFromCA(fontSizeCA);
        uid.addComponent(component, fontSizeCA, 2, 5, 1, 1);

        //Find the Font ConfigurationAttribute
        SourceListCA styleCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(TextElement.sStyle))
                styleCA = (SourceListCA) ca;
        //Create the list of component composing the ConfigurationAttribute representation.
        name = new JLabel(i18n.tr(styleCA.getName()));
        uid.addComponent(name, styleCA, 0, 6, 2, 1);

        JComboBox<ContainerItem<TextElement.Style>> styleBox = new JComboBox<>();
        for(TextElement.Style style : TextElement.Style.values()) {
            styleBox.addItem(new ContainerItem<>(style, i18n.tr(style.name())));
            if(TextElement.Style.valueOf(styleCA.getSelected()).equals(style)){
                styleBox.setSelectedItem(styleBox.getItemAt(styleBox.getItemCount()-1));
            }
        }

        styleBox.putClientProperty("ca", styleCA);
        styleBox.addActionListener(EventHandler.create(ActionListener.class, this, "onStyleChanged", "source"));
        uid.addComponent(styleBox, styleCA, 2, 6, 1, 1);


        //Find the Font ConfigurationAttribute
        SourceListCA alignmentCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(TextElement.sAlignment))
                alignmentCA = (SourceListCA)ca;
        //Create the list of component composing the ConfigurationAttribute representation.
        name = new JLabel(i18n.tr(alignmentCA.getName()));
        uid.addComponent(name, alignmentCA, 0, 7, 2, 1);

        JComboBox<ContainerItem<TextElement.Alignment>> alignmentBox = new JComboBox<>();
        for(TextElement.Alignment alignment : TextElement.Alignment.values()) {
            alignmentBox.addItem(new ContainerItem<>(alignment, i18n.tr(alignment.name())));
            if(TextElement.Alignment.valueOf(alignmentCA.getSelected()).equals(alignment)){
                alignmentBox.setSelectedItem(alignmentBox.getItemAt(alignmentBox.getItemCount()-1));
            }
        }

        alignmentBox.putClientProperty("ca", alignmentCA);
        alignmentBox.addActionListener(EventHandler.create(ActionListener.class, this, "onAlignmentChanged", "source"));
        uid.addComponent(alignmentBox, alignmentCA, 2, 7, 1, 1);

        //Find the Font ConfigurationAttribute
        ColorCA textColorCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(TextElement.sTextColor))
                textColorCA = (ColorCA)ca;
        //Create the list of component composing the ConfigurationAttribute representation.
        name = new JLabel(i18n.tr(textColorCA.getName()));
        uid.addComponent(name, textColorCA, 0, 8, 3, 1);

        component = uic.getCAManager().getRenderer(textColorCA).createJComponentFromCA(textColorCA);
        uid.addComponent(component, textColorCA, 2, 8, 1, 1);

        //Find the Font ConfigurationAttribute
        ColorCA backgroundColorCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(TextElement.sBackColor))
                backgroundColorCA = (ColorCA)ca;
        //Create the list of component composing the ConfigurationAttribute representation.
        name = new JLabel(i18n.tr(backgroundColorCA.getName()));
        uid.addComponent(name, backgroundColorCA, 0, 9, 3, 1);

        component = uic.getCAManager().getRenderer(backgroundColorCA).createJComponentFromCA(backgroundColorCA);
        uid.addComponent(component, backgroundColorCA, 2, 9, 1, 1);

        //Find the Font ConfigurationAttribute
        IntegerCA alphaCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals(TextElement.sAlpha))
                alphaCA = (IntegerCA)ca;
        //Create the list of component composing the ConfigurationAttribute representation.
        name = new JLabel(i18n.tr(alphaCA.getName()));
        uid.addComponent(name, alphaCA, 0, 10, 1, 1);

        component = uic.getCAManager().getRenderer(alphaCA).createJComponentFromCA(alphaCA);
        uid.addComponent(component, alphaCA, 2, 10, 1, 1);

        return uid;
    }

    public void onStyleChanged(JComboBox comboBox){
        SourceListCA styleCA = (SourceListCA) comboBox.getClientProperty("ca");
        styleCA.select(((ContainerItem<TextElement.Style>)comboBox.getSelectedItem()).getKey().name());
    }

    public void onAlignmentChanged(JComboBox comboBox){
        SourceListCA alignmentCA = (SourceListCA) comboBox.getClientProperty("ca");
        alignmentCA.select(((ContainerItem<TextElement.Alignment>)comboBox.getSelectedItem()).getKey().name());
    }
}
