package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;

/**
 * Text attribute representing a simple text.
 */
public class Text extends ConfigurationAttribute<String>{

    /**
     * Main constructor.
     * @param name Name of the Text in its GraphicalElement.
     */
    public Text(String name) {
        super(name);
    }
}
