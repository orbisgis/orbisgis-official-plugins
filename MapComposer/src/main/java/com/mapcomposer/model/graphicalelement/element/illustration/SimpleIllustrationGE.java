package com.mapcomposer.model.graphicalelement.element.illustration;

import com.mapcomposer.model.configurationattribute.attribute.SourceCA;
import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.utils.CAFactory;
import com.mapcomposer.model.graphicalelement.interfaces.IllustrationElement;
import com.mapcomposer.model.graphicalelement.element.SimpleGE;
import java.util.ArrayList;
import java.util.List;

/**
 * Root class for illustration GraphicalElements.
 */
public class SimpleIllustrationGE extends SimpleGE implements IllustrationElement{
    
    /** Path to the data source of the element.*/;
    private SourceCA path;
    
    /**Main constructor.*/
    public SimpleIllustrationGE(){
        super();
        path = CAFactory.createSourceCA("Path");
    }
    
    /**Copy constructor.*/
    public SimpleIllustrationGE(SimpleIllustrationGE sige){
        super(sige);
        path = sige.path;
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
        list.add(path);
        return list;
    }
}
