package com.mapcomposer.model.graphicalelement.element.cartographic;

import java.util.ArrayList;
import java.util.List;
import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Source;

/**
 * Orientation of the map (direction of the north in the map).
 */
public class Orientation extends CartographicElement {
    
    /** Icon of the orientation*/
    private final Source icon;
    
    /**Main constructor*/
    public Orientation(){
        super();
        icon = new Source("Path");
    }

    /**
     * Returns the icon source path.
     * @return The icon source path.
     */
    public String getIconPath() {
        return icon.getPropertyValue();
    }
    
    /**
     * Sets the icon source path.
     * @param path New path of the icon.
     */
    public void setIconPath(String path) {
        icon.setPropertyValue(path);
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.addAll(this.getAllAttributes());
        list.add(icon);
        return list;
    }

    @Override
    protected void initialisation() {
        this.setHeight(50);
        this.setWidth(50);
    }
}
