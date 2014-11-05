package org.orbisgis.mapcomposer.model.configurationattribute.attribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;

/**
 * The Source attribute contain the path to a specified data source like OWS-Context, data, image ...
 */
public final class SourceCA extends BaseCA<String>{
    /** Property itself */
    private String value;
    
    public SourceCA(){}
    
    @Override public void setValue(String value) {this.value=value;}

    @Override public String getValue() {return value;}

    @Override public boolean isSameValue(ConfigurationAttribute ca) {
        return ca.getValue().equals(value);
    }
}
