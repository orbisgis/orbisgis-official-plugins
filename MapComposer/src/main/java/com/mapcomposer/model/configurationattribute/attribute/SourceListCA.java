package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.controller.UIController;
import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.interfaces.ListCA;
import com.mapcomposer.model.configurationattribute.interfaces.RefreshCA;
import java.util.ArrayList;
import java.util.List;

/**
 * String List ConfigurationAttribute.
 * StringListCA implements ListCA and contain String fields.
 */
public class SourceListCA extends BaseListCA<String> implements RefreshCA{
    /** Index of the value selected.*/
    private int index;
    /** Property itself */
    private List<String> list;
    
    public SourceListCA(){
        index=-1;
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

    @Override
    public void refresh(UIController uic) {
        List<String> temp = new ArrayList<>();
        for(String s : list)
            if(!temp.contains(s)) temp.add(s);
        list=temp;
    }
}
