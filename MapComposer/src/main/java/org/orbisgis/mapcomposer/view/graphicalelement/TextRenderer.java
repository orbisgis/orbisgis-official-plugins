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

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.element.text.TextElement;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
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
}
