package com.mapcomposer.model.configurationattribute.attribute;

/**
 * The Choice attribute contains severals fields that can be selected.
 */
public final class Choice extends SimpleListCA<String>{
    
    /**
     * Main constructor.
     * @param name Name of the Choice in its GraphicalElement.
     */
    public Choice(String name) {
        super(name);
    }
}
