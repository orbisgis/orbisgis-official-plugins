package com.mapcomposer.view.ui;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.utils.CAManager;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.cartographic.Key;
import com.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import com.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import com.mapcomposer.model.graphicalelement.element.cartographic.Scale;
import com.mapcomposer.model.graphicalelement.element.illustration.Data;
import com.mapcomposer.model.graphicalelement.element.illustration.Image;
import com.mapcomposer.model.graphicalelement.element.text.TextElement;
import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Lateral shutter containing the configuration elements.
 */
public class ConfigurationShutter extends Shutter{
    
    /**Unique instance of the class*/
    private static ConfigurationShutter INSTANCE = null;
    
    /**Private constructor*/
    private ConfigurationShutter(){
        super(300, Shutter.LEFT_SHUTTER);
    }
    
    public void dispalyConfiguration(GraphicalElement ge){
        JPanel pan = new JPanel();
        pan.setLayout(new BoxLayout(pan, BoxLayout.PAGE_AXIS));
        for(ConfigurationAttribute ca : ge.getAllAttributes()){
            System.out.println(ca.getPropertyName());
            System.out.println(ca.getPropertyValue());
            pan.add(CAManager.getInstance().getRenderer(ca).render(ca));
        }
        JScrollPane listScroller = new JScrollPane(pan);
        this.add(listScroller);
    }
    
    /**
     * Returns the unique instace of the class.
     * @return The unique instance of te class.
     */
    public static ConfigurationShutter getInstance(){
        if(INSTANCE==null){
            INSTANCE = new ConfigurationShutter();
        }
        return INSTANCE;
    }
    
}
