package com.mapcomposer.model.graphicalelement.element.cartographic;

/**
 * Key of a map. Actually not supported because the informations aren't well defined into the OWS-C.
 */
public class Key extends CartographicElement{

    @Override
    protected void initialisation() {
        this.setHeight(50);
        this.setWidth(50);
    }
    
}
