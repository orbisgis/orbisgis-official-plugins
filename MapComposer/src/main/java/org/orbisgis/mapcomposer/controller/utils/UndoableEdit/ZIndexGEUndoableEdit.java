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

package org.orbisgis.mapcomposer.controller.utils.UndoableEdit;

import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents and edition of the CompositionArea : when the user modify z-index of an element.
 * It manages the action to execute when the edit should be undone or redone.
 *
 * @author Sylvain PALOMINOS
 */

public class ZIndexGEUndoableEdit implements UndoableEdit {

    /** Main controller of the application */
    private MainController mainController;

    /** List of GraphicalElements modified by the edit */
    private Map<GraphicalElement, Integer> mapGEZIndex;

    /** True if this edit is significant, meaning that it should be presented to the user, false otherwise. */
    private boolean significant;

    /** True if the edit can be used, false otherwise. */
    private boolean alive;

    /**
     * ZIndexGEUndoableEdit constructor.
     * @param mainController MainController of the Application.
     * @param listGE List of GraphicalElements concerned by the edit.
     * @param significant True if the edit is significant, meaning that it should be presented to the user, false otherwise.
     */
    public ZIndexGEUndoableEdit(MainController mainController, List<GraphicalElement> listGE, boolean significant){
        this.mainController = mainController;
        this.mapGEZIndex = new HashMap<>();
        for(GraphicalElement ge : listGE) {
            this.mapGEZIndex.put(ge, ge.getZ());
        }
        this.significant = significant;
        this.alive = true;
    }

    @Override
    public void undo() throws CannotUndoException {
        //First get the list of the GraphicalElement contained by the edit
        List<GraphicalElement> listGEEdit = new ArrayList<>();
        for(GraphicalElement ge : mapGEZIndex.keySet())
            listGEEdit.add(ge);
        //Then gets the list of graphicalElement of the application ad put the GE to restored z-index
        List<GraphicalElement> listGEApplication = mainController.getGEList();
        for(GraphicalElement ge : listGEEdit){
            int size = listGEApplication.size() - 1;
            //Puts the GE to the restored z-index
            listGEApplication.remove(ge);
            listGEApplication.add(size-mapGEZIndex.get(ge), ge);
            //Saves the new z-index for redoing
            mapGEZIndex.remove(ge);
            mapGEZIndex.put(ge, ge.getZ());
        }
        //To finish reverses the list of GraphicalElement before setting the z-index
        List<GraphicalElement> reverse = new ArrayList<>();
        for(GraphicalElement ge : listGEApplication)
            reverse.add(0, ge);
        mainController.getCompositionAreaController().setZIndex(reverse);
        mainController.getCompositionAreaController().refreshGE(reverse);
    }

    @Override
    public boolean canUndo() {
        return (mainController != null && alive && mapGEZIndex != null);
    }

    @Override
    public void redo() throws CannotRedoException {
        //First get the list of the GraphicalElement contained by the edit
        List<GraphicalElement> listGEEdit = new ArrayList<>();
        for(GraphicalElement ge : mapGEZIndex.keySet())
            listGEEdit.add(ge);
        //Then gets the list of graphicalElement of the application ad put the GE to restored z-index
        List<GraphicalElement> listGEApplication = mainController.getGEList();
        for(GraphicalElement ge : listGEEdit){
            int size = listGEApplication.size() - 1;
            //Puts the GE to the restored z-index
            listGEApplication.remove(ge);
            listGEApplication.add(size-mapGEZIndex.get(ge), ge);
            //Saves the new z-index for undoing
            mapGEZIndex.remove(ge);
            mapGEZIndex.put(ge, ge.getZ());
        }
        //To finish reverses the list of GraphicalElement before setting the z-index
        List<GraphicalElement> reverse = new ArrayList<>();
        for(GraphicalElement ge : listGEApplication)
            reverse.add(0, ge);
        mainController.getCompositionAreaController().setZIndex(reverse);
        mainController.getCompositionAreaController().refreshGE(reverse);
    }

    @Override
    public boolean canRedo() {
        return (mainController != null && alive && mapGEZIndex != null);
    }

    @Override
    public void die() {
        alive = false;
    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        if(anEdit instanceof ZIndexGEUndoableEdit && !anEdit.isSignificant()){
            boolean flag = true;
            for(GraphicalElement ge : ((ZIndexGEUndoableEdit) anEdit).mapGEZIndex.keySet()){
                if(!mapGEZIndex.containsKey(ge))
                    flag = false;
            }
            return flag;
        }
        return false;
    }

    @Override
    public boolean replaceEdit(UndoableEdit anEdit) {
        return false;
    }

    @Override
    public boolean isSignificant() {
        return significant;
    }

    @Override
    public String getPresentationName() {
        return null;
    }

    @Override
    public String getUndoPresentationName() {
        return null;
    }

    @Override
    public String getRedoPresentationName() {
        return null;
    }
}
