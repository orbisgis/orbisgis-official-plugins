package org.orbisgis.mapcomposer.model.configurationattribute.attribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;

import java.util.Map;

/**
 * This class represent a text or a string and extends the BaseCA abstract class.
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.BaseCA
 */
public class StringCA extends BaseCA<String>{

    /** Property itself */
    private String value;

    /**
     * Empty constructor used in the SaveHandler, on loading a project save.
     */
    public StringCA(){
        super("void", false);
        value = "";
    }

    /**
     * Default constructor for the StringCA class.
     * @param name Name of the ConfigurationAttribute
     * @param readOnly Read-only mode
     * @param string Value contained by the CA
     */
    public StringCA(String name, boolean readOnly, String string){
        super(name, readOnly);
        value = string;
    }
    
    @Override public void setValue(String value) {this.value=value;}

    @Override public String getValue() {return value;}

    @Override public boolean isSameValue(ConfigurationAttribute configurationAttribute) {
        return configurationAttribute.getValue().equals(value);
    }

    @Override
    public void setField(String name, String value) {
        super.setField(name, value);
        if(name.equals("value"))
            this.value=value;
    }

    @Override
    public Map<String, Object> getAllFields() {
        Map ret = super.getAllFields();
        ret.put("value", value);
        return ret;
    }
}
