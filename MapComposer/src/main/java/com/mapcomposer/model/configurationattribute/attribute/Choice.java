package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import java.util.ArrayList;
import java.util.List;

/**
 * The Choice attribute contains severals fields that can be selected.
 */
public class Choice extends ConfigurationAttribute<List<String>>{
    /** Index of the choice selected.*/
    private int index = -1;

    /**
     * Main constructor.
     * @param name Name of the Choice in its GraphicalElement.
     */
    public Choice(String name) {
        super(name);
        this.setPropertyValue(new ArrayList<String>());
    }
    
    /**
     * Appends the specified field to the end of the choice list. 
     * @param choice Choice to appended to the list.
     */
    public void add(String choice){
        this.getPropertyValue().add(choice);
    }
    
    /**
     * Returns the the selected Choice. If the index isn't right, it returns NULL.
     * @return The choice selected.
     */
    public String getSelected(){
        if(index>0 && index<this.getPropertyValue().size()){
            return this.getPropertyValue().get(index);
        }
        else{
            return null;
        }
    }
    
    /**
     * Sets the selection to the choice given in parameter.
     * @param choice Choice to select.
     */
    public void select(String choice){
        this.index = this.getPropertyValue().indexOf(choice);
    }
    
    /**
     * Removes the first occurrence of the specified choice from this list, if it is present.
     * If the list does not contain the choice, it is unchanged.
     * Returns true if this list contained the specified choice (or equivalently, if this list changed as a result of the call). 
     * @param choice Choice to remove from the list.
     * @return True if this list contained the specified choice, false otherwise.
     */
    public boolean remove(String choice){
        if(this.getPropertyValue().indexOf(choice)==this.index){
            this.index=-1;
        }
        return this.getPropertyValue().remove(choice);
    }
    
}
