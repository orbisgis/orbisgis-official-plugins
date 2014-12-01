package org.orbisgis.mapcomposer.model.configurationattribute.attribute;

import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;
import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.RefreshCA;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * This class represent a list of source and extends the BaseListCA abstract class.
 * A source can be a list of path to files or a list of values (like up, down, left, right).
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.BaseListCA
 */
public class SourceListCA extends BaseListCA<String> implements RefreshCA{
    /** Index of the value selected.*/
    private int index;

    /** Property itself */
    private List<String> list;

    /**
     * Empty constructor used in the SaveHandler, on loading a project save.
     */
    public SourceListCA(){
        super("void", false);
        this.list  = new ArrayList<>();
    }

    /**
     * Default constructor for the SourceListCA.
     * @param name Name of the ConfigurationAttribute
     * @param readOnly Boolean to enable or not the modification of the CA
     */
    public SourceListCA(String name, boolean readOnly){
        super(name, readOnly);
        index = 0;
        list = new ArrayList<>();
    }

    @Override public void setValue(List<String> value) {this.list=value;}
    @Override public List<String> getValue() {return list;}

    @Override public boolean isSameValue(ConfigurationAttribute configurationAttribute) {
        if(configurationAttribute instanceof ListCA){
            if(getSelected()==null)
                return false;
            return getSelected().equals(((ListCA) configurationAttribute).getSelected());
        }
        return false;
    }

    @Override public void       add(String value)       {list.add(value);}
    @Override public boolean    remove(String value)    {return this.list.remove(value);}
    
    @Override public String     getSelected()           {return list.get(index);}
    @Override public void       select(String choice)   {index=list.indexOf(choice);}

    @Override
    public void refresh(UIController uic) {
        List<String> temp = new ArrayList<>();
        for(String s : list)
            if(!temp.contains(s)) temp.add(s);
        list=temp;
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
