package org.orbisgis.mapcomposer.model.graphicalelement.utils;

import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.Scale;
import org.orbisgis.mapcomposer.model.graphicalelement.element.illustration.Image;
import org.orbisgis.mapcomposer.model.graphicalelement.element.text.TextElement;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;

import java.awt.*;

public class GEFactory {

    public static GraphicalElement createGE(Class<? extends GraphicalElement> c){
        if(c.equals(Document.class))
            return createDocument();
        if(c.equals(TextElement.class))
            return createTextElement();
        if(c.equals(Image.class))
            return createImage();
        if(c.equals(MapImage.class))
            return createMapImage();
        if(c.equals(Orientation.class))
            return createOrientation();
        if(c.equals(Scale.class))
            return createScale();
        else return null;
    }

    public static Document createDocument(){
        Document d = new Document();

        d.setHeight(50);
        d.setWidth(50);
        d.setRotation(0);
        d.setX(10);
        d.setY(10);

        d.setFormat(Document.Format.A4);
        d.setName("Document");
        d.setOrientation("Portrait");

        return d;
    }

    public static TextElement createTextElement(){
        TextElement te = new TextElement();

        te.setHeight(50);
        te.setWidth(50);
        te.setRotation(0);
        te.setX(10);
        te.setY(10);

        te.setAlpha(0);
        te.setColorBack(Color.white);
        te.setColorText(Color.black);
        te.setFontSize(12);
        te.setStyle(TextElement.Alignment.CENTER);
        te.setText("Some text");

        return te;
    }

    public static Image createImage(){
        Image i = new Image();

        i.setHeight(50);
        i.setWidth(50);
        i.setRotation(0);
        i.setX(10);
        i.setY(10);

        i.setPath("/path/to/file");

        return i;
    }

    public static MapImage createMapImage(){
        MapImage mi = new MapImage();

        mi.setHeight(50);
        mi.setWidth(50);
        mi.setRotation(0);
        mi.setX(10);
        mi.setY(10);

        mi.setOwsContext("/path/to/ows/context");

        return mi;
    }

    public static Orientation createOrientation(){
        Orientation o = new Orientation();

        o.setHeight(50);
        o.setWidth(50);
        o.setRotation(0);
        o.setX(10);
        o.setY(10);

        o.setOwsContext("/path/to/ows/context");
        o.setIconPath("/path/to/icon");

        return o;
    }

    public static Scale createScale(){
        Scale s = new Scale();

        s.setHeight(50);
        s.setWidth(50);
        s.setRotation(0);
        s.setX(10);
        s.setY(10);

        s.setOwsContext("/path/to/ows/context");

        return s;
    }
}
