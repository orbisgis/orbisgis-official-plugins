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

import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GEProperties;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GERefresh;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.view.ui.CompositionArea;
import org.orbisgis.mapcomposer.view.utils.CompositionAreaOverlay;
import org.orbisgis.mapcomposer.view.utils.CompositionJPanel;
import org.orbisgis.mapcomposer.view.utils.RenderWorker;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.*;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * This controller is in charge of all the interactions with the CompositionArea
 *
 * @author Sylvain PALOMINOS
 */

public class CompositionAreaController {

    public enum ZIndex{ TO_FRONT, FRONT, BACK, TO_BACK; }
    public enum Align{ LEFT, CENTER, RIGHT, TOP, MIDDLE, BOTTOM; }

    /**GraphicalElement stack giving the Z-index information*/
    private Stack<GraphicalElement> zIndexStack;

    /** Executor used for the RenderWorkers. */
    private ExecutorService executorService;

    /**Map doing the link between GraphicalElements and their CompositionJPanel*/
    private HashMap<GraphicalElement, CompositionJPanel> elementJPanelMap;

    /** MainController */
    private MainController mainController;

    /** CompositionArea */
    private CompositionArea compositionArea;

    public CompositionAreaController(MainController mainController){
        this.mainController = mainController;
        executorService = Executors.newFixedThreadPool(1);
        elementJPanelMap = new LinkedHashMap<>();
        zIndexStack = new Stack<>();
    }

    /**
     * Adds a GraphicalElement to the CompositionArea.
     * @param ge GraphicalElement to add.
     */
    public void add(GraphicalElement ge){
        zIndexStack.push(ge);
        elementJPanelMap.put(ge, new CompositionJPanel(ge, mainController));
        compositionArea.addCompositionJPanel(elementJPanelMap.get(ge));
        if(ge instanceof GERefresh){
            ((GERefresh)ge).refresh();
        }
        RenderWorker worker = new RenderWorker(elementJPanelMap.get(ge), mainController.getGEManager().getRenderer(ge.getClass()), ge);
        executorService.submit(worker);
    }

    /**
     * Removes from the CompositionArea a GraphicalElement.
     * @param ge GraphicalElement to remove.
     */
    public void remove(GraphicalElement ge){
        compositionArea.removeGE(elementJPanelMap.get(ge));
        elementJPanelMap.remove(ge);
        zIndexStack.remove(ge);
        compositionArea.refresh();
    }

    /**
     * Removes a list of GraphicalElements from the CompositionArea.
     * @param listGE
     */
    public void remove(List<GraphicalElement> listGE){
        for(GraphicalElement ge : listGE) {
            remove(ge);
        }
        setZIndex(zIndexStack);
    }

    /**
     * Removes all the graphicalElement from the CompositionArea.
     */
    public void removeAll(){
        compositionArea.removeAllGE();
        elementJPanelMap = new HashMap<>();
        zIndexStack = new Stack<>();
    }

    /**
     * Change the Z-index of the displayed GraphicalElement.
     * @param z Change of the Z-index.
     */
    public void changeZIndex(ZIndex z){
        List<GraphicalElement> selectedGE = mainController.getGEController().getSelectedGE();

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
        setZIndex(zIndexStack);
        mainController.getGEController().modifySelectedGE();
    }

    /**
     * Return the BufferedImage of the Document.
     * @return The BufferedImage of the Document.
     */
    public BufferedImage getCompositionAreaBufferedImage(){
        for(GraphicalElement ge : elementJPanelMap.keySet()){
            elementJPanelMap.get(ge).enableBorders(false);
        }
        BufferedImage bi = compositionArea.getDocBufferedImage();

        for(GraphicalElement ge : elementJPanelMap.keySet()){
            elementJPanelMap.get(ge).enableBorders(true);
        }

        return bi;
    }

    /**
     * Returns true if the given GraphicalElement is already drawn in the CompositionArea, false otherwise.
     * @param ge GraphicalElement to test.
     * @return True if drawn in the CompositionArea, false otherwise.
     */
    public boolean isGEDrawn(GraphicalElement ge){
        return elementJPanelMap.containsKey(ge);
    }

    /**
     * Modifies the representation of a GraphicalElement without redrawing it.
     * Moves and resize the CompositionJPanel and stretch the displayed image to the new panel dimension.
     * @param ge GraphicalElement to modify.
     */
    public void modifyCompositionJPanel(GraphicalElement ge){
        elementJPanelMap.get(ge).refresh(null);
        if(ge instanceof Document)
            setDocumentDimension((Document) ge);
    }

    /**
     * Refreshes and redraws all the GraphicalElements displayed into the CompositionArea.
     * @return The last RenderWorker that will be executed.
     */
    public RenderWorker refreshAllGE(){
        RenderWorker lastRenderWorker = null;
        executorService = Executors.newFixedThreadPool(1);
        for(GraphicalElement ge : elementJPanelMap.keySet()){
            RenderWorker rw = new RenderWorker(elementJPanelMap.get(ge), mainController.getGEManager().getRenderer(ge.getClass()), ge);
            executorService.submit(rw);
            lastRenderWorker = rw;
        }
        return lastRenderWorker;
    }

    /**
     * Like the RefreshAllGE method but without doing any rendering.
     */
    public void actuAllGE(){
        for(GraphicalElement ge : elementJPanelMap.keySet()){
            modifyCompositionJPanel(ge);
        }
    }

    /**
     * Redraws all the selected GraphicalElements
     */
    public void refreshGE(List<GraphicalElement> listGE){
        //Copy of the list to avoid ConcurrentModificationException
        List<GraphicalElement> list = new ArrayList<>();
        for(GraphicalElement ge : listGE)
            list.add(ge);
        for(GraphicalElement ge : list)
            refreshGE(ge);
    }

    /**
     * Redraws the given GraphicalElement.
     * It does the refresh of the GE and the actualization of the GE representation in the CompositionArea.
     * The GE representation is completely re-renderer.
     * @param ge GraphicalElement to validate
     */
    public void refreshGE(GraphicalElement ge){
        if(ge instanceof GERefresh)
            ((GERefresh)ge).refresh();
        RenderWorker worker = new RenderWorker(elementJPanelMap.get(ge), mainController.getGEManager().getRenderer(ge.getClass()), ge);
        executorService.submit(worker);
        if(ge instanceof Document)
            compositionArea.setDocumentDimension(new Dimension(ge.getWidth(), ge.getHeight()));
    }

    /**
     * Refreshes and redraws the selected GraphicalElements.
     */
    public void refreshSelectedGE() {
        refreshGE(mainController.getGEController().getSelectedGE());
        mainController.unselectAllGE();
    }

    /**
     * Align all the GraphicalElement contained by the selectedGE list.
     * @param alignment Alignment to apply.
     */
    public void setAlign(Align alignment) {
        List<GraphicalElement> selectedGE = mainController.getGEController().getSelectedGE();
        int xMin, xMax, yMin, yMax;
        boolean alignWithDoc = false;
        if(selectedGE.size() == 0)
            return;
        //If ony one GE is selected, it will ve align with the document
        if(selectedGE.size() == 1) {
            xMin = compositionArea.getDocumentBounds().x;
            xMax = compositionArea.getDocumentBounds().x + compositionArea.getDocumentBounds().width;
            yMin = compositionArea.getDocumentBounds().y;
            yMax = compositionArea.getDocumentBounds().y + compositionArea.getDocumentBounds().height;
            alignWithDoc = true;
        }
        //If more than one GE is selected, align them between themselves
        else{
            xMin = elementJPanelMap.get(selectedGE.get(0)).getX();
            xMax = elementJPanelMap.get(selectedGE.get(0)).getX() + elementJPanelMap.get(selectedGE.get(0)).getWidth();
            yMin = elementJPanelMap.get(selectedGE.get(0)).getY();
            yMax = elementJPanelMap.get(selectedGE.get(0)).getY() + elementJPanelMap.get(selectedGE.get(0)).getHeight();
        }
        switch(alignment){
            case LEFT:
                //Obtain the minimum x position of all the CompositionJPanel from the CompositionArea
                //The x position get take into account the rotation of the GraphicalElement.
                if(!alignWithDoc)
                    for (GraphicalElement ge : selectedGE)
                        if (elementJPanelMap.get(ge).getX() < xMin)
                            xMin = elementJPanelMap.get(ge).getX();
                //Set all the GraphicalElement x position to the one get before
                for(GraphicalElement ge : selectedGE){
                    //Convert the x position to the new one of the GraphicalElement taking into account its rotation angle.
                    double rad = Math.toRadians(ge.getRotation());
                    double newWidth = Math.floor(Math.abs(sin(rad) * ge.getHeight()) + Math.abs(cos(rad) * ge.getWidth()));
                    ge.setX(compositionArea.overlayPointToDocumentPoint(new Point(xMin - (ge.getWidth() - (int) newWidth) / 2, 0)).x);
                }
                break;
            case CENTER:
                if(!alignWithDoc)
                    for (GraphicalElement ge : selectedGE) {
                        if (ge.getX() < xMin)
                            xMin = ge.getX();
                        if (ge.getX()+ge.getWidth() > xMax)
                            xMax = ge.getX()+ge.getWidth();
                    }
                int xMid = (xMax+xMin)/2;
                for(GraphicalElement ge : selectedGE)
                    ge.setX(compositionArea.overlayPointToDocumentPoint(new Point(xMid - elementJPanelMap.get(ge).getWidth()/2, 0)).x);
                break;
            case RIGHT:
                //Obtain the maximum x position of all the CompositionJPanel from the CompositionArea
                //The x position get take into account the rotation of the GraphicalElement.
                if(!alignWithDoc)
                    for (GraphicalElement ge : selectedGE)
                        if (elementJPanelMap.get(ge).getX()+elementJPanelMap.get(ge).getWidth() > xMax)
                            xMax = elementJPanelMap.get(ge).getX()+elementJPanelMap.get(ge).getWidth();
                //Takes into account the border width of the ConfigurationJPanel (2 pixels)
                xMax-=2;
                //Set all the GraphicalElement x position to the one get before
                for(GraphicalElement ge : selectedGE) {
                    //Convert the x position to the new one of the GraphicalElement taking into account its rotation angle.
                    double rad = Math.toRadians(ge.getRotation());
                    double newWidth = Math.ceil(Math.abs(sin(rad) * ge.getHeight()) + Math.abs(cos(rad) * ge.getWidth()));
                    ge.setX(compositionArea.overlayPointToDocumentPoint(new Point(xMax - ge.getWidth() + (ge.getWidth() - (int) newWidth) / 2, 0)).x);
                }
                break;
            case TOP:
                //Obtain the minimum y position of all the CompositionJPanel from the CompositionArea
                //The y position get take into account the rotation of the GraphicalElement.
                if(!alignWithDoc)
                    for (GraphicalElement ge : selectedGE)
                        if (ge.getY() < yMin)
                            yMin = elementJPanelMap.get(ge).getY();
                //Set all the GraphicalElement y position to the one get before
                for(GraphicalElement ge : selectedGE) {
                    //Convert the y position to the new one of the GraphicalElement taking into account its rotation angle.
                    double rad = Math.toRadians(ge.getRotation());
                    double newHeight = Math.floor(Math.abs(sin(rad) * ge.getWidth()) + Math.abs(cos(rad) * ge.getHeight()));
                    ge.setY(compositionArea.overlayPointToDocumentPoint(new Point(0, yMin - (ge.getHeight() - (int) newHeight) / 2)).y);
                }
                break;
            case MIDDLE:
                if(!alignWithDoc)
                    for (GraphicalElement ge : selectedGE) {
                        if (ge.getY() < yMin)
                            yMin = ge.getY();
                        if (ge.getY()+ge.getHeight() > yMax)
                            yMax = ge.getY()+ge.getHeight();
                    }
                int yMid = (yMax+yMin)/2;
                for(GraphicalElement ge : selectedGE)
                    ge.setY(compositionArea.overlayPointToDocumentPoint(new Point (0, yMid - elementJPanelMap.get(ge).getHeight()/2)).y);
                break;
            case BOTTOM:
                //Obtain the maximum y position of all the CompositionJPanel from the CompositionArea
                //The y position get take into account the rotation of the GraphicalElement.
                if(!alignWithDoc)
                    for (GraphicalElement ge : selectedGE)
                        if (elementJPanelMap.get(ge).getY()+elementJPanelMap.get(ge).getHeight() > yMax)
                            yMax = elementJPanelMap.get(ge).getY()+elementJPanelMap.get(ge).getHeight();
                //Takes into account the border width ConfigurationJPanel (2 pixels)
                yMax-=2;
                //Set all the GraphicalElement y position to the one get before
                for(GraphicalElement ge : selectedGE) {
                    //Convert the y position to the new one of the GraphicalElement taking into account its rotation angle.
                    double rad = Math.toRadians(ge.getRotation());
                    double newHeight = Math.ceil(Math.abs(sin(rad)*ge.getWidth())+Math.abs(cos(rad)*ge.getHeight()));
                    ge.setY(compositionArea.overlayPointToDocumentPoint(new Point(0, yMax - ge.getHeight() + (ge.getHeight() - (int) newHeight) / 2)).y);
                }
                break;
        }
        mainController.getGEController().modifySelectedGE();
    }

    /**
     * Sets the z index of CompositionJPanels corresponding to the GraphicalElements given in argument.
     * With the position of the GraphicalElements in the given List, sets their z index inside of the CompositionArea.
     * @param listGE List of GraphicalElement that the z index was modified.
     */
    public void setZIndex(List<GraphicalElement> listGE){
        zIndexStack = new Stack<>();
        zIndexStack.addAll(listGE);
        for(GraphicalElement ge : listGE){
            ge.setZ(listGE.indexOf(ge));
            compositionArea.setZIndex(elementJPanelMap.get(ge), listGE.indexOf(ge));
        }
    }

    /**
     * Sets the CompositionArea to control.
     * @param compositionArea
     */
    public void setCompositionArea(CompositionArea compositionArea){
        this.compositionArea = compositionArea;
    }

    /**
     * Sets the new size of the document representation in the CompositionArea.
     * @param document Document represented in the CompositionArea.
     */
    public void setDocumentDimension(Document document){
        compositionArea.setDocumentDimension(document.getDimension());
    }

    /**
     * Sets the ratio to respect on resizing or creating an element in the CompositionArea.
     * @param ratio
     */
    public void setOverlayRatio(float ratio){
        compositionArea.getOverlay().setRatio(ratio);
    }

    /**
     * Sets the mode (None, New GE, Resize GE) of the CompositionAreaOverlay.
     * @param mode
     */
    public void setOverlayMode(CompositionAreaOverlay.Mode mode){
        compositionArea.getOverlay().setMode(mode);
    }

    /**
     * Sets the message displayed by the CompositionAreaOverlay.
     * @param message
     */
    public void setOverlayMessage(String message){
        compositionArea.getOverlay().writeMessage(message);
    }

    /**
     * Select a GraphicalElement in the CompositionArea (make its borders orange).
     * @param ge GraphicalElement to select.
     */
    public void selectGE(GraphicalElement ge){
        elementJPanelMap.get(ge).select();
    }

    /**
     * Select a GraphicalElement in the CompositionArea (make its borders orange).
     * @param ge GraphicalElement to unselect.
     */
    public void unselectGE(GraphicalElement ge){
        elementJPanelMap.get(ge).unselect();
    }

    /**
     * Unselects all the GraphicalElements.
     */
    public void unselectAllGE(){
        for(GraphicalElement ge : elementJPanelMap.keySet())
            elementJPanelMap.get(ge).unselect();
    }
}
