package com.mapcomposer.model.configurationattribute;

import java.util.ArrayList;
import java.util.List;

/**
 * This ConfigurationAttribute extended class represent a list of value.
 * It permits to manage the list of values with basic function such as add, remove, and also to set the selected value.
 * @param <T> Type of the values contained.
 */
public abstract class ListCA<T> extends ConfigurationAttribute<List<T>> {
    /** Index of the value selected.*/
    private int index;

    /**
     * Main constructor.
     * @param name Name of the value in its GraphicalElement.
     */
    public ListCA(String name) {
        super(name);
        super.setValue(new ArrayList<T>());
        this.index = -1;
    }
    
    /**
     * Appends the specified value to the end of the value list. 
     * @param value Value to appended to the list.
     */
    public void add(T value){
        this.getValue().add(value);
    }
    
    /**
     * Returns the the selected value. If the index isn't right, it returns NULL.
     * @return The value selected.
     */
    public T getSelected(){
        if(index>=0 && index<this.getValue().size()){
            return this.getValue().get(index);
        }
        else{
            return null;
        }
    }
    
    /**
     * Selects the value given in parameter.
     * @param choice Choice to select.
     */
    public void select(T choice){
        this.index = this.getValue().indexOf(choice);
    }
    
    
    /**
     * Removes the first occurrence of the specified value from this list, if it is present.
     * If the list does not contain the value, it is unchanged.
     * Returns true if this list contained the specified value (or equivalently, if this list changed as a result of the call). 
     * @param value Value to remove from the list.
     * @return True if this list contained the specified value, false otherwise.
     */
    public boolean remove(T value){
        if(this.getValue().indexOf(value)==this.index){
            this.index=-1;
        }
        return this.getValue().remove(value);
    }
}
