package com.mapcomposer.view.ui;

import javax.swing.JPanel;

/**
 * Area for the map document composition.
 */
public class CompositionArea extends JPanel{
    
    /**Unique instance of the class.*/
    private static CompositionArea INSTANCE=null;
    
    /**Private constructor.*/
    private CompositionArea(){
        super();
    }
    
    /**
     * Returns the unique instance of the class.
     * @return 
     */
    public static CompositionArea getInstance(){
        if(INSTANCE==null){
            INSTANCE = new CompositionArea();
        }
        return INSTANCE;
    }
}
