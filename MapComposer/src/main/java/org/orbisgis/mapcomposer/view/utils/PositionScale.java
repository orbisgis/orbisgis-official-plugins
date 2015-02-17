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

import org.orbisgis.mapcomposer.view.ui.CompositionArea;

import javax.swing.JComponent;
import java.awt.*;
import java.awt.event.AdjustmentEvent;

/**
 * This class is used as a header view of a JScrollPane
 * It draw as scale in centimeters which 0 point is on the document origin and with a cursor indicating the mouse position.
 *
 * @author Sylvain PALOMINOS
 */

public class PositionScale extends JComponent {
    /** Dot Per Inch value */
    public static final int DPI = Toolkit.getDefaultToolkit().getScreenResolution();
    /** Indicate if the PositionScale is horizontal.*/
    public static final int HORIZONTAL = 0;
    /** Indicate if the PositionScale is vertical.*/
    public static final int VERTICAL = 1;
    /** Size of the PositionScale (width if it is vertical, height if it is horizontal).*/
    public static int SIZE;

    /** Orientation of the PositionScale.*/
    public int orientation;
    /** Size in pixels of a unit (1 cm or 1 inch) */
    private int units;

    /** Mouse position in the CompositionArea */
    private Point mousePosition;
    /** Position of the origin point of the document. Used to know where the 0 should be placed.*/
    private int documentOriginPosition;
    /** Among of pixel scrolled by the user */
    private int scrollValue;

    /**
     * Main constructor.
     * @param orientation Orientation of the PositionScale
     */
    public PositionScale(int orientation, int dimension) {
        this.orientation = orientation;
        mousePosition = new Point(0, 0);
        documentOriginPosition = 0;
        units = DPI;
        SIZE = dimension;
        this.setPreferredSize(new Dimension(SIZE, SIZE));
    }

    /**
     * Set the new position of the mouse.
     * @param p Position of the mouse in the CompositionJPanel.
     */
    public void setMousePosition(Point p){
        mousePosition = new Point(p.x - SIZE, p.y - SIZE);
        this.revalidate();
        this.repaint();
    }

    @Override
    protected void paintComponent(Graphics g) {
        //Fill the scale area with grey color
        g.setColor(Color.LIGHT_GRAY);
        g.fillRect(this.getX(), this.getY(), this.getWidth(), this.getHeight());

        //Sets the font and text color
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g.setColor(Color.black);

        //Get the length of the Scale
        int length;
        if (orientation == HORIZONTAL)
            length = this.getWidth();
        else
            length = this.getHeight();

        //Get the start position of the document (the 0 inch/cm)
        int startPos = (documentOriginPosition-scrollValue)%units;
        int endPos = length;
        //Get the 1/10 graduation
        float deciUnits = (float)units/10;

        //Draw the little graduation from the scale start to the first big graduation
        for(float j=startPos; j>0; j-=deciUnits){
            if (orientation == HORIZONTAL)
                g.drawLine((int)j, SIZE - 1, (int)j, SIZE - 10 - 1);
            else
                g.drawLine(SIZE - 1, (int)j, SIZE - 10 - 1, (int)j);
        }

        //For each big graduation
        for(int i=startPos; i<endPos; i+=units){
            //Between each big graduations draw the little one
            for(float j=i; j<i+units-deciUnits+1; j+=deciUnits){
                if (orientation == HORIZONTAL)
                    g.drawLine((int)j, SIZE - 1, (int)j, SIZE - 10 - 1);
                else
                    g.drawLine(SIZE - 1, (int)j, SIZE - 10 - 1, (int)j);
            }
            //Get the value of the big graduation
            int positionValue = (i+scrollValue+units-1-documentOriginPosition)/units;
            if(i<documentOriginPosition)
                positionValue--;
            //Draw the big graduation and its value
            if (orientation == HORIZONTAL) {
                g.drawLine(i, SIZE - 1, i, SIZE - 20 - 1);
                if(i>0)
                    g.drawString(Integer.toString(positionValue), i, 21);
            } else {
                g.drawLine(SIZE - 1, i, SIZE - 20 - 1, i);
                if(i>0)
                    g.drawString(Integer.toString(positionValue), 9, i);
            }
        }

        //Draw the mouse position cursor
        Graphics2D graph = (Graphics2D)g;
        graph.setStroke(new BasicStroke(5));
        if (orientation == HORIZONTAL)
            graph.drawLine(mousePosition.x, 10, mousePosition.x, SIZE-10);
        else
            graph.drawLine(10, mousePosition.y, SIZE-10, mousePosition.y);
    }

    /**
     * Sets the position of the 0 of the scale according to the document position.
     * @param documentOriginPosition Position of the origin of the document.
     */
    public void setDocumentOriginPosition(int documentOriginPosition) {
        this.documentOriginPosition = documentOriginPosition;
        this.repaint();
    }

    /**
     * Sets the value of the JScrollBar to take it into account on drawing th PositionScale
     * @param ae AdjustmentEvent happening in the ScrollBar.
     */
    public void setScrollValue(AdjustmentEvent ae){
        this.scrollValue = ae.getValue();
        this.repaint();
    }

    /**
     * Sets the dimension unit (inch or mm)
     *  1 for inch
     *  2 for mm
     * @param unit Unit to use for the scale.
     */
    public void setUnit(int unit) {
        if(unit == CompositionArea.UNIT_INCH){
            units = (int)((double) DPI);
        }
        else if(unit == CompositionArea.UNIT_MM){
            units = (int)((double) DPI / 2.54);
        }
        this.repaint();
    }
}