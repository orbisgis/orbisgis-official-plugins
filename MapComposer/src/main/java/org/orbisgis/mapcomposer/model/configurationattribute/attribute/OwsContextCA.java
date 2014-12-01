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
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.layerModel.OwsMapContext;
import org.orbisgis.progress.NullProgressMonitor;
import org.slf4j.LoggerFactory;

/**
 * This class represent a list of path to OwsMapContext files created by the user in OrbisGIS.
 * The OwsMapContexts are saved in the workspace. The class use the OrbisGIS functions (thanks to the LinkToOrbisGIS class) to get and load them.
 * It contains all the information of the map and permit to get the map rendering, the scale value ...
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.BaseListCA
 */
public class OwsContextCA extends BaseListCA<String> implements RefreshCA{
    /** Index of the value selected.*/
    private int index;

    /** Property itself */
    private List<String> list;

    /** OwsMapContext corresponding to the path selected in the list.*/
    private OwsMapContext omc;

    /**
     * Empty constructor used in the SaveHandler, on loading a project save.
     */
    public OwsContextCA(){
        super("void", false);
        this.list  = new ArrayList<>();
    }

    /**
     * Default constructor for the OwsContextCA.
     * @param name Name of the ConfigurationAttribute
     * @param readOnly Boolean to enable or not the modification of the CA
     */
    public OwsContextCA(String name, boolean readOnly){
        super(name, readOnly);
        list = new ArrayList<>();
        index = 0;
        omc = null;
    }

    /**
     * Return the OwsMapContext contained represented by the selected path in the list.
     * @return The OwsMapContext selected
     */
    public OwsMapContext getOwsMapContext(){return omc;}

    @Override
    public void refresh(UIController uic) {
        //Refresh of the file list
        loadListFiles();
        //Refresh of the selected file
        if(omc==null)
            omc = new OwsMapContext(LinkToOrbisGIS.getInstance().getDataManager());
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
     * Go to the workspace and add to the list all the .ows files.
     */
    private void loadListFiles(){
        File f = new File(LinkToOrbisGIS.getInstance().getViewWorkspace().getCoreWorkspace().getWorkspaceFolder()+"/maps/");
        //save the selected path
        String s = getSelected();
        list=new ArrayList<>();
        //add all the files in the maps folder of the workspace
        for(File file : f.listFiles()){
            if(file.getAbsolutePath().toLowerCase().contains(".ows"))
                list.add(file.getAbsolutePath());
        }
        //try to reselect the path saved
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

    @Override public boolean isSameValue(ConfigurationAttribute configurationAttribute) {
        if(configurationAttribute instanceof ListCA){
            if(getSelected()==null)
                return false;
            return getSelected().equals(((ListCA) configurationAttribute).getSelected());
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

    @Override
    public Map<String, Object> getAllFields() {
        Map ret = super.getAllFields();
        ret.put("index", index);
        String s="";
        for(String str : list)
            s+=str+",";
        if(list.size()>0)s=s.substring(0, s.length()-1);
        ret.put("list", s);
        return ret;
    }
}
