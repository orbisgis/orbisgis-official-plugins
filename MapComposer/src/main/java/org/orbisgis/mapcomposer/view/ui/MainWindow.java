package org.orbisgis.mapcomposer.view.ui;

import org.orbisgis.mapcomposer.controller.UIController;
import org.orbisgis.mapcomposer.controller.UIController.Align;
import static org.orbisgis.mapcomposer.controller.UIController.ZIndex;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.Scale;
import org.orbisgis.mapcomposer.model.graphicalelement.element.illustration.Image;
import org.orbisgis.mapcomposer.model.graphicalelement.element.text.TextElement;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement.Property;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import javax.swing.BoxLayout;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import javax.swing.JSpinner;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.SwingConstants;
import javax.swing.event.ChangeListener;
import org.orbisgis.view.components.actions.ActionCommands;
import org.orbisgis.viewapi.components.actions.DefaultAction;
import org.orbisgis.viewapi.main.frames.ext.MainFrameAction;

/**
 * Main window of the map composer. during the developement, the map composer will be a separeted window.
 * Once finished, it probably will become a part of OrbisGIS using the dockable interface.
 */
public class MainWindow extends JFrame implements MainFrameAction{
    
    //String used to define the toolbars actions
    public static final String MENU_MAPCOMPOSER = "MapComposer";
    public static final String NEW_COMPOSER = "NEW_COMPOSER";
    public static final String CONFIGURATION = "CONFIGURATION";
    public static final String SAVE = "SAVE";
    public static final String LOAD = "LOAD";
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
    public static final String PROPERTIES = "PROPERTIES";
    public static final String DELETE = "DELETE";
    
    /** ActionCommands for the buttons. */
    private final ActionCommands actions = new ActionCommands();
    /** JToolBar for the buttons. It's registered in the action ActionCommands. */
    private final JToolBar toolBar = new JToolBar();
    
    /** JToolBar for the spinners.
     * The spinners are used to change the position, the size and the rotation of selected GraphicalElements. */
    private final JToolBar toolBarSpin = new JToolBar();
    
    /** Spinner for the x position. */
    private JSpinner spinX=null;
    /** Spinner for the y position. */
    private JSpinner spinY=null;
    /** Spinner for the width. */
    private JSpinner spinW=null;
    /** Spinner for the height. */
    private JSpinner spinH=null;
    /** Spinner for the rotation. */
    private JSpinner spinR=null;
    
    private UIController uic;
    private CompositionArea compArea;
    
    /** Public main constructor. */
    public MainWindow(UIController uic){
        super("Map composer");
        this.uic=uic;
        this.compArea = new CompositionArea();
        //Sets the default size to the window
        this.setSize(1024, 768);
        
        //Creates the panel containing the two tool bars.
        JPanel top = new JPanel();
        top.setLayout(new BoxLayout(top, BoxLayout.Y_AXIS));
        top.add(toolBar);
        top.add(toolBarSpin);
        this.add(top, BorderLayout.PAGE_START);
        
        //Sets the button tool bar.
        toolBar.setFloatable(false);
        toolBarSpin.setFloatable(false);
        actions.setAccelerators(rootPane);
        actions.registerContainer(toolBar);
        
        actions.addAction(createAction(NEW_COMPOSER, "", "Create a new document", "new_composer.png", this, "newComposer", null));
        actions.addAction(createAction(CONFIGURATION, "", "Show the document configuration dialog", "configuration.png", uic, "showDocProperties", null));
        actions.addAction(createAction(SAVE, "", "Save the document", "save.png", uic, "save", null));
        actions.addAction(createAction(LOAD, "", "Load the document", "properties.png", uic, "load", null));
        actions.addAction(createAction(EXPORT_COMPOSER, "", "Export the document", "export_composer.png", this, "exportComposer", null));
        addSeparatortTo(toolBar);
        actions.addAction(createAction(ADD_MAP, "", "Add a map element", "add_map.png", this, "addMap", null));
        actions.addAction(createAction(ADD_TEXT,  "", "Add a text element",  "add_text.png", this, "addText", null));
        actions.addAction(createAction(ADD_LEGEND, "", "Add a legend element", "add_legend.png", this, "addLegend", null));
        actions.addAction(createAction(ADD_ORIENTATION, "", "Add an orientation element", "compass.png", this, "addOrientation", null));
        actions.addAction(createAction(ADD_SCALE, "", "Add a scale element", "add_scale.png", this, "addScale", null));
        actions.addAction(createAction(ADD_PICTURE, "", "Add a picture element", "add_picture.png", this, "addPicture", null));
        addSeparatortTo(toolBar);
        actions.addAction(createAction(DRAW_CIRCLE, "", "Add a circle element", "draw_circle.png", this, "drawCircle", null));
        actions.addAction(createAction(DRAW_POLYGON, "", "Add a polygon element", "draw_polygon.png", this, "drawPolygon", null));
        addSeparatortTo(toolBar);
        actions.addAction(createAction(MOVE_BACK, "", "Move to the back", "move_back.png", this, "moveBack", null));
        actions.addAction(createAction(MOVE_DOWN, "", "Move down", "move_down.png", this, "moveDown", null));
        actions.addAction(createAction(MOVE_ON, "", "Move on", "move_on.png", this, "moveOn", null));
        actions.addAction(createAction(MOVE_FRONT, "", "Move to the front", "move_front.png", this, "moveFront", null));
        addSeparatortTo(toolBar);
        actions.addAction(createAction(ALIGN_TO_LEFT, "", "Align to the left", "align_to_left.png", this, "alignToLeft", null));
        actions.addAction(createAction(ALIGN_TO_CENTER, "", "Align to the center", "align_to_center.png", this, "alignToCenter", null));
        actions.addAction(createAction(ALIGN_TO_RIGHT, "", "Align to the right", "align_to_right.png", this, "alignToRight", null));
        actions.addAction(createAction(ALIGN_TO_BOTTOM, "", "Align to the bottom", "align_to_bottom.png", this, "alignToBottom", null));
        actions.addAction(createAction(ALIGN_TO_MIDDLE, "", "Align to the middle", "align_to_middle.png", this, "alignToMiddle", null));
        actions.addAction(createAction(ALIGN_TO_TOP, "", "Align to the top", "align_to_top.png", this, "alignToTop", null));
        addSeparatortTo(toolBar);
        actions.addAction(createAction(PROPERTIES, "", "Show selected elements properties", "properties.png", uic, "showProperties", null));
        actions.addAction(createAction(DELETE, "", "Delete selected elements", "delete.png", uic, "remove", null));
        toolBar.add(new JSeparator(SwingConstants.VERTICAL));
        
        //Sets the spinners tool bar.
        spinX = createSpinner(" X : ", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        spinY = createSpinner(" Y : ", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        spinW = createSpinner(" W : ", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        spinH = createSpinner(" H : ", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        toolBarSpin.add(new JLabel(new ImageIcon(MainWindow.class.getResource("rotation.png"))));
        spinR = createSpinner("", 0, -360, 360);
        toolBarSpin.add(new JSeparator(SwingConstants.VERTICAL));
        
        //Adds the composition area.
        this.add(compArea, BorderLayout.CENTER);
    }
    
    /**
     * Adds a well sized separator to a tool bar.
     * @param toolBar Tool bar needing a separator.
     */
    private void addSeparatortTo(JToolBar toolBar){
        JSeparator sep = new JSeparator(SwingConstants.VERTICAL);
        sep.setMaximumSize(new Dimension(8, 32));
        toolBar.add(sep);
    }
    
    public CompositionArea getCompositionArea(){return compArea;}
    
    /**
     * Creates and adds to the toolBarSpin a spinner and its label.
     * The spinner and its label are setted with the given function argument.
     * The function return the spinner reference to permite to listen to their modification.
     * @param label Name of the spinner.
     * @param value Actual value of the spinner.
     * @param minValue Minimum value of the spinner.
     * @param maxValue Maximum value of the spinner.
     * @return The reference to the created spinner.
     */
    private JSpinner createSpinner(String label, int value, int minValue, int maxValue){
        toolBarSpin.add(new JLabel(label));
        JSpinner spin = new JSpinner(new SpinnerNumberModel(value, minValue, maxValue, 1));
        spin.addChangeListener(EventHandler.create(ChangeListener.class, this, "spinChange", "source"));
        spin.setMaximumSize(new Dimension(64, 32));
        spin.setMinimumSize(new Dimension(32, 32));
        spin.setPreferredSize(new Dimension(64, 32));
        spin.setEnabled(false);
        toolBarSpin.add(spin);
        toolBarSpin.addSeparator();
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
    
    private DefaultAction createAction(String actionID, String actionLabel, String actionToolTip, String actionIconName, Object target, String ActionFunctionName, KeyStroke keyStroke){
        return new DefaultAction(
                            actionID,
                            actionLabel,
                            actionToolTip,
                            new ImageIcon(MainWindow.class.getResource(actionIconName)),
                            EventHandler.create(ActionListener.class, target, ActionFunctionName),
                            keyStroke
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
