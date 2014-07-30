package com.mapcomposer.view.graphicalelement;

import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import com.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import java.awt.BorderLayout;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Renderer associated to the Orientation GraphicalElement.
 */
public class OrientationRenderer extends GERenderer{
    
    @Override
    public JPanel render(GraphicalElement ge) {
        //Gets back the panel rendered by the mother class
        JPanel panel = super.render(ge);
        
        // Draw the orientation into 
        File f = new File(((Orientation)ge).getIconPath());
        if((new File(((Orientation)ge).getIconPath())).isFile()){
            JLabel label = new JLabel(new ImageIcon(((Orientation)ge).getIconPath()));
            panel.add(label, BorderLayout.CENTER);
        }
        else{
            System.out.println(((MapImage)ge).getImage());
        }
        return panel;
    }
    
}
