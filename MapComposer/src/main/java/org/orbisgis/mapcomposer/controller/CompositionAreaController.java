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
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GERefresh;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.view.ui.CompositionArea;
import org.orbisgis.mapcomposer.view.utils.CompositionJPanel;
import org.orbisgis.mapcomposer.view.utils.RenderWorker;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * @author Sylvain PALOMINOS
 */

public class CompositionAreaController {

    /** Executor used for the RenderWorkers. */
    private ExecutorService executorService;

    /**Map doing the link between GraphicalElements and their CompositionJPanel*/
    private HashMap<GraphicalElement, CompositionJPanel> elementJPanelMap;

    UIController uic;
    
    CompositionArea compositionArea;

    public CompositionAreaController(UIController uic, CompositionArea compositionArea){
        this.uic = uic;
        executorService = Executors.newFixedThreadPool(1);
        elementJPanelMap = new LinkedHashMap<>();
        this.compositionArea = compositionArea;
    }
    
    public RenderWorker refreshAllGE(){
        RenderWorker lastRenderWorker = null;
        executorService = Executors.newFixedThreadPool(1);
        for(GraphicalElement ge : elementJPanelMap.keySet()){
            RenderWorker rw = new RenderWorker(elementJPanelMap.get(ge), uic.getGEManager().getRenderer(ge.getClass()), ge);
            executorService.submit(rw);
            lastRenderWorker = rw;
        }
        return lastRenderWorker;
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
     * It does the refresh of the GE, the actualization of the GE representation in the CompositionArea and refreshes the spinner state.
     * The GE representation is completely re-renderer.
     * @param ge GraphicalElement to validate
     */
    public void refreshGE(GraphicalElement ge){
        if(ge instanceof GERefresh)
            ((GERefresh)ge).refresh();
        uic.unselectGE(ge);
        RenderWorker worker = new RenderWorker(elementJPanelMap.get(ge), uic.getGEManager().getRenderer(ge.getClass()), ge);
        executorService.submit(worker);
        if(ge instanceof Document)
            compositionArea.setDocumentDimension(new Dimension(ge.getWidth(), ge.getHeight()));
    }

    public void setZIndex(List<GraphicalElement> listGE){
        for(GraphicalElement ge : listGE){
            compositionArea.setZIndex(elementJPanelMap.get(ge), listGE.indexOf(ge));
        }
    }

    public void remove(GraphicalElement ge){
        compositionArea.remove(elementJPanelMap.get(ge));
        elementJPanelMap.remove(ge);
    }

    public void remove(List<GraphicalElement> listGE){
        for(GraphicalElement ge : listGE) {
            remove(ge);
        }
    }

    public void removeAll(){
        for(GraphicalElement ge : elementJPanelMap.keySet()) {
            compositionArea.remove(elementJPanelMap.get(ge));
        }
        elementJPanelMap = new HashMap<>();
    }

    public void add(GraphicalElement ge){
        elementJPanelMap.put(ge, new CompositionJPanel(ge, uic));
        compositionArea.add(elementJPanelMap.get(ge));
        if(ge instanceof GERefresh){
            ((GERefresh)ge).refresh();
        }
        RenderWorker worker = new RenderWorker(elementJPanelMap.get(ge), uic.getGEManager().getRenderer(ge.getClass()), ge);
        executorService.submit(worker);
    }

    public void setDocumentDimension(Document document){
        compositionArea.setDocumentDimension(document.getDimension());
    }
    
    public void selectGE(GraphicalElement ge){
        elementJPanelMap.get(ge).select();
    }
    
    public void unselectGE(GraphicalElement ge){
        elementJPanelMap.get(ge).unselect();
    }
    
    public void unselectAllGE(){
        for(GraphicalElement ge : elementJPanelMap.keySet())
            elementJPanelMap.get(ge).select();
    }
    
    public void modifyCompositionJPanel(GraphicalElement ge){
        elementJPanelMap.get(ge).modify(ge.getX(), ge.getY(), ge.getWidth(), ge.getHeight(), ge.getRotation());
        if(ge instanceof Document)
            setDocumentDimension((Document)ge);
    }
    
    public boolean isGEDrawn(GraphicalElement ge){
        return elementJPanelMap.containsKey(ge);
    }

    public void setAlign( UIController.Align alignment) {
        if(uic.getSelectedGE().size()>0){
            int xMin;
            int xMax;
            int yMin;
            int yMax;
            switch(alignment){
                case LEFT:
                    //Obtain the minimum x position of all the CompositionJPanel from the CompositionArea
                    //The x position get take into account the rotation of the GraphicalElement.
                    xMin=elementJPanelMap.get(uic.getSelectedGE().get(0)).getX();
                    for (GraphicalElement ge : uic.getSelectedGE())
                        if (elementJPanelMap.get(ge).getX() < xMin)
                            xMin = elementJPanelMap.get(ge).getX();
                    //Set all the GraphicalElement x position to the one get before
                    for(GraphicalElement ge : uic.getSelectedGE()){
                        //Convert the x position to the new one of the GraphicalElement taking into account its rotation angle.
                        double rad = Math.toRadians(ge.getRotation());
                        double newWidth = Math.floor(Math.abs(sin(rad) * ge.getHeight()) + Math.abs(cos(rad) * ge.getWidth()));
                        ge.setX(xMin-(ge.getWidth()-(int)newWidth)/2);
                    }
                    break;
                case CENTER:
                    xMin=uic.getSelectedGE().get(0).getX();
                    xMax=uic.getSelectedGE().get(0).getX()+uic.getSelectedGE().get(0).getWidth();
                    for (GraphicalElement ge : uic.getSelectedGE()) {
                        if (ge.getX() < xMin)
                            xMin = ge.getX();
                        if (ge.getX()+ge.getWidth() > xMax)
                            xMax = ge.getX()+ge.getWidth();
                    }
                    int xMid = (xMax+xMin)/2;
                    for(GraphicalElement ge : uic.getSelectedGE())
                        ge.setX(xMid-ge.getWidth()/2);
                    break;
                case RIGHT:
                    //Obtain the maximum x position of all the CompositionJPanel from the CompositionArea
                    //The x position get take into account the rotation of the GraphicalElement.
                    xMax=elementJPanelMap.get(uic.getSelectedGE().get(0)).getX()+elementJPanelMap.get(uic.getSelectedGE().get(0)).getWidth();
                    for (GraphicalElement ge : uic.getSelectedGE())
                        if (elementJPanelMap.get(ge).getX()+elementJPanelMap.get(ge).getWidth() > xMax)
                            xMax = elementJPanelMap.get(ge).getX()+elementJPanelMap.get(ge).getWidth();
                    //Takes into account the border width of the ConfigurationJPanel (2 pixels)
                    xMax-=2;
                    //Set all the GraphicalElement x position to the one get before
                    for(GraphicalElement ge : uic.getSelectedGE()) {
                        //Convert the x position to the new one of the GraphicalElement taking into account its rotation angle.
                        double rad = Math.toRadians(ge.getRotation());
                        double newWidth = Math.ceil(Math.abs(sin(rad) * ge.getHeight()) + Math.abs(cos(rad) * ge.getWidth()));
                        ge.setX(xMax-ge.getWidth()+(ge.getWidth()-(int)newWidth)/2);
                    }
                    break;
                case TOP:
                    //Obtain the minimum y position of all the CompositionJPanel from the CompositionArea
                    //The y position get take into account the rotation of the GraphicalElement.
                    yMin=elementJPanelMap.get(uic.getSelectedGE().get(0)).getY();
                    for (GraphicalElement ge : uic.getSelectedGE())
                        if (ge.getY() < yMin)
                            yMin = elementJPanelMap.get(ge).getY();
                    //Set all the GraphicalElement y position to the one get before
                    for(GraphicalElement ge : uic.getSelectedGE()) {
                        //Convert the y position to the new one of the GraphicalElement taking into account its rotation angle.
                        double rad = Math.toRadians(ge.getRotation());
                        double newHeight = Math.floor(Math.abs(sin(rad) * ge.getWidth()) + Math.abs(cos(rad) * ge.getHeight()));
                        ge.setY(yMin - (ge.getHeight() - (int) newHeight) / 2);
                    }
                    break;
                case MIDDLE:
                    yMin=uic.getSelectedGE().get(0).getY();
                    yMax=uic.getSelectedGE().get(0).getY()+uic.getSelectedGE().get(0).getHeight();
                    for (GraphicalElement ge : uic.getSelectedGE()) {
                        if (ge.getY() < yMin)
                            yMin = ge.getY();
                        if (ge.getY()+ge.getHeight() > yMax)
                            yMax = ge.getY()+ge.getHeight();
                    }
                    int yMid = (yMax+yMin)/2;
                    for(GraphicalElement ge : uic.getSelectedGE())
                        ge.setY(yMid-ge.getHeight()/2);
                    break;
                case BOTTOM:
                    //Obtain the maximum y position of all the CompositionJPanel from the CompositionArea
                    //The y position get take into account the rotation of the GraphicalElement.
                    yMax=elementJPanelMap.get(uic.getSelectedGE().get(0)).getY()+elementJPanelMap.get(uic.getSelectedGE().get(0)).getHeight();
                    for (GraphicalElement ge : uic.getSelectedGE())
                        if (elementJPanelMap.get(ge).getY()+elementJPanelMap.get(ge).getHeight() > yMax)
                            yMax = elementJPanelMap.get(ge).getY()+elementJPanelMap.get(ge).getHeight();
                    //Takes into account the border width ConfigurationJPanel (2 pixels)
                    yMax-=2;
                    //Set all the GraphicalElement y position to the one get before
                    for(GraphicalElement ge : uic.getSelectedGE()) {
                        //Convert the y position to the new one of the GraphicalElement taking into account its rotation angle.
                        double rad = Math.toRadians(ge.getRotation());
                        double newHeight = Math.ceil(Math.abs(sin(rad)*ge.getWidth())+Math.abs(cos(rad)*ge.getHeight()));
                        ge.setY(yMax - ge.getHeight() + (ge.getHeight()-(int)newHeight)/2);
                    }
                    break;
            }
            uic.modifySelectedGE();
        }
    }

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
}
