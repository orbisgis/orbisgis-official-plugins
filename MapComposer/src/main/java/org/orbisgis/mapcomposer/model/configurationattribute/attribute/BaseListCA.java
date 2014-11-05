package org.orbisgis.mapcomposer.model.configurationattribute.attribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ListCA;

/**
 * Implementation of the common function of ConfigurationAttributes.
 */
public abstract class BaseListCA<T> implements ListCA<T>{
    /** Name of the property */
    private String name;
    /** Lock. If the property is locked, it can't be changed. */
    private boolean lock;

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
    
}
