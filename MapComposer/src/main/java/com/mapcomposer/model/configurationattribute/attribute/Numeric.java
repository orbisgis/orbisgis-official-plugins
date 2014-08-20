package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;

/**
 * Numeric attribute.
 */
public final class Numeric extends ConfigurationAttribute<Integer> {
    
    /** Minimum value */
    private final int min;
    /** Maximum value */
    private final int max;
    /** Boolean to enable or not the max and min value */
    private final boolean limits;

    /**
     * Main constructor.
     * @param name Name of the Numeric in its GraphicalElement.
     * @param minimum Minimum value.
     * @param maximum Maximum value.
     */
    public Numeric(String name, int minimum, int maximum) {
        super(name);
        min = minimum;
        max = maximum;
        limits=true;
    }
    
    /**
     * Main constructor.
     * @param name Name of the Numeric in its GraphicalElement.
     */
    public Numeric(String name) {
        super(name);
        min=0;
        max=0;
        limits=false;
    }
    
    /**
     * Returns the minimum value.
     * @return The minimum value.
     */
    public int getMin(){
        return min;
    }
    
    /**
     * Returns the maximum value.
     * @return The maximum value.
     */
    public int getMax(){
        return max;
    }
    
    /**
     * Returns the limits boolean.
     * @return The limits boolean.
     */
    public boolean getLimits(){
        return limits;
    }
}
