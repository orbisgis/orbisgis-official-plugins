package org.orbisgis.mapcomposer.model.graphicalelement.element;

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.IntegerCA;
import org.orbisgis.mapcomposer.model.configurationattribute.utils.CAFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Simple GE implementation.
 * It contains all the main ConfigurationAttributes (CA) and the implementations of the interface functions.
 */
public abstract class SimpleGE implements GraphicalElement{
    /** x position of the GE.*/
    private IntegerCA x;
    /** y position of the GE.*/
    private IntegerCA y;
    /** Inclination of the GE.*/
    private IntegerCA rotation;
    /** Heght of the GE.*/
    private IntegerCA height;
    /** Width of the GE.*/
    private IntegerCA width;
    private int z;
    
    /**
     * Main constructor.
     */
    public SimpleGE(){
        //ConfigurationAttribute instantiation
        x=CAFactory.createIntegerCA("x");
        y=CAFactory.createIntegerCA("y");
        rotation=CAFactory.createIntegerCA("Rotation", -360, 360, 0);
        height=CAFactory.createIntegerCA("Height", Integer.MIN_VALUE, Integer.MAX_VALUE, 50);
        width=CAFactory.createIntegerCA("Width", Integer.MIN_VALUE, Integer.MAX_VALUE, 50);
        this.setWidth(50);
        this.setHeight(50);
    }
    

    @Override public void setX(int x)   {this.x.setValue(x);}
    @Override public void setY(int y)   {this.y.setValue(y);}
    @Override public void setZ(int z)   {this.z=z; }
    @Override public void setRotation(int rotation) {this.rotation.setValue(rotation);}
    @Override public void setHeight(int height)     {this.height.setValue(height);}
    @Override public void setWidth(int width)       {this.width.setValue(width);}
    
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