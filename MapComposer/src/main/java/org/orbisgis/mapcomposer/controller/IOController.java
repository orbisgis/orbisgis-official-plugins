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

package org.orbisgis.mapcomposer.controller;

import org.orbisgis.mapcomposer.model.configurationattribute.utils.CAManager;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.utils.GEManager;
import org.orbisgis.mapcomposer.model.utils.SaveAndLoadHandler;
import org.orbisgis.sif.UIFactory;
import org.orbisgis.sif.components.SaveFilePanel;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.imageio.ImageIO;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * This controller manage the save, load and export actions.
 *
 * @author Sylvain PALOMINOS
 */

public class IOController {

    /** SaveAndLoadHandler */
    private SaveAndLoadHandler saveNLoadHandler;

    /** Object for the translation*/
    private static final I18n i18n = I18nFactory.getI18n(IOController.class);

    /** GEManager */
    private GEManager geManager;

    /** GEManager */
    private CAManager caManager;

    private final static int pngId = 111531;
    private final static int htmlId = 3213613;
    private final static int pdfId = 111220;

    public IOController(GEManager geManager, CAManager caManager){
        this.geManager = geManager;
        this.caManager = caManager;
        saveNLoadHandler = new SaveAndLoadHandler(geManager, caManager);
    }

    /**
     * Run saveProject function of the SaveHandler.
     */
    public void saveDocument(List<GraphicalElement> listGEToSave){
        try {
            saveNLoadHandler.saveProject(listGEToSave);
        } catch (NoSuchMethodException|IOException ex) {
            LoggerFactory.getLogger(MainController.class).error(ex.getMessage());
        }
    }

    /**
     * Run loadProject function from the SaveHandler and draw loaded GE.
     */
    public List<GraphicalElement> loadDocument(){
        try {
            return saveNLoadHandler.loadProject();
        } catch (ParserConfigurationException |SAXException |IOException ex) {
            LoggerFactory.getLogger(MainController.class).error(ex.getMessage());
            return null;
        }
    }

    /**
     * Exports to document into png file.
     * First renders again all the GraphicalElement to make sure that the graphical representation are at their best quality.
     * Then exports the CompositionArea.
     */
    public void export(List<GraphicalElement> listGEToExport){
        SaveFilePanel saveFilePanel = new SaveFilePanel("UIController.Export", i18n.tr("Export document"));
        saveFilePanel.addFilter(new String[]{"png"}, "PNG files");
        saveFilePanel.addFilter(new String[]{"html"}, "HTML web page");
        saveFilePanel.addFilter(new String[]{"pdf"}, "PDF files");
        saveFilePanel.loadState();
        if(UIFactory.showDialog(saveFilePanel)){
            String path = saveFilePanel.getSelectedFile().getAbsolutePath();
            switch(saveFilePanel.getCurrentFilterId()){
                case pngId:
                    exportAsPNG(listGEToExport, path);
                    break;
                case htmlId:
                    break;
                case pdfId:
                    break;
            }
        }
    }

    /**
     * Export the Document as a PNG image file.
     * @param listGEToExport List of GraphicalElement to export.
     * @param path File path to export.
     */
    private void exportAsPNG(List<GraphicalElement> listGEToExport, String path){
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
                throw new IllegalArgumentException("Error on export : The list of GraphicalElement to export does not contain any Document GE.");
            }
            //Else draw each GraphicalElement in the BufferedImage
            else {
                Graphics2D graphics2D = bi.createGraphics();
                for(GraphicalElement ge : listGEToExport){
                    graphics2D.drawImage(geManager.getRenderer(ge.getClass()).createImageFromGE(ge), ge.getX(), ge.getY(), null);
                }
                graphics2D.dispose();
                ImageIO.write(bi, "png", new File(path));
            }
        } catch (IllegalArgumentException|IOException ex) {
            LoggerFactory.getLogger(MainController.class).error(ex.getMessage());
        }
    }
}
