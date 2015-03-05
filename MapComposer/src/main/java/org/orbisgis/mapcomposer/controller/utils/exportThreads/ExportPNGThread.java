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

package org.orbisgis.mapcomposer.controller.utils.exportThreads;

import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.utils.GEManager;
import org.orbisgis.mapcomposer.view.graphicalelement.RendererRaster;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.imageio.ImageIO;
import javax.swing.JProgressBar;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * This thread exports the document as a PNG image file.
 * @author Sylvain PALOMINOS
 */

public class ExportPNGThread extends Thread {
    /** List of GraphicalElements to export*/
    private List<GraphicalElement> listGEToExport;
    /** Path of the export file */
    private String path;
    /** Progress bar where the progression is shown */
    private JProgressBar progressBar;
    /** GeManager used to get the GraphicalElement rendering methods. */
    private GEManager geManager;

    /** Translation*/
    private static final I18n i18n = I18nFactory.getI18n(ExportPNGThread.class);

    /**
     * Main constructor
     * @param listGEToExport List of GraphicalElement to export.
     * @param path File path to export.
     * @param progressBar Progress bar where the progression is shown.
     */
    public ExportPNGThread(List<GraphicalElement> listGEToExport, String path, JProgressBar progressBar, GEManager geManager){
        //As this class is a thread, the GE can be modified while being export, so they have to be cloned
        this.listGEToExport = new ArrayList<>();
        for(GraphicalElement ge : listGEToExport){
            this.listGEToExport.add(ge.deepCopy());
        }
        this.path = path;
        this.geManager = geManager;
        //If the ProgressBar is null, instantiate one
        if(progressBar != null)
            this.progressBar = progressBar;
        else
            this.progressBar = new JProgressBar();
    }

    @Override
    public void run() {
        try{
            BufferedImage bi = null;
            //Find the Document GE to create the BufferedImage where all the GE will be drawn
            for(GraphicalElement ge : listGEToExport){
                if(ge instanceof Document){
                    bi = new BufferedImage(ge.getWidth(), ge.getHeight(), BufferedImage.TYPE_INT_ARGB);
                }
            }
            //If no Document was created, throw an exception
            if(bi == null){
                throw new IllegalArgumentException(i18n.tr("Error on export : The list of GraphicalElement to export does not contain any Document GE."));
            }

            progressBar.setValue(0);

            //Draw each GraphicalElement in the BufferedImage
            Graphics2D graphics2D = bi.createGraphics();
            for(GraphicalElement ge : listGEToExport){
                double rad = Math.toRadians(ge.getRotation());
                //Width and Height of the rectangle containing the rotated bufferedImage
                final double newWidth = Math.abs(cos(rad)*ge.getWidth())+Math.abs(sin(rad)*ge.getHeight());
                final double newHeight = Math.abs(cos(rad)*ge.getHeight())+Math.abs(sin(rad)*ge.getWidth());
                final int maxWidth = Math.max((int)newWidth, ge.getWidth());
                final int maxHeight = Math.max((int)newHeight, ge.getHeight());
                //Draw the GraphicalElement in a Graphics2D
                BufferedImage bufferedImage = ((RendererRaster)geManager.getRenderer(ge.getClass())).createGEImage(ge);
                graphics2D.drawImage(bufferedImage, ge.getX() + (ge.getWidth() - maxWidth) / 2, ge.getY() + (ge.getHeight() - maxHeight) / 2, null);
                //Set the progress bar value
                progressBar.setValue((listGEToExport.indexOf(ge) * 100) / listGEToExport.size());
                progressBar.revalidate();
            }
            graphics2D.dispose();
            //Write the BufferedImage
            ImageIO.write(bi, "png", new File(path));

            progressBar.setValue(progressBar.getMaximum());

        } catch (IllegalArgumentException|IOException ex) {
            LoggerFactory.getLogger(MainController.class).error(ex.getMessage());
        }
    }
}
