package com.mapcomposer.model.graphicalelement.element.text;

import java.util.ArrayList;
import java.util.List;
import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Choice;
import com.mapcomposer.model.configurationattribute.attribute.ColorCA;
import com.mapcomposer.model.configurationattribute.attribute.Text;
import com.mapcomposer.model.configurationattribute.attribute.Numeric;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

/**
 * GraphicalElement displaying a text. Several aspects can be defined such as the text color, the font,the font size ...
 */
public class TextElement extends GraphicalElement{
    /** Fonts allowed */
    private final Choice font;
    /** Color of the Text */
    private final ColorCA colorText;
    /** Color of the background */
    private final ColorCA colorBack;
    /** Alignment of the text */
    private final Choice alignment;
    /** Style (plain, italic, bold) of the text */
    private final Choice style;
    /** Size of the font */
    private final Numeric fontSize;
    /** Text itself */
    private final Text text;
    /** Alpha (transparency value) */
    private final Numeric alpha;
    
    /**
     * Public main constructor.
     */
    public TextElement(){
        //ConfigurationAttribute instantiation
        this.font = new Choice("Font");
        this.colorText = new ColorCA("Text color");
        this.colorBack = new ColorCA("Background color");
        this.alignment = new Choice("Alignment");
        this.style = new Choice("Style");
        this.fontSize = new Numeric("Font size", 1, 99999);
        this.text = new Text("Text");
        this.alpha = new Numeric("Alpha", 0, 255);
        
        //ConfigurationAttribute initialisation
        for(String s : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()){
            this.font.add(s);
        }
        this.colorBack.setValue(Color.WHITE);
        this.colorText.setValue(Color.BLACK);
        this.alignment.add("Left");
        this.style.add(Style.PLAIN.getName());
        this.style.add(Style.ITALIC.getName());
        this.style.add(Style.BOLD.getName());
        this.style.select("plain");
        this.fontSize.setValue(8);
        this.text.setValue("no text");
        this.alpha.setValue(0);
        this.setHeight(30);
        this.setWidth(150);
    }
    
    /**
     * Clone constructor.
     * @param ge
     */
    public TextElement(TextElement ge){
        super(ge);
        //ConfigurationAttribute instantiation
        this.font = ge.font;
        this.colorText = ge.colorText;
        this.colorBack = ge.colorBack;
        this.alignment = ge.alignment;
        this.style = ge.style;
        this.fontSize = ge.fontSize;
        this.text = ge.text;
        this.alpha = ge.alpha;
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.addAll( super.getAllAttributes());
        list.add(font);
        list.add(colorBack);
        list.add(colorText);
        list.add(alignment);
        list.add(style);
        list.add(fontSize);
        list.add(text);
        list.add(alpha);
        return list;
    }

    /**
     * Add a font available.
     * @param font A new font.
     */
    public void addFont(String font) {
        this.font.add(font);
    }

    /**
     * Sets the color of the text.
     * @param color The new color.
     */
    public void setColorText(Color color) {
        this.colorText.setValue(color);
    }

    /**
     * Sets the color of the background.
     * @param color The new color.
     */
    public void setColorBack(Color color) {
        this.colorBack.setValue(color);
    }

    /**
     * Sets the font size of the text.
     * @param fontSize The new font size.
     */
    public void setFontSize(int fontSize) {
        this.fontSize.setValue(fontSize);
    }

    /**
     * Sets the text of the element
     * @param text The new text.
     */
    public void setText(String text) {
        this.text.setValue(text);
    }

    /**
     * Sets the alpha value of the element
     * @param alpha The new value.
     */
    public void setAlpha(int alpha) {
        this.alpha.setValue(alpha);
    }

    /**
     * Sets the style of the element
     * @param style The new style.
     */
    public void setStyle(Style style) {
        this.style.select(style.getName());
    }
    
    
    /**
     * Returns the selected font.
     * @return font The font selected.
     */
    public String getFont() {
        return this.font.getSelected();
    }

    /**
     * Returns the text color.
     * @return The color of the text.
     */
    public Color getColorBack() {
        return this.colorBack.getValue();
    }

    /**
     * Returns the background color.
     * @return The color of the background.
     */
    public Color getColorText() {
        return this.colorText.getValue();
    }

    /**
     * Returns the selected alignment.
     * @return The alignment selected.
     */
    public String getAlignment() {
        return this.alignment.getSelected();
    }

    /**
     * Returns the selected stle.
     * @return The style selected.
     */
    public int getStyle() {
        return Style.getByName(this.style.getSelected()).getValue();
    }

    /**
     * Returns the font size.
     * @return The font size.
     */
    public Integer getFontSize() {
        return this.fontSize.getValue();
    }

    /**
     * Returns text of the element.
     * @return The text of the element.
     */
    public String getText() {
        return this.text.getValue();
    }

    /**
     * Returns the alpha value of the element.
     * @return The alpha value of the element.
     */
    public int getAlpha() {
        return this.alpha.getValue();
    }
    
    
    public static enum Style{
        PLAIN("plain", Font.PLAIN),
        ITALIC("italic", Font.ITALIC),
        BOLD("bold", Font.BOLD);

        private final String name;
        private final int value;
        
        private Style(final String name, final int value) {
            this.name = name;
            this.value = value;
        }

        public String getName() {
            return name;
        }

        public int getValue() {
            return value;
        }

        static Style getByName(final String name) {
            for (final Style nvp : values()) {
                if (nvp.getName().equals(name)) {
                    return nvp;
                }
            }
            throw new IllegalArgumentException("Invalid name: " + name);
        }
    }
    
    @Override
    public Class<? extends GraphicalElement> getCommonClass(Class<? extends GraphicalElement> c) {
        if(c.isAssignableFrom(this.getClass()))
            return c;
        else 
            return GraphicalElement.class;
    }
}
