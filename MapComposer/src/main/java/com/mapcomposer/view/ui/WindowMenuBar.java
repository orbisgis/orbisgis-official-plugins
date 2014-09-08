package com.mapcomposer.view.ui;

//import javax.swing.Icon;
import com.mapcomposer.controller.UIController;
import com.mapcomposer.model.graphicalelement.element.Document;
import com.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import com.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import com.mapcomposer.model.graphicalelement.element.cartographic.Scale;
import com.mapcomposer.model.graphicalelement.element.illustration.Image;
import com.mapcomposer.model.graphicalelement.element.text.TextElement;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Menu bar of the main window. It contains all the menu element such as open new document, save document, export document ...
 */
public class WindowMenuBar extends JMenuBar implements ActionListener{  
    
    private final JMenuItem newDoc;
    
    private final JMenuItem toFront;
    private final JMenuItem front;
    private final JMenuItem back;
    private final JMenuItem toBack;
    
    private final JMenuItem remove;
    
    private final JMenuItem textElement;
    private final JMenuItem image;
    private final JMenuItem mapImage;
    private final JMenuItem orientation;
    private final JMenuItem scale;
    
    /**Public main constructor.*/
    public WindowMenuBar(){
        super();
        JMenu menu1 = new JMenu("Files");
        newDoc = new JMenuItem("New");
        newDoc.addActionListener(this);
        menu1.add(newDoc);
        menu1.add(new JMenuItem("Save"/*, (Icon) OrbisGISIcon.getIconImage("save")*/));
        menu1.add(new JMenuItem("Export"/*, (Icon) OrbisGISIcon.getIconImage("export_image")*/));
        this.add(menu1);
        
        JMenu menu2 = new JMenu("Overlapping");
        toFront = new JMenuItem("toFront");
        toFront.addActionListener(this);
        menu2.add(toFront);
        front = new JMenuItem("front");
        front.addActionListener(this);
        menu2.add(front);
        back = new JMenuItem("back");
        back.addActionListener(this);
        menu2.add(back);
        toBack = new JMenuItem("toBack");
        toBack.addActionListener(this);
        menu2.add(toBack);
        menu2.addSeparator();
        remove = new JMenuItem("remove");
        remove.addActionListener(this);
        menu2.add(remove);
        this.add(menu2);
        
        JMenu menu3 = new JMenu("Elements");
        textElement = new JMenuItem("textElement");
        textElement.addActionListener(this);
        menu3.add(textElement);
        image = new JMenuItem("image");
        image.addActionListener(this);
        menu3.add(image);
        mapImage = new JMenuItem("mapImage");
        mapImage.addActionListener(this);
        menu3.add(mapImage);
        orientation = new JMenuItem("orientation");
        orientation.addActionListener(this);
        menu3.add(orientation);
        scale = new JMenuItem("scale");
        scale.addActionListener(this);
        menu3.add(scale);
        this.add(menu3);
    }
    @Override
    public void actionPerformed(ActionEvent ae) {
        
        if(ae.getSource()==newDoc){
            UIController.getInstance().removeAllGE();
            UIController.getInstance().addGE(Document.class);
        }
        
        
        if(ae.getSource()==toFront)
            UIController.getInstance().zindexChange(UIController.toFront);
        if(ae.getSource()==front)
            UIController.getInstance().zindexChange(UIController.front);
        if(ae.getSource()==back)
            UIController.getInstance().zindexChange(UIController.back);
        if(ae.getSource()==toBack)
            UIController.getInstance().zindexChange(UIController.toBack);
        
        if(ae.getSource()==remove)
            UIController.getInstance().remove();
        
        
        if(ae.getSource()==textElement)
            UIController.getInstance().addGE(TextElement.class);
        if(ae.getSource()==image)
            UIController.getInstance().addGE(Image.class);
        if(ae.getSource()==mapImage)
            UIController.getInstance().addGE(MapImage.class);
        if(ae.getSource()==orientation)
            UIController.getInstance().addGE(Orientation.class);
        if(ae.getSource()==scale)
            UIController.getInstance().addGE(Scale.class);
    }
}
