package com.mapcomposer.view.graphicalelement;

import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.orbisgis.progress.NullProgressMonitor;

/**
 * Renderer associated to the Cartographic GraphicalElement.
 */
public class MapImageRenderer extends GERenderer {

    @Override
    public JPanel render(GraphicalElement ge) {
        //Gets back the panel rendered by the mother class
        JPanel panel = super.render(ge);
        
        // Draw in buffered image
        ((MapImage)ge).refresh();
        if(((MapImage)ge).getImage()!=null){
            JLabel label = new JLabel(new ImageIcon(((MapImage)ge).getImage()));
            panel.add(label, BorderLayout.CENTER);
        }
        else{
            System.out.println(((MapImage)ge).getImage());
        }
        return panel;
    }
    
}
