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

import org.orbisgis.sif.edition.EditorManager;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.ServiceReference;


public class ChartFactory {
    /**
     * Create a bar chart
     * @param title title
     * @param categoryAxisLabel YAxis label
     * @param valueAxisLabel XAxis label
     * @param sqlQuery SQL Query
     */
    public static void createBarChart(String title, String categoryAxisLabel, String valueAxisLabel, String sqlQuery) {
        ChartElement chart = new ChartElement(title, categoryAxisLabel, valueAxisLabel, sqlQuery);
        BundleContext thisBundle = FrameworkUtil.getBundle(ChartFactory.class).getBundleContext();
        ServiceReference<?> serviceReference = thisBundle.getServiceReference(EditorManager.class.getName());
        // serviceReference  may be null if not available
        if(serviceReference != null) {
            EditorManager editorManager= (EditorManager ) thisBundle .
                    getService(serviceReference );
            editorManager.openEditable(chart);
        }
    }
}
