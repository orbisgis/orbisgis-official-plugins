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

import java.util.Map;
import javax.swing.JComponent;
import javax.swing.JPanel;
import net.miginfocom.swing.MigLayout;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.orbisgis.chart.icons.ChartIcon;
import org.orbisgis.sif.docking.DockingLocation;
import org.orbisgis.sif.docking.DockingPanelParameters;
import org.orbisgis.sif.edition.EditableElement;
import org.orbisgis.sif.edition.EditorDockable;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

/**
 *
 * @author Erwan Bocher
 */

@Component(service = {ChartPanelView.class}, factory = ChartPanelView.SERVICE_FACTORY_ID)
public class ChartPanelView extends JPanel implements EditorDockable {
    
    //private static final I18n I18N = I18nFactory.getI18n(ChartPanelView.class);
    private DockingPanelParameters dockingPanelParameters = new DockingPanelParameters();
    
    public static final String SERVICE_FACTORY_ID = "org.orbisgis.chart.ChartPanelView";   
    private ChartElement chartElement;
    public static final String EDITOR_NAME = "Chart";
    
    @Activate
    public void init(Map<String, Object> attributes) {
        this.setLayout(new MigLayout("fill"));
        dockingPanelParameters.setName(EDITOR_NAME);
        dockingPanelParameters.setTitle("Chart");
        dockingPanelParameters.setTitleIcon(ChartIcon.getIcon("icon"));
        dockingPanelParameters.setDefaultDockingLocation(new DockingLocation(DockingLocation.Location.STACKED_ON, ChartEditorFactory.class.getSimpleName()));

        setEditableElement((ChartElement) attributes.get("editableElement"));
    }

    @Override
    public DockingPanelParameters getDockingParameters() {
        return dockingPanelParameters;
    }

    @Override
    public JComponent getComponent() {
        return this;
    }

    @Override
    public boolean match(EditableElement editableElement) {
        return editableElement instanceof ChartElement;
    }

    @Override
    public EditableElement getEditableElement() {
        return chartElement;
    }

    @Override
    public void setEditableElement(EditableElement editableElement) {
        if (editableElement instanceof ChartElement) {
            this.chartElement = (ChartElement) editableElement;
            ChartPanel chartPanel = new ChartPanel((JFreeChart) chartElement.getObject());      
            add(chartPanel, "growx, growy");
        }    
    }
}
