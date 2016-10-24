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

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;
import javax.sql.DataSource;
import javax.swing.JComponent;
import org.jfree.chart.ChartPanel;
import org.jfree.data.jdbc.JDBCCategoryDataset;
import org.orbisgis.chart.icons.ChartIcon;
import org.orbisgis.sif.docking.DockingLocation;
import org.orbisgis.sif.docking.DockingPanelParameters;
import org.orbisgis.sif.edition.EditableElement;
import org.orbisgis.sif.edition.EditorDockable;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 *
 * @author Erwan Bocher
 */

@Component(service = {ChartPanelView.class}, factory = ChartPanelView.SERVICE_FACTORY_ID)
public class ChartPanelView implements EditorDockable {
    
    //private static final I18n I18N = I18nFactory.getI18n(ChartPanelView.class);
    private DockingPanelParameters dockingPanelParameters = new DockingPanelParameters();
    private static final Logger LOGGER = LoggerFactory.getLogger(ChartPanelView.class);
    public static final String SERVICE_FACTORY_ID = "org.orbisgis.chart.ChartPanelView";   
    private ChartElement chartElement;
    public static final String EDITOR_NAME = "Chart";
    private ChartPanel chartPanel;
    private DataSource dataSource;

    @Activate
    public void init(Map<String, Object> attributes) {
        dockingPanelParameters.setName(EDITOR_NAME);
        dockingPanelParameters.setTitle("Chart");
        dockingPanelParameters.setTitleIcon(ChartIcon.getIcon("icon"));
        dockingPanelParameters.setDefaultDockingLocation(new DockingLocation(DockingLocation.Location.STACKED_ON, ChartEditorFactory.class.getSimpleName()));
        setEditableElement((ChartElement) attributes.get("editableElement"));
    }

    @Reference
    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    public void unsetDataSource(DataSource dataSource) {
        this.dataSource = null;
    }

    @Override
    public DockingPanelParameters getDockingParameters() {
        return dockingPanelParameters;
    }

    @Override
    public JComponent getComponent() {
        return chartPanel;
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
            ChartElement externalChartData = (ChartElement) editableElement;
            try(Connection connection = dataSource.getConnection()) {
                JDBCCategoryDataset dataset = new JDBCCategoryDataset(connection, externalChartData.getSqlQuery());
                chartPanel = new ChartPanel(org.jfree.chart.ChartFactory.createBarChart(externalChartData.getTitle(),
                        externalChartData.getCategoryAxisLabel(), externalChartData.getValueAxisLabel(), dataset));
                this.chartElement = externalChartData;
            } catch (SQLException ex) {
                LOGGER.error(ex.getLocalizedMessage(), ex);
            }
        }
    }
}
