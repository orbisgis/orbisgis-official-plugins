package com.mapcomposer.model.configurationattribute;

/**
 * A ConfigurationAttribute (CA) represent a single property of a GraphicalElement (GE).
 * Creating a class to represent a complex attribute (such as a path to an OWS-Context) permit to simplify its use and to make lighter the GE class.
 * It also allows the declaration of several methods to manage its value and to create an common rendering methods for its display into the ConfigurationShutter in the UI.
 * @param <T> Class of the property represented. As example, for a text attribute the class should be String.
 */
public abstract class ConfigurationAttribute<T> {
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
    public ConfigurationAttribute(String name){
        this.name = name;
        t=null;
        lock=false;
    }
    
    /**
     * Lock the ConfigurationAttribute.
     */
    public void lock(){
        lock=true;
    }
    
    
    /**
     * Unlock the ConfigurationAttribute.
     */
    public void unlock(){
        lock=false;
    }
    
    /**
     * Return the lock state of the ConfigurationAttribute
     * @return The lock state.
     */
    public boolean isLocked(){
        return lock;
    }
    
    /**
     * Setter of the property value.
     * @param value New value of the property.
     * @return True if the value was setted, false otherwise.
     */
    public boolean setValue(T value){
        if(!lock)
            this.t=value;
        return lock;
    }
    
    /**
     * Getter returning the property value.
     * @return The property value.
     */
    public T getValue(){
        return t;
    }
    
    /**
     * Getter returning the property name.
     * @return The property name.
     */
    public String getName(){
        return name;
    }
}
