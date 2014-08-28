package com.mapcomposer.view.ui;

import com.mapcomposer.view.utils.CompositionJPanel;
import javax.swing.JPanel;

/**
 * Area for the map document composition.
 */
public class CompositionArea extends JPanel{
    
    /**Unique instance of the class.*/
    private static CompositionArea INSTANCE=null;
    
    /**Private constructor.*/
    private CompositionArea(){
        super(null);
    }
    
    /**
     * Returns the unique instance of the class.
     * @return The unique instance of the class.
     */
    public static CompositionArea getInstance(){
        if(INSTANCE==null){
            INSTANCE = new CompositionArea();
        }
        return INSTANCE;
    }
    
    /**
     * Adds a CompositionPanel to itself. Should be call only once for each GraphicalElement.
     * @param panel CompositionPanel to add.
     */
    public void addGE(CompositionJPanel panel){
        this.add(panel);
    }
    
    public void removeGE(CompositionJPanel panel){
        this.remove(panel);
    }
}
