package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;

/**
 * The Source attribute contain the path to a specified data source like OWS-Context, data, image ...
 */
public class Source extends ConfigurationAttribute<String>{

    /**
     * Main constructor.
     * @param name Name of the Source in its GraphicalElement.
     */
    public Source(String name) {
        super(name);
    }
    
}
