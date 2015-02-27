package org.orbisgis.groovy.icons;

import org.orbisgis.sif.icons.BaseIcon;
import org.slf4j.LoggerFactory;

import javax.swing.ImageIcon;
import java.awt.Image;

/**
 * @author Nicolas Fortin
 */
public class GroovyIcon {
    private static BaseIcon iconManager = new BaseIcon(LoggerFactory.getLogger(GroovyIcon.class));

    /**
     * This is a static class
     */
    private GroovyIcon() {
    }

    /**
     * Retrieve icon awt Image by its name
     * @param iconName The icon name, without extension. All icons are stored in the png format.
     * @return The Image content requested, or an Image corresponding to a Missing Resource
     */
    public static Image getIconImage(String iconName) {
        return iconManager.getIconImage(GroovyIcon.class, iconName);
    }
    /**
     * Retrieve icon by its name
     * @param iconName The icon name, without extension. All icons are stored in the png format.
     * @return The ImageIcon requested, or an ImageIcon corresponding to a Missing Resource
     */
    public static ImageIcon getIcon(String iconName) {
        return iconManager.getIcon(GroovyIcon.class, iconName);
    }
}