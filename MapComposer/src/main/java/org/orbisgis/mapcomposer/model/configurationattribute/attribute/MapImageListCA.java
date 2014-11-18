package org.orbisgis.mapcomposer.model.configurationattribute.attribute;
import org.mozilla.javascript.edu.emory.mathcs.backport.java.util.Arrays;
import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.RefreshCA;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;

import java.util.List;
import java.util.Map;

/**
 * The attribute contain the link to a MapImage
 */
public class MapImageListCA extends BaseListCA<String> implements RefreshCA{
    /** Index of the value selected.*/
    private int index;
    /** Property itself */
    private List<String> list;
    private MapImage mi;
        
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
    
    @Override public String getSelected(){
        if(index>=0 && index<list.size())
            return list.get(index);
        else
            return null;
    }
    @Override public void       select(String choice)   {index=list.indexOf(choice);}

    /**
     * Verify if the files path still right.
     * If the file doesn't exist, it's path is removed from the list.
     */
    @Override
    public void refresh(UIController uic) {
        for(String mi : this.getValue()){
            boolean flag=false;
            for(GraphicalElement ge : uic.getGEMap().keySet())
                if(ge instanceof MapImage)
                    if(ge.toString().equals(mi)) {
                        flag = true;
                        this.mi=(MapImage)ge;
                    }
            if(!flag)
                this.remove(mi);
        }
        
        for(Object ge : uic.getGEMap().keySet().toArray()){
            if(ge instanceof MapImage){
                if(!this.getValue().contains(ge.toString())){
                    this.add(ge.toString());
                }
            }
        }
        
    }

    public MapImage getMapImage(){
        return mi;
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
