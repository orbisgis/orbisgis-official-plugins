package com.mapcomposer.view.graphicalelement;

import com.mapcomposer.controller.UIController;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import java.awt.Insets;
import javax.swing.JPanel;

/**
 * Base renderer for GraphicalElement.
 * Every extention of the renderer sould call super.render(ge) to get the panel where the element is displayed.
 */
public class GERenderer {
    /**
     * Renders the GrapgicalElement given.
     * @param ge GraphicalElement to render.
     * @return JPanel of the GraphicalElement.
     */
    public JPanel render(GraphicalElement ge){
        JPanel panel = UIController.getPanel(ge);
        Insets i = panel.getInsets();
        panel.setBounds(ge.getX(), ge.getY(), ge.getWidth(), ge.getHeight());
        //TODO : implement a method to rotate the panel
        //((Graphics2D)panel.getGraphics()).rotate(Math.toRadians(ge.getRotation()));
        return panel;
    }
}
