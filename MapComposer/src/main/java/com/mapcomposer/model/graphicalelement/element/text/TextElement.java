package com.mapcomposer.model.graphicalelement.element.text;

import java.util.ArrayList;
import java.util.List;
import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Choice;
import com.mapcomposer.model.configurationattribute.attribute.Text;
import com.mapcomposer.model.configurationattribute.attribute.Numeric;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

/**
 * GraphicalElement displaying a text. Several aspects can be defined such as the text color, the font,the font size ...
 */
public class TextElement extends GraphicalElement{
    private final Choice font;
    private final Choice color;
    private final Choice alignment;
    private final Choice style;
    private final Numeric fontSize;
    private final Text text;
    
    /**
     * Public main constructor.
     */
    public TextElement(){
        this.font = new Choice("Font");
        this.color = new Choice("Color");
        this.alignment = new Choice("Alignment");
        this.style = new Choice("Style");
        this.fontSize = new Numeric("Font size");
        this.text = new Text("Text");
        
        for(String s : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()){
            this.font.add(s);
        }
        this.color.add("black");
        this.alignment.add("Left");
        this.style.add(Style.PLAIN.getName());
        this.style.add(Style.ITALIC.getName());
        this.style.add(Style.BOLD.getName());
        this.style.select("plain");
        System.out.println(this.style.getSelected());
        System.out.println(this.style.getValue());
        this.fontSize.setValue(8);
        this.text.setValue("no text");
        this.setHeight(30);
        this.setWidth(150);
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
        list.addAll( super.getAllAttributes());
        list.add(font);
        list.add(color);
        list.add(alignment);
        list.add(fontSize);
        list.add(text);
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
     * Add a predefined color.
     * @param color A new color.
     */
    public void addColor(String color) {
        this.color.add(color);
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
     * Returns the selected color.
     * @return The color selected.
     */
    public String getColor() {
        return this.color.getSelected();
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
}
