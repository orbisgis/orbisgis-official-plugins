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

package org.orbisgis.mapcomposer.controller.utils;

import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Sylvain PALOMINOS
 */

public class RemoveGEUndoableEdit implements UndoableEdit {

    private MainController mainController;

    private List<GraphicalElement> listGE;

    private boolean significant;

    private boolean alive;

    public RemoveGEUndoableEdit(MainController mainController, GraphicalElement ge, boolean significant){
        this.mainController = mainController;
        this.listGE = new ArrayList<>();
        listGE.add(ge);
        this.significant = significant;
        alive = true;
    }

    @Override
    public void undo() throws CannotUndoException {
        for(GraphicalElement ge : listGE)
            mainController.addGE(ge);
    }

    @Override
    public boolean canUndo() {
        System.out.println("canUndo");
        return (mainController != null && alive);
    }

    @Override
    public void redo() throws CannotRedoException {
        for(GraphicalElement ge : listGE)
            mainController.removeGE(ge);
    }

    @Override
    public boolean canRedo() {
        System.out.println("canRedo");
        return (mainController != null && alive);
    }

    @Override
    public void die() {
        alive = false;
    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        return false;
    }

    @Override
    public boolean replaceEdit(UndoableEdit anEdit) {
        if(anEdit instanceof RemoveGEUndoableEdit && !anEdit.isSignificant()){
            this.listGE.addAll(((RemoveGEUndoableEdit) anEdit).listGE);
            return true;
        }
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
