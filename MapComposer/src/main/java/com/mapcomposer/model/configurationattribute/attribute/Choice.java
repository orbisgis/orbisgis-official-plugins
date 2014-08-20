package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.ListCA;

/**
 * The Choice attribute contains severals fields that can be selected.
 */
public final class Choice extends ListCA<String>{
    
    /**
     * Main constructor.
     * @param name Name of the Choice in its GraphicalElement.
     */
    public Choice(String name) {
        super(name);
    }
}
