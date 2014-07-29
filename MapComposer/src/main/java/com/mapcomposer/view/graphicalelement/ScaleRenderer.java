package com.mapcomposer.view.graphicalelement;

import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.cartographic.Scale;
import java.awt.BorderLayout;
import java.awt.Graphics;
import java.awt.Toolkit;
import javax.swing.JPanel;

/**
 * Renderer associated to the scale GraphicalElement.
 */
public class ScaleRenderer extends GERenderer{
    
    /**Dot per inch screen resolution. */
    private final int dpi;
    /**Dot per millimeter screen resolution. */
    private final int dpmm;
    
    /**
     * Main constructor
     */
    public ScaleRenderer(){
        //gets the screen dpi
        this.dpi = Toolkit.getDefaultToolkit().getScreenResolution();
        this.dpmm = (int)(((double)dpi)/25.4);
        System.out.println("dpi : "+dpi+"; dpmm : "+dpmm);
    }
    
    @Override
    public JPanel render(GraphicalElement ge){
        JPanel panel = super.render(ge);
        int resolution=-1;
        
        //Get the map scale
        ((Scale)ge).refresh();
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
        
        panel.setLayout(new BorderLayout());
        panel.add(new drawPanel(resolution, ge.getHeight(), ge.getWidth()), BorderLayout.CENTER);
        return panel;
    }
    
    /**
     * Private class that define the panel where the scale is drawn.
     */
    private class drawPanel extends JPanel{
        private int resolution, height, width;
        
        public drawPanel(int resolution, int height, int width){
            this.resolution = resolution;
            this.height = height;
            this.width = width;
        }
        
        @Override
        public void paintComponent(Graphics g) {
            super.paintComponent(g);
            //Used to alternate the white and the back rectangle
            boolean updown = false;
            int i=0;
            int width = this.width;
            while(width>=resolution){
                if(updown){
                g.drawRect(i*resolution, 0, resolution, height/2-1);
                g.fillRect(i*resolution, height/2-1, resolution, height-height/2-1);
                }
                else{
                    g.fillRect(i*resolution, 0, resolution, height/2-1);
                    g.drawRect(i*resolution, height/2-1, resolution, height-height/2-1);
                }
                updown=!updown;
                width-=resolution;
                i++;
            }
            if(updown){
                g.drawRect(i*resolution, 0, width-1, height/2-1);
                g.fillRect(i*resolution, height/2-1, width, height-height/2-1);
            }
            else{
                g.fillRect(i*resolution, 0, width, height/2-1);
                g.drawRect(i*resolution, height/2-1, width-1, height-height/2-1);
            }
        }
    }
}
