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

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.beans.EventHandler;
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
public class CompositionArea extends JPanel{

    /** LayerUI use (in this case it's a CompositionAreaOverlay) to display information in the CompositionArea. */
    private LayerUI<JComponent> layerUI;

    /**JScrollPane of the CompositionArea. */
    private final JScrollPane scrollPane;

    /** JLayeredPane where all the CompositionJPanel will be added */
    private JLayeredPane layeredPane;
    
    /**JPanel corresponding to the Document GE. */
    private JPanel document;

    /**Dimension of the document into the CompositionArea. */
    private Dimension dimension;

    /** MainController */
    private MainController mainController;

    /** Tool bar added a the bottom of the CompositionArea to display information */
    private JToolBar bottomJToolBar;

    /** JLabel displaying the mouse position */
    private JLabel positionJLabel;

    /** Spinner to set the zoom value */
    private JSpinner spinnerZoom;

    /**
     * Main constructor.
     */
    public CompositionArea(MainController mainController){
        super(new BorderLayout());
        this.mainController = mainController;
        this.document = null;
        this.dimension = new Dimension(50, 50);

        //Creates the layer for the layeredPane
        this.layeredPane = new JLayeredPane();
        LayerUI LayeredPaneLayerUI = new LayeredPaneOverlay(this);
        JLayer LayeredPaneJLayer = new JLayer<>(layeredPane, LayeredPaneLayerUI);

        //Adds the layer to a JPanel
        JComponent body = new JPanel(new BorderLayout());
        body.add(LayeredPaneJLayer, BorderLayout.CENTER);

        //Sets the ScrollPane that will contain the layeredPane and its JLayer
        scrollPane = new JScrollPane(body, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);

        //Creates the layer for the whole compositionArea
        this.layerUI = new CompositionAreaOverlay(mainController);
        JLayer compositionAreaJLayer = new JLayer<>(scrollPane, this.layerUI);

        //Adds the scrollPane and its layer to the CompositionArea
        this.add(compositionAreaJLayer, BorderLayout.CENTER);

        //Sets the tool bar at the bottom of the CompositionArea
        bottomJToolBar = new JToolBar();
        bottomJToolBar.setLayout(new BorderLayout());

        //Sets the label containig the mouse position
        positionJLabel = new JLabel("x : px ,  y : px");
        //Puts it into a JComponent
        JComponent componentPosition = new JPanel(new FlowLayout());
        componentPosition.add(positionJLabel);
        componentPosition.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        //Sets the zoom spinner
        spinnerZoom = new JSpinner(new SpinnerNumberModel(100, 10, 1000, 1));
        spinnerZoom.setPreferredSize(new Dimension(80, (int) spinnerZoom.getPreferredSize().getHeight()));
        //Puts it into a JComponent
        JComponent component = new JPanel(new FlowLayout());
        component.add(new JLabel("Zoom : "));
        component.add(spinnerZoom);
        component.setBorder(BorderFactory.createLineBorder(Color.LIGHT_GRAY));

        //Adds the two components
        bottomJToolBar.add(componentPosition, BorderLayout.LINE_START);
        bottomJToolBar.add(component, BorderLayout.LINE_END);
        bottomJToolBar.setFloatable(false);

        //Adds the JToolBar
        this.add(bottomJToolBar, BorderLayout.PAGE_END);

        layeredPane.addMouseListener(EventHandler.create(MouseListener.class, mainController, "unselectAllGE", null, "mouseClicked"));
        layeredPane.addComponentListener(EventHandler.create(ComponentListener.class, this, "actuDocumentPosition", null, "componentResized"));
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
                //Add the panel at the center
                this.layeredPane.add(panel);
                int width = ((Document) panel.getGE()).getDimension().width;
                int height = ((Document) panel.getGE()).getDimension().height;
                panel.setBounds((layeredPane.getWidth() - width) / 2, (layeredPane.getHeight() - height) / 2, width, height);
                //If the GE is an instance of Document, register the CompositionJPanel as this.document and adds the border to it
                if (panel.getGE() instanceof Document) {
                    document = panel;
                    Border border = BorderFactory.createMatteBorder(0, 0, 1, 1, Color.LIGHT_GRAY);
                    border = BorderFactory.createCompoundBorder(border, BorderFactory.createMatteBorder(0, 0, 1, 1, Color.GRAY));
                    border = BorderFactory.createCompoundBorder(border, BorderFactory.createMatteBorder(1, 1, 1, 1, Color.DARK_GRAY));
                    document.setBorder(border);
                }
            }
            else
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
    }

    /**
     * Set all the GE bounds according to the new window dimensions.
     */
    public void actuDocumentPosition(){
        if(document != null) {
            document.setBounds((layeredPane.getWidth() - dimension.width) / 2, (layeredPane.getHeight() - dimension.height) / 2, dimension.width, dimension.height);
            mainController.getCompositionAreaController().refreshAllGE();
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
    }
    
    /**
     * Refreshes the CompositionArea.
     */
    public void refresh(){
        layeredPane.revalidate();
        layeredPane.repaint();
    }
    
    /**
     * Returns the buffered image corresponding to the whole CompositionArea.
     * @return The buffered image corresponding to the CompositionArea.
     */
    public BufferedImage getDocBufferedImage(){
        BufferedImage bi = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        layeredPane.paint(g);
        g.dispose();
        return bi;
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
     * @return Position on the document of screenPoint.
     */
    public Point screenPointToDocumentPoint(Point screenPoint){
        int x = screenPoint.x;
        x-=document.getX();
        x+=scrollPane.getHorizontalScrollBar().getValue();

                int y = screenPoint.y;
        y-=document.getY();
        y+=scrollPane.getVerticalScrollBar().getValue();

                System.out.println(x+", "+y);

                return new Point(x, y);
        }

    /**
     * Convert a point on the document to a new one which represent its position on the screen, according to the document position and to the scroll value.
     * @param documentPoint Point on the document.
     * @return Position on the screen of documentPoint.
     */
    public Point documentPointToScreenPoint(Point documentPoint){
        int x = documentPoint.x;
        x+=document.getX();
        x-=scrollPane.getHorizontalScrollBar().getValue();

        int y = documentPoint.y;
        y+=document.getY();
        y-=scrollPane.getVerticalScrollBar().getValue();

        return new Point(x, y);
    }

    /**
     * Sets the value of the mouse position in the positionJLabel with the given position in argument (location on screen)
     * @param position Location on the screen of the mouse.
     */
    public void setMousePosition(Point position){
        if(document != null) {
            position.translate(-document.getLocationOnScreen().x, -document.getLocationOnScreen().y);
            positionJLabel.setText("x : " + position.x + "px ,  y : " + position.y + "px");
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
}
