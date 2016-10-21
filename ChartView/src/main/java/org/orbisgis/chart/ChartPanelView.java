
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
    
    private DockingPanelParameters dockingPanelParameters = new DockingPanelParameters();
    
    public static final String SERVICE_FACTORY_ID = "org.orbisgis.chart.ChartPanel";       
    private ChartPanel chartPanel;
    private ChartElement chartElement;
    

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
            this.chartElement = (ChartElement) editableElement;
            chartPanel = new ChartPanel((JFreeChart) chartElement.getObject());
        }    
    }
}
