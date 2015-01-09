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
    public UIPanel createConfigurationPanel(GraphicalElement ge, UIController uic) {
        //Create the UIDialogProperties
        UIDialogProperties uid = new UIDialogProperties(uic);
        //Get the GraphicalELement
        TextElement te = (TextElement)ge;

        //Find the Text ConfigurationAttribute
        StringCA textCA = null;
        for(ConfigurationAttribute ca : te.getAllAttributes())
            if(ca.getValue().equals(te.getText()))
                textCA = (StringCA)ca;
        //Set the JPanel of the ca
        JComponent text = new JPanel();
        text.setLayout(new BoxLayout(text, BoxLayout.Y_AXIS));
        JLabel textName = new JLabel(textCA.getName());
        textName.setAlignmentX(Component.LEFT_ALIGNMENT);
        text.add(textName);
        text.add(uic.getCAManager().getRenderer(textCA).createJComponentFromCA(textCA));
        uid.addJComponent(text, textCA, true);

        //Find the Font ConfigurationAttribute
        SourceListCA fontCA = null;
        for(ConfigurationAttribute ca : te.getAllAttributes())
            if(ca instanceof ListCA)
                if(((ListCA)ca).getSelected().equals(te.getFont()))
                    fontCA = (SourceListCA)ca;
        //Set the JPanel of the ca
        JComponent font = new JPanel();
        font.setLayout(new FlowLayout());
        JLabel fontName = new JLabel(fontCA.getName());
        fontName.setAlignmentX(Component.LEFT_ALIGNMENT);
        font.add(fontName);
        font.add(Box.createHorizontalGlue());
        font.add(uic.getCAManager().getRenderer(fontCA).createJComponentFromCA(fontCA));
        uid.addJComponent(font, fontCA, true);

        //Find the Font ConfigurationAttribute
        IntegerCA fontSizeCA = null;
        for(ConfigurationAttribute ca : te.getAllAttributes())
            if(ca.getValue().equals(te.getFontSize()))
                fontSizeCA = (IntegerCA)ca;
        //Set the JPanel of the ca
        JComponent fontSize = new JPanel();
        fontSize.setLayout(new FlowLayout());
        JLabel fontSizeName = new JLabel(fontSizeCA.getName());
        fontSizeName.setAlignmentX(Component.LEFT_ALIGNMENT);
        fontSize.add(fontSizeName);
        font.add(Box.createHorizontalGlue());
        fontSize.add(uic.getCAManager().getRenderer(fontSizeCA).createJComponentFromCA(fontSizeCA));
        uid.addJComponent(fontSize, fontSizeCA, true);

        //Find the Font ConfigurationAttribute
        SourceListCA StyleCA = null;
        for(ConfigurationAttribute ca : te.getAllAttributes())
            if(ca instanceof ListCA)
                if (TextElement.Style.BOLD.name() == ((ListCA) ca).getSelected() ||
                        TextElement.Style.ITALIC.name() == ((ListCA) ca).getSelected() ||
                        TextElement.Style.PLAIN.name() == ((ListCA) ca).getSelected())
                    StyleCA = (SourceListCA) ca;
        //Set the JPanel of the ca
        JComponent style = new JPanel();
        style.setLayout(new FlowLayout());
        JLabel styleName = new JLabel(StyleCA.getName());
        styleName.setAlignmentX(Component.LEFT_ALIGNMENT);
        style.add(styleName);
        style.add(Box.createHorizontalGlue());
        style.add(uic.getCAManager().getRenderer(StyleCA).createJComponentFromCA(StyleCA));
        uid.addJComponent(style, StyleCA, true);


        //Find the Font ConfigurationAttribute
        SourceListCA alignmentCA = null;
        for(ConfigurationAttribute ca : te.getAllAttributes())
            if(ca instanceof ListCA)
                if(TextElement.Alignment.CENTER.name() == ((ListCA)ca).getSelected() ||
                        TextElement.Alignment.LEFT.name() == ((ListCA)ca).getSelected() ||
                        TextElement.Alignment.RIGHT.name() == ((ListCA)ca).getSelected())
                        alignmentCA = (SourceListCA)ca;
        //Set the JPanel of the ca
        JComponent alignment = new JPanel();
        alignment.setLayout(new FlowLayout());
        alignment.setBackground(Color.black);
        JLabel alignmentName = new JLabel(alignmentCA.getName());
        alignmentName.setAlignmentX(Component.LEFT_ALIGNMENT);
        alignment.add(alignmentName);
        alignment.add(Box.createHorizontalGlue());
        alignment.add(uic.getCAManager().getRenderer(alignmentCA).createJComponentFromCA(alignmentCA));
        uid.addJComponent(alignment, alignmentCA, true);

        return uid;
    }
}
