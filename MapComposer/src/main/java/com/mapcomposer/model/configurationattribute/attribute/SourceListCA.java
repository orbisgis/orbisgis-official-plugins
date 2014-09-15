package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.interfaces.ListCA;
import java.util.ArrayList;
import java.util.List;

/**
 * String List ConfigurationAttribute.
 * StringListCA implements ListCA and contain String fields.
 */
public class SourceListCA extends BaseListCA<String>{
    /** Index of the value selected.*/
    private int index;
    /** Property itself */
    private List<String> list;
    
    public SourceListCA(){
        index=-1;
        list=new ArrayList<>();
    }

    @Override public void setValue(List<String> value) {this.list=value;}
    @Override public List<String> getValue() {return list;}

    @Override public boolean isSameValue(ConfigurationAttribute ca) {
        if(ca instanceof ListCA){
            if(getSelected()==null)
                return false;
            return getSelected().equals(((ListCA)ca).getSelected());
        }
        return false;
    }

    @Override public void       add(String value)       {list.add(value);}
    @Override public boolean    remove(String value)    {return this.list.remove(value);}
    
    @Override public String     getSelected()           {return list.get(index);}
    @Override public void       select(String choice)   {index=list.indexOf(choice);}
}
