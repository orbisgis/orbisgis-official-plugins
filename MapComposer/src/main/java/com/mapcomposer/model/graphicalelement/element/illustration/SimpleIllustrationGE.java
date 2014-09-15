package com.mapcomposer.model.graphicalelement.element.illustration;

import com.mapcomposer.model.graphicalelement.interfaces.IllustrationElement;
import com.mapcomposer.Configuration;
import java.util.ArrayList;
import java.util.List;
import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.SourceCA;
import com.mapcomposer.model.graphicalelement.element.SimpleGE;

/**
 * Root class for illustration GraphicalElements.
 */
public class SimpleIllustrationGE extends SimpleGE implements IllustrationElement{
    
    /** Path to the data source of the element.*/;
    private final SourceCA path;
    
    /**Main constructor.*/
    public SimpleIllustrationGE(){
        path = new SourceCA();
        path.setName("Path");
        
        setDefaultValue();
    }
    
    /**
     * Returns the absolute path value.
     * @return The absolute path string to the source.
     */
    public String getPath(){
        return path.getValue();
    }
    
    /**
     * Sets the absolute path to the source.
     * @param absolutePath New absolute path to the source.
     */
    public void setPath(String absolutePath){
        this.path.setValue(absolutePath);
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.addAll(super.getAllAttributes());
        list.add(path);
        return list;
    }
    
    private void setDefaultValue(){
        path.setValue(Configuration.defaultImagePath);
    }
}
