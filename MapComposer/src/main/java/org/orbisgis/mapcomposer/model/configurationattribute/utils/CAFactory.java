package org.orbisgis.mapcomposer.model.configurationattribute.utils;

import org.orbisgis.mapcomposer.model.configurationattribute.attribute.ColorCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.FileListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.IntegerCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.MapImageListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.OwsContextCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.SimpleMapImageGE;
import java.awt.Color;
import java.io.File;
import java.util.ArrayList;

/**
 * Factory for ConfigurationAttributes
 */
public class CAFactory {
    
    public static ColorCA createColorCA(String name){
        ColorCA color = new ColorCA();
        color.setLock(false);
        color.setName(name);
        color.setValue(Color.GRAY);
        return color;
    }
    
    public static FileListCA createFileListCA(String name){
        FileListCA flca = new FileListCA();
        flca.setLock(false);
        flca.setName(name);
        flca.setValue(new ArrayList<File>());
        return flca;
    }
    
    public static IntegerCA createIntegerCA(String name){
        IntegerCA ica = new IntegerCA();
        ica.setLock(false);
        ica.setName(name);
        ica.setLimits(false);
        ica.setValue(0);
        ica.setMin(0);
        ica.setMax(0);
        return ica;
    }
    
    /**
     * Create an integer ConfigurationAttribute. 
     * @param name Name of the CA.
     * @param minimum Minimum of the CA value.
     * @param maximum Maximum of the CA value.
     * @param value Value of the CA.
     * @return The created CA.
     */
    public static IntegerCA createIntegerCA(String name, int minimum, int maximum, int value){
        IntegerCA ica = new IntegerCA();
        ica.setLock(false);
        ica.setName(name);
        //Tests if the minimum<value<maximum
        if(minimum<maximum && minimum<value && value<maximum){
            ica.setLimits(true);
            ica.setMin(minimum);
            ica.setMax(maximum);
           ica.setValue(value);
        }
        else
            ica.setValue(0);
        return ica;
    }
    
    public static MapImageListCA createMapImageListCA(String name){
        MapImageListCA milka = new MapImageListCA();
        milka.setLock(false);
        milka.setName(name);
        milka.setValue(new ArrayList<SimpleMapImageGE>());
        return milka;
    }
    
    public static OwsContextCA createOwsContextCA(String name){
        OwsContextCA occa = new OwsContextCA();
        occa.setLock(false);
        occa.setName(name);
        occa.setValue(new ArrayList<String>());
        return occa;
    }
    
    public static SourceCA createSourceCA(String name){
        SourceCA source = new SourceCA();
        source.setLock(false);
        source.setName(name);
        source.setValue("/path/to/source");
        return source;
    }
    
    public static SourceListCA createSourceListCA(String name){
        SourceListCA slca = new SourceListCA();
        slca.setLock(false);
        slca.setName(name);
        slca.setValue(new ArrayList<String>());
        return slca;
    }
    
    public static StringCA createStringCA(String name){
        StringCA string = new StringCA();
        string.setLock(false);
        string.setName(name);
        string.setValue("Some text");
        return string;
    }
}
