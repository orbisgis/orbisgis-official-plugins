package org.orbisgis.chart;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import javax.sql.DataSource;
import org.orbisgis.sif.docking.DockingPanelLayout;
import org.orbisgis.sif.edition.EditableElement;
import org.orbisgis.sif.edition.EditorDockable;

import org.orbisgis.sif.edition.EditorFactory;
import org.osgi.service.component.ComponentFactory;
import org.osgi.service.component.ComponentInstance;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

/**
 *
 * @author Erwan Bocher
 */
@Component(service = EditorFactory.class, immediate = true)
public class ChartEditorFactory implements EditorFactory {

   public static final String factoryId = "ChartFactory";
    protected final static I18n I18N = I18nFactory.getI18n(ChartEditorFactory.class);
    private ComponentFactory componentFactory;
    private List<ComponentInstance> instanceList = new ArrayList<>();
    private Connection con;

    /**
     * Default constructor
     */
    public ChartEditorFactory() {

    }   
   

    @Reference(target = "(component.factory="+ChartPanelView.SERVICE_FACTORY_ID+")")
    public void setComponentFactory(ComponentFactory componentFactory) {
        this.componentFactory = componentFactory;
    }

    public void unsetComponentFactory(ComponentFactory componentFactory) {
        this.componentFactory = null;
    }

    @Override
    public String getId() {
        return factoryId;
    }

    @Override
    public void dispose() {
    }

    @Deactivate
    public void deactivate() {
        for(ComponentInstance  componentInstance : instanceList) {
            componentInstance.dispose();
        }
        instanceList.clear();
    }

    @Override
    public DockingPanelLayout makeEditableLayout(EditableElement editable) {
        if(editable instanceof ChartElement) {
            return (ChartElement) editable;
        } else {
            return null;
        }
    }

    @Override
    public DockingPanelLayout makeEmptyLayout() {
        return new ChartElement();
    }

    @Override
    public boolean match(DockingPanelLayout layout) {
        return layout instanceof ChartElement;
    }

    @Override
    public EditorDockable create(DockingPanelLayout layout) {
        Dictionary<String,Object> initValues = new Hashtable<>();
        initValues.put("editableElement", layout);
        ComponentInstance chartPanelFactory = componentFactory.newInstance(initValues);
        instanceList.add(chartPanelFactory);
        return (ChartPanelView) chartPanelFactory.getInstance();
    }
    
}
