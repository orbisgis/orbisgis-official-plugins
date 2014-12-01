package org.orbisgis.mapcomposer.model.configurationattribute.attribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;

import java.util.Map;

/**
 * The Source attribute contain the path to a specified data source like data, image ...
 * It extends the BaseCA abstract class.
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.BaseCA
 */
public class SourceCA extends BaseCA<String>{
    /** Property itself */
    private String value;

    /**
     * Empty constructor used in the SaveHandler, on loading a project save.
     */
    public SourceCA(){
        super("void", false);
        value = "";
    }

    /**
     * Default constructor for the SourceCA.
     * @param name Name of the ConfigurationAttribute
     * @param readOnly Boolean to enable or not the modification of the CA
     */
    public SourceCA(String name, boolean readOnly){
        super(name, readOnly);
        value = "";
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
