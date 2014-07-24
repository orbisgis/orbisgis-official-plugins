package com.mapcomposer.view.graphicalelement;

import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.cartographic.CartographicElement;
import java.awt.BorderLayout;
import java.awt.image.BufferedImage;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import org.orbisgis.coremap.map.MapTransform;
import org.orbisgis.progress.NullProgressMonitor;

/**
 * Renderer associated to the Cartographic GraphicalElement.
 */
public class CartographicRenderer extends GERenderer {

    @Override
    public JPanel render(GraphicalElement ge) {
        //Gets back the panel rendered by the mother class
        JPanel panel = super.render(ge);
        
        /* Just for test the code
        File file = new File(LinkToOrbisGIS.getInstance().getViewWorkspace().getCoreWorkspace().getWorkspaceFolder()+"/maps/landcover2000.ows");      
        //OWS
        OwsMapContext omc = new OwsMapContext(LinkToOrbisGIS.getInstance().getDataManager());
        omc.setLocation(file.toURI());
        try {
            omc.read(new FileInputStream(file));
            System.out.println("read OK");
            omc.open(new NullProgressMonitor());
            System.out.println("open OK");
        } catch (FileNotFoundException | LayerException ex) {
            Logger.getLogger(CartographicRenderer.class.getName()).log(Level.SEVERE, null, ex);
        }*/
        
        // Draw in buffered image
        MapTransform mapTransform = new MapTransform();
        //mapTransform.setExtent(omc.getBoundingBox());
        CartographicElement ce = (CartographicElement)ge;
        mapTransform.setExtent(ce.getOwsMapContext().getBoundingBox());
        BufferedImage outImage = new BufferedImage(50, 150, BufferedImage.TYPE_INT_RGB);
        mapTransform.setImage(outImage);
        //omc.draw(mapTransform, new NullProgressMonitor());
        ce.getOwsMapContext().draw(mapTransform, new NullProgressMonitor());
        JLabel label = new JLabel(new ImageIcon(outImage));
        
        panel.add(label, BorderLayout.CENTER);
        return panel;
    }
    
}
