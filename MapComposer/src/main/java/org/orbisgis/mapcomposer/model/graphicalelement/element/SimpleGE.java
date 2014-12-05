package org.orbisgis.mapcomposer.model.graphicalelement.element;

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.IntegerCA;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple implementation of the GraphicalElement interface.
 * It contains all the basic ConfigurationAttributes (CA) and the implementation of the interface functions.
 */
public abstract class SimpleGE implements GraphicalElement{
    /** x position of the GE.*/
    private IntegerCA x;
    /** y position of the GE.*/
    private IntegerCA y;
    /** Inclination of the GE.*/
    private IntegerCA rotation;
    /** Height of the GE.*/
    private IntegerCA height;
    /** Width of the GE.*/
    private IntegerCA width;
    /** Z index of the GE.*/
    private int z;
    
    /**
     * Main constructor.
     */
    public SimpleGE(){
        //ConfigurationAttribute instantiation
        x= new IntegerCA("x", false, 0);
        y= new IntegerCA("y", false, 0);
        rotation= new IntegerCA("Rotation", false, 0, true, -360, 360);
        height= new IntegerCA("Height", false, 50, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
        width= new IntegerCA("Width", false, 50, true, Integer.MIN_VALUE, Integer.MAX_VALUE);
    }
    
//Setters
    @Override public void setX(int x)   {this.x.setValue(x);}
    @Override public void setY(int y)   {this.y.setValue(y);}
    @Override public void setZ(int z)   {this.z=z; }
    @Override public void setRotation(int rotation) {this.rotation.setValue(rotation);}
    @Override public void setHeight(int height)     {this.height.setValue(height);}
    @Override public void setWidth(int width)       {this.width.setValue(width);}
//Getters
    @Override public int getX() {return this.x.getValue();}
    @Override public int getY() {return this.y.getValue();}
    @Override public int getZ() {return z;}
    @Override public int getRotation()  {return this.rotation.getValue();}
    @Override public int getHeight()    {return this.height.getValue();}
    @Override public int getWidth()     {return this.width.getValue();}

    @Override
    public void setAttribute(ConfigurationAttribute ca) {
        if(ca.getName().equals(x.getName()))
            x=(IntegerCA)ca;
        if(ca.getName().equals(y.getName()))
            y=(IntegerCA)ca;
        if(ca.getName().equals(rotation.getName()))
            rotation=(IntegerCA)ca;
        if(ca.getName().equals(height.getName()))
            height=(IntegerCA)ca;
        if(ca.getName().equals(width.getName()))
            width=(IntegerCA)ca;
    }

    @Override
    public List<ConfigurationAttribute> getSavableAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.add(x);
        list.add(y);
        list.add(width);
        list.add(height);
        list.add(rotation);
        return list;
    }
}