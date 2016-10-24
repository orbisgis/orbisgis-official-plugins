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
        
        ChartElement chart = new ChartElement(getDataManager(thisBundle), title, categoryAxisLabel, valueAxisLabel, sqlQuery);
        chart.setChart(ChartElement.CHART.BARCHART);
        ServiceReference<?> serviceReference = thisBundle.getServiceReference(EditorManager.class.getName());
        // serviceReference  may be null if not available
        if (serviceReference != null) {
            EditorManager editorManager = (EditorManager) thisBundle.
                    getService(serviceReference);
            editorManager.openEditable(chart);
        } else {
            LOGGER.error("Cannot create the bar chart");
        }
        
    }
    
    /**
     * Creates an area chart with default settings.
     * 
     * @param title
     * @param categoryAxisLabel
     * @param valueAxisLabel
     * @param sqlQuery 
     */
    public static void createAreaChart(String title, String categoryAxisLabel, String valueAxisLabel, String sqlQuery) {        
        BundleContext thisBundle = FrameworkUtil.getBundle(ChartFactory.class).getBundleContext();        
        ChartElement chart = new ChartElement(getDataManager(thisBundle),title, categoryAxisLabel, valueAxisLabel, sqlQuery);
        chart.setChart(ChartElement.CHART.AREACHART);
        ServiceReference<?> serviceReference = thisBundle.getServiceReference(EditorManager.class.getName());
        // serviceReference  may be null if not available
        if(serviceReference != null) {
            EditorManager editorManager= (EditorManager ) thisBundle .
                    getService(serviceReference );
            editorManager.openEditable(chart);
        }
    }
    
    public static DataManager getDataManager(BundleContext thisBundle) {
        ServiceReference<?> serviceDataManager = thisBundle.getServiceReference(DataManager.class.getName());
        if (serviceDataManager != null) {
            return (DataManager) thisBundle.getService(serviceDataManager);
        } else {
            LOGGER.error("Cannot access to the data");
        }
        return null;
    }
}
