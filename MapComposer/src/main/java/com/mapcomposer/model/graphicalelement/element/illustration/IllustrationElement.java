package com.mapcomposer.model.graphicalelement.element.illustration;

import java.util.ArrayList;
import java.util.List;
import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Source;
import com.mapcomposer.model.graphicalelement.GraphicalElement;

/**
 * Root class for illustration GraphicalElements.
 */
public abstract class IllustrationElement extends GraphicalElement{
    
    /** Path to the data source of the element.*/;
    private final Source path;
    
    /**Main constructor.*/
    public IllustrationElement(){
        path = new Source("Path");
    }
    
    /**
     * Returns the absolute path value.
     * @return The absolute path string to the source.
     */
    public String getPath(){
        return path.getPropertyValue();
    }
    
    /**
     * Sets the absolute path to the source.
     * @param absolutePath New absolute path to the source.
     */
    public void setPath(String absolutePath){
        this.path.setPropertyValue(absolutePath);
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.addAll(this.getAllAttributes());
        list.add(path);
        return list;
    }
}
