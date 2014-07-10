package com.mapcomposer.model.graphicalelement.utils;

import java.util.HashMap;
import java.util.Map;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.cartographic.Key;
import com.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import com.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import com.mapcomposer.model.graphicalelement.element.cartographic.Scale;
import com.mapcomposer.model.graphicalelement.element.illustration.Data;
import com.mapcomposer.model.graphicalelement.element.illustration.Image;
import com.mapcomposer.model.graphicalelement.element.text.TextElement;

/**
 * The class manages the link between the GraphicalElements (GE) and their Renderer.
 * When a GE need to be displayed, the Renderer will be get via this class.
 * So to be used, both should be registered in the GEManager.
 */
public class GEManager {
    /** Instance of the class. */
    private static GEManager INSTANCE;
    /** HashMap to link the GE to its Renderer.*/
    private static Map<Class<? extends GraphicalElement>, Class<? extends GERenderer>> mapRenderer;
    
    /** Private constuctor.*/
    private GEManager(){
        mapRenderer = new HashMap();
        //Adding the original GE and their Renderer
        mapRenderer.put(Key.class, GERenderer.class);
        mapRenderer.put(MapImage.class, GERenderer.class);
        mapRenderer.put(Orientation.class, GERenderer.class);
        mapRenderer.put(Scale.class, GERenderer.class);
        mapRenderer.put(Data.class, GERenderer.class);
        mapRenderer.put(Image.class, GERenderer.class);
        mapRenderer.put(TextElement.class, GERenderer.class);
    }
    
    /**
     * Returns the instance of the manager.
     * @return The instance of the manager.
     */
    public static GEManager getInstance(){
        if(INSTANCE == null){
            INSTANCE = new GEManager();
        }
        return INSTANCE;
    }
    
    /**
     * Returns the Renderer corresponding to the GE object given in argument.
     * @param ge GE class that needs to be rendered.
     * @return The Renderer of the GE.
     */
    public Class<? extends GERenderer> render(Class<? extends GraphicalElement> ge){
        return mapRenderer.get(ge);
    }
    
    /**
     * Add the GE class and its Renderer class.
     * @param geClass Class of the GE.
     * @param renderer Class of its Renderer.
     */
    public void addGE(Class<? extends GraphicalElement> geClass, Class<? extends GERenderer> renderer){
        mapRenderer.put(geClass, renderer);
    }
}
