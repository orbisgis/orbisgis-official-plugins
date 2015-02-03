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
import java.awt.*;
import java.util.*;
import java.util.List;

/**
 * This class represents and edition of the CompositionArea : when the user modify the X and Y position of an element.
 * It manages the action to execute when the edit should be undone or redone.
 *
 * @author Sylvain PALOMINOS
 */

public class MoveGEUndoableEdit implements UndoableEdit {

    /** Main controller of the application */
    private MainController mainController;

    /** Map of GraphicalElements modified by the edit and its x and y position, its width, its height, and its rotation. */
    private Map<GraphicalElement, Point> mapGEPoint;

    /** True if this edit is significant, meaning that it should be presented to the user, false otherwise. */
    private boolean significant;

    /** True if the edit can be used, false otherwise. */
    private boolean alive;

    /**
     * MoveGEUndoableEdit constructor.
     * @param mainController MainController of the Application.
     * @param listGE List of GraphicalElements concerned by the edit.
     * @param significant True if the edit is significant, meaning that it should be presented to the user, false otherwise.
     */
    public MoveGEUndoableEdit(MainController mainController, List<GraphicalElement> listGE, boolean significant){
        this.mainController = mainController;
        this.mapGEPoint = new HashMap<>();
        //For each GraphicalElement, saves the x and y position.
        for(GraphicalElement ge : listGE) {
            this.mapGEPoint.put(ge, new Point(ge.getX(), ge.getY()));
        }
        this.significant = significant;
        this.alive = true;
    }

    @Override
    public void undo() throws CannotUndoException {
        //First get the list of the GraphicalElements
        List<GraphicalElement> listGE = new ArrayList<>();
        for(GraphicalElement ge : mapGEPoint.keySet())
            listGE.add(ge);
        //Then, for each one, restores the old position and save the new one.
        for(GraphicalElement ge : listGE) {
            //Gets the new position
            Point point = new Point(ge.getX(), ge.getY());
            //Restores the old position
            ge.setX((int) mapGEPoint.get(ge).getX());
            ge.setY((int) mapGEPoint.get(ge).getY());
            //Saves the new position
            mapGEPoint.remove(ge);
            mapGEPoint.put(ge, point);
            mainController.modifyGE(ge, ge.deepCopy());
        }
    }

    @Override
    public boolean canUndo() {
        return (mainController != null && alive && mapGEPoint != null);
    }

    @Override
    public void redo() throws CannotRedoException {
        //First get the list of the GraphicalElements
        List<GraphicalElement> listGE = new ArrayList<>();
        for(GraphicalElement ge : mapGEPoint.keySet())
            listGE.add(ge);
        //Then, for each one, restores the old position and save the new one.
        for(GraphicalElement ge : listGE) {
            //Gets the new position
            Point point = new Point(ge.getX(), ge.getY());
            //Restores the old position
            ge.setX((int) mapGEPoint.get(ge).getX());
            ge.setY((int) mapGEPoint.get(ge).getY());
            //Saves the new position
            mapGEPoint.remove(ge);
            mapGEPoint.put(ge, point);
            mainController.modifyGE(ge, ge.deepCopy());
        }
    }

    @Override
    public boolean canRedo() {
        return (mainController != null && alive && mapGEPoint != null);
    }

    @Override
    public void die() {
        alive = false;
    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        //First test if the edit in argument is an instance of MoveGEUndoableEdit and if it's not significant.
        if(anEdit instanceof ConfigurationGEUndoableEdit && !anEdit.isSignificant()){
            //For each GraphicalElement contained by the edit, test if it is not contained by this edit.
            for(GraphicalElement ge : ((MoveGEUndoableEdit) anEdit).mapGEPoint.keySet()){
                if(!mapGEPoint.containsKey(ge)){
                    //In this case, merges both edits : adds all the GraphicalElements and the position contained by the edit given in argument to this edit.
                    mapGEPoint.put(ge, ((MoveGEUndoableEdit) anEdit).mapGEPoint.get(ge));
                }
            }
            return true;
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
