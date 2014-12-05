package org.orbisgis.mapcomposer.model.configurationattribute.attribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import java.awt.Color;
import java.util.Map;

/**
 * This CA represent a color field (like text color or background color) and extends the abstract class BaseCA.
 * On saving, only string type can be used, so the color is saved in a String containing 4 int : red, green, blue, alpha.
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.BaseCA
 */
public class ColorCA extends BaseCA<Color> {

    /** Property itself.*/
    private Color color;

    /**
     * Empty constructor used in the SaveHandler, on loading a project save.
     */
    public ColorCA(){
        super("void", false);
        color = Color.GRAY;
    }

    /**
     * Default constructor of the ColorCA.
     * @param name Name of the ConfigurationAttribute
     * @param readOnly Boolean to enable or not the modification of the CA
     * @param color
     */
    public ColorCA(String name, boolean readOnly, Color color){
        super(name, readOnly);
        this.color = color;
    }

    @Override public void setValue(Color value) {
        this.color = value;
    }

    @Override public Color getValue() {return color;}

    @Override public boolean isSameValue(ConfigurationAttribute configurationAttribute) {
        return configurationAttribute.getValue().equals(this.getValue());
    }


    @Override
    public void setField(String name, String value) {
        super.setField(name, value);
        if(name.equals("color")) {
            //Decode the string value of the color from String[] to Color
            String[] s = value.split(",");
            this.color = new Color(Integer.parseInt(s[0]), Integer.parseInt(s[1]), Integer.parseInt(s[2]), Integer.parseInt(s[3]));
        }
    }

    @Override
    public Map<String, Object> getAllFields() {
        Map ret = super.getAllFields();
        //Save the color field as a String like "r,g,b,a" corresponding ot the red,green,blue,alpha value of the color
        ret.put("color", color.getRed() + "," + color.getGreen() + "," + color.getBlue() + "," + color.getAlpha());
        return ret;
    }

}
