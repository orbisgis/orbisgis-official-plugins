package com.mapcomposer.view.ui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BorderFactory;
import javax.swing.JPanel;
import javax.swing.border.Border;

/**
 * Root class for the laterals shutters.
 */
public abstract class Shutter extends JPanel implements MouseListener{
    
    /** Code for the left position of the Shutter */
    public static int LEFT_SHUTTER = 1;
    /** Code for the right position of the Shutter */
    public static int RIGHT_SHUTTER = 2;
    /** Base width of the lateral button */
    private static final int clickWidth=20;
    /** Minimum size of the Shutter to only show the lateral button */
    private static final int minWidth=clickWidth+8;
    
    /** Normal width ofthe shutter.*/
    private final int width;
    
    /** JPanel containing the body ofthe shutter */
    private final JPanel contentPanel;
    
    /** JPanel of thelateral click button */
    private final JPanel click;
    
    /**Boolean representing the state of the shutter*/
    private boolean open;
    
    /**
     * Main constructor.
     * @param defaultWidth Default width of the shutter when it's open.
     * @param position Position code for the side of the Shutter. Can be LEFT_SHUTTER or RIGHT_SHUTTER.
     */
    public Shutter(int defaultWidth, int position){
        //Border definition.
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        
        contentPanel = new JPanel();
        //Click button creation.
        click = new JPanel();
        click.setPreferredSize(new Dimension(clickWidth, 0));
        click.setBorder(raisedbevel);
        
        //Shutter creation.
        width = defaultWidth;
        this.setPreferredSize(new Dimension(width, 0));
        this.setBorder(BorderFactory.createCompoundBorder(raisedbevel, loweredbevel));
        
        //Positionning the shutter
        this.setLayout(new BorderLayout());
        this.add(contentPanel, BorderLayout.CENTER);
        if(position==LEFT_SHUTTER)
            this.add(click, BorderLayout.LINE_END);
        if(position==RIGHT_SHUTTER)
            this.add(click, BorderLayout.LINE_START);
        
        open=true;
        
        click.addMouseListener(this);
    }
    
    /**
     * Returns the JPanel of the body of the Shutter.
     * @return Body Jpanel.
     */
    public JPanel getContentPanel(){
        return contentPanel;
    }
    
    /**
     * Toggles the state of the Shutter (open or close).
     */
    public void toggle(){
        if(open){
            this.setPreferredSize(new Dimension(minWidth, 0));
        }
        else{
            this.setPreferredSize(new Dimension(width, 0));
        }
        this.revalidate();
        open =! open;
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        if(e.getSource()==click){
            toggle();
            System.out.println("click");
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getSource()==click){
            click.setBorder(BorderFactory.createLoweredBevelBorder());
            System.out.println("pressed");
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(e.getSource()==click){
            click.setBorder(BorderFactory.createRaisedBevelBorder());
            System.out.println("released");
        }
        
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        
    }

    @Override
    public void mouseExited(MouseEvent e) {
        
    }
}
