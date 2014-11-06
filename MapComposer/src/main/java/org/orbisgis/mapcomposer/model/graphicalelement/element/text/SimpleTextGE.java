package org.orbisgis.mapcomposer.model.graphicalelement.element.text;

import java.util.ArrayList;
import java.util.List;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.ColorCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.IntegerCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceListCA;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA;
import org.orbisgis.mapcomposer.model.configurationattribute.utils.CAFactory;
import org.orbisgis.mapcomposer.model.graphicalelement.element.SimpleGE;
import java.awt.Color;
import java.awt.Font;
import java.awt.GraphicsEnvironment;

/**
 * GraphicalElement displaying a text. Several aspects can be defined such as the text color, the font,the font size ...
 */
public class SimpleTextGE extends SimpleGE{
    /** Fonts allowed */
    private SourceListCA font;
    /** Color of the Text */
    private ColorCA colorText;
    /** Color of the background */
    private ColorCA colorBack;
    /** Alignment of the text */
    private SourceListCA alignment;
    /** Style (plain, italic, bold) of the text */
    private SourceListCA style;
    /** Size of the font */
    private IntegerCA fontSize;
    /** Text itself */
    private StringCA text;
    /** Alpha (transparency value) */
    private IntegerCA alpha;
    
    /**
     * Public main constructor.
     */
    public SimpleTextGE(){
        //ConfigurationAttribute instantiation
        this.font =CAFactory.createSourceListCA("Font");
        this.colorText = CAFactory.createColorCA("Text color");
        this.colorBack = CAFactory.createColorCA("Background color");
        this.alignment = CAFactory.createSourceListCA("Alignment");
        this.style = CAFactory.createSourceListCA("Style");
        this.fontSize = CAFactory.createIntegerCA("Font size", 1, Integer.MAX_VALUE, 12);
        this.text = CAFactory.createStringCA("Text");
        this.alpha = CAFactory.createIntegerCA("Alpha", 0, 255, 0);
        //ConfigurationAttribute initialisation
        for(String s : GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()){
            this.font.add(s);
        }
        this.font.select(GraphicsEnvironment.getLocalGraphicsEnvironment().getAvailableFontFamilyNames()[0]);
        this.alignment.add(Alignment.LEFT.name());
        this.alignment.add(Alignment.CENTER.name());
        this.alignment.add(Alignment.RIGHT.name());
        this.alignment.select(Alignment.CENTER.name());
        this.style.add(Style.PLAIN.name());
        this.style.add(Style.ITALIC.name());
        this.style.add(Style.BOLD.name());
        this.style.select(Style.PLAIN.name());
    }
    
    /**
     * Public copy constructor.
     * @param stge SimpleTextGE to copy.
     */
    public SimpleTextGE(SimpleTextGE stge){
        super(stge);
        //ConfigurationAttribute instantiation
        this.font = stge.font;
        this.colorText = stge.colorText;
        this.colorBack = stge.colorBack;
        this.alignment = stge.alignment;
        this.style = stge.style;
        this.fontSize = stge.fontSize;
        this.text = stge.text;
        this.alpha = stge.alpha;
    }

    @Override
    public List<ConfigurationAttribute> getAllAttributes() {
        List<ConfigurationAttribute> list = new ArrayList<>();
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
    public void addFont(String font) {this.font.add(font);}

    /**
     * Sets the color of the text.
     * @param color The new color.
     */
    public void setColorText(Color color) {this.colorText.setValue(color);}

    /**
     * Sets the color of the background.
     * @param color The new color.
     */
    public void setColorBack(Color color) {this.colorBack.setValue(color);}

    /**
     * Sets the font size of the text.
     * @param fontSize The new font size.
     */
    public void setFontSize(int fontSize) {this.fontSize.setValue(fontSize);}

    /**
     * Sets the text of the element
     * @param text The new text.
     */
    public void setText(String text) {this.text.setValue(text);}

    /**
     * Sets the alpha value of the element
     * @param alpha The new value.
     */
    public void setAlpha(int alpha) {this.alpha.setValue(alpha);}

    /**
     * Sets the style of the element
     * @param style The new style.
     */
    public void setStyle(Style style) {this.style.select(style.name());}

    /**
     * Sets the alignment of the text
     * @param alignment The new alignment.
     */
    public void setStyle(Alignment alignment) {this.alignment.select(alignment.name());}
    
    
    /**
     * Returns the selected font.
     * @return font The font selected.
     */
    public String getFont() {return this.font.getSelected();}

    /**
     * Returns the text color.
     * @return The color of the text.
     */
    public Color getColorBack() {return this.colorBack.getValue();}

    /**
     * Returns the background color.
     * @return The color of the background.
     */
    public Color getColorText() {return this.colorText.getValue();}

    /**
     * Returns the selected alignment.
     * @return The alignment selected.
     */
    public Alignment getAlignment() {return Alignment.valueOf(this.alignment.getSelected());}

    /**
     * Returns the selected stle.
     * @return The style selected.
     */
    public int getStyle() {return Style.valueOf(this.style.getSelected()).getFontStyle();}

    /**
     * Returns the font size.
     * @return The font size.
     */
    public Integer getFontSize() {return this.fontSize.getValue();}

    /**
     * Returns text of the element.
     * @return The text of the element.
     */
    public String getText() {return this.text.getValue();}

    /**
     * Returns the alpha value of the element.
     * @return The alpha value of the element.
     */
    public int getAlpha() {return this.alpha.getValue();}
    
    /**
     * Enumeration for the text alignment.
     */
    public static enum Alignment{ LEFT, CENTER, RIGHT; }
    
    /**
     * Enumeration for the text font style.
     */
    public static enum Style{
        PLAIN(Font.PLAIN),
        ITALIC(Font.ITALIC),
        BOLD(Font.BOLD);
        /**Font style value.*/
        private final int value;
        /**Enum constructor*/
        private Style(final int value) {this.value = value;}
        /**
         * Returns the font style.
         * @return The font style.
         */
        public int getFontStyle() {return value;}
    }
}
