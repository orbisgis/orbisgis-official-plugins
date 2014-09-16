package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.interfaces.RefreshCA;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * This class represent a list of different files path.
 */
public final class FileList extends SimpleListCA<String> implements RefreshCA{
    
    /**
     * Main constructor.
     * @param name Name of the FileList in its GraphicalElement.
     */
    public FileList(String name) {
        super(name);
    }    

    /**
     * Verify if the files path still right.
     * If the file doesn't exist, it's path is removed from the list.
     */
    @Override
    public void refresh() {
        List<String> list = new ArrayList<>();
        //Add to a list all the wrong path
        for(String path : this.getValue()){
            if(!(new File(path)).exists()){
                list.add(path);
            }
        }
        //Remove the files path
        for(String path : list){
            this.remove(path);
        }
    }
}
