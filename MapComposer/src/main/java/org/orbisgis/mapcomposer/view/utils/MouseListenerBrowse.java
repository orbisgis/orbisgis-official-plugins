package org.orbisgis.mapcomposer.view.utils;

import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JTextField;

/**
 * Mouse Listener that open a file browser.
 */
public class MouseListenerBrowse implements MouseListener{
    private final JTextField jtf;
    
    public MouseListenerBrowse(JTextField jtf){
        this.jtf = jtf;
    }
    
    @Override
    public void mouseClicked(MouseEvent me) { 
        final JFileChooser fc = new JFileChooser();
        String path = jtf.getText();
        if(new File(path).exists())
            fc.setCurrentDirectory(new File(path.substring(0, path.lastIndexOf("/"))));
        if(fc.showOpenDialog(new JFrame())==JFileChooser.APPROVE_OPTION){
            jtf.setText(fc.getSelectedFile().getAbsolutePath());
        }
    }
    @Override public void mousePressed(MouseEvent me) {}
    @Override public void mouseReleased(MouseEvent me) {}
    @Override public void mouseEntered(MouseEvent me) {}
    @Override public void mouseExited(MouseEvent me) {}
}
