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
import org.orbisgis.mapcomposer.model.configurationattribute.utils.CAManager;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.element.illustration.SimpleIllustrationGE;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.*;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement.Property;
import org.orbisgis.mapcomposer.model.graphicalelement.utils.GEManager;
import org.orbisgis.mapcomposer.view.ui.MainWindow;
import org.orbisgis.mapcomposer.view.utils.CompositionAreaOverlay;
import org.orbisgis.mapcomposer.view.utils.UIDialogProperties;
import org.orbisgis.sif.SIFDialog;
import org.orbisgis.sif.UIFactory;
import org.orbisgis.sif.UIPanel;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.beans.EventHandler;
import java.io.File;
import java.io.IOException;
import java.util.*;

import javax.imageio.ImageIO;
import javax.swing.undo.*;

import org.slf4j.LoggerFactory;

/**
 * This class manager the interaction between the MainWindows, the CompositionArea and and the data model.
 *
 * @author Sylvain PALOMINOS
 */
public class UIController implements StateEditable{

    /** CAManager */
    private CAManager caManager;

    /** GEManager */
    private GEManager geManager;
    
    /**This list contain all the GraphicalElement selected by the user*/
    private List<GraphicalElement> selectedGE;

    /**This list contain all the GraphicalElements that will be modified (z-index change, validation of the CA)
     * It's different from the SelectedGE list to permit to modify elements without loosing the selected GE.
     * e.g. modify one element by double clicking while keeping GraphicalElements selected.
     */
    private List<GraphicalElement> toBeSet;
    
    /**GraphicalElement stack giving the Z-index information*/
    private Stack<GraphicalElement> zIndexStack;
    
    private MainWindow mainWindow;

    /** Instance of the new GraphicalElement to create. */
    private GraphicalElement newGE;

    private UndoManager undoManager;

    private CompositionAreaController compositionAreaController;
    private IOController ioController;
    
    /**
     * Main constructor.
     */
    public UIController(){
        //Initialize the different attributes
        caManager = new CAManager();
        geManager = new GEManager();
        selectedGE = new ArrayList<>();
        toBeSet = new ArrayList<>();
        zIndexStack = new Stack<>();
        ioController = new IOController(this);
        mainWindow = new MainWindow(this);
        mainWindow.setLocationRelativeTo(null);
        undoManager = new UndoManager();
        compositionAreaController = new CompositionAreaController(this, mainWindow.getCompositionArea());
        UIFactory.setMainFrame(mainWindow);
    }

    public void undo(){
        //undoManager.undo();
        if(undoManager.canUndo())
            undoManager.undo();
        else
            System.out.println("can't undo");
    }

    public void redo(){
        //undoManager.redo();
        if(undoManager.canRedo())
            undoManager.redo();
        else
            System.out.println("can't redo");
    }

    /**
     * Returns true if the CompositionArea already contain a Document GE, false otherwise.
     * @return true if a document GE exist, false otherwise.
     */
    public boolean isDocumentCreated(){
        for(GraphicalElement ge : zIndexStack)
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
     * Change the Z-index of the displayed GraphicalElement.
     * @param z Change of the Z-index.
     */
    public void changeZIndex(ZIndex z){
        Stack<GraphicalElement> temp = new Stack<>();
        Stack<GraphicalElement> tempBack = new Stack<>();
        Stack<GraphicalElement> tempFront = new Stack<>();
        //Get the GE implementing the GEProperties interface and which are always on the back
        for(GraphicalElement ge : zIndexStack){
            if(ge instanceof GEProperties && ((GEProperties)ge).isAlwaysOnBack()){
                tempBack.push(ge);
                temp.add(ge);
            }
        }
        //Get the GE implementing the GEProperties interface and which are always on the front
        for(GraphicalElement ge : zIndexStack){
            if(ge instanceof  GEProperties && ((GEProperties)ge).isAlwaysOnTop()){
                tempFront.push(ge);
                temp.add(ge);
            }
        }
        //Remove the previous detected elements
        for(GraphicalElement ge : temp){
            zIndexStack.remove(ge);
        }
        temp = new Stack<>();

        //Move the others GE
        //In each cases : first sort the GE from the selectedGE list,
        //Then place each GE at the good index in the stack.
        switch (z){
            case TO_FRONT:
                Collections.sort(selectedGE, new Comparator<GraphicalElement>() {
                    @Override
                    public int compare(GraphicalElement ge1, GraphicalElement ge2) {
                        if(zIndexStack.indexOf(ge1)>zIndexStack.indexOf(ge2)) return -1;
                        if(zIndexStack.indexOf(ge1)<zIndexStack.indexOf(ge2)) return 1;
                        return 0;
                    }
                });
                for(GraphicalElement ge : selectedGE) {
                    zIndexStack.remove(ge);
                    zIndexStack.add(0, ge);
                }
                break;
            case FRONT:
                Collections.sort(selectedGE, new Comparator<GraphicalElement>() {
                    @Override
                    public int compare(GraphicalElement ge1, GraphicalElement ge2) {
                        if(zIndexStack.indexOf(ge1)>zIndexStack.indexOf(ge2)) return 1;
                        if(zIndexStack.indexOf(ge1)<zIndexStack.indexOf(ge2)) return -1;
                        return 0;
                    }
                });
                temp.addAll(zIndexStack);
                for(GraphicalElement ge : selectedGE) {
                    if (zIndexStack.indexOf(ge) > 0) {
                        int index = temp.indexOf(ge) - 1;
                        zIndexStack.remove(ge);
                        zIndexStack.add(index, ge);
                    }
                }
                break;
            case BACK:
                Collections.sort(selectedGE, new Comparator<GraphicalElement>() {
                    @Override
                    public int compare(GraphicalElement ge1, GraphicalElement ge2) {
                        if(zIndexStack.indexOf(ge1)>zIndexStack.indexOf(ge2)) return -1;
                        if(zIndexStack.indexOf(ge1)<zIndexStack.indexOf(ge2)) return 1;
                        return 0;
                    }
                });
                temp.addAll(zIndexStack);
                for(GraphicalElement ge : selectedGE) {
                    if (zIndexStack.indexOf(ge) < zIndexStack.size() - 1) {
                        int index = temp.indexOf(ge) + 1;
                        zIndexStack.remove(ge);
                        zIndexStack.add(index, ge);
                    }
                }
                break;
            case TO_BACK:
                Collections.sort(selectedGE, new Comparator<GraphicalElement>() {
                    @Override
                    public int compare(GraphicalElement ge1, GraphicalElement ge2) {
                        if(zIndexStack.indexOf(ge1)>zIndexStack.indexOf(ge2)) return 1;
                        if(zIndexStack.indexOf(ge1)<zIndexStack.indexOf(ge2)) return -1;
                        return 0;
                    }
                });
                for(GraphicalElement ge : selectedGE) {
                    zIndexStack.remove(ge);
                    zIndexStack.add(ge);
                }
                break;
        }

        //Add to the stack the GE of the front
        zIndexStack.addAll(tempFront);
        //Add to the stack the GE of the back
        zIndexStack.addAll(tempBack);
        //Set the z-index of the GE from their stack position
        compositionAreaController.setZIndex(zIndexStack);
        modifySelectedGE();
    }
    
    /**
     * Selects a GraphicalElement and redisplays the ConfigurationAttributes.
     * @param ge GraphicalElement to select.
     */
    public void selectGE(GraphicalElement ge){
        selectedGE.add(ge);
        compositionAreaController.selectGE(ge);
        refreshSpin();
    }

    /**
     * Refreshes the JSpinner value and state (enable or not) with the values from the selected GEs.
     * The method test each GraphicalElement from the selectedGE list to know if every single GE property (X and Y position, width, height, rotation) is the same for the GEs.
     * In the case where a property is the same for all the selected GE, the corresponding spinner is set to the common value and enabled.
     * In the case where a property is different for the selected GE, the corresponding spinner is set to 0 and disabled.
     * In the case where no GE are selected, all the spinners are set to 0 and disabled.
     */
    public void refreshSpin(){
        //Set the default state for the spinner (enabled and value to 0).
        boolean boolX=false, boolY=false, boolW=false, boolH=false, boolR=false;
        int x=0, y=0, w=0, h=0, r=0;
        //If GEs are selected, test each
        if(!selectedGE.isEmpty()){
            x=selectedGE.get(0).getX();
            y=selectedGE.get(0).getY();
            w=selectedGE.get(0).getWidth();
            h=selectedGE.get(0).getHeight();
            r=selectedGE.get(0).getRotation();
            //Test each GE
            for(GraphicalElement graph : selectedGE){
                if (x != graph.getX()){ boolX=true;x=selectedGE.get(0).getX();}
                if(y!=graph.getY()){ boolY=true;y=selectedGE.get(0).getY();}
                if(w!=graph.getWidth()){ boolW=true;w=selectedGE.get(0).getWidth();}
                if(h!=graph.getHeight()){ boolH=true;h=selectedGE.get(0).getHeight();}
                if(r!=graph.getRotation()){ boolR=true;r=selectedGE.get(0).getRotation();}
            }
        }
        //If no GE are selected, lock all the spinners
        else {
            boolX = true;
            boolY = true;
            boolW = true;
            boolH = true;
            boolR = true;
        }
        //Sets the spinners.
        mainWindow.setSpinner(boolX, x, Property.X);
        mainWindow.setSpinner(boolY, y, Property.Y);
        mainWindow.setSpinner(boolW, w, Property.WIDTH);
        mainWindow.setSpinner(boolH, h, Property.HEIGHT);
        mainWindow.setSpinner(boolR, r, Property.ROTATION);
    }
    
    /**
     * Unselects a GraphicalElement and redisplays the ConfigurationAttributes.
     * @param ge GraphicalElement to select.
     */
    public void unselectGE(GraphicalElement ge){
        selectedGE.remove(ge);
        compositionAreaController.unselectGE(ge);
        refreshSpin();
    }
    
    /**
     * Unselect all the GraphicalElement.
     * Reset the selectedGE list and unselect all the CompositionJPanel in the compositionArea.
     */
    public void unselectAllGE(){
        //Unselect all the GraphicalElements
        compositionAreaController.unselectAllGE();
        selectedGE= new ArrayList<>();
        refreshSpin();
    }
    
    /**
     * Removes all the selected GraphicalElement.
     */
    public void removeSelectedGE(){
        for(GraphicalElement ge : selectedGE){
            compositionAreaController.remove(ge);
            zIndexStack.remove(ge);
        }
        compositionAreaController.setZIndex(zIndexStack);
        selectedGE=new ArrayList<>();
        mainWindow.getCompositionArea().refresh();
    }

    /**
     * Modify the position and size of all the selected GraphicalElements
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
        compositionAreaController.modifyCompositionJPanel(ge);
        refreshSpin();
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
                        ((RefreshCA) dialogPropertiesCA).refresh(this);
                    ge.setAttribute(dialogPropertiesCA);
                }
            }
            //If the GraphicalElement was already added to the document
            if(compositionAreaController.isGEDrawn(ge))
                compositionAreaController.refreshGE(ge);
            //Set the CompositionAreaOverlay ratio in the case of the GraphicalElement was not already added to the Document
            else{
                //Give the ratio to the CompositionAreaOverlay();
                if(newGE instanceof SimpleIllustrationGE) {
                    try {
                        BufferedImage bi = ImageIO.read(new File(((SimpleIllustrationGE) newGE).getPath()));
                        mainWindow.getCompositionArea().getOverlay().setRatio((float) bi.getWidth() / bi.getHeight());
                    } catch (IOException e) {
                        LoggerFactory.getLogger(UIController.class).error(e.getMessage());
                    }
                }
            }
        }
        toBeSet = new ArrayList<>();
    }
    
    /**
     * Returns the list of ConfigurationAttributes that all the selected GraphicalElements have in common.
     * @return List of common ConfigurationAttributes (common about the names and not values).
     */
    private List<ConfigurationAttribute> getCommonAttributes(){
        List<ConfigurationAttribute> list = toBeSet.get(0).getAllAttributes();
        List<ConfigurationAttribute> listToRemove = new ArrayList<>();
        //Compare each the CA of the list to those of the GE from selectedGE
        boolean flag=false;
        for(ConfigurationAttribute caList : list){
            for(GraphicalElement ge : toBeSet){
                flag=false;
                for(ConfigurationAttribute caGE : ge.getAllAttributes()){
                    //refresh the attributes
                    if(caGE instanceof RefreshCA) ((RefreshCA)caGE).refresh(this);
                    if(caList instanceof RefreshCA) ((RefreshCA)caList).refresh(this);
                    //Compare them and if both represent the same property, lock the ConfigurationAttribute
                    if(caList.isSameName(caGE)){
                        flag=true;
                        caList.setReadOnly(!caList.isSameValue(caGE));
                    }
                }
            }
            //If the CA isn't in common, it's added to a list to be removed after
            if(!flag){
                listToRemove.add(caList);
            }
        }
        for(ConfigurationAttribute ca : listToRemove){
            list.remove(ca);
        }
        
        return list;
    }

    /**
     * Returns the list of all the GraphicalElements added to the document.
     * @return The list of GraphicalElements
     */
    public List<GraphicalElement> getGEList(){
        List list = new ArrayList<>();
        list.addAll(zIndexStack);
        return list;
    }

    /**
     * Remove all the displayed GE from the panel.
     */
    public void removeAllGE() {
        compositionAreaController.removeAll();
        selectedGE = new ArrayList<>();
        zIndexStack = new Stack<>();
    }

    public void removeGE(GraphicalElement ge) {
        compositionAreaController.remove(ge);
        selectedGE.remove(ge);
        zIndexStack.remove(ge);
    }

    /**
     * Add to the project the given GE (that is just loaded)..
     * @param ge GE to add to the project.
     */
    public void addGE(GraphicalElement ge) {
        compositionAreaController.add(ge);
        zIndexStack.push(ge);
        //Apply the z-index change to only the GraphicalElement ge.
        List temp = selectedGE;
        selectedGE = new ArrayList<>();
        selectedGE.add(ge);
        changeZIndex(ZIndex.TO_FRONT);
        selectedGE = temp;
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
            if( !(newGE instanceof GEProperties) && !isDocumentCreated()) {
                this.mainWindow.getCompositionArea().getOverlay().writeMessage("First create a new document or open an existing project.");
                return;
            }

            //Test if the newGE implements GEProperties and if it needs a Document GE and if there isn't a Document GE
            if( newGE instanceof GEProperties && ((GEProperties)newGE).isDocumentNeeded() && !isDocumentCreated()) {
                this.mainWindow.getCompositionArea().getOverlay().writeMessage("First create a new document or open an existing project.");
                return;
            }

            //Refresh the ConfigurationAttributes to initialize them
            for(ConfigurationAttribute ca : newGE.getAllAttributes())
                if(ca instanceof RefreshCA)
                    ((RefreshCA)ca).refresh(this);

            showGEProperties(newGE).addWindowListener(EventHandler.create(WindowListener.class, this, "drawGE", "", "windowClosed"));
        } catch (InstantiationException | IllegalAccessException ex) {
            LoggerFactory.getLogger(UIController.class).error(ex.getMessage());
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
                //If the newGE is a Document GE, then draw it immediately
                if(newGE instanceof GEProperties && !((GEProperties)newGE).isDrawnByUser()){
                    addGE(newGE);
                    compositionAreaController.refreshGE(newGE);
                    newGE=null;
                }
                else {
                    //If the image has an already set path value, get the image ratio
                    if (newGE instanceof SimpleIllustrationGE) {
                        File f = new File(((SimpleIllustrationGE) newGE).getPath());
                        if (f.exists() && f.isFile()) {
                            try {
                                BufferedImage bi = ImageIO.read(f);
                                mainWindow.getCompositionArea().getOverlay().setRatio((float) bi.getWidth() / bi.getHeight());
                            } catch (IOException e) {
                                LoggerFactory.getLogger(UIController.class).error(e.getMessage());
                            }
                        }
                    }
                    mainWindow.getCompositionArea().setOverlayMode(CompositionAreaOverlay.Mode.NEW_GE);
                    mainWindow.getCompositionArea().getOverlay().writeMessage("Now you can draw the GraphicalElement.");
                }

                List<GraphicalElement> list = new ArrayList<>();
                list.add(newGE);
                undoManager.addEdit(new UndoableEdit(UndoableEdit.EditType.ADD_GE, list, this));
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
            addGE(newGE);
            compositionAreaController.refreshGE(newGE);
        }
        mainWindow.getCompositionArea().setOverlayMode(CompositionAreaOverlay.Mode.NONE);
        newGE=null;
    }
    


    /**
     * Open a dialog window with all the ConfigurationAttributes common to the selected GraphicalElement.
     */
    public void showSelectedGEProperties(){
        if(selectedGE.size()>0){
            //Test if all the selected GE has the same class or not.
            boolean hasSameClass = true;
            for(GraphicalElement ge : selectedGE)
                if(ge.getClass()!=selectedGE.get(0).getClass())
                    hasSameClass=false;
            //If the only one GraphicalElement is selected, the locking checkboxes are hidden
            toBeSet.addAll(selectedGE);
            //Create and show the properties dialog.
            UIPanel panel = null;
            if(hasSameClass) {
                //Try to create an equivalent GE with the common attributes
                panel = geManager.getRenderer(selectedGE.get(0).getClass()).createConfigurationPanel(getCommonAttributes(), this, true);
            }
            if(panel==null)
                panel = new UIDialogProperties(getCommonAttributes(), this, true);
            SIFDialog dialog = UIFactory.getSimpleDialog(panel, mainWindow, true);
            dialog.setVisible(true);
            dialog.pack();
            dialog.setAlwaysOnTop(true);
        }
    }

    /**
     * Open and return a dialog window with all the ConfigurationAttributes from the given GraphicalElement.
     * @return The configuration dialog.
     */
    public SIFDialog showGEProperties(GraphicalElement ge){
        toBeSet.add(ge);
        //Create and show the properties dialog.
        UIPanel panel = geManager.getRenderer(ge.getClass()).createConfigurationPanel(ge.getAllAttributes(), this, false);
        if(panel==null)
            panel = new UIDialogProperties(ge.getAllAttributes(), this, false);
        SIFDialog dialog = UIFactory.getSimpleDialog(panel, mainWindow, true);
        dialog.setVisible(true);
        dialog.pack();
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(mainWindow);
        return dialog;
    }
    
    /**
     * Open a dialog window with all the document properties.
     */
    public void showDocProperties(){
        Document doc=null;
        for(GraphicalElement ge : zIndexStack)
            if(ge instanceof Document){
                doc=(Document)ge;
                break;
            }
        
        if(doc!=null){
            for(GraphicalElement graph : selectedGE)
                unselectGE(graph);
            toBeSet.add(doc);
            //Create and show the properties dialog.
            UIPanel panel = new UIDialogProperties(getCommonAttributes(), this, false);
            SIFDialog dialog = UIFactory.getSimpleDialog(panel, mainWindow, true);
            dialog.setVisible(true);
            dialog.pack();
            dialog.setAlwaysOnTop(true);
        }
    }

    @Override
    public void storeState(Hashtable state) {
        state.put("MW", mainWindow);
        System.out.println("store "+state.size());
    }

    @Override
    public void restoreState(Hashtable state) {
        System.out.println("restore "+state.size());
        //mainWindow = (MainWindow) hashtable.get("MW");
        //mainWindow.getCompositionArea().refresh();
    }

    public enum ZIndex{
        TO_FRONT, FRONT, BACK, TO_BACK;
    }

    public enum Align{
        LEFT, CENTER, RIGHT, TOP, MIDDLE, BOTTOM;
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

    public GEManager getGEManager(){
        return geManager;
    }

    public List<GraphicalElement> getSelectedGE(){
        return selectedGE;
    }

    public CompositionAreaController getCompositionAreaController(){
        return compositionAreaController;
    }
    public IOController getIOController(){
        return ioController;
    }

    public static class UndoableEdit extends AbstractUndoableEdit{

        public enum EditType {ADD_GE};

        private EditType editType;
        private List<GraphicalElement> listGE;
        private UIController uic;

        public UndoableEdit(EditType editType, List<GraphicalElement> listGE, UIController uic){
            this.editType = editType;
            this.listGE = listGE;
            this.uic = uic;
        }

        @Override
        public void redo() throws CannotRedoException {
            super.redo();
            for(GraphicalElement ge : listGE)
                uic.addGE(ge);
            uic.getMainWindow().getCompositionArea().refresh();
        }

        @Override
        public void undo() throws CannotUndoException {
            super.undo();
            for(GraphicalElement ge : listGE)
                uic.removeGE(ge);
            uic.getMainWindow().getCompositionArea().refresh();
        }
    }

    public void refreshSelectedGE() {
        compositionAreaController.refreshGE(selectedGE);
    }
}
