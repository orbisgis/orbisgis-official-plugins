package com.mapcomposer.model.graphicalelement.element.cartographic;

import com.mapcomposer.Configuration;
import java.util.ArrayList;
import java.util.List;
import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.SourceCA;
import com.mapcomposer.model.configurationattribute.utils.CAFactory;

/**
 * Orientation of the map (direction of the north in the map).
 */
public final class Orientation extends SimpleCartoGE {
    
    /** Icon of the orientation*/
    private final SourceCA icon;
    
    /**Main constructor*/
    public Orientation(){
        super();
        icon = CAFactory.createSourceCA("Path");
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
}
