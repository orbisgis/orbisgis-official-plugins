package org.orbisgis.mapcomposer.view.utils;

import java.awt.Color;
import java.awt.event.MouseListener;
import java.beans.EventHandler;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JPanel;

/**
 * Color chooser
 */
public class ColorChooser extends JFrame{
    private final JButton label;
    private final JButton button;
    final JColorChooser jcc;
    
    public ColorChooser(JButton label){
        this.label = label;
        jcc = new JColorChooser(Color.yellow);
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(jcc);
        this.button = new JButton("Ok");
        this.button.addMouseListener(EventHandler.create(MouseListener.class, this, "mouseClicked", "source", "mouseClicked"));
        this.button.setSize(40, 20);
        panel.add(button);
        this.add(panel);
        this.pack();
    }

   public void mouseClicked(Object o) {
        if(o==button){
            label.setBackground(jcc.getColor());
            this.setVisible(false);
        }
    }
}
