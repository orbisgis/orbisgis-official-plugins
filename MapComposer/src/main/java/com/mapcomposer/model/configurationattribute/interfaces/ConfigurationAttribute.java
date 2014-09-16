package com.mapcomposer.model.configurationattribute.interfaces;

/**
 * A ConfigurationAttribute (CA) represent a single property of a GraphicalElement (GE).
 * Creating a class to represent a complex attribute (such as a path to an OWS-Context) permit to simplify its use and to make lighter the GE class.
 * It also allows the declaration of several methods to manage its value and to create a common rendering methods for a COnfigurationAttribute class display into the ConfigurationShutter in the UI.
 * @param <T> Class of the property represented. As example, for a text attribute the class should be String.
 */
public interface ConfigurationAttribute<T> {
    
    /**
     * Sets the lock state of the ConfigurationAttribute.
     * @param lock Lock state of the CA.
     */
    public void setLock(boolean lock);
    
    /**
     * Return the lock state of the ConfigurationAttribute
     * @return The lock state.
     */
    public boolean isLocked();
    
    /**
     * Setter of the property value.
     * @param value New value of the property.
     * @return True if the value was setted, false otherwise.
     */
    public boolean setValue(T value);
    
    /**
     * Getter returning the property value.
     * @return The property value.
     */
    public T getValue();
    
    /**
     * Getter returning the property name.
     * @return The property name.
     */
    public String getName();
    
    /**
     * Compare if two ConfigurationAttributes represents the same property.
     * @param ca ConbfigurationAttribute to compare with
     * @return True if the property represented is the same, false otherwise.
     */
    public boolean isSameProperty(ConfigurationAttribute ca);
    
    @Override
    public boolean equals(Object other);
    
    @Override
    public int hashCode();
}
