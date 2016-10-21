package org.orbisgis.chart;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.jdbc.JDBCCategoryDataset;
import org.orbisgis.commons.progress.ProgressMonitor;
import org.orbisgis.sif.docking.DockingPanelLayout;
import org.orbisgis.sif.docking.XElement;
import org.orbisgis.sif.edition.AbstractEditableElement;
import org.orbisgis.sif.edition.EditableElementException;

/**
 * Chart serialisation
 * @author Erwan Bocher
 */
public class ChartElement extends AbstractEditableElement implements DocumentListener, DockingPanelLayout {

    JFreeChart chart;
    
    @Override
    public String getTypeId() {
        return "ChartElement";
    }

    @Override
    public void open(ProgressMonitor progressMonitor) throws UnsupportedOperationException, EditableElementException {
        //TODO
    }

    @Override
    public void save() throws UnsupportedOperationException, EditableElementException {
        //TODO
    }

    @Override
    public void close(ProgressMonitor progressMonitor) throws UnsupportedOperationException, EditableElementException {
        //TODO
    }

    @Override
    public Object getObject() throws UnsupportedOperationException {
        return chart;
    }

    @Override
    public void insertUpdate(DocumentEvent e) {
        //TODO
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        //TODO
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        //TODO
    }

    @Override
    public void writeStream(DataOutputStream out) throws IOException {
        //TODO
    }

    @Override
    public void readStream(DataInputStream in) throws IOException {
        //TODO
    }

    @Override
    public void writeXML(XElement element) {
        //TODO
    }

    @Override
    public void readXML(XElement element) {
        //TODO
    }
    
    /**
     * Create a bar chart
     * @param con
     * @param title
     * @param categoryAxisLabel
     * @param valueAxisLabel
     * @param sqlQuery
     * @throws SQLException 
     */
    public void createBarChart(Connection con, String title, String categoryAxisLabel, String valueAxisLabel, String sqlQuery) throws SQLException{
        JDBCCategoryDataset dataset = new JDBCCategoryDataset(con, sqlQuery);
        chart = org.jfree.chart.ChartFactory.createBarChart(title, categoryAxisLabel, valueAxisLabel, dataset); 
    }
    
}
