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
    
    public static int LEFT_SHUTTER = 1;
    public static int RIGHT_SHUTTER = 2;
    private static final int clickWidth=20;
    private static final int minWidth=28;
    
    /**Normal width ofthe shutter.*/
    private final int width;
    
    private final JPanel contentPanel;
    
    private final JPanel click;
    
    /**Boolean representing the state of the shutter*/
    private boolean open;
    
    public Shutter(int defaultWidth, int position){
        
        Border raisedbevel = BorderFactory.createRaisedBevelBorder();
        Border loweredbevel = BorderFactory.createLoweredBevelBorder();
        
        contentPanel = new JPanel();
        click = new JPanel();
        click.setPreferredSize(new Dimension(clickWidth, 0));
        click.setBorder(raisedbevel);
        
        width = defaultWidth;
        this.setPreferredSize(new Dimension(width, 0));
        this.setBorder(BorderFactory.createCompoundBorder(raisedbevel, loweredbevel));
        
        this.setLayout(new BorderLayout());
        this.add(contentPanel, BorderLayout.CENTER);
        if(position==LEFT_SHUTTER)
            this.add(click, BorderLayout.LINE_END);
        if(position==RIGHT_SHUTTER)
            this.add(click, BorderLayout.LINE_START);
        
        open=true;
        
        click.addMouseListener(this);
    }
    
    public JPanel getContentPanel(){
        return contentPanel;
    }
    
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
