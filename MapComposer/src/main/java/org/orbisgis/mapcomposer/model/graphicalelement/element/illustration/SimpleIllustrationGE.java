package org.orbisgis.mapcomposer.model.graphicalelement.element.illustration;

import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.IllustrationElement;
import org.orbisgis.mapcomposer.model.graphicalelement.element.SimpleGE;

import java.util.ArrayList;
import java.util.List;

/**
 * This class is a simple implementation of the IllustrationElement interface and extends the SimpleGE class.
 */
public abstract class SimpleIllustrationGE extends SimpleGE implements IllustrationElement{
    
    /** Path to the data source of the element.*/;
    private SourceCA path;

    /** Last source path used by a SimpleIllustrationGE **/
    private static String lastPathSet = "";
    
    /**Main constructor.*/
    public SimpleIllustrationGE(){
        super();
        path = new SourceCA("Path", false);
        path.setValue(lastPathSet);
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
        lastPathSet = absolutePath;
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.add(path);
        return list;
    }

    @Override
    public List<ConfigurationAttribute> getSavableAttributes() {
        List<ConfigurationAttribute> list = super.getSavableAttributes();
        list.add(path);
        return list;
    }

    @Override
    public void setAttribute(ConfigurationAttribute ca) {
        super.setAttribute(ca);
        if(ca.getName().equals(path.getName())) {
            path = (SourceCA) ca;
            lastPathSet = ((SourceCA)ca).getValue();
        }
    }
}
