package org.orbisgis.mapcomposer.model.configurationattribute.interfaces;

import java.util.Map;

/**
 * A ConfigurationAttribute (CA) represent a single property of a GraphicalElement (GE) like the width, the position, the text color ...
 * Creating a class to represent a complex attribute (such as a path to a source file) permit to simplify its use and makes lighter the GE class (its fields can manage themselves).
 * A ConfigurationAttribute contain at least 3 fields :
 *  - The value of the property represented by the ConfigurationAttribute
 *  - The name of the property
 *  - A boolean telling if the ConfigurationAttribute is in read-only mode or not
 *
 * @param <T> The Class of the property represented. For example, for a text attribute the class should be String.
 */
public interface ConfigurationAttribute<T> {

     /*************************/
    /** Setters and getters **/
   /*************************/

    /**
     * Sets if the ConfigurationAttribute is in read-only mode or not.
     * This field is used on configuring a GraphicalElement.
     * When several GraphicalElements are selected, if two same ConfigurationAttributes have different value, they are in read-only mode.
     * e.g. : Two TextElement have the text color ConfigurationAttribute. If the value of first one is red and the second is green, the text color ConfigurationAttributes is in read-only mode.
     * @param readOnly True if the ConfigurationAttribute can be modified, false otherwise.
     */
    public void setReadOnly(boolean readOnly);
    
    /**
     * Returns if the ConfigurationAttribute is in read-only mode or not.
     * @return True if the ConfigurationAttribute is in read-only mode, false otherwise.
     */
    public boolean getReadOnly();
    
    /**
     * Sets the value of the property represented by the ConfigurationAttribute.
     * @param value New value of the property represented by the ConfigurationAttribute.
     */
    public void setValue(T value);
    
    /**
     * Returns the value of the property represented by the ConfigurationAttribute.
     * @return The value of the property.
     */
    public T getValue();
    
    /**
     * Sets the name of the property represented by the ConfigurationAttribute.
     * @param name The name of the property.
     */
    public void setName(String name);
    
    /**
     * Returns the name of the property represented by the ConfigurationAttribute.
     * @return The name of the property.
     */
    public String getName();

     /***********************/
    /** Comparison methods */
   /***********************/
    
    /**
     * Returns true if the two ConfigurationAttributes represents the same property (if they have the same name), false otherwise.
     * @param configurationAttribute ConfigurationAttribute to compare with.
     * @return True if the property represented is the same for the two ConfigurationAttribute, false otherwise.
     */
    public boolean isSameName(ConfigurationAttribute configurationAttribute);
    
    /**
     * Returns true if the value contained by the two ConfigurationAttributes are the equals, false otherwise.
     * @param configurationAttribute ConfigurationAttribute to compare with
     * @return True if the values of the two ConfigurationAttribute are the same, false otherwise.
     */
    public boolean isSameValue(ConfigurationAttribute configurationAttribute);

     /**************************/
    /** Save and load methods */
   /**************************/

    /**
     * Sets the ConfigurationAttribute field corresponding to the given name with the given value.
     * This method is used on loading a document project. With the data loaded, a void ConfigurationAttribute is created and the fields are set one by one with this method.
     * All the fields set by this method should be returned by the method getAllFields().
     * @param name Name of the field.
     * @param value Value of the field.
     * @see ConfigurationAttribute#getAllFields()
     * @see org.orbisgis.mapcomposer.model.graphicalelement.utils.SaveHandler#endElement(String, String, String)
     */
    public void setField(String name, String value);

    /**
     * Return a Map of all the ConfigurationAttributes fields (value, name ...).
     * The map contains the field name as key and the field value as value.
     * This methods is used on saving a document project. The data contained in the returned map are written in the save file.
     * All the fields return by this method should be set in the method setField(String, String).
     * @return Map containing the field names as key and the field values as value.
     * @see ConfigurationAttribute#setField(String, String)
     * @see org.orbisgis.mapcomposer.model.graphicalelement.utils.SaveHandler#save(java.util.List, String)
     */
    public Map<String, Object> getAllFields();
}
