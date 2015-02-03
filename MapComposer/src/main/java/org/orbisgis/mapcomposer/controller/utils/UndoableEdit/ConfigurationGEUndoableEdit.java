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
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.RefreshCA;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;

import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEdit;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class represents and edition of the CompositionArea : when the user configure an element or a list of elements.
 * It manages the action to execute when the edit should be undone or redone.
 *
 * @author Sylvain PALOMINOS
 */

public class ConfigurationGEUndoableEdit implements UndoableEdit {

    /** Main controller of the application */
    private MainController mainController;

    /** Map of GraphicalElements modified by the edit and the ConfigurationAttributes state */
    private Map<GraphicalElement, List<ConfigurationAttribute>> mapGECopy;

    /** True if this edit is significant, meaning that it should be presented to the user, false otherwise. */
    private boolean significant;

    /** True if the edit can be used, false otherwise. */
    private boolean alive;

    /**
     * ConfigurationGEUndoableEdit constructor.
     * @param mainController MainController of the Application.
     * @param listGE List of the GraphicalElements concerned by the edit.
     * @param significant True if the edit is significant, meaning that it should be presented to the user, false otherwise.
     */
    public ConfigurationGEUndoableEdit(MainController mainController, List<GraphicalElement> listGE, boolean significant){
        this.mainController = mainController;
        this.mapGECopy = new HashMap<>();
        //Saves the GraphicalElement and a copy of its attributes
        for(GraphicalElement ge : listGE) {
            this.mapGECopy.put(ge, ge.deepCopy().getAllAttributes());
        }
        this.significant = significant;
        this.alive = true;
    }

    @Override
    public void undo() throws CannotUndoException {
        //First gets the list of the GraphicalElements saved
        List<GraphicalElement> listGE = new ArrayList<>();
        for(GraphicalElement ge : mapGECopy.keySet())
            listGE.add(ge);
        //Then for each one
        for(GraphicalElement ge : listGE){
            //Does a copy
            GraphicalElement copy = ge.deepCopy();
            //Restore its old attributes
            for(ConfigurationAttribute ca : mapGECopy.get(ge)){
                if(ca instanceof RefreshCA)
                    ((RefreshCA)ca).refresh(mainController);
                ge.setAttribute(ca);
            }
            //Save its new attributes (thank to its copy)
            mapGECopy.remove(ge);
            mapGECopy.put(ge, copy.getAllAttributes());

            mainController.getCompositionAreaController().refreshGE(ge);
        }
    }

    @Override
    public boolean canUndo() {
        return (mainController != null && alive && mapGECopy != null);
    }

    @Override
    public void redo() throws CannotRedoException {
        //First gets the list of the GraphicalElements saved
        List<GraphicalElement> listGE = new ArrayList<>();
        for(GraphicalElement ge : mapGECopy.keySet())
            listGE.add(ge);
        //Then for each one
        for(GraphicalElement ge : listGE){
            //Does a copy
            GraphicalElement copy = ge.deepCopy();
            //Restore its old attributes
            for(ConfigurationAttribute ca : mapGECopy.get(ge)){
                if(ca instanceof RefreshCA)
                    ((RefreshCA)ca).refresh(mainController);
                ge.setAttribute(ca);
            }
            //Save its new attributes (thank to its copy)
            mapGECopy.remove(ge);
            mapGECopy.put(ge, copy.getAllAttributes());

            mainController.getCompositionAreaController().refreshGE(ge);
        }
    }

    @Override
    public boolean canRedo() {
        return (mainController != null && alive && mapGECopy != null);
    }

    @Override
    public void die() {
        alive = false;
    }

    @Override
    public boolean addEdit(UndoableEdit anEdit) {
        //First test if the edit in argument is an instance of ConfigurationGEUndoableEdit and if it's not significant.
        if(anEdit instanceof ConfigurationGEUndoableEdit && !anEdit.isSignificant()){
            //For each GraphicalElement contained by the edit, test if it is not contained by this edit.
            for(GraphicalElement ge : ((ConfigurationGEUndoableEdit) anEdit).mapGECopy.keySet()){
                if(!mapGECopy.containsKey(ge)){
                    //In this case, merges both edits : adds all the GraphicalElements and the ConfigurationAttributes contained by the edit given in argument to this edit.
                    mapGECopy.put(ge, ((ConfigurationGEUndoableEdit) anEdit).mapGECopy.get(ge));
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
