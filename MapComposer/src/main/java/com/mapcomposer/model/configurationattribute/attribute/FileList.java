package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a list of differents files path.
 */
public class FileList extends ConfigurationAttribute<List<String>>{
    /** Index of the file path selected*/
    private int index = -1;

    public FileList(String name) {
        super(name);
        this.setPropertyValue(new ArrayList<String>());
    }
    /**
     * Appends the specified file path to the end of this list. 
     * @param filePath filename to appended to the list
     */
    public void add(String filePath){
        this.getPropertyValue().add(filePath);
    }
    
    /**
     * Returns the the selected file path. If the index isn't right, it returns NULL.
     * @return The file path selected.
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
     * Sets the selected file path.
     * @param filePath Name of the file path to select.
     */
    public void select(String filePath){
        this.index = this.getPropertyValue().indexOf(filePath);
    }
    
    /**
     * Removes the first occurrence of the specified choice from this list, if it is present.
     * If the list does not contain the filename, it is unchanged.
     * Returns true if this list contained the specified filename (or equivalently, if this list changed as a result of the call). 
     * @param filePath File path to remove from the list.
     * @return True if this list contained the specified file path, false otherwise.
     */
    public boolean remove(String filePath){
        if(this.getPropertyValue().indexOf(filePath)==index){
            this.index=-1;
        }
        return this.getPropertyValue().remove(filePath);
    }
    
}
