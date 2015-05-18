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

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.RefreshCA;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.element.illustration.SimpleIllustrationGE;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GEProperties;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GERefresh;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.view.utils.CompositionAreaOverlay;
import org.orbisgis.sif.SIFDialog;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.imageio.ImageIO;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.beans.EventHandler;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This controller manage all the GraphicalElement created in the project.
 *
 * @author Sylvain PALOMINOS
 */

public class GEController {

    /** Instance of the new GraphicalElement to create. */
    private GraphicalElement newGE;

    /**This list contain all the GraphicalElement selected by the user*/
    private List<GraphicalElement> selectedGE;

    /** List of all the GraphicalElements created */
    private List<GraphicalElement> listGE;

    /** List of GraphicalElements that will be configured */
    private List<GraphicalElement> toBeSet;

    /** MainController */
    private MainController mainController;

    /** Object for the translation*/
    private static final I18n i18n = I18nFactory.getI18n(GEController.class);

    public GEController(MainController mainController){
        selectedGE = new ArrayList<>();
        this.mainController = mainController;
        listGE = new ArrayList<>();
        toBeSet = new ArrayList<>();
    }

    /**
     * Modifies the position and size of all the selected GraphicalElements.
     */
    public void modifySelectedGE(){
        for(GraphicalElement ge : selectedGE)
            modifyGE(ge);
    }

    /**
     * modify the position and the size of the given GraphicalElement.
     * It does the refresh of the GE, the resizing of the GE representation in the CompositionArea and refreshes the spinner state.
     * The GE representation is just moved and scaled, not completely re-renderer.
     * @param ge GraphicalElement to validate
     */
    public void modifyGE(GraphicalElement ge){
        if(ge instanceof GERefresh)
            ((GERefresh)ge).refresh();
        mainController.getCompositionAreaController().modifyCompositionJPanel(ge);
        mainController.getUIController().refreshSpin();
    }

    /**
     * Reads a list of ConfigurationAttribute and set the GraphicalElement with it.
     * This action is done when the button validate of the properties dialog is clicked.
     * @param caList List of ConfigurationAttributes to read.
     */
    public void validateCAList(List<ConfigurationAttribute> caList) {
        //Apply the function to all the selected GraphicalElements
        for(GraphicalElement ge : toBeSet){
            //Takes each CA from the list of CA to validate
            for(ConfigurationAttribute dialogPropertiesCA : caList) {
                if (!dialogPropertiesCA.getReadOnly()) {
                    if (dialogPropertiesCA instanceof RefreshCA)
                        ((RefreshCA) dialogPropertiesCA).refresh(mainController);
                    ge.setAttribute(dialogPropertiesCA);
                }
            }
            //If the GraphicalElement was already added to the document
            if(mainController.getCompositionAreaController().isGEDrawn(ge))
                mainController.getCompositionAreaController().refreshGE(ge);
                //Set the CompositionAreaOverlay ratio in the case of the GraphicalElement was not already added to the Document
            else{
                //Give the ratio to the CompositionAreaOverlay();
                if(newGE instanceof SimpleIllustrationGE) {
                    try {
                        BufferedImage bi = ImageIO.read(new File(((SimpleIllustrationGE) newGE).getPath()));
                        mainController.getCompositionAreaController().setOverlayRatio((float) bi.getWidth() / bi.getHeight());
                    } catch (IOException e) {
                        LoggerFactory.getLogger(GEController.class).error(e.getMessage());
                    }
                }
            }
        }
        toBeSet = new ArrayList<>();
    }

    /**
     * Selects a GraphicalElement.
     * @param ge GraphicalElement to select.
     */
    public void selectGE(GraphicalElement ge){
        selectedGE.add(ge);
    }

    /**
     * Unselects a GraphicalElement.
     * @param ge GraphicalElement to select.
     */
    public void unselectGE(GraphicalElement ge){
        selectedGE.remove(ge);
    }

    /**
     * Unselect all the GraphicalElement.
     */
    public void unselectAllGE(){
        selectedGE= new ArrayList<>();
    }

    /**
     * Removes all the selected GraphicalElement.
     */
    public void removeSelectedGE(){
        listGE.removeAll(selectedGE);
        selectedGE=new ArrayList<>();
    }

    /**
     * Returns the list of all the GraphicalElements added to the document.
     * @return The list of GraphicalElements
     */
    public List<GraphicalElement> getGEList(){
        return listGE;
    }

    /**
     * Removes all the selected GraphicalElement.
     */
    public void removeAllGE() {
        selectedGE = new ArrayList<>();
        listGE = new ArrayList<>();
    }

    /**
     * Removes the given GraphicalElement.
     * @param ge GE to remove.
     */
    public void removeGE(GraphicalElement ge) {
        selectedGE.remove(ge);
        listGE.remove(ge);
    }

    /**
     * Add to the project the given GE (that is just loaded)..
     * @param ge GE to add to the project.
     */
    public void addGE(GraphicalElement ge) {
        //Apply the z-index change to only the GraphicalElement ge.
        listGE.add(ge);
        List temp = selectedGE;
        selectedGE = new ArrayList<>();
        selectedGE.add(ge);
        mainController.getCompositionAreaController().changeZIndex(CompositionAreaController.ZIndex.TO_FRONT);
        selectedGE = temp;
    }

    /**
     * Apply a change of a property to the selected GraphicalElements.
     * It's mainly used when the value of a spinner of the tool bar change of value.
     * @param prop
     * @param i
     */
    public void changeProperty(GraphicalElement.Property prop, int i){
        for(GraphicalElement ge : selectedGE){
            switch(prop){
                case X:
                    ge.setX(i);
                    break;
                case Y:
                    ge.setY(i);
                    break;
                case WIDTH:
                    ge.setWidth(i);
                    break;
                case HEIGHT:
                    ge.setHeight(i);
                    break;
                case ROTATION:
                    ge.setRotation(i);
                    break;
            }
        }
        modifySelectedGE();
    }

    /**
     * Return the list of selected GraphicalElements
     * @return
     */
    public List<GraphicalElement> getSelectedGE(){
        return selectedGE;
    }

    /**
     * Does the instantiation of the new GraphicalElement and open the configuration dialog.
     * @param newGEClass
     */
    public void instantiateNewGE(Class<? extends GraphicalElement> newGEClass) {
        try {
            //Instantiate the GraphicalElement
            newGE = newGEClass.newInstance();
            //Test if the newGE doesn't implement GEProperties and if there isn't a Document GE
            if( !(newGE instanceof GEProperties) && !mainController.isDocumentCreated()) {
                mainController.getCompositionAreaController().setOverlayMessage(i18n.tr("First create a new document " +
                        "or open an existing one."));
                return;
            }

            //Test if the newGE implements GEProperties and if it needs a Document GE and if there isn't a Document GE
            if( newGE instanceof GEProperties && ((GEProperties)newGE).isDocumentNeeded() && !mainController.isDocumentCreated()) {
                mainController.getCompositionAreaController().setOverlayMessage(i18n.tr("First create a new document " +
                        "or open an existing one."));
                return;
            }

            //Refresh the ConfigurationAttributes to initialize them
            for(ConfigurationAttribute ca : newGE.getAllAttributes())
                if(ca instanceof RefreshCA)
                    ((RefreshCA)ca).refresh(mainController);

            mainController.getUIController().showGEProperties(newGE).addWindowListener(EventHandler.create(WindowListener.class, this, "drawGE", "", "windowClosed"));
        } catch (InstantiationException | IllegalAccessException ex) {
            LoggerFactory.getLogger(MainController.class).error(ex.getMessage());
        }
    }

    /**
     * Allow the user to draw the size of the new GraphicalElement
     * @param winEvent
     */
    public void drawGE(WindowEvent winEvent){
        if(winEvent.getSource() instanceof SIFDialog && newGE != null) {
            SIFDialog sifDialog = (SIFDialog) winEvent.getSource();
            if(sifDialog.isAccepted()) {
                if(newGE instanceof Document){
                    ((Document)newGE).refresh();
                }
                //If the newGE is a Document GE, then draw it immediately
                if(newGE instanceof GEProperties && !((GEProperties)newGE).isDrawnByUser()){
                    mainController.addGE(newGE);
                    mainController.getCompositionAreaController().refreshGE(newGE);
                    newGE=null;
                }
                else {
                    //If the image has an already set path value, get the image ratio
                    if (newGE instanceof SimpleIllustrationGE) {
                        File f = new File(((SimpleIllustrationGE) newGE).getPath());
                        if (f.exists() && f.isFile()) {
                            try {
                                BufferedImage bi = ImageIO.read(f);
                                mainController.getCompositionAreaController().setOverlayRatio((float) bi.getWidth() / bi.getHeight());
                            } catch (IOException e) {
                                LoggerFactory.getLogger(MainController.class).error(e.getMessage());
                            }
                        }
                    }
                    mainController.getCompositionAreaController().setOverlayMode(CompositionAreaOverlay.Mode.NEW_GE);
                    mainController.getCompositionAreaController().setOverlayMessage(i18n.tr("Now you can draw the " +
                            "GraphicalElement. Hold SHIFT to keep the width/height ratio"));
                }

                List<GraphicalElement> list = new ArrayList<>();
                list.add(newGE);
            }
            else{
                newGE=null;
            }
        }
    }

    /**
     * Set the new GraphicalElement and then open the properties dialog.
     */
    public void setNewGE(int x, int y, int width, int height) {
        //Sets the newGE
        if (newGE!=null){
            newGE.setX(x);
            newGE.setY(y);
            newGE.setWidth(width);
            newGE.setHeight(height);
            mainController.addGE(newGE);
            mainController.getCompositionAreaController().refreshGE(newGE);
        }
        mainController.getCompositionAreaController().setOverlayMode(CompositionAreaOverlay.Mode.NONE);
        newGE=null;
    }

    /**
     * Sets the list of GraphicalElements that will be configured.
     * @param list
     */
    public void setToBeSetList(List<GraphicalElement> list){
        toBeSet = list;
    }

    /**
     * Returns the list of GraphicalElements that will be configured.
     * @return List of GraphicalElements that will be set.
     */
    public List<GraphicalElement> getToBeSet(){
        return toBeSet;
    }
}
