package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;

/**
 * Numeric attribute.
 */
public class Numeric extends ConfigurationAttribute<Integer> {

    /**
     * Main constructor.
     * @param name Name of the Numeric in its GraphicalElement.
     */
    public Numeric(String name) {
        super(name);
    }
}
