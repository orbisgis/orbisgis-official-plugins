package com.mapcomposer.view.ui;

import com.mapcomposer.controller.UIController;
import com.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import com.mapcomposer.model.graphicalelement.utils.GEManager;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Lateral shutter containing the GraphicalElements.
 */
public class ElementShutter extends Shutter{
    
    /**Unique instance of the class*/
    private static ElementShutter INSTANCE = null;
    /**Button to add MapImage */
    private JButton mapImage;
    
    /**Private constructor*/
    private ElementShutter(){
        super(200, Shutter.RIGHT_SHUTTER);
        JPanel pan = new JPanel();
        mapImage = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("defaultData/map.png")));
        mapImage.setPreferredSize(new Dimension(100, 100));
        mapImage.addMouseListener(this);
        pan.add(mapImage);
        this.setBodyPanel(pan);
    }
    
    /**
     * Return the unique instance of the class.
     * @return Unique instanceof the class.
     */
    public static ElementShutter getInstance(){
        if(INSTANCE==null){
            INSTANCE = new ElementShutter();
        }
        return INSTANCE;
    }
    
    @Override
    public void mouseClicked(MouseEvent e) {
        super.mouseClicked(e);
        if(e.getSource()==mapImage){
            UIController.getInstance().addGE(MapImage.class);
        }
    }
}
