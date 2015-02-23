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

import org.orbisgis.mapcomposer.controller.utils.exportThreads.ExportPDFThread;
import org.orbisgis.mapcomposer.controller.utils.exportThreads.ExportPNGThread;
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
import javax.swing.JProgressBar;
import javax.xml.parsers.ParserConfigurationException;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller manage the save, load and export actions.
 *
 * @author Sylvain PALOMINOS
 */

public class IOController {

    /** SaveAndLoadHandler which manage saving as xml and loading the GraphicalElements*/
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
     * Runs saveProject function of the SaveHandler.
     * @param listGEToSave List of GraphicalElements to save.
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
     * @return The list of GraphicalElements just loaded.
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
     * Exports the document into the format selected in the export dialog window (SaveFilePanel class)
     * @param listGEToExport List of GraphicalElement to export.
     * @param progressBar Progress bar where should be shown the progression. Can be null.
     */
    public void export(List<GraphicalElement> listGEToExport, JProgressBar progressBar){
        //Creates the export dialog window
        SaveFilePanel saveFilePanel = new SaveFilePanel("UIController.Export", i18n.tr("Export document"));
        //Adds the file type filters
        saveFilePanel.addFilter(new String[]{"png"}, "PNG files");
        //saveFilePanel.addFilter(new String[]{"html"}, "HTML web page");
        saveFilePanel.addFilter(new String[]{"pdf"}, "PDF files");
        saveFilePanel.loadState();
        //Wait the window answer and if the user validate
        if(UIFactory.showDialog(saveFilePanel)){
            String path = saveFilePanel.getSelectedFile().getAbsolutePath();
            Thread threadExport = null;
            //Create the good exporting thread according to the file type selected by the user
            switch(saveFilePanel.getCurrentFilterId()){
                case pngId:
                    threadExport = new ExportPNGThread(listGEToExport, path, progressBar, geManager);
                    break;
                case htmlId:
                    break;
                case pdfId:
                    threadExport = new ExportPDFThread(listGEToExport, path, progressBar, geManager);
                    break;
            }
            //Run the export into another thread not to freeze the MapComposer
            if(threadExport != null)
                threadExport.start();
        }
    }
}
