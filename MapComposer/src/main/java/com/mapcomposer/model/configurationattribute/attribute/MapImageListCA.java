package com.mapcomposer.model.configurationattribute.attribute;
import com.mapcomposer.controller.UIController;
import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.interfaces.ListCA;
import com.mapcomposer.model.configurationattribute.interfaces.RefreshCA;
import com.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import com.mapcomposer.model.graphicalelement.element.cartographic.SimpleMapImageGE;
import java.util.List;

/**
 * The attribute contain the link to a MapImage
 */
public class MapImageListCA extends BaseListCA<SimpleMapImageGE> implements RefreshCA{
    /** Index of the value selected.*/
    private int index;
    /** Property itself */
    private List<SimpleMapImageGE> list;
    
    public MapImageListCA(){
        index=-1;
    }
        
    @Override public void setValue(List<SimpleMapImageGE> value) {this.list=value;}
    @Override public List<SimpleMapImageGE> getValue() {return list;}

    @Override public boolean isSameValue(ConfigurationAttribute ca) {
        if(ca instanceof ListCA){
            if(getSelected()==null)
                return false;
            return getSelected().equals(((ListCA)ca).getSelected());
        }
        return false;
    }

    @Override public void       add(SimpleMapImageGE value)       {list.add(value);}
    @Override public boolean    remove(SimpleMapImageGE value)    {return this.list.remove(value);}
    
    @Override public SimpleMapImageGE     getSelected(){
        if(index>=0 && index<list.size())
            return list.get(index);
        else
            return null;
    }
    @Override public void       select(SimpleMapImageGE choice)   {index=list.indexOf(choice);}

    /**
     * Verify if the files path still right.
     * If the file doesn't exist, it's path is removed from the list.
     */
    @Override
    public void refresh() {
        for(SimpleMapImageGE mi : this.getValue()){
            if(!UIController.getInstance().getGEMap().containsKey(mi)){
                this.remove(((SimpleMapImageGE)mi));
            }
        }
        
        for(Object ge : UIController.getInstance().getGEMap().keySet().toArray()){
            if(ge instanceof MapImage){
                if(!this.getValue().contains((SimpleMapImageGE)ge)){
                    this.add(((MapImage)ge));
                }
            }
        }
        
    }
}
