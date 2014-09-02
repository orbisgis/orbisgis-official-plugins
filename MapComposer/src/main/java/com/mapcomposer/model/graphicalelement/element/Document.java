package com.mapcomposer.model.graphicalelement.element;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Choice;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * GraphicalElement representing the document.
 */
public class Document extends GraphicalElement{
    /**true : portrait, false : landscape*/
    private Choice orientation;
    
    private Choice format;
    
    public Document(){
        orientation=new Choice("Orientation");
        format=new Choice("Format");
        
        orientation.add("Lanscape");
        orientation.add("Portrait");
        orientation.select("Landscape");
        format.add(Format.A3.getName());
        format.add(Format.A4.getName());
        format.add(Format.CUSTOM.getName());
        format.select(Format.A4.getName());
        this.setX(0);
        this.setY(0);
    }
    
    public Format getFormat(){
        return Format.getByName(format.getSelected());
    }
    
    public void setFormat(Format f){
        format.select(f.getName());
        if(!f.getName().equals(Format.CUSTOM.getName())){
            this.setWidth(f.getPixelWidth());
            this.setHeight(f.getPixelHeight());
        }
    }
    
    public void setOrientation(String o){
        orientation.select(o);
    }
    
    public static enum Format{
        A4(210, 297, "A4"),
        A3(297, 420, "A3"),
        CUSTOM(0, 0, "CUSTOM");
        
        private int w;
        private int h;
        private String name;
        
        private Format(int w, int h, String name){
            this.w=w;
            this.h=h;
            this.name=name;
        }
        
        public String getName(){
            return name;
        }
        
        public static Format getByName(String name){
            for (final Document.Format nvp : values()) {
                if (nvp.getName().equals(name)) {
                    return nvp;
                }
            }
            throw new IllegalArgumentException("Invalid name: " + name);
        }
        public Dimension getMMDimension(){
            return new Dimension(w, h);
        }
        public Dimension getPixelDimension(){
            return new Dimension((int)(java.awt.Toolkit.getDefaultToolkit().getScreenResolution()*w/25.4), (int)(java.awt.Toolkit.getDefaultToolkit().getScreenResolution()*h/25.4));
        }
        public int getPixelHeight(){
            return (int)(java.awt.Toolkit.getDefaultToolkit().getScreenResolution()*h/25.4);
        }
        public int getPixelWidth(){
            return (int)(java.awt.Toolkit.getDefaultToolkit().getScreenResolution()*w/25.4);
        }
        public int getMMHeight(){
            return h;
        }
        public int getMMWidth(){
            return w;
        }
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.addAll( super.getAllAttributes());
        list.add(format);
        list.add(orientation);
        return list;
    }
}
