package com.mapcomposer.view.ui;

import com.mapcomposer.controller.UIController;
import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JSplitPane;
import org.orbisgis.view.icons.OrbisGISIcon;
import org.orbisgis.viewapi.components.actions.DefaultAction;
import org.orbisgis.viewapi.main.frames.ext.MainFrameAction;
import static org.orbisgis.viewapi.main.frames.ext.MainFrameAction.MENU_TOOLS;
//import org.orbisgis.view.icons.OrbisGISIcon;

/**
 * Main window of the map composer. during the developement, the map composer will be a separeted window.
 * Once finished, it probably will become a part of OrbisGIS using the dockable interface.
 */
public class MainWindow extends JFrame implements MainFrameAction{
    
    /**Unique instance of the class.*/
    private static MainWindow INSTANCE=null;
    
    public static String MENU_MAPCOMPOSER = "MapComposer";
    
    /**Private constructor.*/
    private MainWindow(){
        super("Map composer");
        //Sets the default size to the window
        this.setSize(1024, 768);
        this.setIconImage(OrbisGISIcon.getIconImage("mini_orbisgis"));
        this.setJMenuBar(new WindowMenuBar());
        
        JPanel pan = new JPanel();
        pan.setLayout(new BorderLayout());
        JSplitPane pane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ConfigurationShutter.getInstance(), CompositionArea.getInstance());
        pane1.setOneTouchExpandable(true);
        this.setContentPane(pane1);
        
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
    
    public void showMapComposer(){
        this.setVisible(true);
    }

    @Override
    public List<Action> createActions(org.orbisgis.viewapi.main.frames.ext.MainWindow target) {
        List<Action> actions = new ArrayList<Action>();
        actions.add(new DefaultAction(MENU_MAPCOMPOSER,"Map Composer",
                new ImageIcon(),
                EventHandler.create(ActionListener.class,this,"showMapComposer")).setParent(MENU_TOOLS));
        return actions;
    }

    @Override
    public void disposeActions(org.orbisgis.viewapi.main.frames.ext.MainWindow target, List<Action> actions) {
        this.dispose();
    }
}
