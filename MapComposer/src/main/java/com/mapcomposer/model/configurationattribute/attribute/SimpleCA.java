package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import java.util.Objects;

/**
 * Simple ConfigurationAttribute.
 * ConfigurationAttribute simple implementation allowing to create simples CA.
 * All the interface functions are implemented.
 * @param <T> Class of the property represented. As example, for a text attribute the class should be String.
 */
public abstract class SimpleCA<T> implements ConfigurationAttribute<T> {
    /** Name of the property */
    private final String name;
    /** Property itself */
    private T t;
    /** Lock. If the property is locked, it can't be changed. */
    private boolean lock;
    
    /**
     * Main public constructor doing the initialisation of the property and setting the name.
     * @param name Name of the property.
     */
    public SimpleCA(String name){
        this.name = name;
        t=null;
        lock=false;
    }

    @Override
    public void setLock(boolean lock){
        this.lock=lock;
    }

    @Override
    public boolean isLocked(){
        return lock;
    }
    
    @Override
    public boolean setValue(T value){
        if(!lock)
            this.t=value;
        return lock;
    }
    
    @Override
    public T getValue(){
        return t;
    }
    
    @Override
    public String getName(){
        return name;
    }
    
    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if(!(other instanceof SimpleCA)) return false;
        else {
            return ((ConfigurationAttribute)other).getName().equals(name) &&
                   ((ConfigurationAttribute)other).getValue().equals(t);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.name);
        hash = 13 * hash + Objects.hashCode(this.t);
        return hash;
    }

    @Override
    public boolean isSameProperty(ConfigurationAttribute ca) {
        return this.getName().equals(ca.getName());
    }
}
