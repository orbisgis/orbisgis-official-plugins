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

import org.orbisgis.mapcomposer.controller.utils.UndoableEdit.*;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.utils.CAManager;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.*;
import org.orbisgis.mapcomposer.model.graphicalelement.utils.GEManager;
import org.orbisgis.mapcomposer.view.ui.MainWindow;
import org.orbisgis.sif.UIFactory;

import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.List;

import javax.swing.*;
import javax.swing.undo.*;

/**
 * This class manager the interaction between different controllers.
 *
 * @author Sylvain PALOMINOS
 */
public class MainController{

    /** CAManager */
    private CAManager caManager;

    /** GEManager */
    private GEManager geManager;

    /** MainWindow displayed to the user */
    private MainWindow mainWindow;

    /** UndoManager in charge of everything about unoing and redoing */
    private UndoManager undoManager;

    /** Controller in charge of the CompositionArea (which is inside the MainWindow) */
    private CompositionAreaController compositionAreaController;
    /** Controller in charge of exporting, loading and saving */
    private IOController ioController;
    /** Controller in charge of the interaction between the user and the interface */
    private UIController uiController;
    /** Controller in charge of all the GraphicalElements created */
    private GEController geController;

    /** True if the controller is actually undoing or redoing an action (used to not register an action done to undo or redo an old action). */
    private boolean undoRedo;
    /** Property actually changed by the user by using the mouse wheel on a spinner of the tool bar. */
    private GraphicalElement.Property mouseWheelChangedProp;
    /** Timer used to know if the user has stopped to use the wheel since enough time. */
    private Timer waitEndWheelTimer;
    /** Time after which it's considered that the user has stopped to use the mouse wheel. */
    private static final int waitEndWheelTime = 1000;

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
        undoManager.setLimit(50);
        UIFactory.setMainFrame(mainWindow);
        undoRedo = false;
        mouseWheelChangedProp = null;
        waitEndWheelTimer = new Timer(waitEndWheelTime, EventHandler.create(ActionListener.class, this, "wheelEnd"));
    }

    public void undo(){
        if(undoManager.canUndo()) {
            //Block the registering of edit actions
            undoRedo = true;
            undoManager.undo();
            //Enable the registering of edit actions
            undoRedo = false;
            uiController.refreshSpin();
        }
        else
            compositionAreaController.setOverlayMessage("can't undo");
    }

    public void redo(){
        if(undoManager.canRedo()) {
            //Block the registering of edit actions
            undoRedo = true;
            undoManager.redo();
            //Enable the registering of edit actions
            undoRedo = false;
            uiController.refreshSpin();
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

    /**
     * Returns the MainWindow of the application.
     * @return The MainWindow of the application.
     */
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
        if(!undoRedo) {
            boolean flag = true;
            for(GraphicalElement ge : geController.getSelectedGE()) {
                undoManager.addEdit(new RemoveGEUndoableEdit(this, ge, flag));
                flag = false;
            }
        }
        compositionAreaController.remove(geController.getSelectedGE());
        geController.removeSelectedGE();
        mainWindow.getCompositionArea().refresh();
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
        if(!undoRedo) {
            boolean flag = true;
            for(GraphicalElement ge : geController.getSelectedGE()) {
                undoManager.addEdit(new RemoveGEUndoableEdit(this, ge, flag));
                flag = false;
            }
        }
        compositionAreaController.removeAll();
        geController.removeAllGE();
    }

    /**
     * Removes a GraphicalElement
     * @param ge
     */
    public void removeGE(GraphicalElement ge) {
        if(!undoRedo) {
            undoManager.addEdit(new RemoveGEUndoableEdit(this, ge, true));
        }
        compositionAreaController.remove(ge);
        geController.removeGE(ge);
    }

    /**
     * Add to the project the given GE (that is just loaded)..
     * @param ge GE to add to the project.
     */
    public void addGE(GraphicalElement ge) {
        if(!undoRedo) {
            undoManager.addEdit(new AddGEUndoableEdit(this, ge, true));
        }
        compositionAreaController.add(ge);
        geController.addGE(ge);
    }

    /**
     * Sets all the GraphicalElements with the given ConfigurationAttributes (which were just configured).
     * @param listCA List of ConfigurationAttributes configured
     */
    public void validateCAList(List<ConfigurationAttribute> listCA){
        //Saves the GraphicalElement state before applying the configuration
        if(!undoRedo) {
            undoManager.addEdit(new ConfigurationGEUndoableEdit(this, geController.getToBeSet(), true));
        }
        //Apply the configuration
        geController.validateCAList(listCA);
    }

    /**
     * Move the selected GraphicalElement with the given alignment.
     * @param alignment Alignment to apply.
     */
    public void setSelectedGEAlignment(CompositionAreaController.Align alignment){
        //Saves the GraphicalElement state before applying the alignment
        if(!undoRedo) {
            undoManager.addEdit(new MoveGEUndoableEdit(this, geController.getSelectedGE(), true));
        }
        //Apply the alignment
        compositionAreaController.setAlign(alignment);
    }

    /**
     * Modify the z-index of the selected elements according to the value given in argument.
     * @param zIndex Modification to apply to the GE.
     */
    public void setSelectedGEZIndex(CompositionAreaController.ZIndex zIndex){
        //Saves the GraphicalElement state before applying the z-index change
        if(!undoRedo) {
            undoManager.addEdit(new ZIndexGEUndoableEdit(this, geController.getSelectedGE(), true));
        }
        //Apply the z-index change
        compositionAreaController.changeZIndex(zIndex);
    }

    /**
     * Modify the basic properties (x and y position, width, height, and rotation) of the original GE with the value contained by the modifiedCopy GE.
     * @param original GraphicalElement to modify.
     * @param modifiedCopy Modified copy of the GraphicalElement.
     */
    public void modifyGE(GraphicalElement original, GraphicalElement modifiedCopy){
        if(!undoRedo) {
            List<GraphicalElement> listGE = new ArrayList<>();
            listGE.add(original);
            undoManager.addEdit(new ModifyGEUndoableEdit(this, listGE, true));
        }
        original.setX(modifiedCopy.getX());
        original.setY(modifiedCopy.getY());
        original.setWidth(modifiedCopy.getWidth());
        original.setHeight(modifiedCopy.getHeight());
        original.setRotation(modifiedCopy.getRotation());
        geController.modifyGE(original);
    }

    /**
     * Change a property of a GraphicalElement.
     * @param prop Property to modify.
     * @param value New value of the property.
     */
    public void changeProperty(GraphicalElement.Property prop, int value){
        if(mouseWheelChangedProp == null || mouseWheelChangedProp != prop){
            waitEndWheelTimer.start();
            mouseWheelChangedProp = prop;
            undoManager.addEdit(new ModifyGEUndoableEdit(this, geController.getSelectedGE(), true));
        }
        geController.changeProperty(prop, value);
        waitEndWheelTimer.restart();
    }

    /**
     * Action done when the mouse wheel has stopped.
     */
    public void wheelEnd(){
        mouseWheelChangedProp = null;
    }

    /**
     * Returns the GEManager.
     * @return The GEManager
     */
    public GEManager getGEManager(){
        return geManager;
    }

    /**
     * Returns the CompositionAreaController.
     * @return The CompositionAreaController.
     */
    public CompositionAreaController getCompositionAreaController(){
        return compositionAreaController;
    }
    /**
     * Returns the IOController.
     * @return The IOController.
     */
    public IOController getIOController(){
        return ioController;
    }
    /**
     * Returns the UIController.
     * @return The UIController.
     */
    public UIController getUIController(){
        return uiController;
    }
    /**
     * Returns the GEController.
     * @return The GEController.
     */
    public GEController getGEController(){
        return geController;
    }
}
 
