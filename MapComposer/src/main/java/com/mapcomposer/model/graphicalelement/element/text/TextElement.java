package com.mapcomposer.model.graphicalelement.element.text;

import java.util.ArrayList;
import java.util.List;
import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Choice;
import com.mapcomposer.model.configurationattribute.attribute.Text;
import com.mapcomposer.model.configurationattribute.attribute.Numeric;
import com.mapcomposer.model.graphicalelement.GraphicalElement;

/**
 * GraphicalElement displaying a text. Several aspects can be defined such as the text color, the font,the font size ...
 */
public class TextElement extends GraphicalElement{
    private final Choice font;
    private final Choice color;
    private final Choice alignment;
    private final Numeric fontSize;
    private final Text text;
    
    /**
     * Public main constructor.
     */
    public TextElement(){
        this.font = new Choice("Font");
        this.color = new Choice("Color");
        this.alignment = new Choice("Alignment");
        this.fontSize = new Numeric("Font size");
        this.text = new Text("Text");
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
     * Add an alignement.
     * @param alignment A new alignement.
     */
    public void addAlignment(String alignment) {
        this.alignment.add(alignment);
    }

    /**
     * Sets the font size of the text.
     * @param fontSize The new font size.
     */
    public void setFontSize(int fontSize) {
        this.fontSize.setPropertyValue(fontSize);
    }

    /**
     * Sets the text of the element
     * @param text The new text.
     */
    public void setText(String text) {
        this.text.setPropertyValue(text);
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
     * Returns the font size.
     * @return The font size.
     */
    public Integer getFontSize() {
        return this.fontSize.getPropertyValue();
    }

    /**
     * Returns text of the element.
     * @return The text of the element.
     */
    public String getText() {
        return this.text.getPropertyValue();
    }

    @Override
    protected void initialisation() {
        this.setText("no text");
        this.setHeight(30);
        this.setWidth(150);
    }
}
