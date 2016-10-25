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
import java.util.concurrent.ExecutorService;
import org.jfree.chart.JFreeChart;
import org.jfree.data.jdbc.JDBCCategoryDataset;
import org.jfree.data.jdbc.JDBCXYDataset;
import org.orbisgis.commons.progress.SwingWorkerPM;
import org.orbisgis.corejdbc.DataManager;
import org.orbisgis.sif.edition.EditorManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class ChartFactory {
    
    private static final Logger LOGGER = LoggerFactory.getLogger(ChartFactory.class);
    
    /**
     * Create a bar chart
     * @param title title
     * @param categoryAxisLabel YAxis label
     * @param valueAxisLabel XAxis label
     * @param sqlQuery SQL Query
     */
    public static void createBarChart(String title, String categoryAxisLabel, String valueAxisLabel, String sqlQuery) {
        BundleContext thisBundle = FrameworkUtil.getBundle(ChartFactory.class).getBundleContext();
        ChartElement chart = new ChartElement(sqlQuery, title);
        ExecutorService executorService = getExecutorService(thisBundle);
        LoadCategoryDataset loadCategoryDataset = new LoadCategoryDataset(sqlQuery, thisBundle);
        if (executorService != null) {
            executorService.execute(loadCategoryDataset);
        } else {
            loadCategoryDataset.execute();
        }
        JFreeChart jfreechart = org.jfree.chart.ChartFactory.createBarChart(title,
                categoryAxisLabel, valueAxisLabel, loadCategoryDataset.getDataset());
        chart.setJFreeChart(jfreechart);
        openChartElement(thisBundle, chart);

    }
    
    /**
     * Creates a scatter plot with default settings.
     * 
     * @param title
     * @param xAxisLabel
     * @param yAxisLabel
     * @param sqlQuery 
     */
    public static void createScatterPlot(String title, String xAxisLabel, String yAxisLabel, String sqlQuery) {        
        BundleContext thisBundle = FrameworkUtil.getBundle(ChartFactory.class).getBundleContext();        
        ChartElement chart = new ChartElement(sqlQuery, title);
        try (Connection connection = getDataManager(thisBundle).getDataSource().getConnection()) {
            JDBCXYDataset dataset = new JDBCXYDataset(connection, sqlQuery);
            JFreeChart jfreechart = org.jfree.chart.ChartFactory.createScatterPlot(title,
                    xAxisLabel, yAxisLabel, dataset);
            chart.setJFreeChart(jfreechart);
            openChartElement(thisBundle, chart);
        } catch (SQLException ex) {
            LOGGER.error(ex.getLocalizedMessage(), ex);
        }
    }
    
    /**
     * Get the dataManager 
     * @param thisBundle
     * @return 
     */
    public static DataManager getDataManager(BundleContext thisBundle) {
        ServiceReference<?> serviceDataManager = thisBundle.getServiceReference(DataManager.class.getName());
        if (serviceDataManager != null) {
            return (DataManager) thisBundle.getService(serviceDataManager);
        } else {
            throw new IllegalArgumentException("Cannot access to the data");
        }
    }
    
    /**
     * Get the ExecutorService 
     * @param thisBundle
     * @return 
     */
    public static ExecutorService getExecutorService(BundleContext thisBundle) {
        ServiceReference<?> serviceExecutor = thisBundle.getServiceReference(ExecutorService.class.getName());
        if (serviceExecutor != null) {
            return (ExecutorService) thisBundle.getService(serviceExecutor);
        } else {
            return null;
        }
    }
    
    /**
     * Open the {@link org.orbisgis.char.ChartElement}
     * 
     * @param thisBundle
     * @param chart 
     */
    public static void openChartElement(BundleContext thisBundle, ChartElement chart){
        ServiceReference<?> serviceReference = thisBundle.getServiceReference(EditorManager.class.getName());
        // serviceReference  may be null if not available
        if(serviceReference != null) {
            EditorManager editorManager= (EditorManager ) thisBundle .
                    getService(serviceReference );
            editorManager.openEditable(chart);
        }
    }
    
    /**
     * Load the table data into a CategoryDataset
     *
     * @author Erwan Bocher
     */
    public static class LoadCategoryDataset extends SwingWorkerPM {

        private final String sqlQuery;
        private final BundleContext thisBundle;
        private JDBCCategoryDataset dataset;

        public  LoadCategoryDataset(String sqlQuery, BundleContext thisBundle){
            this.sqlQuery=sqlQuery;
            this.thisBundle=thisBundle;
            setTaskName("Preparing chart data");
        }
        @Override
        protected Object doInBackground() throws Exception {
            try (Connection connection = getDataManager(thisBundle).getDataSource().getConnection()) {
                dataset = new JDBCCategoryDataset(connection, sqlQuery);
            } catch (SQLException ex) {
                LOGGER.error(ex.getLocalizedMessage(), ex);
            }
            return null;
        }

        public JDBCCategoryDataset getDataset() {
            return dataset;
        }
    }
    
    
}
