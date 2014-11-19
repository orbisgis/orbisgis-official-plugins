package org.orbisgis.mapcomposer.model.configurationattribute.attribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;

import java.util.Map;

/**
 * IntegerCA attribute.
 */
public class IntegerCA extends BaseCA<Integer> {
    /** Property itself */
    private Integer value = 0;
    /** Minimum value */
    private int min = Integer.MIN_VALUE;
    /** Maximum value */
    private int max = Integer.MAX_VALUE;
    /** Boolean to enable or not the max and min value */
    private boolean limits = false;
    
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

    @Override
    public void setField(String name, String value) {
        super.setField(name, value);
        if(name.equals("value"))
            this.value=Integer.parseInt(value);
        if(name.equals("min"))
            min = Integer.parseInt(value);
        if(name.equals("max"))
            max = Integer.parseInt(value);
        if(name.equals("limits"))
            limits = Boolean.parseBoolean(value);
    }

    public Map<String, Object> getSavableField() {
        Map ret = super.getSavableField();
        ret.put("value", value);
        ret.put("min", min);
        ret.put("max", max);
        ret.put("limits", limits);
        return ret;
    }
}
