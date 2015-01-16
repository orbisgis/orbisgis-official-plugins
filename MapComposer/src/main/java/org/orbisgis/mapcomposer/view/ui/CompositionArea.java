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

import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.view.utils.CompositionAreaOverlay;
import org.orbisgis.mapcomposer.view.utils.CompositionJPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.beans.EventHandler;
import javax.swing.*;
import javax.swing.event.UndoableEditListener;
import javax.swing.plaf.LayerUI;
import javax.swing.undo.UndoableEditSupport;

/**
 * Area for the map document composition.
 * All the GraphicalElement will be drawn inside.
 *
 * @author Sylvain PALOMINOS
 */
public class CompositionArea extends JPanel{

    /**JScrollPane of the CompositionArea. */
    private final JScrollPane scrollPane;
    
    /**Main JPanel of the CompositionArea. */
    private final JPanel panel = new JPanel(null);

    /**Dimension of the document into the CompositionArea. */
    private Dimension dimension = new Dimension(50, 50);

    /** LayerUI use (in this case it's a CompositionAreaOverlay) to display information in the CompositionArea. */
    private LayerUI<JComponent> layerUI;

    /** JLayer used to link the LayerUI and the CompositionArea. */
    private JLayer<JComponent> jLayer;
    /**
     * Main constructor.
     */
    public CompositionArea(UIController uiController){
        super(new BorderLayout());
        JPanel body = new JPanel(new BorderLayout());
        body.add(panel, BorderLayout.CENTER);
        scrollPane = new JScrollPane(body, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.add(scrollPane, BorderLayout.CENTER);

        layerUI = new CompositionAreaOverlay(uiController);
        jLayer = new JLayer<>(panel, layerUI);
        this.add(jLayer);

        panel.addMouseListener(EventHandler.create(MouseListener.class, uiController, "unselectAllGE", null, "mouseClicked"));
    }

    /**
     * Enable or disable the CompositionAreaOverlay.
     * @param mode If true enable the overlay, disable it otherwise
     */
    public void setOverlayMode(CompositionAreaOverlay.Mode mode){
        ((CompositionAreaOverlay)layerUI).setMode(mode);
    }
    
    /**
     * Adds a CompositionPanel to itself. Should be call only once for each GraphicalElement.
     * @param panel CompositionPanel to add.
     */
    public void addGE(CompositionJPanel panel){
        this.panel.add(panel);
    }
    
    /**
     * Removes the given panel representing a GE.
     * @param panel Panel to remove.
     */
    public void removeGE(CompositionJPanel panel){
        if(this.panel.isAncestorOf(panel))
            this.panel.remove(panel);
    }
    
    /**
     * Sets the dimension of the document in the compositionArea.
     * This method should be called on the document properties definition.
     * @param dimension Dimension of the document.
     */
    public void setDocumentDimension(Dimension dimension){
        this.dimension =dimension;
        this.panel.setPreferredSize(this.dimension);
    }

    /**
     * Sets the z-index of an element in the compositionArea.
     * @param comp Component to set.
     * @param i New z-index.
     */
    public void setZIndex(CompositionJPanel comp, int i) {
        panel.setComponentZOrder(comp, i);
    }

    /**
     * Removes all the drawn elements on the CompositionArea.
     */
    public void removeAllGE() {
        panel.removeAll();
        panel.revalidate();
    }
    
    /**
     * Refreshes the CompositionArea.
     */
    public void refresh(){
        panel.revalidate();
        panel.repaint();
    }
    
    /**
     * Returns the buffered image corresponding to the whole CompositionArea.
     * @return The buffered image corresponding to the CompositionArea.
     */
    public BufferedImage getDocBufferedImage(){
        BufferedImage bi = new BufferedImage(dimension.width, dimension.height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        panel.paint(g);
        g.dispose();
        return bi;
    }

    public CompositionAreaOverlay getOverlay(){
        return (CompositionAreaOverlay)layerUI;
    }
}
