package com.mapcomposer.model.graphicalelement.interfaces;

import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import java.util.List;

/**
 * Root interface for the GraphicalElements (GraphicalElement) objects.
 * It contains all the main function for the definition of the .
 */
public interface GraphicalElement {

    /**
     * Sets the x position.
     * @param x The x position of the GE.
     */
    public void setX(Integer x);

    /**
     * Sets the y position.
     * @param y The y position of the GE.
     */
    public void setY(int y);

    /**
     * Sets the rotation angle.
     * @param rotation The rotation angle of the GE.
     */
    public void setRotation(int rotation);

    /**
     * Sets the height.
     * @param height The height of the GE.
     */
    public void setHeight(int height);

    /**
     * Sets the width.
     * @param width The width of the GE.
     */
    public void setWidth(int width);

    /**
     * Gets the x position.
     * @return The x position of the GE.
     */
    public int getX();

    /**
     * Gets the y position.
     * @return The y position of the GE.
     */
    public int getY();

    /**
     * Gets the rotation angle.
     * @return The rotation angle of the GE.
     */
    public int getRotation();

    /**
     * Gets the height.
     * @return The height of the GE.
     */
    public int getHeight();

    /**
     * Gets the width.
     * @return The width of the GE.
     */
    public int getWidth();
    
    /**
     * Returns all the attributes. This function need be be redefined in child class.
     * @return List of all the attributes.
     */
    public List<ConfigurationAttribute> getAllAttributes();
}
