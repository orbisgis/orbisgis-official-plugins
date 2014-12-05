package org.orbisgis.mapcomposer.model.graphicalelement.interfaces;

/**
 * This interface extends the GraphicalElement interface and represent an illustration like an image, data ...
 * It contain the basic filed of a GraphicalElement plus a path to the source (SourceCA) used to generate the illustration.
 */
public interface IllustrationElement extends GraphicalElement{
    /**
     * Returns the absolute path to the illustration source.
     * @return The absolute path string to the illustration source.
     */
    public String getPath();
    
    /**
     * Sets the absolute path to the illustration source.
     * @param absolutePath New absolute path to the illustration source.
     */
    public void setPath(String absolutePath);
}
