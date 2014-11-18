package org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic;

import java.util.ArrayList;
import java.util.List;

import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceCA;
import org.orbisgis.mapcomposer.model.configurationattribute.utils.CAFactory;

/**
 * SimpleOrientationGE of the map (direction of the north in the map).
 */
public class Orientation extends SimpleCartoGE{
    
    /** Icon of the orientation*/
    private SourceCA icon;
    
    /**Main constructor*/
    public Orientation(){
        super();
        icon = CAFactory.createSourceCA("Path");
    }

    /**
     * Returns the icon source path.
     * @return The icon source path.
     */
    public String getIconPath() {return icon.getValue();}
    
    /**
     * Sets the icon source path.
     * @param path New path of the icon.
     */
    public void setIconPath(String path) {icon.setValue(path);}

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.addAll(super.getAllAttributes());
        list.add(icon);
        return list;
    }

    @Override
    public List<ConfigurationAttribute> getSavableAttributes() {
        List<ConfigurationAttribute> list = super.getSavableAttributes();
        list.addAll(super.getAllAttributes());
        list.add(icon);
        return list;
    }

    @Override
    public void setAttribute(ConfigurationAttribute ca) {
        super.setAttribute(ca);
        if(ca.getName().equals(icon.getName()))
            icon=(SourceCA)ca;
    }
}
