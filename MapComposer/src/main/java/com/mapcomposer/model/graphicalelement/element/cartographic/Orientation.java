package com.mapcomposer.model.graphicalelement.element.cartographic;

import com.mapcomposer.Configuration;
import java.util.ArrayList;
import java.util.List;
import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Source;

/**
 * Orientation of the map (direction of the north in the map).
 */
public final class Orientation extends CartographicElement {
    
    /** Icon of the orientation*/
    private final Source icon;
    
    /**Main constructor*/
    public Orientation(){
        super();
        icon = new Source("Path");
        //TODO : set a default value
        setDefaultValue();
    }

    /**
     * Returns the icon source path.
     * @return The icon source path.
     */
    public String getIconPath() {
        return icon.getValue();
    }
    
    /**
     * Sets the icon source path.
     * @param path New path of the icon.
     */
    public void setIconPath(String path) {
        icon.setValue(path);
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.addAll(super.getAllAttributes());
        list.add(icon);
        return list;
    }
    
    private void setDefaultValue(){
        icon.setValue(Configuration.defaultIconPath);
    }
    
    public void setDefaultElementShutter(){
        super.setDefaultElementShutter();
        icon.setValue(Configuration.defaultESIconPath);
    }
}
