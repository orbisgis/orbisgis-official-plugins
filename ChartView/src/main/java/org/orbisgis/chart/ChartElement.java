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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import org.jfree.chart.JFreeChart;
import org.orbisgis.commons.progress.ProgressMonitor;
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
    private String sqlQuery;
    
    private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ChartElement.class);
    private JFreeChart jfreechart;
    private String CHART_OBJECT = "CHART_OBJECT";
    private String CHART_QUERY = "CHART_QUERY";
    private String title = "A chart element";

    private CHART chartType;

    public enum CHART {
        BARCHART, AREACHART, BARCHART3D, BUBBLECHART, HISTOGRAM, LINECHART, LINECHART3D, PIECHART, PIECHART3D,
        SCATTERPLOT, TIMESERIESCHART, XYAREACHART, XYBARCHART, XYLINECHART
    };

   
    public ChartElement(CHART chartType, String sqlQuery, String title) {
        this.chartType=chartType;
        this.sqlQuery = sqlQuery;
        this.title=title;
    }

    public ChartElement() {   
    }

    @Override
    public String getTypeId() {
        return "ChartElement";
    }

    @Override
    public boolean isModified() {
        return true;
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
        if(jfreechart!=null){
            try { 
                byte[] bytes = convertToBytes(jfreechart);
                element.addByteArray(CHART_OBJECT, bytes);
                element.addString(CHART_QUERY, getSqlQuery());
            } catch (IOException ex) {
                LOGGER.error("Cannot save the chart", ex);
            }
        
        }
    }

    @Override
    public void readXML(XElement element) {
        byte[] chartBytes = element.getByteArray(CHART_OBJECT);
        if(chartBytes!=null){
            try {
                jfreechart = convertFromBytes(chartBytes);
                sqlQuery = element.getString(CHART_QUERY);
            } catch (IOException | ClassNotFoundException ex) {
                LOGGER.error("Cannot read the chart",ex);
            }           
        }        
    }    

    public String getSqlQuery() {
        return sqlQuery;
    }
    

    /**
     * Return a  JFreeChart object
     * 
     * @return 
     */
    public JFreeChart getJfreeChart() {
        return jfreechart;
    }
    
    /**
     * Set the JFreeChart object
     * @param jfreechart 
     */
    public void setJFreeChart(JFreeChart jfreechart) {
        this.jfreechart=jfreechart;
    }
    
    /**
     * Convert the JFreeChart object to byte array
     * @param jFreeChart
     * @return
     * @throws IOException 
     */
    private byte[] convertToBytes(JFreeChart jFreeChart) throws IOException {
        try (ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ObjectOutputStream out = new ObjectOutputStream(bos)) {
            out.writeObject(jFreeChart);
            return bos.toByteArray();
        }
    }

    /**
     * Get the JFreeChart object
     * @param bytes
     * @return
     * @throws IOException
     * @throws ClassNotFoundException 
     */
    private JFreeChart convertFromBytes(byte[] bytes) throws IOException, ClassNotFoundException {
        try (ByteArrayInputStream bis = new ByteArrayInputStream(bytes);
                ObjectInputStream in = new ObjectInputStream(bis)) {
            return (JFreeChart) in.readObject();
        }
    }

    @Override
    public String toString() {
        return "Chart : " + title;
    }

    /*
    * Return the type of chart
     */
    public CHART getChartType() {
        return chartType;
    }
    
}
