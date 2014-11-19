package org.orbisgis.mapcomposer.model.graphicalelement.interfaces;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.xml.sax.Attributes;

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
    public void setX(int x);

    /**
     * Sets the y position.
     * @param y The y position of the GE.
     */
    public void setY(int y);
    
    /**
     * Sets the z position.
     * Only used on saveProject and loadProject to keep the z position.
     * @param z The z position.
     */
    public void setZ(int z);

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
     * Returns the z position.
     * Only used on saveProject and loadProject to keep the z position.
     * @return The z position.
     */
    public int getZ();

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
    
    public enum Property{X, Y, WIDTH, HEIGHT, ROTATION;}

    /**
     * Sets the ConfigurationAttribute contained in the GE corresponding to the given CA.
     * @param ca CA to register.
     */
    public void setAttribute(ConfigurationAttribute ca);

    /**
     * Returns all the savable attributes of the GE.
     */
    public List<ConfigurationAttribute> getSavableAttributes();


}
