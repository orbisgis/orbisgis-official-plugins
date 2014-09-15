package com.mapcomposer.model.configurationattribute.utils;

import com.mapcomposer.model.configurationattribute.attribute.ColorCA;
import com.mapcomposer.model.configurationattribute.attribute.FileListCA;
import com.mapcomposer.model.configurationattribute.attribute.IntegerCA;
import com.mapcomposer.model.configurationattribute.attribute.MapImageListCA;
import com.mapcomposer.model.configurationattribute.attribute.OwsContextCA;
import com.mapcomposer.model.configurationattribute.attribute.SourceCA;
import com.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import com.mapcomposer.model.configurationattribute.attribute.StringCA;
import com.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
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
    
    public static IntegerCA createIntegerCA(String name, int minimum, int maximum){
        IntegerCA ica = new IntegerCA();
        ica.setLock(false);
        ica.setName(name);
        if(minimum<maximum){
            ica.setLimits(true);
            ica.setMin(minimum);
            ica.setMax(maximum);
            ica.setValue(minimum);
            if(minimum<0 && 0<maximum)
                ica.setValue(0);
        }
        return ica;
    }
    
    public static MapImageListCA createMapImageListCA(String name){
        MapImageListCA milka = new MapImageListCA();
        milka.setLock(false);
        milka.setName(name);
        milka.setValue(new ArrayList<MapImage>());
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
        source.setValue("no_value");
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
        string.setValue("no_value");
        return string;
    }
}
