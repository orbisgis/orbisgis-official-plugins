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

package org.orbisgis.mapcomposer.view.ui;

import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GEProperties;
import org.orbisgis.mapcomposer.view.utils.CompositionAreaOverlay;
import org.orbisgis.mapcomposer.view.utils.CompositionJPanel;
import org.orbisgis.mapcomposer.view.utils.PositionScale;

import java.awt.*;
import java.awt.event.*;
import java.beans.EventHandler;
import java.text.DecimalFormat;
import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.plaf.LayerUI;

/**
 * Area for the map document composition. It's composed of two parts;
 * - The area for drawing the GraphicalElements
 * - A bottom tool bar.
 *
 * The area where are drawn the GraphicalElements is composed of (JFrame <- JPanel means a JPanel inside a JFrame):
 * Layer <- JScrollPanel <- JComponent <- Layer <- JLayeredPane
 *   |                                       |
 *   |_ CompositionAreaOverlay               |_ LayeredPaneOverlay
 *
 * @author Sylvain PALOMINOS
 */
public class CompositionArea extends JPanel {

    /** LayerUI used (in this case it's a CompositionAreaOverlay) to display information in the CompositionArea. */
    private LayerUI<JComponent> layerUI;

    /**JScrollPane of the CompositionArea. */
    private JScrollPane scrollPane;

    /** JLayeredPane where all the CompositionJPanel will be added */
    private JLayeredPane layeredPane;
    
    /**JPanel corresponding to the Document GE. */
    private JPanel document;

    /**Dimension of the document into the CompositionArea. */
    private Dimension dimension;

    /** MainController */
    private MainController mainController;

    /** JLabel displaying the mouse position */
    private JLabel positionJLabel;

    /** Fixed size of the PositionScale used as header view in the JScrollPane */
    private final static int POSITIONSCALE_DIMENSION = 30;
    /** The vertical PositionScale */
    private PositionScale verticalPositionScale;

    /** The horizontal PositionScale */
    private PositionScale horizontalPositionScale;

    /** ProgressBar to indicate the progression on exporting the document */
    private JProgressBar progressBar;

    public static final int UNIT_INCH = 1;
    public static final int UNIT_MM = 2;

    /** Dimension unit used */
    private int unit;

    /** Corner button of the ScrollPane to change the dimension unit */
    private JButton inchOrCm;

    /**
     * Main constructor.
     */
    public CompositionArea(MainController mainController){
        super(new BorderLayout());
        this.mainController = mainController;
        this.document = null;
        this.unit = UNIT_INCH;

        //Creates the layer for the layeredPane
        this.layeredPane = new JLayeredPane();
        LayerUI<JComponent> LayeredPaneLayerUI = new LayeredPaneOverlay(this);
        JLayer LayeredPaneJLayer = new JLayer<>(layeredPane, LayeredPaneLayerUI);

        //Adds the layer to a JPanel
        JComponent body = new JPanel(new BorderLayout());
        body.add(LayeredPaneJLayer, BorderLayout.CENTER);

        //Sets the ScrollPane that will contain the layeredPane and sets its header view.
        scrollPane = new JScrollPane(body, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        verticalPositionScale = new PositionScale(PositionScale.VERTICAL, POSITIONSCALE_DIMENSION);
        horizontalPositionScale = new PositionScale(PositionScale.HORIZONTAL, POSITIONSCALE_DIMENSION);
        inchOrCm = new JButton("in");
        inchOrCm.setMargin(new Insets(0, 0, 0, 0));
        inchOrCm.setFont(new Font("Arial", Font.PLAIN, 8));
        inchOrCm.addActionListener(EventHandler.create(ActionListener.class, this, "toggleInchOrCm"));
        scrollPane.setRowHeaderView(verticalPositionScale);
        scrollPane.setColumnHeaderView(horizontalPositionScale);
        scrollPane.getHorizontalScrollBar().addAdjustmentListener(EventHandler.create(AdjustmentListener.class, horizontalPositionScale, "setScrollValue", ""));
        scrollPane.getVerticalScrollBar().addAdjustmentListener(EventHandler.create(AdjustmentListener.class, verticalPositionScale, "setScrollValue", ""));
        scrollPane.setCorner(JScrollPane.UPPER_LEFT_CORNER, inchOrCm);

        //Creates the layer for the whole compositionArea
        this.layerUI = new CompositionAreaOverlay(mainController);
        JLayer compositionAreaJLayer = new JLayer<>(scrollPane, this.layerUI);

        //Adds the scrollPane and its layer to the CompositionArea
        this.add(compositionAreaJLayer, BorderLayout.CENTER);

        //Sets the tool bar at the bottom of the CompositionArea
        JToolBar bottomJToolBar = new JToolBar();
        bottomJToolBar.setLayout(new BorderLayout());

        //Sets the label containing the mouse position
        positionJLabel = new JLabel();
        //Puts it into a JComponent
        JComponent componentPosition = new JPanel(new FlowLayout());
        componentPosition.add(positionJLabel);
        componentPosition.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        /*
        //Sets the zoom spinner
        JSpinner spinnerZoom = new JSpinner(new SpinnerNumberModel(100, 10, 1000, 1));
        spinnerZoom.setPreferredSize(new Dimension(80, (int) spinnerZoom.getPreferredSize().getHeight()));
        //Puts it into a JComponent
        JComponent component = new JPanel(new FlowLayout());
        component.add(new JLabel("Zoom : "));
        component.add(spinnerZoom);
        component.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));
        */

        //Sets the progress bar
        progressBar = new JProgressBar();
        //progressBar.setVisible(false);

        //Adds the two components
        bottomJToolBar.add(componentPosition, BorderLayout.LINE_START);
        bottomJToolBar.add(progressBar, BorderLayout.CENTER);
        //bottomJToolBar.add(component, BorderLayout.LINE_END);
        bottomJToolBar.setFloatable(false);

        //Adds the JToolBar
        this.add(bottomJToolBar, BorderLayout.PAGE_END);

        layeredPane.addMouseListener(EventHandler.create(MouseListener.class, mainController, "unselectAllGE", null, "mouseClicked"));
        layeredPane.addComponentListener(EventHandler.create(ComponentListener.class, this, "actuDocumentPosition", null, "componentResized"));
    }

    /**
     * Sets the value of the JProgressBar.
     * @param value New value of the JProgressBar.
     */
    public void setProgressBarValue(int value){
        progressBar.setValue(value);
    }

    /**
     * Returns the JProgressBar of the CompositionArea.
     * @return The JProgressBar of the CompositionArea.
     */
    public JProgressBar getProgressionBar(){
        return progressBar;
    }

    /**
     * Changes the unit used for the dimensions.
     */
    public void toggleInchOrCm(){
        if(unit == UNIT_INCH){
            unit = UNIT_MM;
            inchOrCm.setText("mm");
            verticalPositionScale.setUnit(UNIT_MM);
            horizontalPositionScale.setUnit(UNIT_MM);
        }
        else if(unit == UNIT_MM){
            unit = UNIT_INCH;
            inchOrCm.setText("in");
            verticalPositionScale.setUnit(UNIT_INCH);
            horizontalPositionScale.setUnit(UNIT_INCH);
        }
        positionJLabel.revalidate();
    }
    
    /**
     * Adds a CompositionPanel. Should be call only once for each GraphicalElement.
     * @param panel CompositionPanel to add.
     */
    public void addCompositionJPanel(CompositionJPanel panel){
        //verify if the CompositionJPanel has not already been added
        if(!this.layeredPane.isAncestorOf(panel)) {
            //First test if the GE represented by the CompositionJPanel should be always centered
            if (panel.getGE() instanceof GEProperties && ((GEProperties) panel.getGE()).isAlwaysCentered()) {
                int width = panel.getGE().getWidth();
                int height = panel.getGE().getHeight();
                panel.setBounds((layeredPane.getWidth() - width) / 2, (layeredPane.getHeight() - height) / 2, width, height);
                //If the GE is an instance of Document, register the CompositionJPanel as this.document and adds the border to it
                if (panel.getGE() instanceof Document) {
                    document = panel;
                    Border border = BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY);
                    border = BorderFactory.createCompoundBorder(border, BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY));
                    border = BorderFactory.createCompoundBorder(border, BorderFactory.createMatteBorder(0, 0, 1, 1, Color.DARK_GRAY));
                    document.setBorder(border);
                }
            }
            this.layeredPane.add(panel);
        }
    }
    
    /**
     * Removes the given panel representing a GE.
     * @param panel Panel to remove.
     */
    public void removeGE(CompositionJPanel panel){
        if(this.layeredPane.isAncestorOf(panel))
            this.layeredPane.remove(panel);
    }
    
    /**
     * Sets the dimension of the document in the compositionArea.
     * This method should be called on the document properties definition.
     * @param dimension Dimension of the document.
     */
    public void setDocumentDimension(Dimension dimension){
        this.dimension =dimension;
        this.layeredPane.setPreferredSize(this.dimension);
        document.setBounds((layeredPane.getWidth() - dimension.width) / 2, (layeredPane.getHeight() - dimension.height) / 2, dimension.width, dimension.height);
    }

    /**
     * Sets all the GE bounds according to the new window dimensions.
     */
    public void actuDocumentPosition(){
        if(document != null) {
            document.setBounds((layeredPane.getWidth() - dimension.width) / 2, (layeredPane.getHeight() - dimension.height) / 2, dimension.width, dimension.height);
            mainController.getCompositionAreaController().actuAllGE();
            horizontalPositionScale.setDocumentOriginPosition((layeredPane.getWidth() - dimension.width) / 2,
                    document.getWidth());
            verticalPositionScale.setDocumentOriginPosition((layeredPane.getHeight() - dimension.height) / 2,
                    document.getHeight());
        }
    }

    /**
     * Sets the z-index of an element in the compositionArea.
     * @param comp Component to set.
     * @param i New z-index.
     */
    public void setZIndex(CompositionJPanel comp, int i) {
        if(layeredPane.isAncestorOf(comp))
            layeredPane.setComponentZOrder(comp, i);
    }

    /**
     * Removes all the drawn elements on the CompositionArea.
     */
    public void removeAllGE() {
        layeredPane.removeAll();
        layeredPane.revalidate();
        document = null;
    }
    
    /**
     * Refreshes the CompositionArea.
     */
    public void refresh(){
        layeredPane.revalidate();
        layeredPane.repaint();
    }

    /**
     * Returns the CompositionAreaOverlay associated to the CompositionArea.
     * @return The CompositionAreaOverlay.
     */
    public CompositionAreaOverlay getOverlay(){
        return (CompositionAreaOverlay)layerUI;
    }

    /**
     * Convert a point on the screen to a new one which represent its position on the document, according to the document position and to the scroll value.
     * @param screenPoint Point on the screen.
     * @return Position on the document of the screenPoint.
     */
    public Point screenPointToDocumentPoint(Point screenPoint){
        int x = screenPoint.x;
        x-=scrollPane.getX();
        x-=scrollPane.getViewport().getX();
        x-=document.getX();
        x+=scrollPane.getHorizontalScrollBar().getValue();

        int y = screenPoint.y;
        y-=scrollPane.getY();
        y-=scrollPane.getViewport().getY();
        y-=document.getY();
        y+=scrollPane.getVerticalScrollBar().getValue();

        return new Point(x, y);
    }

    /**
     * Convert a point on the screen to a new one which represent its position on the CompositionAreaOverlay, according to the scroll value.
     * @param screenPoint Point on the screen.
     * @return Position on the CompositionAreaOverlay of the screenPoint.
     */
    public Point screenPointToOverlayPoint(Point screenPoint){
        int x = screenPoint.x;
        x-=this.getLocationOnScreen().x;
        x-=scrollPane.getX();

        int y = screenPoint.y;
        y-=this.getLocationOnScreen().y;
        y-= scrollPane.getY();

        return new Point(x, y);
    }

    /**
     * Convert a point on the overlay to a new one which represent its position on the Document GE, according to the scroll value.
     * @param screenPoint Point on the overlay.
     * @return Position on the Document GE of the screenPoint.
     */
    public Point overlayPointToDocumentPoint(Point screenPoint){
        int x = screenPoint.x;
        x-=scrollPane.getX();
        x-=document.getX();
        x+=scrollPane.getHorizontalScrollBar().getValue();

        int y = screenPoint.y;
        y-=scrollPane.getY();
        y-=document.getY();
        y+=scrollPane.getVerticalScrollBar().getValue();

        return new Point(x, y);
    }

    /**
     * Convert a point on the document to a new one which represent its position on the layeredPane, according to the document position.
     * @param documentPoint Point on the document.
     * @return Position on the screen of the documentPoint.
     */
    public Point documentPointToLayeredPanePoint(Point documentPoint){
        int x = documentPoint.x;
        x+=document.getX();

        int y = documentPoint.y;
        y+=document.getY();

        return new Point(x, y);
    }

    /**
     * Sets the value of the mouse position in the positionJLabel with the given position in argument (location on screen)
     * @param position Location on the screen of the mouse.
     */
    public void setMousePosition(Point position){
        if(document != null) {
            Point p = screenPointToOverlayPoint(position);
            verticalPositionScale.setMousePosition(p);
            horizontalPositionScale.setMousePosition(p);

            //Calculate the position in inch or mm of the mouse for the positionLabel
            float x = (float)(position.x-document.getLocationOnScreen().x) / PositionScale.DPI;
            float y = (float)(position.y-document.getLocationOnScreen().y) / PositionScale.DPI;
            String unitName = "px";
            DecimalFormat df = new DecimalFormat();
            if(unit == UNIT_INCH) {
                df.setMaximumFractionDigits(2);
                unitName = "in";
            }
            else if(unit == UNIT_MM) {
                df.setMaximumFractionDigits(0);
                x *= 25.4;
                y *= 25.4;
                unitName = "mm";
            }
            positionJLabel.setText("x : " + df.format(x) + unitName + " , y : " + df.format(y) + unitName);
        }
    }

    /**
     * Overlay used to get the mouse position in the layeredPanel.
     */
    private static final class LayeredPaneOverlay extends LayerUI<JComponent>{
        private CompositionArea compositionArea;
        /**
         * Main constructor.
         */
        public LayeredPaneOverlay(CompositionArea compositionArea){
            this.compositionArea = compositionArea;
        }

        @Override
        protected void processMouseMotionEvent(MouseEvent e, JLayer<? extends JComponent> l) {
            compositionArea.setMousePosition(e.getLocationOnScreen());
        }

        @Override
        public void installUI(JComponent c) {
            super.installUI(c);
            JLayer jlayer = (JLayer)c;
            jlayer.setLayerEventMask( AWTEvent.MOUSE_MOTION_EVENT_MASK );
        }

        @Override
        public void uninstallUI(JComponent c) {
            JLayer jlayer = (JLayer)c;
            jlayer.setLayerEventMask(0);
            super.uninstallUI(c);
        }
    }

    /**
     * Returns the bounds of the document panel.
     * @return The bounds of the document panel.
     */
    public Rectangle getDocumentBounds(){
        return document.getBounds();
    }

    /**
     * Configure the CompositionArea.
     * @param unit The unit to use.
     */
    public void configure(int unit){
        this.unit = unit;
        if(unit == UNIT_MM)
            inchOrCm.setText("mm");
        else
            inchOrCm.setText("in");
        verticalPositionScale.setUnit(unit);
        horizontalPositionScale.setUnit(unit);
    }

    /**
     * Returns the actual unit used.
     * @return The actual unit used.
     */
    public int getUnit(){
        return unit;
    }
}
