package org.orbisgis.chart;

import java.sql.Connection;
import java.sql.SQLException;
import org.orbisgis.sif.edition.EditorManager;
import org.osgi.service.component.annotations.Reference;

/**
 *
 * @author ebocher
 */


public class ChartFactory {

    private final EditorManager editorManager;
    private final Connection con;
    
    
    public ChartFactory(Connection con, EditorManager editorManager){
        this.con = con;
        this.editorManager =editorManager;
    }
    
    
    
    /**
     * Create a bar chart
     * 
     * @param title
     * @param categoryAxisLabel
     * @param valueAxisLabel
     * @param sqlQuery 
     */
    public void createBarChart(String title, String categoryAxisLabel, String valueAxisLabel, String sqlQuery) throws SQLException{
        ChartElement chart = new ChartElement();
        chart.createBarChart(con, title, categoryAxisLabel, valueAxisLabel, sqlQuery);        
        editorManager.openEditable(chart);
    }
}
