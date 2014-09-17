package com.mapcomposer.model.graphicalelement.element.cartographic;

import java.util.ArrayList;
import java.util.List;
import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.SourceCA;
import com.mapcomposer.model.configurationattribute.utils.CAFactory;

/**
 * SimpleOrientationGE of the map (direction of the north in the map).
 */
public class SimpleOrientationGE extends SimpleCartoGE{
    
    /** Icon of the orientation*/
    private SourceCA icon;
    
    /**Main constructor*/
    public SimpleOrientationGE(){
        super();
        icon = CAFactory.createSourceCA("Path");
    }
    
    /**
     * Copy constructor.
     * @param soge SimpleOrientationGE to copy.
     */
    public SimpleOrientationGE(SimpleOrientationGE soge){
        super(soge);
        icon = soge.icon;
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
}
