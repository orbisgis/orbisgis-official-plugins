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

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.utils.SaveAndLoadHandler;
import org.orbisgis.mapcomposer.view.utils.RenderWorker;
import org.orbisgis.sif.UIFactory;
import org.orbisgis.sif.components.SaveFilePanel;
import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

    /** MainController to get access to the other controllers*/
    private MainController mainController;

    public IOController(MainController mainController){
        this.mainController = mainController;
        saveNLoadHandler = new SaveAndLoadHandler(mainController.getGEManager(), mainController.getCAManager());
    }

    /**
     * Run saveProject function of the SaveHandler.
     */
    public void saveDocument(){
        try {
            saveNLoadHandler.saveProject(mainController.getGEList());
        } catch (NoSuchMethodException|IOException ex) {
            LoggerFactory.getLogger(MainController.class).error(ex.getMessage());
        }
    }

    /**
     * Run loadProject function from the SaveHandler and draw loaded GE.
     */
    public void loadDocument(){
        try {
            List<GraphicalElement> list = saveNLoadHandler.loadProject();
            //Test if the file was successfully loaded.
            if(list != null) {
                mainController.removeAllGE();
                //Add all the GE starting from the last one (to get the good z-index)
                for (int i = 0; i < list.size(); i++)
                    mainController.addGE(list.get(i));
            }
        } catch (ParserConfigurationException |SAXException |IOException ex) {
            LoggerFactory.getLogger(MainController.class).error(ex.getMessage());
        }
    }

    /**
     * Exports to document into png file.
     * First renders again all the GraphicalElement to make sure that the graphical representation are at their best quality.
     * Then exports the CompositionArea.
     */
    public void export(){
        //Render again all the GE. All the RenderWorkers are saved into a list and the export will be done only when all will be terminated.
        RenderWorker lastRenderWorker = mainController.getCompositionAreaController().refreshAllGE();

        //Add to the lastRenderWorker a listener to open a saveFilePanel just after the rendering is done
        //If the lastRenderWorker is null it means that there is nothing to export, so skip the exportation
        if(lastRenderWorker!=null) {
            lastRenderWorker.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                    //Verify if the property state is at DONE
                    if (propertyChangeEvent.getNewValue().equals(SwingWorker.StateValue.DONE)) {
                        //Creates and sets the file chooser
                        SaveFilePanel saveFilePanel = new SaveFilePanel("UIController.Export", "Export document");
                        saveFilePanel.addFilter(new String[]{"png"}, "PNG files");
                        saveFilePanel.loadState();
                        if(UIFactory.showDialog(saveFilePanel)){
                            String path = saveFilePanel.getSelectedFile().getAbsolutePath();

                            try{
                                ImageIO.write(mainController.getCompositionAreaController().getCompositionAreaBufferedImage(), "png", new File(path));
                            } catch (IOException ex) {
                                LoggerFactory.getLogger(MainController.class).error(ex.getMessage());
                            }
                        }
                    }
                }
            });
        }
    }
}
