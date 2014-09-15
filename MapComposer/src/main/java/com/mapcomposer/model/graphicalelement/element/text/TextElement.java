package com.mapcomposer.model.graphicalelement.element.text;

import com.mapcomposer.Configuration;
import java.util.ArrayList;
import java.util.List;
import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.ColorCA;
import com.mapcomposer.model.configurationattribute.attribute.IntegerCA;
import com.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import com.mapcomposer.model.configurationattribute.attribute.StringCA;
import com.mapcomposer.model.graphicalelement.element.SimpleGE;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

/**
 * GraphicalElement displaying a text. Several aspects can be defined such as the text color, the font,the font size ...
 */
public final class TextElement extends SimpleGE{
    /** Fonts allowed */
    private final SourceListCA font;
    /** Color of the Text */
    private final ColorCA colorText;
    /** Color of the background */
    private final ColorCA colorBack;
    /** Alignment of the text */
    private final SourceListCA alignment;
    /** Style (plain, italic, bold) of the text */
    private final SourceListCA style;
    /** Size of the font */
    private final IntegerCA fontSize;
    /** Text itself */
    private final StringCA text;
    /** Alpha (transparency value) */
    private final IntegerCA alpha;
    
    /**
     * Public main constructor.
     */
    public TextElement(){
        //ConfigurationAttribute instantiation
        this.font = new SourceListCA();
        this.font.setName("Font");
        this.colorText = new ColorCA();
        this.colorText.setName("Text color");
        this.colorBack = new ColorCA();
        this.colorBack.setName("Background color");
        this.alignment = new SourceListCA();
        this.alignment.setName("Alignment");
        this.style = new SourceListCA();
        this.style.setName("Style");
        this.fontSize = new IntegerCA("Font size", 1, 99999);
        this.text = new StringCA();
        this.text.setName("Text");
        this.alpha = new IntegerCA("Alpha", 0, 255);
        
        //ConfigurationAttribute initialisation
        for(String s : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()){
            this.font.add(s);
        }
        this.alignment.add(Alignment.LEFT.getName());
        this.alignment.add(Alignment.CENTER.getName());
        this.alignment.add(Alignment.RIGHT.getName());
        this.style.add(Style.PLAIN.getName());
        this.style.add(Style.ITALIC.getName());
        this.style.add(Style.BOLD.getName());
        
        setDefaultValue();
        
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
     * Sets the alignment of the text
     * @param alignment The new alignment.
     */
    public void setStyle(Alignment alignment) {
        this.alignment.select(alignment.getName());
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
    public Alignment getAlignment() {
        return Alignment.getByName(this.alignment.getSelected());
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
    
    public static enum Alignment{
        LEFT("left"),
        CENTER("center"),
        RIGHT("right");
        
        private final String name;
        
        private Alignment(final String name) {
            this.name = name;
        }
        
        public String getName() {
            return name;
        }
        
        static Alignment getByName(final String name) {
            for (final Alignment nvp : values()) {
                if (nvp.getName().equals(name)) {
                    return nvp;
                }
            }
            throw new IllegalArgumentException("Invalid name: " + name);
        }
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
    
    private void setDefaultValue(){
        this.font.select(Configuration.defaultFont);
        this.colorBack.setValue(Configuration.defaultColorBack);
        this.colorText.setValue(Configuration.defaultColorText);
        this.alignment.select(Configuration.defaultAlignment);
        this.style.select(Configuration.defaultStyle);
        this.fontSize.setValue(Configuration.defaultFontSize);
        this.text.setValue(Configuration.defaultText);
        this.alpha.setValue(Configuration.defaultAlpha);
    }
    
    public void setDefaultElementShutter(){
        this.font.select(Configuration.defaultESFont);
        this.colorBack.setValue(Configuration.defaultESColorBack);
        this.colorText.setValue(Configuration.defaultESColorText);
        this.alignment.select(Configuration.defaultESAlignment);
        this.style.select(Configuration.defaultESStyle);
        this.fontSize.setValue(Configuration.defaultESFontSize);
        this.text.setValue(Configuration.defaultESText);
        this.alpha.setValue(Configuration.defaultESAlpha);
    }
}
