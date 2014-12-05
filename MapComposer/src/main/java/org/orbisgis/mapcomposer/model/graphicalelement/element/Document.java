package org.orbisgis.mapcomposer.model.graphicalelement.element;

import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.AlwaysOnBack;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GERefresh;

import java.awt.Dimension;
import java.util.ArrayList;
import java.util.List;

/**
 * This GraphicalElement represents the document itself. it is display as a blank page, always behind all the other GraphicalElement.
 * The user can set its orientation (portrait or landscape), it format (A4, A3 or custom) and its name.
 */
public class Document extends SimpleGE implements GERefresh, AlwaysOnBack {

    /**Document orientation (portrait or landscape)*/
    private SourceListCA orientation;

    /**Size of the document*/
    private SourceListCA format;

    /**Name of the document*/
    private StringCA name;

    /** Enumeration of the orientation possibilities : portrait or landscape.*/
    public enum Orientation{PORTRAIT, LANDSCAPE}
    
    /**
     * Main constructor.
     */
    public Document(){
        //ConfigurationAttribute instantiation
        orientation= new SourceListCA("Orientation", false);
        format= new SourceListCA("Format", false);
        name= new StringCA("Name", false, "Document title");
        //Sets the orientation CA
        orientation.add(Orientation.LANDSCAPE.name());
        orientation.add(Orientation.PORTRAIT.name());
        orientation.select(Orientation.LANDSCAPE.name());
        //Sets the format CA
        format.add(Format.A3.name());
        format.add(Format.A4.name());
        format.add(Format.CUSTOM.name());
        format.select(Format.A4.name());
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
    public void setFormat(Format f){format.select(f.name());}
    
    /**
     * Return the format of the document.
     * @return Format of the document.
     */
    public Format getFormat(){return Format.valueOf(format.getSelected());}
    
    /**
     * Sets the orientation of the document.
     * @param o New document orientation.
     */
    public void setOrientation(Orientation o){orientation.select(o.name());}

    /**
     * Return the dimension of the document according to the format and to the orientation of the document.
     * @return The dimension of the document.
     */
    public Dimension getDimension(){
        Dimension dim;
        if(orientation.getSelected().equals(Orientation.PORTRAIT.name()))
            dim = new Dimension(Format.valueOf(format.getSelected()).getPixelWidth(), Format.valueOf(format.getSelected()).getPixelHeight());
        else
            dim = new Dimension(Format.valueOf(format.getSelected()).getPixelHeight(), Format.valueOf(format.getSelected()).getPixelWidth());
        return dim;
    }

    @Override
    public void refresh() {
        //If the document format isn't CUSTOM, the set the specified width and height. (If the format is CUSTOM keep the set height and width)
        if(!getFormat().name().equals(Format.CUSTOM.name())){
            this.setWidth(getFormat().getPixelWidth());
            this.setHeight(getFormat().getPixelHeight());
        }
        //If the orientation is landscape, invert the height and width.
        if(orientation.getSelected().equals(Orientation.LANDSCAPE.name())){
            this.setHeight(Format.valueOf(format.getSelected()).getPixelWidth());
            this.setWidth(Format.valueOf(format.getSelected()).getPixelHeight());
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

    @Override
    public List<ConfigurationAttribute> getSavableAttributes() {
        List<ConfigurationAttribute> list = super.getSavableAttributes();
        list.add(format);
        list.add(orientation);
        list.add(name);
        return list;
    }

    @Override
    public void setAttribute(ConfigurationAttribute ca) {
        super.setAttribute(ca);
        if(ca.getName().equals(orientation.getName()))
            orientation=(SourceListCA)ca;
        if(ca.getName().equals(format.getName()))
            format=(SourceListCA)ca;
        if(ca.getName().equals(name.getName()))
            name=(StringCA)ca;
    }

    /**
     * Enumeration of the different Documents formats.
     * To each format the corresponding dimensions are associated.
     */
    public static enum Format{
        A4(210, 297),
        A3(297, 420),
        CUSTOM(0, 0);
        /**Width of the format*/
        private final int w;
        /**Height of the format*/
        private final int h;

        /**DPI of the screen. As java don't detect well the dpi, it is set manually.*/
        private final int dpi = 96;

        /**
         * Main constructor.
         * @param w Width of the format.
         * @param h Height of the format.
         */
        private Format(int w, int h){
            this.w=w;
            this.h=h;
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
    }

}
