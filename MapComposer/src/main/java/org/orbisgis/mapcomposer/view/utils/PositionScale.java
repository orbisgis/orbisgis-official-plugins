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
    private int documentSize;
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
    protected void paintComponent(Graphics graphics) {
        Graphics2D g = (Graphics2D)graphics;
        //Gets the position od the document start and end on the scale
        int startDocumentPos = (documentOriginPosition-scrollValue);
        int endDocumentPos = (documentOriginPosition+documentSize-scrollValue);

        int unitPosition = startDocumentPos%units;

        //Gets the scale start position, length and width
        int xRect = this.getX();
        int yRect = this.getY();
        int wRect = this.getWidth();
        int hRect = this.getHeight();

        //Then draw the background of the scale : gray until the document start point, white until the document end
        // point and gray until the end of the scale.
        if (orientation == HORIZONTAL) {
            yRect += 5;
            hRect -= 10;

            g.setColor(Color.GRAY);
            g.fillRect(xRect, yRect-1, startDocumentPos, hRect+2);
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(xRect, yRect, startDocumentPos-1, hRect);

            g.setColor(Color.WHITE);
            g.fillRect(startDocumentPos, yRect-1, endDocumentPos, hRect+2);

            g.setColor(Color.GRAY);
            g.fillRect(endDocumentPos, yRect-1, wRect, hRect+2);
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(endDocumentPos+1, yRect, wRect, hRect);
        }
        else{
            xRect += 5;
            wRect -= 10;

            g.setColor(Color.GRAY);
            g.fillRect(xRect-1, yRect, wRect+2, startDocumentPos);
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(xRect, yRect, wRect, startDocumentPos-1);

            g.setColor(Color.WHITE);
            g.fillRect(xRect-1, startDocumentPos, wRect+2, endDocumentPos);

            g.setColor(Color.GRAY);
            g.fillRect(xRect-1, endDocumentPos, wRect+2, hRect);
            g.setColor(Color.LIGHT_GRAY);
            g.fillRect(xRect, endDocumentPos+1, wRect, hRect);
        }

        //Sets the font and text color
        g.setFont(new Font("SansSerif", Font.PLAIN, 10));
        g.setColor(Color.black);

        //Get the length of the Scale
        int length;
        if (orientation == HORIZONTAL)
            length = wRect;
        else
            length = hRect;

        //Get the half and deci unit
        float halfUnit = (float)units/2;
        float deciUnit = (float)units/10;

        //Draw the graduations for one unit from the start of the scale to the end
        for(;unitPosition<length; unitPosition+=units) {

            //Draw the half graduation before the unit graduation
            if (orientation == HORIZONTAL) {
                g.drawLine((int) (unitPosition - halfUnit), yRect + hRect / 2 - 3, (int) (unitPosition - halfUnit), yRect

                        + hRect / 2 + 3);
            } else {
                g.drawLine(xRect + wRect / 2 - 3, (int) (unitPosition - halfUnit), xRect + wRect / 2 + 3, (int)
                        (unitPosition - halfUnit));
            }

            //Draw the deci graduation before and after the unit graduation
            String unitStr = Integer.toString((unitPosition-startDocumentPos)/units);

            int unitStrWidth = (int) g.getFont().getStringBounds(unitStr, g.getFontRenderContext()).getWidth();
            int unitStrHeight = (int) g.getFont().getStringBounds(unitStr, g.getFontRenderContext()).getHeight();

            int nbrDeciGrad = (int) ((halfUnit - unitStrWidth) / deciUnit);

            if (orientation == HORIZONTAL) {
                //Draw the little graduation before the first big graduation of necessary
                for (float i = unitPosition - halfUnit; i < unitPosition - unitStrWidth / 2 - 1; i += deciUnit) {
                    g.drawLine((int) i, yRect + hRect / 2 - 1, (int) i, yRect + hRect / 2 + 1);
                }
                //Draw the little graduation before the first big graduation of necessary
                for (float i = unitPosition + halfUnit; i > unitPosition + unitStrWidth / 2 + 1; i -= deciUnit) {
                    g.drawLine((int) i, yRect + hRect / 2 - 1, (int) i, yRect + hRect / 2 + 1);
                }
            }
            else {
                //Draw the little graduation before the first big graduation of necessary
                for (float i = unitPosition - halfUnit; i < unitPosition - unitStrWidth / 2 - 1; i += deciUnit) {
                    g.drawLine(xRect + wRect / 2 - 1, (int) i, xRect + wRect / 2 + 1, (int) i);
                }
                //Draw the little graduation before the first big graduation of necessary
                for (float i = unitPosition + halfUnit; i > unitPosition + unitStrWidth / 2 + 1; i -= deciUnit) {
                    g.drawLine(xRect + wRect / 2 - 1, (int) i, xRect + wRect / 2 + 1, (int) i);
                }
            }

            //Draw the unit graduation
            if (orientation == HORIZONTAL) {
                g.drawLine(unitPosition, yRect, unitPosition, yRect + 2);
                g.drawLine(unitPosition, yRect + hRect, unitPosition, yRect + hRect - 2);
                if (unitPosition > 0)
                    g.drawString(unitStr, unitPosition - unitStrWidth / 2, yRect + hRect / 2 + unitStrHeight / 2 - 1);
            } else {
                g.drawLine(xRect, unitPosition, xRect + 2, unitPosition);
                g.drawLine(xRect + wRect, unitPosition, xRect + wRect - 2, unitPosition);
                if (unitPosition > 0)
                    g.drawString(unitStr, xRect + wRect / 2 - unitStrWidth / 2, unitPosition + unitStrHeight / 2 - 1);
            }
        }
        
        //Draw the mouse position cursor
        g.setStroke(new BasicStroke(3));
        if (orientation == HORIZONTAL)
            g.drawLine(mousePosition.x-1, 10, mousePosition.x-1, SIZE-10);
        else
            g.drawLine(10, mousePosition.y-1, SIZE-10, mousePosition.y-1);
    }

    /**
     * Sets the position of the 0 of the scale according to the document position.
     * @param documentOriginPosition Position of the origin of the document.
     */
    public void setDocumentOriginPosition(int documentOriginPosition, int documentSize) {
        this.documentOriginPosition = documentOriginPosition;
        this.documentSize = documentSize;
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

    /**
     * Sets the new Size of the scales.
     * @param size New size of the scales.
     */
    public void setSize(int size){
        SIZE = size;
        this.setPreferredSize(new Dimension(size, size));
        this.repaint();
    }
}