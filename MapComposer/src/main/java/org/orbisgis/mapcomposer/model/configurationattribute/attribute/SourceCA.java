package org.orbisgis.mapcomposer.model.configurationattribute.attribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;

import java.util.Map;

/**
 * The Source attribute contain the path to a specified data source like OWS-Context, data, image ...
 */
public class SourceCA extends BaseCA<String>{
    /** Property itself */
    private String value;
    
    @Override public void setValue(String value) {this.value=value;}

    @Override public String getValue() {return value;}

    @Override public boolean isSameValue(ConfigurationAttribute ca) {
        return ca.getValue().equals(value);
    }

    @Override
    public void setField(String name, String value) {
        super.setField(name, value);
        if(name.equals("value"))
            this.value=value;
    }

    public Map<String, Object> getSavableField() {
        Map ret = super.getSavableField();
        ret.put("value", value);
        return ret;
    }
}
