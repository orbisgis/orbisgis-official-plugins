package org.orbisgis.mapcomposer.model.configurationattribute.attribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;

import java.util.HashMap;
import java.util.Map;

/**
 * This class implements the ConfigurationAttribute (CA) interface and is a base for the natives CA.
 * All the methods implemented are common for each native CA.
 * It can also be used as a base to develop custom CA.
 */
public abstract class BaseCA<T> implements ConfigurationAttribute<T>{

    /** Name of the property. */
    private String name;

    /** Read-only field. It tells if the value can be modified or not. */
    private boolean readOnly;

    /**
     * Default constructor setting the name and the readOnly mode.
     * @param name Name of the ConfigurationAttribute
     * @param readOnly ReadOnly mode
     */
    public BaseCA(String name, boolean readOnly){
        this.name = name;
        this.readOnly = readOnly;
    }

    @Override public void setReadOnly(boolean readOnly) {this.readOnly = readOnly;}
    @Override public boolean getReadOnly()              {return readOnly;}

    @Override public String getName()           {return name;}
    @Override public void setName(String name)  {this.name = name;}
    
    @Override public boolean isSameName(ConfigurationAttribute configurationAttribute){
        return this.name.equals(configurationAttribute.getName());
    }

    @Override
    public void setField(String name, String value) {
        if(name.equals("name"))
            this.name=value;
        else if(name.equals("readOnly"))
            this.readOnly = Boolean.parseBoolean(value);
    }

    @Override
    public Map<String, Object> getAllFields() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("readOnly", readOnly);
        return map;
    }
}
