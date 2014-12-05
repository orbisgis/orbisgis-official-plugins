package org.orbisgis.mapcomposer.view.graphicalelement;

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.Scale;
import java.awt.Toolkit;
import java.awt.image.BufferedImage;

/**
 * Renderer associated to the scale GraphicalElement.
 */
public class ScaleRenderer extends SimpleGERenderer {
    /**Dot per millimeter screen resolution. */
    private double dpmm;

    @Override
    public BufferedImage createImageFromGE(GraphicalElement ge) {

        int dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        this.dpmm = (((double)dpi)/25.4);
        
        int resolution=-1;
        
        //Get the map scale
        double mapScalemmR = ((Scale)ge).getMapScale();
        //Calculate the real distance in milimeter that the Scale panel width represent.
        double panelWidthmmR = (ge.getWidth()/dpmm)*mapScalemmR;
        
        //Scale resolution. It gives the better size for black and white rectangles of the scale
        for(int i=0; resolution==-1; i++){
            if(panelWidthmmR/Math.pow(10, i)<10){
                resolution=(int)(1*Math.pow(10, i));
            }
            else if(panelWidthmmR/Math.pow(10, i)<50){
                resolution=(int)(5*Math.pow(10, i));
            }
        }
        //Convert the resolution from millimeters per rectangles to pixel per block
        resolution/=mapScalemmR;
        resolution*=dpmm;
        
        //Draw the BufferedImage
        BufferedImage bi = new BufferedImage(ge.getWidth(), ge.getHeight(), BufferedImage.TYPE_INT_RGB);
        
        boolean updown = false;
        int i=0;
        int width = ge.getWidth();
        while(width>=resolution){
            if(updown){
            bi.createGraphics().drawRect(i*resolution, 0, resolution, ge.getHeight()/2-1);
            bi.createGraphics().fillRect(i*resolution, ge.getHeight()/2, resolution, ge.getHeight()-ge.getHeight()/2);
            }
            else{
                bi.createGraphics().fillRect(i*resolution, 0, resolution, ge.getHeight()/2-1);
                bi.createGraphics().drawRect(i*resolution, (ge.getHeight()-1)/2, resolution, ge.getHeight()-ge.getHeight()/2);
            }
            updown=!updown;
            width-=resolution;
            i++;
        }
        if(updown){
            bi.createGraphics().drawRect(i*resolution, 0, width-1, ge.getHeight()/2-1);
            bi.createGraphics().fillRect(i*resolution, ge.getHeight()/2, width, ge.getHeight()-ge.getHeight()/2);
        }
        else{
            bi.createGraphics().fillRect(i*resolution, 0, width, ge.getHeight()/2-1);
            bi.createGraphics().drawRect(i*resolution, (ge.getHeight()-1)/2, width-1, ge.getHeight()-ge.getHeight()/2);
        }

        return applyRotationToBufferedImage(bi, ge);
    }
}
