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

import net.miginfocom.swing.MigLayout;
import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.utils.GEManager;
import org.orbisgis.mapcomposer.view.graphicalelement.RendererRaster;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.imageio.ImageIO;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * This thread exports the document as a PNG image file.
 * @author Sylvain PALOMINOS
 */

public class ExportImageThread implements ExportThread {
    /** Path of the export file */
    private String path;
    /** Progress bar where the progression is shown */
    private JProgressBar progressBar;
    /** GeManager used to get the GraphicalElement rendering methods. */
    private GEManager geManager;

    /** Translation*/
    private static final I18n i18n = I18nFactory.getI18n(ExportImageThread.class);

    /**Map of GraphicalElement and boolean.
     * The boolean tells if the vector rendering should be use or not (if not use the raster rendering)
     **/
    private Map<GraphicalElement, Boolean> geIsVectorMap;

    /** JComboBox component containing the selected image type */
    private JComboBox<String> imageType;

    /**
     * Main constructor
     */
    public ExportImageThread(){
        this.geIsVectorMap = new HashMap<>();
    }

    @Override
    public void run() {
        try{
            BufferedImage bi = null;
            //Find the Document GE to create the BufferedImage where all the GE will be drawn
            for(GraphicalElement ge : geIsVectorMap.keySet()){
                if(ge instanceof Document){
                    bi = new BufferedImage(ge.getWidth(), ge.getHeight(), BufferedImage.TYPE_INT_ARGB);
                }
            }
            //If no Document was created, throw an exception
            if(bi == null){
                throw new IllegalArgumentException(i18n.tr("Error on export : The list of GraphicalElement to export does not contain any Document GE."));
            }

            progressBar.setIndeterminate(true);
            int geCount = 0;
            //Draw each GraphicalElement in the BufferedImage
            Graphics2D graphics2D = bi.createGraphics();
            for(GraphicalElement ge : geIsVectorMap.keySet()){
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
                progressBar.setIndeterminate(false);
                progressBar.setValue((geCount * 100) / geIsVectorMap.keySet().size());
                progressBar.revalidate();
                geCount ++;
            }
            graphics2D.dispose();
            //Write the BufferedImage
            ImageIO.write(bi, (String)imageType.getSelectedItem(), new File(path));

            progressBar.setValue(progressBar.getMaximum());
            //Wait a bit before erasing the progress bar
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                LoggerFactory.getLogger(ExportImageThread.class).error(e.getMessage());
            }
            progressBar.setValue(0);

        } catch (IllegalArgumentException|IOException ex) {
            LoggerFactory.getLogger(ExportImageThread.class).error(ex.getMessage());
        }
    }

    @Override
    public void addData(GraphicalElement ge, Boolean isVector) {
        if(geIsVectorMap.containsKey(ge)) {
            geIsVectorMap.remove(ge);
        }
        geIsVectorMap.put(ge, isVector);
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void setGEManager(GEManager geManager) {
        this.geManager = geManager;
    }

    @Override
    public void setProgressBar(JProgressBar progressBar) {
        if(progressBar != null) {
            this.progressBar = progressBar;
        }
        else {
            this.progressBar = new JProgressBar();
        }
    }

    @Override
    public JComponent constructExportPanel(List<GraphicalElement> listGEToExport) {
        for(GraphicalElement ge : listGEToExport){
            addData(ge, true);
        }

        JPanel panelPNG = new JPanel(new MigLayout());
        panelPNG.add(new JLabel("Image type : "));
        imageType = new JComboBox<>();
        imageType.addItem(I18n.marktr("png"));
        imageType.addItem(I18n.marktr("jpg"));
        imageType.addItem(I18n.marktr("gif"));
        panelPNG.add(imageType, "wrap");
        return panelPNG;
    }

    @Override
    public String getName() {
        return i18n.tr("Image");
    }

    @Override
    public String getDescription() {
        return i18n.tr("Export the document ad an image.");
    }

    @Override
    public Map<String, String> getFileFilters() {
        Map<String, String> ret = new HashMap<>();
        ret.put((String)imageType.getSelectedItem(), i18n.tr(imageType.getSelectedItem() + " image file"));
        return ret;
    }
}
