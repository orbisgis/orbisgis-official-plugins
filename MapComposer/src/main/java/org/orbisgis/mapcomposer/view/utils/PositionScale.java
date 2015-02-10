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

package org.orbisgis.mapcomposer.view.utils;

import javax.swing.*;
import java.awt.*;

/**
 * @author Sylvain PALOMINOS
 */

public class PositionScale extends JComponent {
    public static final int DPI = Toolkit.getDefaultToolkit().
            getScreenResolution();
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int SIZE = 35;

    public int orientation;
    private int increment;
    private int units;

    private Point mousePosition;
    private int documentOriginPosition;

    public PositionScale(int o, boolean m) {
        orientation = o;
        setIncrementAndUnits();
        mousePosition = new Point(0, 0);
        documentOriginPosition = 0;
    }

    public void setMousePosition(Point p){
        if (orientation == HORIZONTAL)
            mousePosition = new Point(p.x, p.y);
        else
            mousePosition = new Point(p.x, p.y);

        this.revalidate();
        this.repaint();
    }

    private void setIncrementAndUnits() {
        units = (int)((double) DPI / (double)2.54); // dots per centimeter
        increment = units;
    }

    public int getIncrement() {
        return increment;
    }

    public void setPreferredHeight(int ph) {
        setPreferredSize(new Dimension(SIZE, ph));
    }

    public void setPreferredWidth(int pw) {
        setPreferredSize(new Dimension(pw, SIZE));
    }

    protected void paintComponent(Graphics g) {

        Rectangle drawHere = g.getClipBounds();
        // Fill clipping area with dirty brown/orange.
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);
        g.setColor(Color.BLACK);
        g.drawRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);

        // Do the ruler labels in a small font that's black.
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g.setColor(Color.black);


        int size;

        if (orientation == HORIZONTAL)
            size = this.getWidth();
        else
            size = this.getHeight();

        int startPos = documentOriginPosition%units;
        int scale = documentOriginPosition/units;

        for(int i=startPos; i<size; i+=units){

            float deciUnits = (float)units/10;
            for(float j=i; j<i+units-deciUnits+1; j+=deciUnits){
                if (orientation == HORIZONTAL)
                    g.drawLine((int)j, SIZE - 1, (int)j, SIZE - 10 - 1);
                else
                    g.drawLine(SIZE - 1, (int)j, SIZE - 10 - 1, (int)j);
            }

            if (orientation == HORIZONTAL) {
                g.drawLine(i, SIZE - 1, i, SIZE - 20 - 1);
                g.drawString(" "+(i/units - scale), i, 21);
            } else {
                g.drawLine(SIZE - 1, i, SIZE - 20 - 1, i);
                g.drawString(" "+(i/units - scale), 9, i);
            }
        }

        Graphics2D graph = (Graphics2D)g;
        graph.setStroke(new BasicStroke(5));
        if (orientation == HORIZONTAL)
            graph.drawLine(mousePosition.x, 10, mousePosition.x, SIZE-10);
        else
            graph.drawLine(10, mousePosition.y, SIZE-10, mousePosition.y);
    }

    /**
     * Sets the position of the 0 of the scale according to the document position.
     * @param documentOriginPosition
     */
    public void setDocumentOriginPosition(int documentOriginPosition) {
        this.documentOriginPosition = documentOriginPosition;
        this.revalidate();
        this.repaint();
    }
}