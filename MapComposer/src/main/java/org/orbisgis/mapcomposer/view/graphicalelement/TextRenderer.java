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

package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.ColorCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.IntegerCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ListCA;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.element.text.TextElement;
import org.orbisgis.mapcomposer.view.utils.UIDialogProperties;
import org.orbisgis.sif.UIPanel;

import javax.swing.*;
import java.awt.*;
import java.awt.font.LineBreakMeasurer;
import java.awt.font.TextAttribute;
import java.awt.font.TextLayout;
import java.awt.image.BufferedImage;
import java.text.AttributedString;
import java.util.ArrayList;
import java.util.List;

/**
 * Renderer associated to the scale GraphicalElement.
 */
public class TextRenderer extends SimpleGERenderer {

    @Override
    public BufferedImage createImageFromGE(GraphicalElement ge){
        TextElement te = ((TextElement)ge);
        //Drawing on a BufferedImage the text.
        BufferedImage bi = new BufferedImage(ge.getWidth(), ge.getHeight(), BufferedImage.TYPE_INT_ARGB);
        Graphics2D graph = bi.createGraphics();
        graph.setColor(new Color(te.getColorBack().getRed(), te.getColorBack().getGreen(), te.getColorBack().getBlue(), te.getAlpha()));
        graph.fillRect(0, 0, te.getWidth(), te.getHeight());
        graph = bi.createGraphics();
        
        //Draw the string and make it fit to the TextElement bounds
        AttributedString attributedString = new AttributedString(te.getText());
        attributedString.addAttribute(TextAttribute.FONT, new Font(te.getFont(), te.getStyle(), te.getFontSize()));
        attributedString.addAttribute(TextAttribute.FOREGROUND, te.getColorText());
        //Cut the text if it's too wide for the BufferedImage width
        int x = 0;
        int y = 0;
        LineBreakMeasurer measurer = new LineBreakMeasurer(attributedString.getIterator(),graph.getFontRenderContext());
        while (measurer.getPosition() < attributedString.getIterator().getEndIndex()) {
            TextLayout textLayout = measurer.nextLayout(te.getWidth());
            y += textLayout.getAscent();
            switch(te.getAlignment()){
                case LEFT:
                    x=0;
                    break;
                case CENTER:
                    x=(int) ((te.getWidth()-textLayout.getBounds().getWidth())/2);
                    break;
                case RIGHT:
                    x=(int) (te.getWidth()-textLayout.getBounds().getWidth());
                    break;
            }
            textLayout.draw(graph, x, y);
            y += textLayout.getDescent() + textLayout.getLeading();
        }

        return applyRotationToBufferedImage(bi, ge);
    }

    @Override
    public UIPanel createConfigurationPanel(List<ConfigurationAttribute> caList, UIController uic, boolean enableLock) {

        //Create the UIDialogProperties that will be returned
        UIDialogProperties uid = new UIDialogProperties(uic);
        
        //Add the text configuration elements
        //Find the Text ConfigurationAttribute
        StringCA textCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals("Text"))
                textCA = (StringCA)ca;
        //Create the list of component composing the ConfigurationAttribute representation.
        List<Component> text = new ArrayList<>();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(new JLabel(textCA.getName()));
        panel.add(uic.getCAManager().getRenderer(textCA).createJComponentFromCA(textCA));
        text.add(panel);
        //Add the ConfigurationAttribute and its representation to the UIDialogProperties
        uid.addComponent(text, textCA, enableLock);

        //Find the Font ConfigurationAttribute
        SourceListCA fontCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals("Font"))
                fontCA = (SourceListCA)ca;
        //Create the list of component composing the ConfigurationAttribute representation.
        List<Component> font = new ArrayList<>();
        font.add(new JLabel(fontCA.getName()));
        font.add(Box.createHorizontalGlue());
        font.add(uic.getCAManager().getRenderer(fontCA).createJComponentFromCA(fontCA));
        //Add the ConfigurationAttribute and its representation to the UIDialogProperties
        uid.addComponent(font, fontCA, enableLock);

        //Find the Font ConfigurationAttribute
        IntegerCA fontSizeCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals("Font size"))
                fontSizeCA = (IntegerCA)ca;
        //Create the list of component composing the ConfigurationAttribute representation.
        List<Component> fontSize = new ArrayList<>();;
        JLabel fontSizeName = new JLabel(fontSizeCA.getName());
        fontSize.add(fontSizeName);
        //Create a kind of tabulation to align elements from different ConfigurationAttribute
        fontSize.add(Box.createHorizontalStrut(100 - fontSizeName.getFontMetrics(fontSizeName.getFont()).stringWidth(fontSizeName.getText())));
        //Limits the size of the component
        JSpinner FontSizeSpinner = (JSpinner)uic.getCAManager().getRenderer(fontSizeCA).createJComponentFromCA(fontSizeCA);
        FontSizeSpinner.setPreferredSize(new Dimension(80, (int) FontSizeSpinner.getPreferredSize().getHeight()));
        fontSize.add(FontSizeSpinner);
        //Add the ConfigurationAttribute and its representation to the UIDialogProperties
        uid.addComponent(fontSize, fontSizeCA, enableLock);

        //Find the Font ConfigurationAttribute
        SourceListCA StyleCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals("Style"))
                StyleCA = (SourceListCA) ca;
        //Create the list of component composing the ConfigurationAttribute representation.
        List<Component> style = new ArrayList<>();
        JLabel styleName = new JLabel(StyleCA.getName());
        style.add(styleName);
        //Create a kind of tabulation to align elements from different ConfigurationAttribute
        style.add(Box.createHorizontalStrut(100 - styleName.getFontMetrics(styleName.getFont()).stringWidth(styleName.getText())));
        JComboBox styleBox = (JComboBox)uic.getCAManager().getRenderer(StyleCA).createJComponentFromCA(StyleCA);
        //Limits the size of the component
        styleBox.setPreferredSize(new Dimension(80, (int) styleBox.getPreferredSize().getHeight()));
        style.add(styleBox);
        //Add the ConfigurationAttribute and its representation to the UIDialogProperties
        uid.addComponent(style, StyleCA, enableLock);


        //Find the Font ConfigurationAttribute
        SourceListCA alignmentCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals("Alignment"))
                alignmentCA = (SourceListCA)ca;
        //Create the list of component composing the ConfigurationAttribute representation.
        List<Component> alignment = new ArrayList<>();
        JLabel alignmentName = new JLabel(alignmentCA.getName());
        alignment.add(alignmentName);
        //Create a kind of tabulation to align elements from different ConfigurationAttribute
        alignment.add(Box.createHorizontalStrut(100 - alignmentName.getFontMetrics(alignmentName.getFont()).stringWidth(alignmentName.getText())));
        JComboBox alignmentBox = (JComboBox)uic.getCAManager().getRenderer(alignmentCA).createJComponentFromCA(alignmentCA);
        //Limits the size of the component
        alignmentBox.setPreferredSize(new Dimension(80, (int) alignmentBox.getPreferredSize().getHeight()));
        alignment.add(alignmentBox);
        //Add the ConfigurationAttribute and its representation to the UIDialogProperties
        uid.addComponent(alignment, alignmentCA, enableLock);

        //Find the Font ConfigurationAttribute
        ColorCA textColorCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals("Text color"))
                textColorCA = (ColorCA)ca;
        //Create the list of component composing the ConfigurationAttribute representation.
        List<Component> textColor = new ArrayList<>();
        JLabel textColorName = new JLabel(textColorCA.getName());
        textColor.add(textColorName);
        //Create a kind of tabulation to align elements from different ConfigurationAttribute
        textColor.add(Box.createHorizontalStrut(140 - textColorName.getFontMetrics(textColorName.getFont()).stringWidth(textColorName.getText())));
        textColor.add(uic.getCAManager().getRenderer(textColorCA).createJComponentFromCA(textColorCA));
        //Add the ConfigurationAttribute and its representation to the UIDialogProperties
        uid.addComponent(textColor, textColorCA, enableLock);

        //Find the Font ConfigurationAttribute
        ColorCA backgroundColorCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals("Background color"))
                backgroundColorCA = (ColorCA)ca;
        //Create the list of component composing the ConfigurationAttribute representation.
        List<Component> backgroundColor = new ArrayList<>();
        JLabel backgroundName = new JLabel(backgroundColorCA.getName());
        backgroundColor.add(backgroundName);
        //Create a kind of tabulation to align elements from different ConfigurationAttribute
        backgroundColor.add(Box.createHorizontalStrut(140 - backgroundName.getFontMetrics(backgroundName.getFont()).stringWidth(backgroundName.getText())));
        backgroundColor.add(uic.getCAManager().getRenderer(backgroundColorCA).createJComponentFromCA(backgroundColorCA));
        //Add the ConfigurationAttribute and its representation to the UIDialogProperties
        uid.addComponent(backgroundColor, backgroundColorCA, enableLock);

        //Find the Font ConfigurationAttribute
        IntegerCA alphaCA = null;
        for(ConfigurationAttribute ca : caList)
            if(ca.getName().equals("Alpha"))
                alphaCA = (IntegerCA)ca;
        //Create the list of component composing the ConfigurationAttribute representation.
        List<Component> alpha = new ArrayList<>();
        alpha.add(new JLabel(alphaCA.getName()));
        JSpinner alphaSpinner = (JSpinner)uic.getCAManager().getRenderer(alphaCA).createJComponentFromCA(alphaCA);
        //Limits the size of the component
        alphaSpinner.setPreferredSize(new Dimension(80, (int) alphaSpinner.getPreferredSize().getHeight()));
        alpha.add(alphaSpinner);
        //Add the ConfigurationAttribute and its representation to the UIDialogProperties
        uid.addComponent(alpha, alphaCA, enableLock);

        return uid;
    }
}
