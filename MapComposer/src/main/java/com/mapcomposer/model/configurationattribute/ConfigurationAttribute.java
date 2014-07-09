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
    
    /**
     * Main public constructor doing the initialisation of the property and setting the name.
     * @param name Name of the property.
     */
    public ConfigurationAttribute(String name){
        this.name = name;
        t=null;
    }
    
    /**
     * Setter of the property value.
     * @param value New value of the property.
     */
    public void setPropertyValue(T value){
        this.t=value;
    }
    
    /**
     * Getter returning the property value.
     * @return The property value.
     */
    public T getPropertyValue(){
        return t;
    }
    
    /**
     * Getter returning the property name.
     * @return The property name.
     */
    public String getPropertyName(){
        return name;
    }
}
