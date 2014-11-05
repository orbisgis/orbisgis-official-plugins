package org.orbisgis.mapcomposer.view.utils;

import java.awt.Color;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Color chooser
 */
public class ColorChooser extends JFrame implements MouseListener{
    private JLabel label;
    private JFrame frame;
    private JButton button;
    final JColorChooser jcc;
    
    public ColorChooser(JLabel label){
        this.label = label;
        jcc = new JColorChooser(Color.yellow);
        frame = new JFrame();
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(jcc);
        button = new JButton("Ok");
        button.addMouseListener(this);
        button.setSize(40, 20);
        panel.add(button);
        frame.add(panel);
        frame.pack();
    }

    @Override public void mouseClicked(MouseEvent me) {
        frame.setVisible(true);
        if(me.getSource()==button){
            label.setForeground(jcc.getColor());
            frame.setVisible(false);
        }
    }

    @Override public void mousePressed(MouseEvent me) {}

    @Override public void mouseReleased(MouseEvent me) {}

    @Override public void mouseEntered(MouseEvent me) {}

    @Override public void mouseExited(MouseEvent me) {}
    
}
