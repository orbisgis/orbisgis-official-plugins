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
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.Scale;
import org.orbisgis.mapcomposer.model.graphicalelement.element.illustration.Image;
import org.orbisgis.mapcomposer.model.graphicalelement.element.shape.OvalGE;
import org.orbisgis.mapcomposer.model.graphicalelement.element.shape.RectangleGE;
import org.orbisgis.mapcomposer.model.graphicalelement.element.text.TextElement;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.view.graphicalelement.CustomConfigurationPanel;
import org.orbisgis.mapcomposer.view.utils.CompositionAreaOverlay;
import org.orbisgis.mapcomposer.view.utils.UIDialogProperties;
import org.orbisgis.sif.SIFDialog;
import org.orbisgis.sif.UIFactory;
import org.orbisgis.sif.UIPanel;
import org.orbisgis.sif.multiInputPanel.MultiInputPanel;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.List;

import static org.orbisgis.mapcomposer.controller.CompositionAreaController.Align.*;
import static org.orbisgis.mapcomposer.controller.CompositionAreaController.ZIndex.*;

/**
 * This class get all the user interaction with the MainWindow.
 *
 * @author Sylvain PALOMINOS
 */

public class UIController {

    private MainController mainController;

    /** Object for the translation*/
    private static final I18n i18n = I18nFactory.getI18n(UIController.class);

    public UIController(MainController mainController){
        this.mainController = mainController;
    }


    public void createDocument(){
        //If the document already contain GraphicalElement, as before removing them
        if(!mainController.getGEList().isEmpty()){
            MultiInputPanel panel = new MultiInputPanel(i18n.tr("New document"));
            panel.addText(i18n.tr("Are you sure to create a new Document ?"));
            panel.addText(i18n.tr("Unsaved changes will be lost."));
            SIFDialog dialog = UIFactory.getSimpleDialog(panel, mainController.getMainWindow(), true);
            dialog.setVisible(true);
            dialog.pack();
            dialog.setAlwaysOnTop(true);
            dialog.setLocationRelativeTo(mainController.getMainWindow());
            dialog.addWindowListener(EventHandler.create(WindowListener.class, this, "instantiateDocument", "", "windowClosed"));
        }
        else{
            instantiateDocument(null);
        }
    }

    public void instantiateDocument(WindowEvent winEvent){
        if(winEvent != null && winEvent.getSource() instanceof SIFDialog) {
            if(!((SIFDialog)winEvent.getSource()).isAccepted()){
                return;
            }
        }
        mainController.removeAllGE();
        mainController.getMainWindow().repaint();
        mainController.getGEController().instantiateNewGE(Document.class);
    }

    public void createMap(){
        mainController.getCompositionAreaController().setOverlayRatio(-1);
        mainController.getGEController().instantiateNewGE(MapImage.class);
        mainController.getCompositionAreaController().setOverlayMode(CompositionAreaOverlay.Mode.NONE);
    }

    public void createText(){
        mainController.getCompositionAreaController().setOverlayRatio(-1);
        mainController.getGEController().instantiateNewGE(TextElement.class);
        mainController.getCompositionAreaController().setOverlayMode(CompositionAreaOverlay.Mode.NONE);
    }

    public void createLegend(){
        mainController.getCompositionAreaController().setOverlayMessage(i18n.tr("Action not supported yet."));
    }

    public void createOrientation(){
        mainController.getCompositionAreaController().setOverlayRatio(-1);
        mainController.getGEController().instantiateNewGE(Orientation.class);
        mainController.getCompositionAreaController().setOverlayMode(CompositionAreaOverlay.Mode.NONE);
    }

    public void createScale(){
        mainController.getCompositionAreaController().setOverlayRatio(-1);
        mainController.getGEController().instantiateNewGE(Scale.class);
        mainController.getCompositionAreaController().setOverlayMode(CompositionAreaOverlay.Mode.NONE);
    }

    public void createPicture(){
        mainController.getCompositionAreaController().setOverlayRatio(-1);
        mainController.getGEController().instantiateNewGE(Image.class);
        mainController.getCompositionAreaController().setOverlayMode(CompositionAreaOverlay.Mode.NONE);

    }

    public void createCircle(){
        mainController.getCompositionAreaController().setOverlayRatio(1);
        mainController.getGEController().instantiateNewGE(OvalGE.class);
        mainController.getCompositionAreaController().setOverlayMode(CompositionAreaOverlay.Mode.NONE);
    }

    public void createPolygon(){
        mainController.getCompositionAreaController().setOverlayRatio(1);
        mainController.getGEController().instantiateNewGE(RectangleGE.class);
        mainController.getCompositionAreaController().setOverlayMode(CompositionAreaOverlay.Mode.NONE);
    }

    public void alignToLeft(){
        mainController.setSelectedGEAlignment(LEFT);
    }
    public void alignToCenter(){
        mainController.setSelectedGEAlignment(CENTER);
    }
    public void alignToRight(){
        mainController.setSelectedGEAlignment(RIGHT);
    }
    public void alignToBottom(){
        mainController.setSelectedGEAlignment(BOTTOM);
    }
    public void alignToMiddle(){
        mainController.setSelectedGEAlignment(MIDDLE);
    }
    public void alignToTop(){
        mainController.setSelectedGEAlignment(TOP);
    }


    public void moveBack(){
        mainController.setSelectedGEZIndex(TO_BACK);
    }
    public void moveDown(){
        mainController.setSelectedGEZIndex(BACK);
    }
    public void moveOn(){
        mainController.setSelectedGEZIndex(FRONT);
    }
    public void moveFront(){
        mainController.setSelectedGEZIndex(TO_FRONT);
    }

    /**
     * Open a dialog window with all the ConfigurationAttributes common to the selected GraphicalElement.
     */
    public void showSelectedGEProperties(){
        List<GraphicalElement> selectedGE = mainController.getGEController().getSelectedGE();
        if(selectedGE.size()>0){
            //Test if all the selected GE has the same class or not.
            boolean hasSameClass = true;
            for(GraphicalElement ge : selectedGE)
                if(ge.getClass()!=selectedGE.get(0).getClass())
                    hasSameClass=false;
            if(hasSameClass) {
                //If the only one GraphicalElement is selected, the locking checkboxes are hidden
                List<GraphicalElement> toBeSet = new ArrayList<>();
                toBeSet.addAll(selectedGE);
                mainController.getGEController().setToBeSetList(toBeSet);
                //Create and show the properties dialog.
                UIPanel panel;
                if (mainController.getGEManager().getRenderer(selectedGE.get(0).getClass()) instanceof CustomConfigurationPanel) {
                    //Try to create an equivalent GE with the common attributes
                    panel = ((CustomConfigurationPanel) mainController.getGEManager().getRenderer(selectedGE.get(0).getClass())).createConfigurationPanel(getCommonAttributes(toBeSet), mainController, true);
                } else
                    panel = new UIDialogProperties(getCommonAttributes(toBeSet), mainController, true);
                SIFDialog dialog = UIFactory.getSimpleDialog(panel, mainController.getMainWindow(), true);
                dialog.setVisible(true);
                dialog.pack();
                dialog.setAlwaysOnTop(true);
            }
        }
    }

    /**
     * Open and return a dialog window with all the ConfigurationAttributes from the given GraphicalElement.
     * @return The configuration dialog.
     */
    public SIFDialog showGEProperties(GraphicalElement ge){
        List<GraphicalElement> toBeSet = new ArrayList<>();
        toBeSet.add(ge);
        mainController.getGEController().setToBeSetList(toBeSet);
        //Create and show the properties dialog.
        UIPanel panel = new UIDialogProperties(ge.getAllAttributes(), mainController, false);
        if(mainController.getGEManager().getRenderer(ge.getClass()) instanceof CustomConfigurationPanel)
            panel = ((CustomConfigurationPanel)mainController.getGEManager().getRenderer(ge.getClass())).createConfigurationPanel(ge.deepCopy().getAllAttributes(), mainController, false);
        SIFDialog dialog = UIFactory.getSimpleDialog(panel, mainController.getMainWindow(), true);
        dialog.setVisible(true);
        dialog.pack();
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(mainController.getMainWindow());
        return dialog;
    }

    /**
     * Open a dialog window with all the document properties.
     */
    public void showDocProperties(){
        Document doc=null;
        for(GraphicalElement ge : mainController.getGEList())
            if(ge instanceof Document){
                doc=(Document)ge;
                break;
            }

        if(doc!=null){
            List<GraphicalElement> toBeSet = new ArrayList<>();
            for(GraphicalElement graph : mainController.getGEController().getSelectedGE())
                mainController.unselectGE(graph);
            toBeSet.add(doc);
            mainController.getGEController().setToBeSetList(toBeSet);
            //Create and show the properties dialog.
            UIPanel panel = ((CustomConfigurationPanel)mainController.getGEManager().getRenderer(doc.getClass())).createConfigurationPanel(doc.deepCopy().getAllAttributes(), mainController, false);
            SIFDialog dialog = UIFactory.getSimpleDialog(panel, mainController.getMainWindow(), true);
            dialog.setVisible(true);
            dialog.pack();
            dialog.setAlwaysOnTop(true);
            dialog.setLocationRelativeTo(mainController.getMainWindow());
        }
    }

    /**
     * Returns the list of ConfigurationAttributes that all the selected GraphicalElements have in common.
     * @return List of common ConfigurationAttributes (common about the names and not values).
     */
    private List<ConfigurationAttribute> getCommonAttributes(List<GraphicalElement> listGE){
        List<ConfigurationAttribute> list = listGE.get(0).deepCopy().getAllAttributes();
        List<ConfigurationAttribute> listToRemove = new ArrayList<>();
        //Compare each the CA of the list to those of the GE from selectedGE
        boolean flag=false;
        for(ConfigurationAttribute caList : list){
            for(GraphicalElement ge : listGE){
                flag=false;
                for(ConfigurationAttribute caGE : ge.getAllAttributes()){
                    //refresh the attributes
                    if(caGE instanceof RefreshCA) ((RefreshCA)caGE).refresh(mainController);
                    if(caList instanceof RefreshCA) ((RefreshCA)caList).refresh(mainController);
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
     * Refreshes the JSpinner value and state (enable or not) with the values from the selected GEs.
     * The method test each GraphicalElement from the selectedGE list to know if every single GE property (X and Y position, width, height, rotation) is the same for the GEs.
     * In the case where a property is the same for all the selected GE, the corresponding spinner is set to the common value and enabled.
     * In the case where a property is different for the selected GE, the corresponding spinner is set to 0 and disabled.
     * In the case where no GE are selected, all the spinners are set to 0 and disabled.
     */
    public void refreshSpin(){
        List<GraphicalElement> selectedGE = mainController.getGEController().getSelectedGE();
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
        mainController.getMainWindow().setSpinner(boolX, x, GraphicalElement.Property.X);
        mainController.getMainWindow().setSpinner(boolY, y, GraphicalElement.Property.Y);
        mainController.getMainWindow().setSpinner(boolW, w, GraphicalElement.Property.WIDTH);
        mainController.getMainWindow().setSpinner(boolH, h, GraphicalElement.Property.HEIGHT);
        mainController.getMainWindow().setSpinner(boolR, r, GraphicalElement.Property.ROTATION);
    }
}
