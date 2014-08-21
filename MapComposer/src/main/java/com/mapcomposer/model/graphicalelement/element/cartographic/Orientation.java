package com.mapcomposer.model.graphicalelement.element.cartographic;

import com.mapcomposer.Configuration;
import java.util.ArrayList;
import java.util.List;
import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Source;
import com.mapcomposer.model.graphicalelement.GraphicalElement;

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
        icon.setValue(".");
    }
    
    /**
     * Clone constructor.
     * @param ge
     */
    public Orientation(Orientation ge){
        super(ge);
        icon = ge.icon;
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
    
    @Override
    public Class<? extends GraphicalElement> getCommonClass(Class<? extends GraphicalElement> c) {
        if(c.isAssignableFrom(this.getClass()))
            return c;
        else if(c.isAssignableFrom(CartographicElement.class))
            return CartographicElement.class;
        else
            return GraphicalElement.class;
    }
    
    private void setDefaultValue(){
        icon.setValue(Configuration.defaultIconPath);
    }
    
    public void setDefaultElementShutter(){
        super.setDefaultElementShutter();
        icon.setValue(Configuration.defaultESIconPath);
    }
}
