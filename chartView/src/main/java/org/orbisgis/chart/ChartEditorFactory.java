/**
 * OrbisGIS is a GIS application dedicated to scientific spatial analysis.
 * This cross-platform GIS is developed at the Lab-STICC laboratory by the DECIDE 
 * team located in University of South Brittany, Vannes.
 * 
 * OrbisGIS is distributed under GPL 3 license.
 *
 * Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
 * Copyright (C) 2015-2016 CNRS (UMR CNRS 6285)
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.chart;

import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import org.orbisgis.sif.docking.DockingPanelLayout;
import org.orbisgis.sif.edition.EditableElement;
import org.orbisgis.sif.edition.EditorDockable;

import org.orbisgis.sif.edition.EditorFactory;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

/**
 * The chart editor factory to open several chart views
 * 
 * @author Erwan Bocher
 */
@Component(service = EditorFactory.class, immediate = true)
public class ChartEditorFactory implements EditorFactory {

    public static final String factoryId = "ChartFactory";
    //protected final static I18n I18N = I18nFactory.getI18n(ChartEditorFactory.class);
    private ComponentFactory componentFactory;
    private List<ComponentInstance> instanceList = new ArrayList<>();   
   
    /**
     * Default constructor
     */
    public ChartEditorFactory() {
    }   
   

    @Reference(target = "(component.factory="+ChartPanelView.SERVICE_FACTORY_ID+")")
    public void setComponentFactory(ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
    }

    public void unsetComponentFactory(ComponentFactory componentFactory) {
        this.componentFactory = null;
    }    

    @Override
    public String getId() {
        return factoryId;
    }

    @Override
    public void dispose() {
    }

    @Deactivate
    public void deactivate() {
        for(ComponentInstance  componentInstance : instanceList) {
            componentInstance.dispose();
        }
        instanceList.clear();
    }

    @Override
    public DockingPanelLayout makeEditableLayout(EditableElement editable) {
        if(editable instanceof ChartElement) {
            return (ChartElement) editable;
        } else {
            return null;
        }
    }

    @Override
    public DockingPanelLayout makeEmptyLayout() {
        return new ChartElement();
    }

    @Override
    public boolean match(DockingPanelLayout layout) {
        return layout instanceof ChartElement;
    }

    @Override
    public EditorDockable create(DockingPanelLayout layout) {
        Dictionary<String,Object> initValues = new Hashtable<>();
        initValues.put("editableElement", layout);
        ComponentInstance chartPanelFactory = componentFactory.newInstance(initValues);
        instanceList.add(chartPanelFactory);
        return (ChartPanelView) chartPanelFactory.getInstance();        
    }
    
}
