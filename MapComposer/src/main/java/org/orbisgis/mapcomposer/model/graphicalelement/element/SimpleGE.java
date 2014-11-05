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
        rotation=CAFactory.createIntegerCA("Rotation", -360, 360);
        height=CAFactory.createIntegerCA("Height");
        width=CAFactory.createIntegerCA("Width");
        this.setWidth(50);
        this.setHeight(50);
    }
    /**
     * Copy constructor.
     * @param sge SimpleGE to copy.
     */
    public SimpleGE(SimpleGE sge){
        //ConfigurationAttribute instantiation
        x=sge.x;
        y=sge.y;
        rotation=sge.rotation;
        height=sge.height;
        width=sge.width;
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
}
