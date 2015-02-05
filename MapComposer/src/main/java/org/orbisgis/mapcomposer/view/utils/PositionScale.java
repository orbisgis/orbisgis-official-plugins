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
    public static final int INCH = Toolkit.getDefaultToolkit().
            getScreenResolution();
    public static final int HORIZONTAL = 0;
    public static final int VERTICAL = 1;
    public static final int SIZE = 35;

    public int orientation;
    public boolean isMetric;
    private int increment;
    private int units;

    private Point mousePosition;
    private boolean justMouse;

    public PositionScale(int o, boolean m) {
        orientation = o;
        isMetric = m;
        setIncrementAndUnits();
        mousePosition = new Point(0, 0);
        justMouse = false;
    }

    public void setMousePosition(Point p){
        if (orientation == HORIZONTAL)
            mousePosition = new Point(p.x, p.y);
        else
            mousePosition = new Point(p.x, p.y);

        justMouse = true;
        this.revalidate();
        this.repaint();
    }

    public void setIsMetric(boolean isMetric) {
        this.isMetric = isMetric;
        setIncrementAndUnits();
        repaint();
    }

    private void setIncrementAndUnits() {
        if (isMetric) {
            units = (int)((double)INCH / (double)2.54); // dots per centimeter
            increment = units;
        } else {
            units = INCH;
            increment = units / 2;
        }
    }

    public boolean isMetric() {
        return this.isMetric;
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
        g.setColor(new Color(230, 163, 4));
        g.fillRect(drawHere.x, drawHere.y, drawHere.width, drawHere.height);

        // Do the ruler labels in a small font that's black.
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g.setColor(Color.black);

        // Some vars we need.
        int end = 0;
        int start = 0;
        int tickLength = 0;
        String text = null;

        // Use clipping bounds to calculate first and last tick locations.
        if (orientation == HORIZONTAL) {
            start = (drawHere.x / increment) * increment;
            end = (((drawHere.x + drawHere.width) / increment) + 1)
                    * increment;
        } else {
            start = (drawHere.y / increment) * increment;
            end = (((drawHere.y + drawHere.height) / increment) + 1)
                    * increment;
        }

        // Make a special case of 0 to display the number
        // within the rule and draw a units label.
        if (start == 0) {
            text = Integer.toString(0) + (isMetric ? " cm" : " in");
            tickLength = 10;
            if (orientation == HORIZONTAL) {
                g.drawLine(0, SIZE - 1, 0, SIZE - tickLength - 1);
                g.drawString(text, 2, 21);
            } else {
                g.drawLine(SIZE - 1, 0, SIZE - tickLength - 1, 0);
                g.drawString(text, 9, 10);
            }
            text = null;
            start = increment;
        }

        // ticks and labels
        for (int i = start; i < end; i += increment) {
            if (i % units == 0) {
                tickLength = 10;
                text = Integer.toString(i / units);
            } else {
                tickLength = 7;
                text = null;
            }

            if (tickLength != 0) {
                if (orientation == HORIZONTAL) {
                    g.drawLine(i, SIZE - 1, i, SIZE - tickLength - 1);
                    if (text != null)
                        g.drawString(text, i - 3, 21);
                } else {
                    g.drawLine(SIZE - 1, i, SIZE - tickLength - 1, i);
                    if (text != null)
                        g.drawString(text, 9, i + 3);
                }
            }
        }
        Graphics2D graph = (Graphics2D)g;
        graph.setStroke(new BasicStroke(5));
        if (orientation == HORIZONTAL)
            graph.drawLine(mousePosition.x, 10, mousePosition.x, 40);
        else
            graph.drawLine(10, mousePosition.y, 40, mousePosition.y);
        justMouse = false;
    }
}
/*
public class PositionScale extends JComponent{

    public enum Orientation{HORIZONTAL, VERTICAL};

    private Orientation orientation;

    private Dimension dimension;

    private int factor;
    private int realValue;

    public void setOrientation(Orientation orientation){
        this.orientation = orientation;
        this.revalidate();
    }

    public void setDimension(Dimension dimension){
        this.setPreferredSize(dimension);
        this.setMaximumSize(dimension);
        this.setMinimumSize(dimension);
        this.dimension = dimension;
        this.revalidate();
    }

    public void setScale(int factor, int realValue){
        this.factor = factor;
        this.realValue = realValue;
        this.revalidate();
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D graph = (Graphics2D)g;
        graph.setStroke(new BasicStroke(2));
        graph.drawLine(0, 0, (int)(dimension.width*0.65), 0);
        for(int i=0; i<dimension.height; i+=factor){
            graph.drawLine(0, i, (int)(dimension.width*0.65), i);
            graph.drawString(""+realValue*(i/factor), (int)(dimension.width*0.70), i);
        }
        graph.setStroke(new BasicStroke(1));
        graph.drawLine(0, 0, (int) (dimension.width * 0.35), 0);
        for(int i=0; i<dimension.height; i+=factor/2){
            graph.drawLine(0, i, (int) (dimension.width * 0.35), i);
        }
        graph.drawLine(0, 0, (int) (dimension.width * 0.15), 0);
        for(int i=0; i<dimension.height; i+=factor/10){
            graph.drawLine(0, i, (int) (dimension.width * 0.15), i);
        }
    }
}
*/