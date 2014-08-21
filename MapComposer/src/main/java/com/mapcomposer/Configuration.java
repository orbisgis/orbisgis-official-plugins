package com.mapcomposer;

import com.mapcomposer.model.graphicalelement.element.text.TextElement;
import java.awt.Color;
import java.awt.GraphicsEnvironment;

/**
 *  Configuration values.
 */
public class Configuration {
    public static int documentWidth = 1000;
    public static int documentHeight = 1000;
    
    
    public static int defaultX = 0;
    public static int defaultY = 0;
    public static int defaultRotation = 0;
    public static int defaultHeight = 50;
    public static int defaultWidth = 50;
    public static String defaultFont = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()[0];
    public static Color defaultColorBack =Color.WHITE;
    public static Color defaultColorText =Color.BLACK;
    public static String defaultAlignment =TextElement.Alignment.CENTER.getName();
    public static String defaultStyle =TextElement.Style.PLAIN.getName();
    public static int defaultFontSize =8;
    public static String defaultText = "Map Composer";
    public static int defaultAlpha = 0;
    public static String defaultImagePath = Configuration.class.getClassLoader().getResource("defaultData/image.png").getPath();
    public static String defaultIconPath = Configuration.class.getClassLoader().getResource("defaultData/orientation.png").getPath();
    
    public static int defaultESX = 0;
    public static int defaultESY = 0;
    public static int defaultESRotation = 0;
    public static int defaultESHeight = 100;
    public static int defaultESWidth = 100;
    public static String defaultESFont = GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()[0];
    public static Color defaultESColorBack =Color.WHITE;
    public static Color defaultESColorText =Color.BLACK;
    public static String defaultESAlignment =TextElement.Alignment.LEFT.getName();
    public static String defaultESStyle =TextElement.Style.PLAIN.getName();
    public static int defaultESFontSize =8;
    public static String defaultESText = "Map Composer";
    public static int defaultESAlpha = 127;
    public static String defaultESImagePath = Configuration.class.getClassLoader().getResource("defaultData/image.png").getPath();
    public static String defaultESIconPath = Configuration.class.getClassLoader().getResource("defaultData/orientation.png").getPath();
}
