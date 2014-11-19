package org.orbisgis.mapcomposer.model.configurationattribute.attribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ListCA;

import java.util.HashMap;
import java.util.Map;

/**
 * Implementation of the common function of ConfigurationAttributes.
 */
public abstract class BaseListCA<T> implements ListCA<T>{
    /** Name of the property */
    private String name = "";
    /** Lock. If the property is locked, it can't be changed. */
    private boolean lock = false;

    public BaseListCA(){
        name="no_name";
        lock=false;
    }
    
    @Override public void setLock(boolean lock) {this.lock=lock;}
    @Override public boolean isLocked()         {return lock;}

    @Override public String getName()           {return name;}
    @Override public void setName(String name)  {this.name = name;}
    
    @Override public boolean isSameName(ConfigurationAttribute ca){
        return this.name.equals(ca.getName());
    }

    public void setField(String name, String value) {
        if(name.equals("name"))
            this.name=value;
        if(name.equals("lock"))
            lock = Boolean.parseBoolean(value);
    }

    public Map<String, Object> getSavableField() {
        Map<String, Object> map = new HashMap<>();
        map.put("name", name);
        map.put("lock", lock);
        return map;
    }
}
