package com.mapcomposer.model.graphicalelement.interfaces;

/**
 * Interface for illustration GraphicalElements.
 */
public interface IllustrationElement extends GraphicalElement{
    /**
     * Returns the absolute path value.
     * @return The absolute path string to the source.
     */
    public String getPath();
    
    /**
     * Sets the absolute path to the source.
     * @param absolutePath New absolute path to the source.
     */
    public void setPath(String absolutePath);
}
