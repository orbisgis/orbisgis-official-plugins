package org.orbisgis.mapcomposer.view.ui;

import org.orbisgis.mapcomposer.view.utils.CompositionJPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Area for the map document composition.
 */
public class CompositionArea extends JPanel{
    /**JscrollPane of the CompositionArea*/
    private JScrollPane pane;
    /**Main JPanel of the CompositionArea*/
    private final JPanel panel;
    
    private Dimension dim;
    
    /**
     * Private constructor.
     */
    public CompositionArea(){
        super(new BorderLayout());
        dim=new Dimension(50, 50);
        panel = new JPanel(null);
        JPanel body = new JPanel(new BorderLayout());
        body.add(panel, BorderLayout.CENTER);
        pane = new JScrollPane(body, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.add(pane, BorderLayout.CENTER);
    }
    
    /**
     * Adds a CompositionPanel to itself. Should be call only once for each GraphicalElement.
     * @param panel CompositionPanel to add.
     */
    public void addGE(CompositionJPanel panel){
        this.panel.add(panel);
    }
    
    /**
     * Removes the given panel representing a GE.
     * @param panel Panel to remove.
     */
    public void removeGE(CompositionJPanel panel){
        if(this.panel.isAncestorOf(panel))
            this.panel.remove(panel);
        this.panel.repaint();
        this.panel.revalidate();
        refresh();
    }
    
    /**
     * Sets the dimension of the document in the compositionArea.
     * This method should be called on the document properties definition.
     * @param dim Dimension of the document.
     */
    public void setDocumentDimension(Dimension dim){
        this.dim=dim;
        this.panel.setPreferredSize(dim);
        this.revalidate();
        this.repaint();
    }

    /**
     * Sets the z-index of an element in the compositionArea.
     * @param comp Component to set.
     * @param i New z-index.
     */
    public void setZIndex(CompositionJPanel comp, int i) {
        panel.setComponentZOrder(comp, i);
    }

    /**
     * Removes all the drawn elements on the CompositionArea.
     */
    public void removeAllGE() {
        panel.removeAll();
        this.repaint();
        this.revalidate();
    }
    
    public void refresh(){
        panel.revalidate();
        panel.repaint();
    }
    
    public BufferedImage getDocBufferedImage(){
        BufferedImage bi = new BufferedImage(dim.width, dim.height, BufferedImage.TYPE_INT_ARGB);
        Graphics g = bi.createGraphics();
        panel.paint(g);
        g.dispose();
        return bi;
    }
}
