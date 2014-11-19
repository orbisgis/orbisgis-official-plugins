package org.orbisgis.mapcomposer.model.configurationattribute.attribute;

import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;
import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.RefreshCA;
import org.orbisgis.mapcomposer.model.utils.LinkToOrbisGIS;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FilenameFilter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.layerModel.OwsMapContext;
import org.orbisgis.progress.NullProgressMonitor;
import org.slf4j.LoggerFactory;

/**
 * ConfigurationAttribute representing a specified OwsMapContext given by OrbisGIS.
 */
public class OwsContextCA extends BaseListCA<String> implements RefreshCA{
    /** Index of the value selected.*/
    private int index = 0;
    /** Property itself */
    private List<String> list = new ArrayList<>();
    private OwsMapContext omc = new OwsMapContext(LinkToOrbisGIS.getInstance().getDataManager());

    public OwsMapContext getOwsMapContext(){return omc;}

    @Override
    public void refresh(UIController uic) {
        //Refresh of the file list
        loadListFiles();
        //Refresh of the selected file
        try {
            reloadSelectedOMC();
        } catch (FileNotFoundException ex) {
            LoggerFactory.getLogger(OwsContextCA.class).error(ex.getMessage());
        }
    }
    
    /**
     * Reload the OWS-Context corresponding to the value of ConfigurationAttribute. 
     * @throws FileNotFoundException 
     */
    private void reloadSelectedOMC() throws FileNotFoundException{
        if(index!=-1){
            try {
                if(omc.isOpen()){
                    omc.close(new NullProgressMonitor());
                }
                omc.read(new FileInputStream(new File(getSelected())));
                omc.open(new NullProgressMonitor());
            } catch (LayerException ex) {
                LoggerFactory.getLogger(OwsContextCA.class).error(ex.getMessage());
            }
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
        if(index==-1 || list.size()==0){
            list.add("/");
            select("/");
        }
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

    @Override
    public void setField(String name, String value) {
        super.setField(name, value);
        if(name.equals("list"))
            list= Arrays.asList(value.split(","));
        if(name.equals("index"))
            index=Integer.parseInt(value);
    }

    public Map<String, Object> getSavableField() {
        Map ret = super.getSavableField();
        ret.put("index", index);
        String s="";
        for(String str : list)
            s+=str+",";
        if(list.size()>0)s=s.substring(0, s.length()-1);
        ret.put("list", s);
        return ret;
    }
}
