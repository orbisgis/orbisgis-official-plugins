/*
* MapComposer is an OrbisGIS plugin dedicated to the creation of cartographic
* documents.
*
* This plugin was firstly developed  at French IRSTV institute as part of the MApUCE project,
* funded by the French Agence Nationale de la Recherche (ANR) under contract ANR-13-VBDU-0004.
* 
* Since 2015, MapComposer is developed and maintened by the GIS group of the DECIDE team of the 
* Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
*
* The GIS group of the DECIDE team is located at :
*
* Laboratoire Lab-STICC – CNRS UMR 6285
* Equipe DECIDE
* UNIVERSITÉ DE BRETAGNE-SUD
* Institut Universitaire de Technologie de Vannes
* 8, Rue Montaigne - BP 561 56017 Vannes Cedex
*
* Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
* Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
*
* The MapComposer plugin is distributed under GPL 3 license. 
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

import org.orbisgis.commons.progress.SwingWorkerPM;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.view.graphicalelement.GERenderer;
import org.orbisgis.mapcomposer.view.graphicalelement.RendererRaster;

import javax.swing.*;
import java.awt.image.BufferedImage;

/**
 * This class extends the SwingWorker and is used to do the rendering of a GraphicalElement without freezing the MapComposer.
 *
 * @author Sylvain PALOMINOS
 */
public class RenderWorker extends SwingWorkerPM {

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
        BufferedImage bi = ((RendererRaster)geRenderer).createGEImage(ge, this.getProgressMonitor());
        compPanel.refresh(bi);
        compPanel.getWaitLayer().stop();
        compPanel.setEnabled(true);
        return null;
    }
}

