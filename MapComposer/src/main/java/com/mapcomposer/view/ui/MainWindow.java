package com.mapcomposer.view.ui;

import com.mapcomposer.controller.UIController;
import com.mapcomposer.controller.UIController.Align;
import static com.mapcomposer.controller.UIController.ZIndex;
import com.mapcomposer.model.graphicalelement.element.Document;
import com.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import com.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import com.mapcomposer.model.graphicalelement.element.cartographic.Scale;
import com.mapcomposer.model.graphicalelement.element.illustration.Image;
import com.mapcomposer.model.graphicalelement.element.text.TextElement;
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
import javax.swing.JToolBar;
import org.orbisgis.view.components.actions.ActionCommands;
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
    public static String NEW_COMPOSER = "NEW_COMPOSER";
    public static String CONFIGURATION = "CONFIGURATION";
    public static String SAVE = "SAVE";
    public static String EXPORT_COMPOSER = "EXPORT_COMPOSER";
    public static String ADD_MAP = "ADD_MAP";
    public static String ADD_TEXT = "ADD_TEXT";
    public static String ADD_LEGEND = "ADD_LEGEND";
    public static String ADD_ORIENTATION = "ADD_ORIENTATION";
    public static String ADD_SCALE = "ADD_ORIENTATION";
    public static String ADD_PICTURE = "ADD_PICTURE";
    public static String DRAW_CIRCLE = "DRAW_CIRCLE";
    public static String DRAW_POLYGON = "DRAW_POLYGON";
    public static String MOVE_BACK = "MOVE_BACK";
    public static String MOVE_DOWN = "MOVE_DOWN";
    public static String MOVE_ON = "MOVE_ON";
    public static String MOVE_FRONT = "MOVE_FRONT";
    public static String ALIGN_TO_LEFT = "ALIGN_TO_LEFT";
    public static String ALIGN_TO_CENTER = "ALIGN_TO_CENTER";
    public static String ALIGN_TO_RIGHT = "ALIGN_TO_RIGHT";
    public static String ALIGN_TO_BOTTOM = "ALIGN_TO_BOTTOM";
    public static String ALIGN_TO_MIDDLE = "ALIGN_TO_MIDDLE";
    public static String ALIGN_TO_TOP = "ALIGN_TO_TOP";
    public static String PROPERTIE = "PROPERTIE";
    public static String DELETE = "DELETE";
    
    private ActionCommands actions = new ActionCommands();
    private JToolBar toolBar = new JToolBar();
    
    /**Private constructor.*/
    private MainWindow(){
        super("Map composer");
        //Sets the default size to the window
        this.setSize(1024, 768);
        this.setIconImage(OrbisGISIcon.getIconImage("mini_orbisgis"));
        //this.setJMenuBar(new WindowMenuBar());
        actions.registerContainer(toolBar);
        this.add(toolBar, BorderLayout.PAGE_START);
        //this.setJMenuBar(menuBar);
        actions.addAction(createAction(NEW_COMPOSER, "", "new_composer.png", "newComposer"));
        actions.addAction(createAction(CONFIGURATION, "", "configuration.png", "configuration"));
        actions.addAction(createAction(SAVE, "", "save.png", "save"));
        actions.addAction(createAction(EXPORT_COMPOSER, "", "export_composer.png", "exportComposer"));
        actions.addAction(createAction(ADD_MAP, "", "add_map.png", "addMap"));
        actions.addAction(createAction(ADD_TEXT, "", "add_text.png", "addText"));
        actions.addAction(createAction(ADD_LEGEND, "", "add_legend.png", "addLegend"));
        actions.addAction(createAction(ADD_ORIENTATION, "", "compass.png", "addOrientation"));
        actions.addAction(createAction(ADD_SCALE, "", "add_scale.png", "addScale"));
        actions.addAction(createAction(DRAW_CIRCLE, "", "draw_circle.png", "drawCircle"));
        actions.addAction(createAction(DRAW_POLYGON, "", "draw_polygon.png", "drawPolygon"));
        actions.addAction(createAction(MOVE_BACK, "", "move_back.png", "moveBack"));
        actions.addAction(createAction(MOVE_DOWN, "", "move_down.png", "moveDown"));
        actions.addAction(createAction(MOVE_ON, "", "move_on.png", "moveOn"));
        actions.addAction(createAction(MOVE_FRONT, "", "move_front.png", "moveFront"));
        actions.addAction(createAction(ALIGN_TO_LEFT, "", "align_to_left.png", "alignToLeft"));
        actions.addAction(createAction(ALIGN_TO_CENTER, "", "align_to_center.png", "alignToCenter"));
        actions.addAction(createAction(ALIGN_TO_RIGHT, "", "align_to_right.png", "alignToRight"));
        actions.addAction(createAction(ALIGN_TO_BOTTOM, "", "align_to_bottom.png", "alignToBottom"));
        actions.addAction(createAction(ALIGN_TO_MIDDLE, "", "align_to_middle.png", "alignToMiddle"));
        actions.addAction(createAction(ALIGN_TO_TOP, "", "align_to_top.png", "alignToTop"));
        actions.addAction(createAction(PROPERTIE, "", "properties.png", "properties"));
        actions.addAction(createAction(DELETE, "", "delete.png", "delete"));
        
        JPanel pan = new JPanel();
        pan.setLayout(new BorderLayout());
        JSplitPane pane1 = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, ConfigurationShutter.getInstance(), CompositionArea.getInstance());
        pane1.setOneTouchExpandable(true);
        this.add(pane1, BorderLayout.CENTER);
        
        //Instantiation of the UIController
        UIController.getInstance();
    }
    
    private DefaultAction createAction(String actionID, String actionLabel, String actionIconName, String ActionFunctionName){
        return new DefaultAction(
                            actionID,
                            actionLabel,
                            new ImageIcon(MainWindow.class.getResource(actionIconName)),
                            EventHandler.create(ActionListener.class, this, ActionFunctionName)
                        );
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
    
    public void newComposer(){
        UIController.getInstance().removeAllGE();
        UIController.getInstance().addGE(Document.class);
    }
    
    public void configuration(){
        
    }
    
    public void save(){
        UIController.getInstance().save();
    }
    
    public void exportComposer(){
        
    }
    
    public void addMap(){
        UIController.getInstance().addGE(MapImage.class);
    }
    
    public void addText(){
        UIController.getInstance().addGE(TextElement.class);
    }
    
    public void addLegend(){
        //Unsupported yet
    }
    
    public void addOrientation(){
        UIController.getInstance().addGE(Orientation.class);
    }
    
    public void addScale(){
        UIController.getInstance().addGE(Scale.class);
    }
    
    public void addImage(){
        UIController.getInstance().addGE(Image.class);
    }
    public void drawCircle(){
        
    }
    public void drawPolygon(){
        
    }
    public void moveBack(){
        UIController.getInstance().zindexChange(ZIndex.TO_BACK);
    }
    public void moveDown(){
        UIController.getInstance().zindexChange(ZIndex.BACK);
    }
    public void moveOn(){
        UIController.getInstance().zindexChange(ZIndex.FRONT);
    }
    public void moveFront(){
        UIController.getInstance().zindexChange(ZIndex.TO_FRONT);
    }
    public void alignToLeft(){
        UIController.getInstance().setAlign(Align.LEFT);
    }
    public void alignToCenter(){
        UIController.getInstance().setAlign(Align.CENTER);
    }
    public void alignToRight(){
        UIController.getInstance().setAlign(Align.RIGHT);
    }
    public void alignToBottom(){
        UIController.getInstance().setAlign(Align.BOTTOM);
    }
    public void alignToMiddle(){
        UIController.getInstance().setAlign(Align.MIDDLE);
    }
    public void alignToTop(){
        UIController.getInstance().setAlign(Align.TOP);
    }
    public void properties(){
        
    }
    public void delete(){
        UIController.getInstance().remove();
    }

    @Override
    public List<Action> createActions(org.orbisgis.viewapi.main.frames.ext.MainWindow target) {
        List<Action> actions = new ArrayList<Action>();
        actions.add(new DefaultAction(MENU_MAPCOMPOSER,"Map Composer",
                new ImageIcon(),
                EventHandler.create(ActionListener.class,this,"showMapComposer")).setParent(MENU_TOOLS));
        return actions;
    }
    public void showMapComposer(){this.setVisible(true);}

    @Override
    public void disposeActions(org.orbisgis.viewapi.main.frames.ext.MainWindow target, List<Action> actions) {
        this.dispose();
    }
}
