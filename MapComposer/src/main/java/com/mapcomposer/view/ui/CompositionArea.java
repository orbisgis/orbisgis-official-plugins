package com.mapcomposer.view.ui;

import com.mapcomposer.view.utils.CompositionJPanel;
import java.awt.BorderLayout;
import java.awt.Dimension;
import javax.swing.JPanel;
import javax.swing.JScrollPane;

/**
 * Area for the map document composition.
 */
public class CompositionArea extends JPanel{
    
    /**Unique instance of the class.*/
    private static CompositionArea INSTANCE=null;
    /**JscrollPane of the CompositionArea*/
    private JScrollPane pane;
    /**Main JPanel of the CompositionArea*/
    private final JPanel panel;
    
    /**
     * Private constructor.
     */
    private CompositionArea(){
        super(new BorderLayout());
        panel = new JPanel(null);
        JPanel body = new JPanel(new BorderLayout());
        body.add(panel, BorderLayout.CENTER);
        pane = new JScrollPane(body, JScrollPane.VERTICAL_SCROLLBAR_ALWAYS, JScrollPane.HORIZONTAL_SCROLLBAR_ALWAYS);
        this.add(pane, BorderLayout.CENTER);
    }
    
    /**
     * Returns the unique instance of the class.
     * @return The unique instance of the class.
     */
    public static CompositionArea getInstance(){
        if(INSTANCE==null){
            INSTANCE = new CompositionArea();
        }
        return INSTANCE;
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
        this.panel.repaint(panel.getBounds());
        this.panel.revalidate();
    }
    
    /**
     * Sets the dimension of the document in the compositionArea.
     * This method should be called on the document properties definition.
     * @param dim Dimension of the document.
     */
    public void setDocumentDimension(Dimension dim){
        this.panel.setPreferredSize(dim);
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
    }
}
