package com.mapcomposer.view.ui;

/**
 * Lateral shutter containing the GraphicalElements.
 */
public class ElementShutter extends Shutter{
    
    /**Unique instance of the class*/
    private static ElementShutter INSTANCE = null;
    
    /**Private constructor*/
    private ElementShutter(){
        super(200, Shutter.RIGHT_SHUTTER);
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
    
}
