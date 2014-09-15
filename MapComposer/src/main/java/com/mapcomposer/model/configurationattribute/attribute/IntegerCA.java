package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;

/**
 * IntegerCA attribute.
 */
public final class IntegerCA extends BaseCA<Integer> {
    /** Property itself */
    private Integer value;
    /** Minimum value */
    private int min;
    /** Maximum value */
    private int max;
    /** Boolean to enable or not the max and min value */
    private boolean limits;

    /**
     * Main constructor.
     * @param name Name of the Numeric in its GraphicalElement.
     * @param minimum Minimum value.
     * @param maximum Maximum value.
     */
    public IntegerCA(String name, int minimum, int maximum) {
        super();
        this.setName(name);
        min = minimum;
        max = maximum;
        limits=true;
    }
    
    /**
     * Main constructor.
     */
    public IntegerCA() {
        super();
        min=0;
        max=0;
        limits=false;
    }
    
    @Override public void setValue(Integer value) {this.value=value;}

    @Override public Integer getValue() {return value;}

    @Override public boolean isSameValue(ConfigurationAttribute ca) {
        return ca.getValue().equals(value);
    }
    
    /**
     * Returns the minimum value.
     * @return The minimum value.
     */
    public int getMin(){return min;}
    
    /**
     * Sets the minimum value.
     * @param min The minimum value.
     */
    public void setMin(int min){this.min=min;}
    
    /**
     * Returns the maximum value.
     * @return The maximum value.
     */
    public int getMax(){return max;}
    
    /**
     * Sets the maximum value.
     * @param max The maximum value.
     */
    public void setMax(int max){
        this.max=max;
    }
    
    /**
     * Returns the limits boolean.
     * @return The limits boolean.
     */
    public boolean getLimits(){
        return limits;
    }
    
    /**
     * Sets the limits value.
     * @param limits The limits value.
     */
    public void setLimits(boolean limits){
        this.limits=limits;
    }
}
