package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;

/**
 * Text attribute representing a simple text.
 */
public final class StringCA extends BaseCA<String>{
    /** Property itself */
    private String value;
    
    public StringCA(){
    }
    
    @Override public void setValue(String value) {this.value=value;}

    @Override public String getValue() {return value;}

    @Override public boolean isSameValue(ConfigurationAttribute ca) {
        return ca.getValue().equals(value);
    }
}
