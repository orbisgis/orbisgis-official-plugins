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

import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.view.graphicalelement.GERenderer;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * This class extends the SwingWorker and is used to do the rendering of a GraphicalElement without freezing the MapComposer.
 *
 * @author Sylvain PALOMINOS
 */
public class RenderWorker extends SwingWorker{

    private CompositionJPanel compPanel;
    private GERenderer geRenderer;
    private GraphicalElement ge;

    /**
     * Main Constructor
     * @param compPanel CompositionPanel where the GraphicalElement should be rendered.
     * @param geRenderer Renderer to use to render the GraphicalElement.
     * @param ge GraphicalElement to render
     */
    public RenderWorker(CompositionJPanel compPanel, GERenderer geRenderer, GraphicalElement ge){
        this.compPanel = compPanel;
        this.geRenderer = geRenderer;
        this.ge = ge;
    }

    @Override
    protected Object doInBackground() throws Exception {
        //Display the wait layer in the CompositionJPanel
        compPanel.setEnabled(false);
        compPanel.getWaitLayer().start();
        BufferedImage bi = geRenderer.createImageFromGE(ge);
        compPanel.redraw(ge.getX(), ge.getY(), ge.getWidth(), ge.getHeight(), ge.getRotation(), bi);
        compPanel.getWaitLayer().stop();
        compPanel.setEnabled(true);
        return null;
    }
}

