package com.mapcomposer.view.ui;

//import javax.swing.Icon;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

/**
 * Menu bar of the main window. It contains all the menu element such as open new document, save document, export document ...
 */
public class WindowMenuBar extends JMenuBar{    
    
    /**Public main constructor.*/
    public WindowMenuBar(){
        super();
        JMenu menu = new JMenu("Files");
        menu.add(new JMenuItem("New"/*, (Icon) OrbisGISIcon.getIconImage("open")*/));
        menu.add(new JMenuItem("Save"/*, (Icon) OrbisGISIcon.getIconImage("save")*/));
        menu.add(new JMenuItem("Export"/*, (Icon) OrbisGISIcon.getIconImage("export_image")*/));
        this.add(menu);
    }
}
