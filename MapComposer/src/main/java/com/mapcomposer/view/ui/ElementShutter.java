package com.mapcomposer.view.ui;

import com.mapcomposer.controller.UIController;
import com.mapcomposer.model.graphicalelement.element.cartographic.Key;
import com.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import com.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import com.mapcomposer.model.graphicalelement.element.cartographic.Scale;
import com.mapcomposer.model.graphicalelement.element.illustration.Image;
import com.mapcomposer.model.graphicalelement.element.text.TextElement;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JPanel;

/**
 * Lateral shutter containing the GraphicalElements.
 */
public class ElementShutter extends Shutter implements MouseListener{
    
    /**Unique instance of the class*/
    private static ElementShutter INSTANCE = null;
    /**Button to add MapImage */
    private final JButton mapImage;
    /**Button to add Key */
    private final JButton key;
    /**Button to add Orientation */
    private final JButton orientation;
    /**Button to add Scale */
    private final JButton scale;
    /**Button to add Text */
    private final JButton text;
    /**Button to add Image */
    private final JButton image;
    
    /**Private constructor*/
    private ElementShutter(){
        super();
        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
        mapImage = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("defaultData/map.png")));
        mapImage.setPreferredSize(new Dimension(100, 100));
        mapImage.addMouseListener(this);
        pan.add(mapImage);
        key = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("defaultData/key.png")));
        key.setPreferredSize(new Dimension(100, 100));
        key.addMouseListener(this);
        pan.add(key);
        orientation = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("defaultData/orientation.png")));
        orientation.setPreferredSize(new Dimension(100, 100));
        orientation.addMouseListener(this);
        pan.add(orientation);
        scale = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("defaultData/scale.png")));
        scale.setPreferredSize(new Dimension(100, 100));
        scale.addMouseListener(this);
        pan.add(scale);
        text = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("defaultData/text.png")));
        text.setPreferredSize(new Dimension(100, 100));
        text.addMouseListener(this);
        pan.add(text);
        image = new JButton(new ImageIcon(this.getClass().getClassLoader().getResource("defaultData/image.png")));
        image.setPreferredSize(new Dimension(100, 100));
        image.addMouseListener(this);
        pan.add(image);
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
        if(e.getSource()==mapImage){
            UIController.getInstance().addGE(MapImage.class);
        }
        if(e.getSource()==key){
            UIController.getInstance().addGE(Key.class);
        }
        if(e.getSource()==orientation){
            UIController.getInstance().addGE(Orientation.class);
        }
        if(e.getSource()==scale){
            UIController.getInstance().addGE(Scale.class);
        }
        if(e.getSource()==text){
            UIController.getInstance().addGE(TextElement.class);
        }
        if(e.getSource()==image){
            UIController.getInstance().addGE(Image.class);
        }
    }

    @Override
    public void mousePressed(MouseEvent me) {}

    @Override
    public void mouseReleased(MouseEvent me) {}

    @Override
    public void mouseEntered(MouseEvent me) {}

    @Override
    public void mouseExited(MouseEvent me) {}
}
