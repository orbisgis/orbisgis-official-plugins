package com.mapcomposer.model.graphicalelement;

import java.util.ArrayList;
import java.util.List;
import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Numeric;

/**
 * Root class for the GraphicalElements (GraphicalElement) objects.
 * It contains all the main ConfigurationAttributes (CA) and their setter and getters.
 */
public abstract class GraphicalElement {
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
    public GraphicalElement(){
        x=new Numeric("x");
        y=new Numeric("y");
        rotation=new Numeric("Rotation");
        height=new Numeric("Height");
        width=new Numeric("Width");
        this.initialisation();
    }

    /**
     * Sets the x position.
     * @param x The x position of the GE.
     */
    public void setX(Integer x) {
        this.x.setPropertyValue(x);
    }

    /**
     * Sets the y position.
     * @param y The y position of the GE.
     */
    public void setY(int y) {
        this.y.setPropertyValue(y);
    }

    /**
     * Sets the rotation angle.
     * @param rotation The rotation angle of the GE.
     */
    public void setRotation(int rotation) {
        this.rotation.setPropertyValue(rotation);
    }

    /**
     * Sets the height.
     * @param height The height of the GE.
     */
    public void setHeight(int height) {
        this.height.setPropertyValue(height);
    }

    /**
     * Sets the width.
     * @param width The width of the GE.
     */
    public void setWidth(int width) {
        this.width.setPropertyValue(width);
    }
    
    /**
     * Returns all the attributes. This function need be be redefined in child class to make it public and acessible to the other classes.
     * @return List of all the attributes.
     */
    protected List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.add(x);
        list.add(y);
        list.add(rotation);
        list.add(height);
        list.add(width);
        return list;
    }
    
    /**
     * Abstract method for the  initialisation of a GE.
     * This function will be called in the constructor and contain the initialisation of the GE fields.
     */
    protected abstract void initialisation();
}
