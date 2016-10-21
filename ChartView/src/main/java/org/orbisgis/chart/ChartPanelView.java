
package org.orbisgis.chart;

import java.sql.Connection;
import java.sql.SQLException;
import javax.sql.DataSource;
import javax.swing.JComponent;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.data.jdbc.JDBCCategoryDataset;
import org.orbisgis.sif.docking.DockingPanelParameters;
import org.orbisgis.sif.edition.EditableElement;
import org.orbisgis.sif.edition.EditorDockable;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 *
 * @author Erwan Bocher
 */

@Component(service = {ChartPanelView.class}, factory = ChartPanelView.SERVICE_FACTORY_ID)
public class ChartPanelView implements EditorDockable {
    
    
    public static final String SERVICE_FACTORY_ID = "org.orbisgis.chart.ChartPanel";       
    
    
    private Connection con;
    
    
    /**
     * Create a bar chart
     * @param title
     * @param categoryAxisLabel
     * @param valueAxisLabel
     * @param query
     * @throws SQLException 
     */
    public void createBarChart(String title, String categoryAxisLabel, String valueAxisLabel, String query) throws SQLException{
        JDBCCategoryDataset dataset = new JDBCCategoryDataset(con, query);
        JFreeChart chart = ChartFactory.createBarChart(title, categoryAxisLabel, valueAxisLabel, dataset);
        ChartPanel chartPanel = new ChartPanel(chart);     
        //Populate the docking view
    }
    
    
    @Reference
    public void setDataSource(DataSource ds) throws SQLException {
        con = ds.getConnection();
    }

    @Override
    public DockingPanelParameters getDockingParameters() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public JComponent getComponent() {
        //Must return the chart panel....
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public boolean match(EditableElement editableElement) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public EditableElement getEditableElement() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void setEditableElement(EditableElement editableElement) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
