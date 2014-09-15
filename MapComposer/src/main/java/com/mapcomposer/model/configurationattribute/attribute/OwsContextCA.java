package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.interfaces.ListCA;
import com.mapcomposer.model.configurationattribute.interfaces.RefreshCA;
import com.mapcomposer.model.utils.LinkToOrbisGIS;
import com.mapcomposer.view.ui.ConfigurationShutter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.layerModel.OwsMapContext;
import org.orbisgis.progress.NullProgressMonitor;

/**
 * ConfigurationAttribute representing a specified OwsMapContext given by OrbisGIS.
 */
public final class OwsContextCA extends BaseListCA<String> implements RefreshCA{
    /** Index of the value selected.*/
    private int index;
    /** Property itself */
    private List<String> list;
    private final OwsMapContext omc;
    
    /**
     * Main constructor.
     * @param name Name of the OwsContext in its GraphicalElement.
     */
    public OwsContextCA() {
        super();
        list = new ArrayList<>();
        loadListFiles();
        if(list.isEmpty())
            this.add("/");
        else
            this.select(list.get(0));
        omc=new OwsMapContext(LinkToOrbisGIS.getInstance().getDataManager());
        this.refresh();
    }
    
    public OwsMapContext getOwsMapContext(){return omc;}

    @Override
    public void refresh() {
        //Refresh of the file list
        loadListFiles();
        //Refresh of the selected file
        try {
            reloadSelectedOMC();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(ConfigurationShutter.getInstance(), "Cannot load the OWS-Context '"+this.getValue()+"'.");
        }
    }
    
    /**
     * Reload the OWS-Context corresponding to the value of ConfigurationAttribute. 
     * @throws FileNotFoundException 
     */
    private void reloadSelectedOMC() throws FileNotFoundException{
        try {
            if(omc.isOpen()){
                omc.close(new NullProgressMonitor());
            }
            omc.read(new FileInputStream(new File(getSelected())));
            omc.open(new NullProgressMonitor());
        } catch (LayerException ex) {
            Logger.getLogger(OwsContextCA.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    /**
     * Set list field with all the files with the oxs extension
     */
    private void loadListFiles(){
        File f = new File(LinkToOrbisGIS.getInstance().getViewWorkspace().getCoreWorkspace().getWorkspaceFolder()+"/maps/");
        //Definition of the FilenameFilter
        FilenameFilter filter = new FilenameFilter() {
            @Override
            public boolean accept(File file, String string) {
                String name = string.toLowerCase();
                return name.contains(".ows");
            }
        };
        String s = getSelected();
        list=new ArrayList<>();
        for(File file : f.listFiles(filter)){
            if(file.getAbsolutePath().toLowerCase().contains(".ows"))
                list.add(file.getAbsolutePath());
        }
        select(s);
    }

    @Override
    public void add(String value) {list.add(value);}

    @Override
    public String getSelected() {
        if(index>=0 && index<list.size())
            return list.get(index);
        else
            return null;
    }

    @Override
    public void select(String choice) {index=list.indexOf(choice);}

    @Override
    public boolean remove(String value) {return list.remove(value);}

    @Override
    public void setValue(List<String> value) {this.list=value;}

    @Override
    public List<String> getValue() {return list;}

    @Override public boolean isSameValue(ConfigurationAttribute ca) {
        if(ca instanceof ListCA){
            if(getSelected()==null)
                return false;
            return getSelected().equals(((ListCA)ca).getSelected());
        }
        return false;
    }
}
