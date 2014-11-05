package org.orbisgis.mapcomposer.model.configurationattribute.attribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import java.awt.Color;

/**
 * Color ConfigurationAttribute.
 */
public class ColorCA extends BaseCA<Color> {
    /** Property itself.
     * As color can't be handle by jibx, the color is represented by the Red, Green, blue, Alpha value. */
    private int[] rgba;
    
    public ColorCA(){
        rgba=new int[4];
    }

    @Override public void setValue(Color value) {
        this.rgba[0]=value.getRed();
        this.rgba[1]=value.getGreen();
        this.rgba[2]=value.getBlue();
        this.rgba[3]=value.getAlpha();
    }

    @Override public Color getValue() {return new Color(rgba[0], rgba[1], rgba[2], rgba[3]);}

    @Override public boolean isSameValue(ConfigurationAttribute ca) {
        return ca.getValue().equals(this.getValue());
    }
    
}
