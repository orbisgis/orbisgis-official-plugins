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
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.utils.CAManager;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.*;
import org.orbisgis.mapcomposer.model.graphicalelement.utils.GEManager;
import org.orbisgis.mapcomposer.view.ui.MainWindow;
import org.orbisgis.sif.UIFactory;

import java.io.*;
import java.util.*;

import javax.swing.undo.*;

/**
 * This class manager the interaction between different controllers.
 *
 * @author Sylvain PALOMINOS
 */
public class MainController/* implements StateEditable*/{

    /** CAManager */
    private CAManager caManager;

    /** GEManager */
    private GEManager geManager;

    private MainWindow mainWindow;

    private UndoManager undoManager;

    private CompositionAreaController compositionAreaController;
    private IOController ioController;
    private UIController uiController;
    private GEController geController;

    private boolean undoRedo;

    /**
     * Main constructor.
     */
    public MainController(){
        //Initialize the different attributes
        caManager = new CAManager();
        geManager = new GEManager();
        ioController = new IOController(this);
        uiController = new UIController(this);
        geController = new GEController(this);
        compositionAreaController = new CompositionAreaController(this);
        mainWindow = new MainWindow(this);
        mainWindow.setLocationRelativeTo(null);
        compositionAreaController.setCompositionArea(mainWindow.getCompositionArea());
        undoManager = new UndoManager();
        UIFactory.setMainFrame(mainWindow);
        undoRedo = false;
    }

    public void undo(){
        if(undoManager.canUndo()) {
            undoRedo = true;
            undoManager.undo();
        }
        else
            compositionAreaController.setOverlayMessage("can't undo");
    }

    public void redo(){
        if(undoManager.canRedo()) {
            undoRedo = true;
            undoManager.redo();
            undoRedo = false;
        }
        else
            compositionAreaController.setOverlayMessage("can't redo");
    }

    /**
     * Returns true if the CompositionArea already contain a Document GE, false otherwise.
     * @return true if a document GE exist, false otherwise.
     */
    public boolean isDocumentCreated(){
        for(GraphicalElement ge : geController.getGEList())
            if(ge instanceof Document)
                return true;
        return false;
    }

    public MainWindow getMainWindow() { return mainWindow; }

    /**
     * Returns the CAManager.
     * @return The CAManager
     */
    public CAManager getCAManager() { return caManager; }

    /**
     * Selects a GraphicalElement and redisplays the ConfigurationAttributes.
     * @param ge GraphicalElement to select.
     */
    public void selectGE(GraphicalElement ge){
        geController.selectGE(ge);
        compositionAreaController.selectGE(ge);
        uiController.refreshSpin();
    }

    /**
     * Unselects a GraphicalElement and redisplays the ConfigurationAttributes.
     * @param ge GraphicalElement to select.
     */
    public void unselectGE(GraphicalElement ge){
        geController.unselectGE(ge);
        compositionAreaController.unselectGE(ge);
        uiController.refreshSpin();
    }

    /**
     * Unselect all the GraphicalElement.
     * Reset the selectedGE list and unselect all the CompositionJPanel in the compositionArea.
     */
    public void unselectAllGE(){
        //Unselect all the GraphicalElements
        compositionAreaController.unselectAllGE();
        geController.unselectAllGE();
        uiController.refreshSpin();
    }

    /**
     * Removes all the selected GraphicalElement.
     */
    public void removeSelectedGE(){
        compositionAreaController.remove(geController.getSelectedGE());
        geController.removeSelectedGE();
        mainWindow.getCompositionArea().refresh();
        if(!undoRedo) {
            undoManager.addEdit(new UndoableEdit(UndoableEdit.EditType.REMOVE_GE, geController.getSelectedGE(), this));
        }
    }

    /**
     * Returns the list of all the GraphicalElements added to the document.
     * @return The list of GraphicalElements
     */
    public List<GraphicalElement> getGEList(){
        return geController.getGEList();
    }

    /**
     * Remove all the displayed GE from the panel.
     */
    public void removeAllGE() {
        compositionAreaController.removeAll();
        geController.removeAllGE();
        if(!undoRedo) {
            undoManager.addEdit(new UndoableEdit(UndoableEdit.EditType.REMOVE_GE, getGEList(), this));
        }
    }

    /**
     * Removes a GraphicalElement
     * @param ge
     */
    public void removeGE(GraphicalElement ge) {
        compositionAreaController.remove(ge);
        geController.removeGE(ge);
        if(!undoRedo) {
            List<GraphicalElement> listGE = new ArrayList<>();
            listGE.add(ge);
            undoManager.addEdit(new UndoableEdit(UndoableEdit.EditType.REMOVE_GE, listGE, this));
        }
    }

    /**
     * Add to the project the given GE (that is just loaded)..
     * @param ge GE to add to the project.
     */
    public void addGE(GraphicalElement ge) {
        compositionAreaController.add(ge);
        geController.addGE(ge);
        if(!undoRedo) {
            List<GraphicalElement> listGE = new ArrayList<>();
            listGE.add(ge);
            undoManager.addEdit(new UndoableEdit(UndoableEdit.EditType.ADD_GE, listGE, this));
        }
    }

    public void validateCAList(List<ConfigurationAttribute> listCA){
        geController.validateCAList(listCA);
        if(!undoRedo) {
            undoManager.addEdit(new UndoableEdit(UndoableEdit.EditType.CONFIGURATION_GE, geController.getSelectedGE(), this));
        }
    }

    public GEManager getGEManager(){
        return geManager;
    }
    public CompositionAreaController getCompositionAreaController(){
        return compositionAreaController;
    }
    public IOController getIOController(){
        return ioController;
    }
    public UIController getUIController(){
        return uiController;
    }
    public GEController getGEController(){
        return geController;
    }

    public static class UndoableEdit extends AbstractUndoableEdit{

        public enum EditType {ADD_GE, REMOVE_GE, CONFIGURATION_GE};

        private EditType editType;
        private List<GraphicalElement> listGE;
        private MainController mainController;

        public UndoableEdit(EditType editType, List<GraphicalElement> listGE, MainController mainController){
            this.editType = editType;
            this.listGE = new ArrayList<>();
            for(GraphicalElement ge : listGE) {
                this.listGE.add(ge);
                if(editType==EditType.CONFIGURATION_GE)
                    this.listGE.add(ge.deepCopy());
            }
            this.mainController = mainController;
        }

        @Override
        public void redo() throws CannotRedoException {
            super.redo();
            switch(editType){
                case ADD_GE:
                    for(GraphicalElement ge : listGE)
                        mainController.addGE(ge);
                    mainController.getCompositionAreaController().refreshGE(listGE);
                    break;
                case REMOVE_GE:
                    for(GraphicalElement ge : listGE)
                        mainController.removeGE(ge);
                    mainController.getCompositionAreaController().refreshGE(listGE);
                    break;
                case CONFIGURATION_GE:
                    for(GraphicalElement geNow : mainController.getGEList()){
                        for(GraphicalElement gePast : listGE){
                            if(geNow == gePast){
                                System.out.println(geNow + ", " + gePast);
                                for(ConfigurationAttribute ca : gePast.getAllAttributes()){
                                    System.out.println("n\t" + ca.getName());
                                    System.out.println("v\t" + ca.getValue().toString());
                                    geNow.setAttribute(ca);
                                }
                            }
                        }
                    }
                    mainController.getCompositionAreaController().refreshGE(listGE);
                    break;
            }
        }

        @Override
        public void undo() throws CannotUndoException {
            super.undo();
            switch(editType){
                case ADD_GE:
                    for(GraphicalElement ge : listGE)
                        mainController.removeGE(ge);
                    mainController.getCompositionAreaController().refreshGE(listGE);
                    break;
                case REMOVE_GE:
                    for(GraphicalElement ge : listGE)
                        mainController.addGE(ge);
                    mainController.getCompositionAreaController().refreshGE(listGE);
                    break;
                case CONFIGURATION_GE:
                    for(int i=0; i<listGE.size()/2; i+=2){
                        if(listGE.get(i) instanceof GERefresh)
                            ((GERefresh) listGE.get(i)).refresh();
                        for(ConfigurationAttribute ca : listGE.get(i+1).getAllAttributes()){
                            listGE.get(i).setAttribute(ca);
                        }
                        mainController.getCompositionAreaController().refreshGE(listGE.get(i));
                    }
                    /*for(GraphicalElement geNow : mainController.getGEList()){
                        for(GraphicalElement gePast : listGE){
                            if(geNow == gePast){
                                System.out.println(geNow + ", " + gePast);
                                for(ConfigurationAttribute ca : gePast.getAllAttributes()){
                                    System.out.println("n\t" + ca.getName());
                                    System.out.println("v\t" + ca.getValue().toString());
                                    System.out.println("s\t" + ((ListCA)ca).getSelected());
                                    geNow.setAttribute(ca);
                                }
                            }
                        }
                    }*/
                    break;
            }
        }
    }
}
 
