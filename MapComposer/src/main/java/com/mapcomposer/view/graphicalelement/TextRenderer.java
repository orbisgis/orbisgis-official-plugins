package com.mapcomposer.view.graphicalelement;

import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.text.TextElement;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;

/**
 * Renderer associated to the scale GraphicalElement.
 */
public class TextRenderer extends GERenderer{

    @Override
    public BufferedImage getcontentImage(GraphicalElement ge){
        TextElement te = ((TextElement)ge);
        //Drawing on a BufferedImage the text.
        BufferedImage bi = new BufferedImage(ge.getWidth(), ge.getHeight(), BufferedImage.TYPE_INT_RGB);
        System.out.println(ge.getWidth()+";"+ge.getHeight());
        Graphics2D graph = bi.createGraphics();
        graph.setFont(new Font(te.getFont(), Font.PLAIN, te.getFontSize()));
        //graph.setColor(Color.red);
        graph.drawString(te.getText(), 0, ge.getHeight()/2);
        return bi;
    }
}
