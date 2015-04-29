/*
* MapComposer is an OrbisGIS plugin dedicated to the creation of cartographic
* documents based on OrbisGIS results.
*
* This plugin is developed at French IRSTV institute as part of the MApUCE project,
* funded by the French Agence Nationale de la Recherche (ANR) under contract ANR-13-VBDU-0004.
*
* The MapComposer plugin is distributed under GPL 3 license. It is produced by the "Atelier SIG"
* team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
*
* Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
*
* This file is part of the MapComposer plugin.
*
* The MapComposer plugin is free software: you can redistribute it and/or modify it under the
* terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
*
* The MapComposer plugin is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
* A PARTICULAR PURPOSE. See the GNU General Public License for more details <http://www.gnu.org/licenses/>.
*/

package org.orbisgis.mapcomposer.model.graphicalelement.utils;

import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.Scale;
import org.orbisgis.mapcomposer.model.graphicalelement.element.illustration.Image;
import org.orbisgis.mapcomposer.model.graphicalelement.element.shape.OvalGE;
import org.orbisgis.mapcomposer.model.graphicalelement.element.shape.RectangleGE;
import org.orbisgis.mapcomposer.model.graphicalelement.element.text.TextElement;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.view.graphicalelement.*;

import java.util.*;


/**
 * This class contain all the GraphicalElement used in the MapComposer. It also manages the link between the GraphicalElements (GE) and their Renderer.
 * To be used, a GraphicalElement should be register with its renderer into this class to be recognise by the composer.
 * Each GraphicalElement or GraphicalElementRenderer which is not register won't be used.
 *
 * @author Sylvain PALOMINOS
 */
public class GEManager {

    /** HashMap to link the GE to its Renderer.*/
    private Map<Class<? extends GraphicalElement>, GERenderer> mapRenderer;
    
    /** Main constructor.*/
    public GEManager(){
        mapRenderer = new HashMap<>();
        //Adding the original GE and their Renderer
        mapRenderer.put(MapImage.class, new MapImageRenderer());
        mapRenderer.put(Orientation.class, new OrientationRenderer());
        mapRenderer.put(Scale.class, new ScaleRenderer());
        mapRenderer.put(Image.class, new ImageRenderer());
        mapRenderer.put(TextElement.class, new TextRenderer());
        mapRenderer.put(Document.class, new DocumentRenderer());
        mapRenderer.put(RectangleGE.class, new RectangleRenderer());
        mapRenderer.put(OvalGE.class, new CircleRenderer());
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
    public void registerGE(Class<? extends GraphicalElement> geClass, GERenderer renderer){
        mapRenderer.put(geClass, renderer);
    }

    /**
     * Return the list of all the previously registered GraphicalElement classes.
     * @return List of the GE.
     */
    public List<Class<? extends GraphicalElement>> getRegisteredGEClasses(){
        List<Class<? extends GraphicalElement>> list = new ArrayList<>();
        for (Map.Entry<Class<? extends GraphicalElement>, GERenderer> classGERendererEntry : mapRenderer.entrySet())
            list.add(classGERendererEntry.getKey());
        return list;
    }
}
