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

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.jfree.chart.JFreeChart;
import org.jfree.data.jdbc.JDBCCategoryDataset;
import org.orbisgis.commons.progress.ProgressMonitor;
import org.orbisgis.corejdbc.DataManager;
import org.orbisgis.sif.docking.DockingPanelLayout;
import org.orbisgis.sif.docking.XElement;
import org.orbisgis.sif.edition.AbstractEditableElement;
import org.orbisgis.sif.edition.EditableElementException;
import org.slf4j.LoggerFactory;

/**
 * Chart serialisation
 * @author Erwan Bocher
 */
public class ChartElement extends AbstractEditableElement implements DockingPanelLayout {
    private String title;
    private String categoryAxisLabel;
    private String valueAxisLabel;
    private String sqlQuery;
    private CHART chartType;
    private DataManager dataManager;
    
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ChartElement.class);
    
    public enum CHART {BARCHART, AREACHART, BARCHART3D, BUBBLECHART, HISTOGRAM, LINECHART, LINECHART3D, PIECHART, PIECHART3D,
    SCATTERPLOT, TIMESERIESCHART, XYAREACHART, XYBARCHART, XYLINECHART}; 

    public ChartElement(DataManager dataManager, String title, String categoryAxisLabel, String valueAxisLabel, String sqlQuery) {
        this.dataManager=dataManager;
        this.title = title;
        this.categoryAxisLabel = categoryAxisLabel;
        this.valueAxisLabel = valueAxisLabel;
        this.sqlQuery = sqlQuery;
    }

    public ChartElement(DataManager dataManager) {
        this.dataManager=dataManager;        
    }

    @Override
    public String getTypeId() {
        return "ChartElement";
    }

    @Override
    public void open(ProgressMonitor progressMonitor) throws UnsupportedOperationException, EditableElementException {        

    }

    @Override
    public void save() throws UnsupportedOperationException, EditableElementException {
        // No file to save
    }

    @Override
    public void close(ProgressMonitor progressMonitor) throws UnsupportedOperationException, EditableElementException {
        // No cache data
    }

    @Override
    public Object getObject() throws UnsupportedOperationException {
        return null;
    }

    @Override
    public void writeStream(DataOutputStream out) throws IOException {

    }

    @Override
    public void readStream(DataInputStream in) throws IOException {

    }

    @Override
    public void writeXML(XElement element) {
        element.addString("title", title);
        element.addString("query", sqlQuery);
        element.addString("categoryAxisLabel", categoryAxisLabel);
        element.addString("valueAxisLabel", valueAxisLabel);
    }

    @Override
    public void readXML(XElement element) {
        title = element.getString("title");
        sqlQuery = element.getString("query");
        categoryAxisLabel = element.getString("categoryAxisLabel");
        valueAxisLabel = element.getString("valueAxisLabel");
    }

    public String getTitle() {
        return title;
    }

    public String getCategoryAxisLabel() {
        return categoryAxisLabel;
    }

    public String getValueAxisLabel() {
        return valueAxisLabel;
    }

    public String getSqlQuery() {
        return sqlQuery;
    }
    
    public void setChart(CHART chart){
        this.chartType =chart;        
    }
    

    /**
     * Return a  JFreeChart object
     * 
     * @return 
     */
    public JFreeChart getJfreeChart() {
        if (chartType != null) {
            switch (chartType) {
                case BARCHART:
                    try (Connection connection = dataManager.getDataSource().getConnection()) {
                        JDBCCategoryDataset dataset = new JDBCCategoryDataset(connection, getSqlQuery());
                        return org.jfree.chart.ChartFactory.createBarChart(getTitle(),
                                getCategoryAxisLabel(), getValueAxisLabel(), dataset);
                    } catch (SQLException ex) {
                        LOGGER.error(ex.getLocalizedMessage(), ex);
                    }
                    break;
                default:
            }
        }
        return null;
    }
    
    
    
}
