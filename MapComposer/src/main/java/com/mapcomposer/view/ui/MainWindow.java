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
import com.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import com.mapcomposer.model.graphicalelement.interfaces.GraphicalElement.Property;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import org.orbisgis.view.components.actions.ActionCommands;
import org.orbisgis.view.icons.OrbisGISIcon;
import org.orbisgis.viewapi.components.actions.DefaultAction;
import org.orbisgis.viewapi.main.frames.ext.MainFrameAction;
import static org.orbisgis.viewapi.main.frames.ext.MainFrameAction.MENU_TOOLS;

/**
 * Main window of the map composer. during the developement, the map composer will be a separeted window.
 * Once finished, it probably will become a part of OrbisGIS using the dockable interface.
 */
public class MainWindow extends JFrame implements MainFrameAction{
    
    public static final String MENU_MAPCOMPOSER = "MapComposer";
    public static final String NEW_COMPOSER = "NEW_COMPOSER";
    public static final String CONFIGURATION = "CONFIGURATION";
    public static final String SAVE = "SAVE";
    public static final String EXPORT_COMPOSER = "EXPORT_COMPOSER";
    public static final String ADD_MAP = "ADD_MAP";
    public static final String ADD_TEXT = "ADD_TEXT";
    public static final String ADD_LEGEND = "ADD_LEGEND";
    public static final String ADD_ORIENTATION = "ADD_ORIENTATION";
    public static final String ADD_SCALE = "ADD_ORIENTATION";
    public static final String ADD_PICTURE = "ADD_PICTURE";
    public static final String DRAW_CIRCLE = "DRAW_CIRCLE";
    public static final String DRAW_POLYGON = "DRAW_POLYGON";
    public static final String MOVE_BACK = "MOVE_BACK";
    public static final String MOVE_DOWN = "MOVE_DOWN";
    public static final String MOVE_ON = "MOVE_ON";
    public static final String MOVE_FRONT = "MOVE_FRONT";
    public static final String ALIGN_TO_LEFT = "ALIGN_TO_LEFT";
    public static final String ALIGN_TO_CENTER = "ALIGN_TO_CENTER";
    public static final String ALIGN_TO_RIGHT = "ALIGN_TO_RIGHT";
    public static final String ALIGN_TO_BOTTOM = "ALIGN_TO_BOTTOM";
    public static final String ALIGN_TO_MIDDLE = "ALIGN_TO_MIDDLE";
    public static final String ALIGN_TO_TOP = "ALIGN_TO_TOP";
    public static final String PROPERTIE = "PROPERTIE";
    public static final String DELETE = "DELETE";
    
    private final ActionCommands actions = new ActionCommands();
    private final JToolBar toolBar = new JToolBar();
    
    private JSpinner spinX=null;
    private JSpinner spinY=null;
    private JSpinner spinW=null;
    private JSpinner spinH=null;
    private JSpinner spinR=null;
    
    private UIController uic;
    private CompositionArea compArea;
    
    /**Private constructor.*/
    public MainWindow(UIController uic){
        super("Map composer");
        this.uic=uic;
        this.compArea = new CompositionArea();
        //Sets the default size to the window
        this.setSize(1024, 768);
        this.setIconImage(OrbisGISIcon.getIconImage("mini_orbisgis"));
        this.add(toolBar, BorderLayout.PAGE_START);
        actions.setAccelerators(rootPane);
        actions.registerContainer(toolBar);
        actions.addAction(createAction(NEW_COMPOSER, "", "new_composer.png", this, "newComposer"));
        actions.addAction(createAction(CONFIGURATION, "", "configuration.png", uic, "showDocProperties"));
        actions.addAction(createAction(SAVE, "", "save.png", uic, "save"));
        actions.addAction(createAction(EXPORT_COMPOSER, "", "export_composer.png", this, "exportComposer"));
        toolBar.addSeparator();
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        actions.addAction(createAction(ADD_MAP, "", "add_map.png", this, "addMap"));
        actions.addAction(createAction(ADD_TEXT, "", "add_text.png", this, "addText"));
        actions.addAction(createAction(ADD_LEGEND, "", "add_legend.png", this, "addLegend"));
        actions.addAction(createAction(ADD_ORIENTATION, "", "compass.png", this, "addOrientation"));
        actions.addAction(createAction(ADD_SCALE, "", "add_scale.png", this, "addScale"));
        actions.addAction(createAction(ADD_PICTURE, "", "add_picture.png", this, "addPicture"));
        toolBar.addSeparator();
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        actions.addAction(createAction(DRAW_CIRCLE, "", "draw_circle.png", this, "drawCircle"));
        actions.addAction(createAction(DRAW_POLYGON, "", "draw_polygon.png", this, "drawPolygon"));
        toolBar.addSeparator();
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        actions.addAction(createAction(MOVE_BACK, "", "move_back.png", this, "moveBack"));
        actions.addAction(createAction(MOVE_DOWN, "", "move_down.png", this, "moveDown"));
        actions.addAction(createAction(MOVE_ON, "", "move_on.png", this, "moveOn"));
        actions.addAction(createAction(MOVE_FRONT, "", "move_front.png", this, "moveFront"));
        toolBar.addSeparator();
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        actions.addAction(createAction(ALIGN_TO_LEFT, "", "align_to_left.png", this, "alignToLeft"));
        actions.addAction(createAction(ALIGN_TO_CENTER, "", "align_to_center.png", this, "alignToCenter"));
        actions.addAction(createAction(ALIGN_TO_RIGHT, "", "align_to_right.png", this, "alignToRight"));
        actions.addAction(createAction(ALIGN_TO_BOTTOM, "", "align_to_bottom.png", this, "alignToBottom"));
        actions.addAction(createAction(ALIGN_TO_MIDDLE, "", "align_to_middle.png", this, "alignToMiddle"));
        actions.addAction(createAction(ALIGN_TO_TOP, "", "align_to_top.png", this, "alignToTop"));
        toolBar.addSeparator();
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        spinX = createSpinner(" X : ", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        spinY = createSpinner(" Y : ", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        spinW = createSpinner(" W : ", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        spinH = createSpinner(" H : ", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        toolBar.add(new JLabel(new ImageIcon(MainWindow.class.getResource("rotation.png"))));
        spinR = createSpinner("", 0, -360, 360);
        toolBar.addSeparator();
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        actions.addAction(createAction(PROPERTIE, "", "properties.png", uic, "showProperties"));
        actions.addAction(createAction(DELETE, "", "delete.png", uic, "remove"));
        
        JPanel pan = new JPanel();
        pan.setLayout(new BorderLayout());
        this.add(compArea, BorderLayout.CENTER);
    }
    
    public CompositionArea getCompositionArea(){return compArea;}
    
    private JSpinner createSpinner(String label, int value, int minValue, int maxValue){
        toolBar.add(new JLabel(label));
        JSpinner spin = new JSpinner(new SpinnerNumberModel(value, minValue, maxValue, 1));
        spin.addChangeListener(EventHandler.create(ChangeListener.class, this, "spinChange", "source"));
        spin.setPreferredSize(new Dimension(32, 32));
        spin.setEnabled(false);
        toolBar.add(spin);
        return spin;
    }
    
    public void spinChange(Object o){
        if(o.equals(spinX))
            if(spinX.isEnabled()) uic.changeProperty(GraphicalElement.Property.X, (Integer)spinX.getModel().getValue());
        if(o.equals(spinY))
            if(spinY.isEnabled()) uic.changeProperty(GraphicalElement.Property.Y, (Integer)spinY.getModel().getValue());
        if(o.equals(spinW))
            if(spinW.isEnabled()) uic.changeProperty(GraphicalElement.Property.WIDTH, (Integer)spinW.getModel().getValue());
        if(o.equals(spinH))
            if(spinH.isEnabled()) uic.changeProperty(GraphicalElement.Property.HEIGHT, (Integer)spinH.getModel().getValue());
        if(o.equals(spinR))
            if(spinR.isEnabled()) uic.changeProperty(GraphicalElement.Property.ROTATION, (Integer)spinR.getModel().getValue());
    }
    
    private DefaultAction createAction(String actionID, String actionLabel, String actionIconName, Object target, String ActionFunctionName){
        return new DefaultAction(
                            actionID,
                            actionLabel,
                            new ImageIcon(MainWindow.class.getResource(actionIconName)),
                            EventHandler.create(ActionListener.class, target, ActionFunctionName)
                        );
    }
    
    public void setSpinner(boolean b, int i, Property prop){
        int val=b ? 0 : i;
        switch(prop){
            case X:
                spinX.setEnabled(!b);
                spinX.setValue(val);
                break;
            case Y:
                spinY.setEnabled(!b);
                spinY.setValue(val);
                break;
            case HEIGHT:
                spinH.setEnabled(!b);
                spinH.setValue(val);
                break;
            case WIDTH:
                spinW.setEnabled(!b);
                spinW.setValue(val);
                break;
            case ROTATION:  
                spinR.setEnabled(!b);
                spinR.setValue(val); 
                break;
        }
    }
    
    public void newComposer(){
        uic.removeAllGE();
        uic.addGE(Document.class);
        uic.showDocProperties();
    }
    
    //change to handler
    public void exportComposer(){
        uic.export();
    }
    
    public void addMap(){
        uic.addGE(MapImage.class);
    }
    
    public void addText(){
        uic.addGE(TextElement.class);
    }
    
    public void addLegend(){
        //Unsupported yet
    }
    
    public void addOrientation(){
        uic.addGE(Orientation.class);
    }
    
    public void addScale(){
        uic.addGE(Scale.class);
    }
    
    public void addPicture(){
        uic.addGE(Image.class);
    }
    public void drawCircle(){
        //Unsupported yet
    }
    public void drawPolygon(){
        //Unsupported yet
    }
    public void moveBack(){
        uic.zindexChange(ZIndex.TO_BACK);
    }
    public void moveDown(){
        uic.zindexChange(ZIndex.BACK);
    }
    public void moveOn(){
        uic.zindexChange(ZIndex.FRONT);
    }
    public void moveFront(){
        uic.zindexChange(ZIndex.TO_FRONT);
    }
    public void alignToLeft(){
        uic.setAlign(Align.LEFT);
    }
    public void alignToCenter(){
        uic.setAlign(Align.CENTER);
    }
    public void alignToRight(){
        uic.setAlign(Align.RIGHT);
    }
    public void alignToBottom(){
        uic.setAlign(Align.BOTTOM);
    }
    public void alignToMiddle(){
        uic.setAlign(Align.MIDDLE);
    }
    public void alignToTop(){
        uic.setAlign(Align.TOP);
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
