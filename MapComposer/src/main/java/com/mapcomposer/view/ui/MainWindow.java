package com.mapcomposer.view.ui;

import com.mapcomposer.controller.UIController;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import org.orbisgis.view.icons.OrbisGISIcon;
//import org.orbisgis.view.icons.OrbisGISIcon;

/**
 * Main window of the map composer. during the developement, the map composer will be a separeted window.
 * Once finished, it probably will become a part of OrbisGIS using the dockable interface.
 */
public class MainWindow extends JFrame{
    
    /**Unique instance of the class.*/
    private static MainWindow INSTANCE=null;
    
    /**Private constructor.*/
    private MainWindow(){
        super("Map composer");
        //Sets the default size to the window
        this.setSize(1024, 768);
        this.setIconImage(OrbisGISIcon.getIconImage("mini_orbisgis"));
        this.setJMenuBar(new WindowMenuBar());
        
        JPanel pan = new JPanel();
        pan.setLayout(new BorderLayout());
        pan.add(ConfigurationShutter.getInstance(), BorderLayout.LINE_START);
        pan.add(CompositionArea.getInstance(), BorderLayout.CENTER);
        pan.add(ElementShutter.getInstance(), BorderLayout.LINE_END);
        this.setContentPane(pan);
        
        //Instantiation of the UIController
        UIController.getInstance();
    }
    
    /**
     * Returns the unique instance of the class.
     * @return The instanceof the class.
     */
    public static MainWindow getInstance(){
        if(INSTANCE==null)
            INSTANCE = new MainWindow();
        return INSTANCE;
    }
}
