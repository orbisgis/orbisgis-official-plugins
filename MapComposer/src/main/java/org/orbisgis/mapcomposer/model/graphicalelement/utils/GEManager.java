package org.orbisgis.mapcomposer.model.graphicalelement.utils;

import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.Scale;
import org.orbisgis.mapcomposer.model.graphicalelement.element.illustration.Image;
import org.orbisgis.mapcomposer.model.graphicalelement.element.text.TextElement;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.view.graphicalelement.*;

import java.util.*;


/**
 * This class contain all the GraphicalElement used in the MapComposer. It also manages the link between the GraphicalElements (GE) and their Renderer.
 * To be used, a GraphicalElement should be register with its renderer into this class to be recognise by the composer.
 * Each GraphicalElement or GraphicalElementRenderer which is not register won't be used.
 */
public class GEManager {

    /** HashMap to link the GE to its Renderer.*/
    private Map<Class<? extends GraphicalElement>, GERenderer> mapRenderer;
    
    /** Main constructor.*/
    public GEManager(){
        mapRenderer = new HashMap();
        //Adding the original GE and their Renderer
        mapRenderer.put(MapImage.class, new MapImageRenderer());
        mapRenderer.put(Orientation.class, new OrientationRenderer());
        mapRenderer.put(Scale.class, new ScaleRenderer());
        mapRenderer.put(Image.class, new ImageRenderer());
        mapRenderer.put(TextElement.class, new TextRenderer());
        mapRenderer.put(Document.class, new DocumentRenderer());
    }
    
    /**
     * Returns the Renderer corresponding to the GE object given in argument.
     * @param ge GE class that needs to be rendered.
     * @return The Renderer of the GE.
     */
    public GERenderer getRenderer(Class<? extends GraphicalElement> ge){
        return mapRenderer.get(ge);
    }
    
    /**
     * Register the GraphicalElement class and its Renderer class.
     * @param geClass Class of the GE.
     * @param renderer Class of its Renderer.
     */
    public void registerGE(Class<? extends GraphicalElement> geClass, SimpleGERenderer renderer){
        mapRenderer.put(geClass, renderer);
    }

    /**
     * Return the list of all the previously registered GraphicalElement classes.
     * @return List of the GE.
     */
    public List<Class<? extends GraphicalElement>> getRegisteredGEClasses(){
        List<Class<? extends GraphicalElement>> list = new ArrayList<>();
        Iterator<Map.Entry<Class<? extends GraphicalElement>, GERenderer>> it = mapRenderer.entrySet().iterator();
        while(it.hasNext())
            list.add(it.next().getKey());
        return list;
    }
}
