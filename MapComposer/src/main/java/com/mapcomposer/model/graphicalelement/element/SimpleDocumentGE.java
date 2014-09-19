package com.mapcomposer.model.graphicalelement.element;

import com.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import com.mapcomposer.model.configurationattribute.attribute.StringCA;
import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.utils.CAFactory;
import com.mapcomposer.model.graphicalelement.interfaces.GERefresh;
import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * GraphicalElement representing the document.
 */
public class SimpleDocumentGE extends SimpleGE implements GERefresh{
    /**CA representing the document orientation*/
    private SourceListCA orientation;
    /**Size of the document*/
    private SourceListCA format;
    /**Name of the document*/
    private StringCA name;
    
    /**
     * Main constructor.
     */
    public SimpleDocumentGE(){
        //ConfigurationAttribute instantiation
        orientation=CAFactory.createSourceListCA("Orientation");
        format=CAFactory.createSourceListCA("Format");
        name=CAFactory.createStringCA("Name");
        //Sets the orientation CA
        orientation.add("Landscape");
        orientation.add("Portrait");
        orientation.select("Landscape");
        //Sets the format CA
        format.add(Format.A3.getName());
        format.add(Format.A4.getName());
        format.add(Format.CUSTOM.getName());
        format.select(Format.A4.getName());
    }
    /**
     * Copy constructor.
     * @param sdge SimpleDocumentGE to copy
     */
    public SimpleDocumentGE(SimpleDocumentGE sdge){
        super(sdge);
        //ConfigurationAttribute instantiation
        orientation=sdge.orientation;
        format=sdge.format;
        name=sdge.name;
    }
    
    /**
     * Sets the document name.
     * @param name New name of the document.
     */
    public void setName(String name){this.name.setValue(name);}
    
    /**
     * Returns the document name.
     * @return The document name.
     */
    public String getName(){return name.getValue();}
    
    /**
     * Sets the format of the document.
     * @param f New format of the document.
     */
    public void setFormat(Format f){format.select(f.getName());}
    
    /**
     * Return the format of the document.
     * @return Format of the document.
     */
    public Format getFormat(){return Format.getByName(format.getSelected());}
    
    /**
     * Sets the orientation of the document.
     * @param o New document orientation.
     */
    public void setOrientation(String o){orientation.select(o);}

    @Override
    public void refresh() {
        //If the document format isn't CUSTOM, the set the specified width and height.
        if(!getFormat().getName().equals(Format.CUSTOM.getName())){
            this.setWidth(getFormat().getPixelWidth());
            this.setHeight(getFormat().getPixelHeight());
        }
        //If the orientation is landscape, invert the height and width.
        if(orientation.getSelected().equals("Landscape")){
            this.setHeight(Format.getByName(format.getSelected()).getPixelWidth());
            this.setWidth(Format.getByName(format.getSelected()).getPixelHeight());
        }
    }
    
    /**
     * Enumeration of the differents Documents formats.
     */
    public static enum Format{
        A4(210, 297, "A4"),
        A3(297, 420, "A3"),
        CUSTOM(0, 0, "CUSTOM");
        /**Width of the format*/
        private final int w;
        /**Height of the format*/
        private final int h;
        /**name of the format*/
        private final String name;
        /**DPI of the screen. As java don't detect well the dpi, it is setted mannually.*/
        private final int dpi = 96;
        
        /**
         * Main constructor.
         * @param w Width of the format.
         * @param h Height of the format.
         * @param name Name of the format.
         */
        private Format(int w, int h, String name){
            this.w=w;
            this.h=h;
            this.name=name;
        }
        
        /**
         * Returns the name of the format.
         * @return Name of the format.
         */
        public String getName(){
            return name;
        }
        
        /**
         * Return the format corresponding to the given name.
         * @param name Name of the format.
         * @return The format corresponding to the name.
         */
        public static Format getByName(String name){
            for (final SimpleDocumentGE.Format nvp : values()) {
                if (nvp.getName().equals(name)) {
                    return nvp;
                }
            }
            throw new IllegalArgumentException("Invalid name: " + name);
        }
        /**
         * Return the dimension of the document in millimeters.
         * @return The dimension of the document in millimeters.
         */
        public Dimension getMMDimension(){
            return new Dimension(w, h);
        }
        /**
         * Return the dimension of the document in pixels.
         * @return The dimension of the document in pixels.
         */
        public Dimension getPixelDimension(){
            return new Dimension((int)(dpi*w/25.4), (int)(dpi*h/25.4));
        }
        /**
         * Return the height of the document in pixels.
         * @return The height of the document in pixels.
         */
        public int getPixelHeight(){
            return (int)(dpi*h/25.4);
        }
        /**
         * Return the width of the document in pixels.
         * @return The width of the document in pixels.
         */
        public int getPixelWidth(){
            return (int)(dpi*w/25.4);
        }
        /**
         * Return the height of the document in millimeters.
         * @return The height of the document in millimeters.
         */
        public int getMMHeight(){
            return h;
        }
        /**
         * Return the width of the document in millimeters.
         * @return The width of the document in millimeters.
         */
        public int getMMWidth(){
            return w;
        }
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.add(format);
        list.add(orientation);
        list.add(name);
        return list;
    }
}
