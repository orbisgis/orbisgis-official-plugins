package com.mapcomposer.model.graphicalelement.element;

import com.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import com.mapcomposer.Configuration;
import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Numeric;
import java.util.ArrayList;
import java.util.List;

/**
 * Simple GE implementation.
 * It contains all the main ConfigurationAttributes (CA) and the implementations of the interface functions.
 */
public abstract class SimpleGE implements GraphicalElement{
    /** x position of the GE.*/
    private final Numeric x;
    /** y position of the GE.*/
    private final Numeric y;
    /** Inclination of the GE.*/
    private final Numeric rotation;
    /** Heght of the GE.*/
    private final Numeric height;
    /** Width of the GE.*/
    private final Numeric width;
    
    /**
     * Main constructor.
     */
    public SimpleGE(){
        //ConfigurationAttribute instantiation
        x=new Numeric("x", 0, Configuration.documentWidth);
        y=new Numeric("y", 0, Configuration.documentHeight);
        rotation=new Numeric("Rotation", -360, 360);
        height=new Numeric("Height");
        width=new Numeric("Width");
        
        setDefaultValue();
    }

    /**
     * Sets the x position.
     * @param x The x position of the GE.
     */
    public void setX(Integer x) {
        this.x.setValue(x);
    }

    /**
     * Sets the y position.
     * @param y The y position of the GE.
     */
    public void setY(int y) {
        this.y.setValue(y);
    }

    /**
     * Sets the rotation angle.
     * @param rotation The rotation angle of the GE.
     */
    public void setRotation(int rotation) {
        this.rotation.setValue(rotation);
    }

    /**
     * Sets the height.
     * @param height The height of the GE.
     */
    public void setHeight(int height) {
        this.height.setValue(height);
    }

    /**
     * Sets the width.
     * @param width The width of the GE.
     */
    public void setWidth(int width) {
        this.width.setValue(width);
    }

    /**
     * Gets the x position.
     * @return The x position of the GE.
     */
    public int getX() {
        return this.x.getValue();
    }

    /**
     * Gets the y position.
     * @return The y position of the GE.
     */
    public int getY() {
        return this.y.getValue();
    }

    /**
     * Gets the rotation angle.
     * @return The rotation angle of the GE.
     */
    public int getRotation() {
        return this.rotation.getValue();
    }

    /**
     * Gets the height.
     * @return The height of the GE.
     */
    public int getHeight() {
        return this.height.getValue();
    }

    /**
     * Gets the width.
     * @return The width of the GE.
     */
    public int getWidth() {
        return this.width.getValue();
    }
    
    /**
     * Returns all the attributes. This function need be be redefined in child class.
     * @return List of all the attributes.
     */
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.add(x);
        list.add(y);
        list.add(rotation);
        list.add(height);
        list.add(width);
        return list;
    }
    
    private void setDefaultValue(){
        x.setValue(Configuration.defaultX);
        y.setValue(Configuration.defaultY);
        rotation.setValue(Configuration.defaultRotation);
        height.setValue(Configuration.defaultHeight);
        width.setValue(Configuration.defaultWidth);
    }
}
