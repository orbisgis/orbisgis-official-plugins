package org.orbisgis.mapcomposer.model.configurationattribute.interfaces;

import java.util.List;

/**
 * This ConfigurationAttribute extended interface represent a list of value.
 * It permits to manage the list of values with basic function such as add, remove, and also to set the selected value.
 * @param <T> Type of the values contained.
 */
public interface ListCA<T> extends ConfigurationAttribute<List<T>> {
    /**
     * Appends the specified value to the end of the value list. 
     * @param value Value to appended to the list.
     */
    public void add(T value);
    
    /**
     * Returns the the selected value. If the index isn't right, it returns NULL.
     * @return The value selected.
     */
    public T getSelected();
    
    /**
     * Selects the value given in parameter.
     * @param choice Choice to select.
     */
    public void select(T choice);
    
    
    /**
     * Removes the first occurrence of the specified value from this list, if it is present.
     * If the list does not contain the value, it is unchanged.
     * Returns true if this list contained the specified value (or equivalently, if this list changed as a result of the call). 
     * @param value Value to remove from the list.
     * @return True if this list contained the specified value, false otherwise.
     */
    public boolean remove(T value);
}
