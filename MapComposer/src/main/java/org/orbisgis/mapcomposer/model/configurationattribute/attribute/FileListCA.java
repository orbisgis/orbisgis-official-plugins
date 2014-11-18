package org.orbisgis.mapcomposer.model.configurationattribute.attribute;

import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;
import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.RefreshCA;
import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represent a list of different files path.
 */
public class FileListCA extends BaseListCA<File> implements RefreshCA{
    /** Index of the value selected.*/
    private int index;
    /** Property itself */
    private List<String> list;

    @Override public void setValue(List<File> value) {
        list=new ArrayList<>();
        for(File f : value)
            list.add(f.getAbsolutePath());
    }
    @Override public List<File> getValue() {
        ArrayList<File> listF=new ArrayList<>();
        for(String s : list)
            listF.add(new File(s));
        return listF;
    }

    @Override public boolean isSameValue(ConfigurationAttribute ca) {
        if(ca instanceof ListCA){
            if(getSelected()==null)
                return false;
            return getSelected().equals(((ListCA)ca).getSelected());
        }
        return false;
    }

    @Override public void       add(File value)       {list.add(value.getAbsolutePath());}
    @Override public boolean    remove(File value)    {return this.list.remove(value.getAbsolutePath());}
    
    @Override public File       getSelected()         {return new File(list.get(index));}
    @Override public void       select(File choice)   {index=list.indexOf(choice.getAbsolutePath());}   

    /**
     * Verify if the files path still right.
     * If the file doesn't exist, it's path is removed from the list.
     */
    @Override
    public void refresh(UIController uic) {
        List<File> listT = new ArrayList<>();
        //Add to a list all the wrong path
        for(File f : this.getValue()){
            if(!f.exists()){
                listT.add(f);
            }
        }
        //Remove the files path
        for(File f : listT){
            this.remove(f);
        }
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
