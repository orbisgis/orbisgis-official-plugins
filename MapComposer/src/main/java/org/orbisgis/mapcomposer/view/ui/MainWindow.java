/*
* MapComposer is an OrbisGIS plugin dedicated to the creation of cartographic
* documents based on OrbisGIS results.
*
* This plugin is developed at French IRSTV institute as part of the MApUCE project,
* funded by the French Agence Nationale de la Recherche (ANR) under contract ANR-13-VBDU-0004.
*
* The MapComposer plugin is distributed under GPL 3 license. It is produced by the "Atelier SIG"
* team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
*
* Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
*
* This file is part of the MapComposer plugin.
*
* The MapComposer plugin is free software: you can redistribute it and/or modify it under the
* terms of the GNU General Public License as published by the Free Software
* Foundation, either version 3 of the License, or (at your option) any later
* version.
*
* The MapComposer plugin is distributed in the hope that it will be useful, but WITHOUT ANY
* WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
* A PARTICULAR PURPOSE. See the GNU General Public License for more details <http://www.gnu.org/licenses/>.
*/

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

import java.awt.*;
import java.awt.event.*;
import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeListener;

import org.orbisgis.mapcomposer.view.utils.CompositionAreaOverlay;
import org.orbisgis.view.components.actions.ActionCommands;
import org.orbisgis.viewapi.components.actions.DefaultAction;
import org.orbisgis.viewapi.main.frames.ext.MainFrameAction;

/**
 * Main window of the map composer. It contain the saverals tool bar and the CompositionArea.
 * It does the link between the user interactions and the UIController
 *
 * @author Sylvain PALOMINOS
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
    public static final String UNDO = "UNDO";
    public static final String REDO = "REDO";

    /** ActionCommands for the buttons. */
    private final ActionCommands actions = new ActionCommands();
    /** JToolBar for the buttons. It's registered in the action ActionCommands. */
    private final JToolBar IconToolBar = new JToolBar();

    /** JToolBar for the spinners.
     * The spinners are used to change the position, the size and the rotation of selected GraphicalElements. */
    private final JToolBar spinnerToolBar = new JToolBar();

    /** Spinner for the x position. */
    private JSpinner spinnerX =null;
    /** Spinner for the y position. */
    private JSpinner spinnerY =null;
    /** Spinner for the width. */
    private JSpinner spinnerW =null;
    /** Spinner for the height. */
    private JSpinner spinnerH =null;
    /** Spinner for the rotation. */
    private JSpinner spinnerR =null;

    private UIController uiController;
    private CompositionArea compositionArea;

    /** Public main constructor. */
    public MainWindow(UIController uiController){
        super("Map composer");
        this.uiController = uiController;
        this.compositionArea = new CompositionArea(uiController);
        //Sets the default size to the window
        this.setSize(1024, 768);
        this.setIconImage(new ImageIcon(MainWindow.class.getResource("map_composer.png")).getImage());

        //Creates the panel containing the two tool bars.
        JPanel toolBarPanel = new JPanel();
        toolBarPanel.setLayout(new BoxLayout(toolBarPanel, BoxLayout.Y_AXIS));
        toolBarPanel.add(IconToolBar);
        toolBarPanel.add(spinnerToolBar);
        this.add(toolBarPanel, BorderLayout.PAGE_START);

        //Sets the button tool bar.
        IconToolBar.setFloatable(false);
        spinnerToolBar.setFloatable(false);
        actions.registerContainer(IconToolBar);

        actions.addAction(createAction(NEW_COMPOSER, "", "Create a new document (Ctrl + N)", "new_composer.png", this, "newComposer", KeyStroke.getKeyStroke("control N")));
        actions.addAction(createAction(CONFIGURATION, "", "Show the document configuration dialog (Ctrl + D)", "configuration.png", uiController, "showDocProperties", KeyStroke.getKeyStroke("control D")));
        actions.addAction(createAction(SAVE, "", "Save the document (Ctrl + S)", "save.png", uiController.getIOController(), "saveDocument", KeyStroke.getKeyStroke("control S")));
        actions.addAction(createAction(LOAD, "", "Load the document (Ctrl + L)", "load.png", uiController.getIOController(), "loadDocument", KeyStroke.getKeyStroke("control L")));
        actions.addAction(createAction(EXPORT_COMPOSER, "", "Export the document (Ctrl + E)", "export_composer.png", uiController.getIOController(), "export", KeyStroke.getKeyStroke("control E")));
        addSeparatortTo(IconToolBar);
        actions.addAction(createAction(ADD_MAP, "", "Add a map element (Alt + M)", "add_map.png", this, "addMap", KeyStroke.getKeyStroke("alt M")));
        actions.addAction(createAction(ADD_TEXT,  "", "Add a text element (Alt + T)",  "add_text.png", this, "addText", KeyStroke.getKeyStroke("alt T")));
        actions.addAction(createAction(ADD_LEGEND, "", "Add a legend element (Alt + L)", "add_legend.png", this, "addLegend", KeyStroke.getKeyStroke("alt L")));
        actions.addAction(createAction(ADD_ORIENTATION, "", "Add an orientation element (Alt + O)", "compass.png", this, "addOrientation", KeyStroke.getKeyStroke("alt O")));
        actions.addAction(createAction(ADD_SCALE, "", "Add a scale element (Alt + S)", "add_scale.png", this, "addScale", KeyStroke.getKeyStroke("alt S")));
        actions.addAction(createAction(ADD_PICTURE, "", "Add a picture element (Alt + I)", "add_picture.png", this, "addPicture", KeyStroke.getKeyStroke("alt I")));
        addSeparatortTo(IconToolBar);
        actions.addAction(createAction(DRAW_CIRCLE, "", "Add a circle element (Alt + C)", "draw_circle.png", this, "drawCircle", KeyStroke.getKeyStroke("alt C")));
        actions.addAction(createAction(DRAW_POLYGON, "", "Add a polygon element (Alt + Y)", "draw_polygon.png", this, "drawPolygon", KeyStroke.getKeyStroke("alt Y")));
        addSeparatortTo(IconToolBar);
        actions.addAction(createAction(MOVE_BACK, "", "Move to the back (Alt + PageDown)", "move_back.png", this, "moveBack", KeyStroke.getKeyStroke("alt PAGE_DOWN")));
        actions.addAction(createAction(MOVE_DOWN, "", "Move down (Alt + Down)", "move_down.png", this, "moveDown", KeyStroke.getKeyStroke("alt DOWN")));
        actions.addAction(createAction(MOVE_ON, "", "Move on (Alt + Up)", "move_on.png", this, "moveOn", KeyStroke.getKeyStroke("alt UP")));
        actions.addAction(createAction(MOVE_FRONT, "", "Move to the front (Alt + PageUp)", "move_front.png", this, "moveFront", KeyStroke.getKeyStroke("alt PAGE_UP")));
        addSeparatortTo(IconToolBar);
        actions.addAction(createAction(ALIGN_TO_LEFT, "", "Align to the left (Alt + numpad 4)", "align_to_left.png", this, "alignToLeft", KeyStroke.getKeyStroke("alt NUMPAD4")));
        actions.addAction(createAction(ALIGN_TO_CENTER, "", "Align to the center", "align_to_center.png", this, "alignToCenter", null));
        actions.addAction(createAction(ALIGN_TO_RIGHT, "", "Align to the right (Alt + numpad 6)", "align_to_right.png", this, "alignToRight", KeyStroke.getKeyStroke("alt NUMPAD6")));
        actions.addAction(createAction(ALIGN_TO_BOTTOM, "", "Align to the bottom (Alt + numpad 2)", "align_to_bottom.png", this, "alignToBottom", KeyStroke.getKeyStroke("alt NUMPAD2")));
        actions.addAction(createAction(ALIGN_TO_MIDDLE, "", "Align to the middle", "align_to_middle.png", this, "alignToMiddle", null));
        actions.addAction(createAction(ALIGN_TO_TOP, "", "Align to the top (Alt + numpad 8)", "align_to_top.png", this, "alignToTop", KeyStroke.getKeyStroke("alt NUMPAD8")));
        addSeparatortTo(IconToolBar);

        actions.addAction(createAction(PROPERTIES, "", "Show selected elements properties (Ctrl + P)", "properties.png", uiController, "showSelectedGEProperties", KeyStroke.getKeyStroke("control P")));
        actions.addAction(createAction(DELETE, "", "Delete selected elements (DELETE)", "delete.png", uiController, "removeSelectedGE", KeyStroke.getKeyStroke("DELETE")));
        actions.addAction(createAction(DELETE, "", "Redraw selected elements (Ctrl + R)", "refresh.png", uiController, "refreshSelectedGE", KeyStroke.getKeyStroke("control R")));
        actions.addAction(createAction(UNDO, "", "Undo the last action", "rotation.png", uiController, "undo", null));
        actions.addAction(createAction(REDO, "", "Redo the last action", "rotation.png", uiController, "redo", null));

        IconToolBar.add(new JSeparator(SwingConstants.VERTICAL));

        //Sets the spinners tool bar.
        spinnerX = createSpinner("X", " X : ", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        spinnerY = createSpinner("Y", " Y : ", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        spinnerW = createSpinner("WIDTH", " W : ", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        spinnerH = createSpinner("HEIGHT", " H : ", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        spinnerToolBar.add(new JLabel(new ImageIcon(MainWindow.class.getResource("rotation.png"))));
        spinnerR = createSpinner("ROTATION", "", 0, -360, 360);
        spinnerToolBar.add(new JSeparator(SwingConstants.VERTICAL));

        //Adds the composition area.
        this.add(compositionArea, BorderLayout.CENTER);

        actions.setAccelerators(rootPane, JComponent.WHEN_IN_FOCUSED_WINDOW);
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

    /**
     * Returns the CompositionArea
     * @return The CompositionArea
     */
    public CompositionArea getCompositionArea(){return compositionArea;}

    /**
     * Creates and adds to the spinnerToolBar a spinner and its label.
     * The spinner and its label are setted with the given function argument.
     * The function return the spinner reference to permite to listen to their modification.
     * @param name Name of the spinner.
     * @param label Text display in the tool bar.
     * @param value Actual value of the spinner.
     * @param minValue Minimum value of the spinner.
     * @param maxValue Maximum value of the spinner.
     * @return The reference to the created spinner.
     */
    private JSpinner createSpinner(String name, String label, int value, int minValue, int maxValue){
        spinnerToolBar.add(new JLabel(label));
        JSpinner spin = new JSpinner(new SpinnerNumberModel(value, minValue, maxValue, 1));
        spin.setName(name);
        spin.addChangeListener(EventHandler.create(ChangeListener.class, this, "spinChange", "source"));
        spin.addMouseWheelListener(EventHandler.create(MouseWheelListener.class, this, "mouseWheel", ""));
        spin.setMaximumSize(new Dimension(64, 32));
        spin.setMinimumSize(new Dimension(32, 32));
        spin.setPreferredSize(new Dimension(64, 32));
        spin.setEnabled(false);
        spinnerToolBar.add(spin);
        spinnerToolBar.addSeparator();
        return spin;
    }

    /**
     * Apply a change in a JSpinner of the tool bar into the selected GraphicalElements via the UIController
     * @param o JSpinner which had changed.
     */
    public void spinChange(Object o){
        if(o instanceof JSpinner){
            JSpinner spinner = (JSpinner) o;
            if(spinner.isEnabled() && spinner.getName() != null) {
                switch (spinner.getName()) {
                    case "X":
                        uiController.changeProperty(GraphicalElement.Property.X, (int) spinnerX.getValue());
                        break;
                    case "Y":
                        uiController.changeProperty(GraphicalElement.Property.Y, (int) spinnerY.getValue());
                        break;
                    case "WIDTH":
                        uiController.changeProperty(GraphicalElement.Property.WIDTH, (int) spinnerW.getValue());
                        break;
                    case "HEIGHT":
                        uiController.changeProperty(GraphicalElement.Property.HEIGHT, (int) spinnerH.getValue());
                        break;
                    case "ROTATION":
                        uiController.changeProperty(GraphicalElement.Property.ROTATION, (int) spinnerR.getValue());
                        break;
                }
            }
        }
    }

    /**
     * Action done when the mouse wheel is used in a JComboBox of the toolbar.
     * @param mwe MouseWheelEvent
     */
    public void mouseWheel(MouseWheelEvent mwe){
        if(mwe.getSource() instanceof JSpinner){
            JSpinner spinner = (JSpinner) mwe.getSource();
            if(spinner.isEnabled() && spinner.getName() != null) {
                switch (spinner.getName()) {
                    case "X":
                        uiController.changeProperty(GraphicalElement.Property.X, (int) spinnerX.getValue() - mwe.getWheelRotation());
                        break;
                    case "Y":
                        uiController.changeProperty(GraphicalElement.Property.Y, (int) spinnerY.getValue() - mwe.getWheelRotation());
                        break;
                    case "WIDTH":
                        uiController.changeProperty(GraphicalElement.Property.WIDTH, (int) spinnerW.getValue() - mwe.getWheelRotation());
                        break;
                    case "HEIGHT":
                        uiController.changeProperty(GraphicalElement.Property.HEIGHT, (int) spinnerH.getValue() - mwe.getWheelRotation());
                        break;
                    case "ROTATION":
                        uiController.changeProperty(GraphicalElement.Property.ROTATION, (int) spinnerR.getValue() - mwe.getWheelRotation());
                        break;
                }
            }
        }
    }

    /**
     * Create a DefaultAction with the given value.
     * @see org.orbisgis.viewapi.components.actions.DefaultAction
     * @param actionID Action identifier
     * @param actionLabel Short label
     * @param actionToolTip Tool tip text
     * @param actionIconName Name of the icon file
     * @param target Target of the action listener
     * @param ActionFunctionName Function of the target
     * @param keyStroke Shortcut for the action
     * @return Configured DefaultAction
     */
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

    /**
     * Configure a JSpinner of the tool bar.
     * It enable or not the spinner and set its value.
     * @param b Enable the spinner if true, disable otherwise
     * @param value New value of the spinner
     * @param prop Property corresponding to the spinner to set
     */
    public void setSpinner(boolean b, int value, Property prop){
        int val=b ? 0 : value;
        JSpinner spinner = null;
        switch(prop){
            case X:
                spinner = spinnerX;
                break;
            case Y:
                spinner = spinnerY;
                break;
            case HEIGHT:
                spinner = spinnerH;
                break;
            case WIDTH:
                spinner = spinnerW;
                break;
            case ROTATION:
                spinner = spinnerR;
                break;
        }
        if(spinner!=null){
            spinner.setModel(new SpinnerNumberModel(value, ((SpinnerNumberModel) spinner.getModel()).getMinimum(), ((SpinnerNumberModel) spinner.getModel()).getMaximum(), 1));
            spinner.setEnabled(!b);
        }
    }

    /**
     * Create a new document.
     */
    public void newComposer(){
        uiController.removeAllGE();
        uiController.instantiateNewGE(Document.class);
    }

    /**
     * Add a MapImage GraphicalElement to the document.
     */
    public void addMap(){
        compositionArea.getOverlay().setRatio(-1);
        uiController.instantiateNewGE(MapImage.class);
        compositionArea.setOverlayMode(CompositionAreaOverlay.Mode.NONE);
    }

    /**
     * Add a TextElement GraphicalElement to the document.
     */
    public void addText(){
        compositionArea.getOverlay().setRatio(-1);
        uiController.instantiateNewGE(TextElement.class);
        compositionArea.setOverlayMode(CompositionAreaOverlay.Mode.NONE);
    }

    /**
     * Add a Legend GraphicalElement to the document.
     */
    public void addLegend(){
        //Unsupported yet
    }

    /**
     * Add a Orientation GraphicalElement to the document.
     */
    public void addOrientation(){
        compositionArea.getOverlay().setRatio(-1);
        uiController.instantiateNewGE(Orientation.class);
        compositionArea.setOverlayMode(CompositionAreaOverlay.Mode.NONE);
    }

    /**
     * Add a Scale GraphicalElement to the document.
     */
    public void addScale(){
        compositionArea.getOverlay().setRatio(-1);
        uiController.instantiateNewGE(Scale.class);
        compositionArea.setOverlayMode(CompositionAreaOverlay.Mode.NONE);
    }

    /**
     * Add a Image GraphicalElement to the document.
     */
    public void addPicture() {
        compositionArea.getOverlay().setRatio(-1);
        uiController.instantiateNewGE(Image.class);
        compositionArea.setOverlayMode(CompositionAreaOverlay.Mode.NONE);
    }
    public void drawCircle(){
        //Unsupported yet
    }
    public void drawPolygon(){
        //Unsupported yet
    }
    public void moveBack(){
        uiController.changeZIndex(ZIndex.TO_BACK);
    }
    public void moveDown(){
        uiController.changeZIndex(ZIndex.BACK);
    }
    public void moveOn(){
        uiController.changeZIndex(ZIndex.FRONT);
    }
    public void moveFront(){
        uiController.changeZIndex(ZIndex.TO_FRONT);
    }
    public void alignToLeft(){
        uiController.getCompositionAreaController().setAlign(Align.LEFT);
    }
    public void alignToCenter(){
        uiController.getCompositionAreaController().setAlign(Align.CENTER);
    }
    public void alignToRight(){
        uiController.getCompositionAreaController().setAlign(Align.RIGHT);
    }
    public void alignToBottom(){
        uiController.getCompositionAreaController().setAlign(Align.BOTTOM);
    }
    public void alignToMiddle(){
        uiController.getCompositionAreaController().setAlign(Align.MIDDLE);
    }
    public void alignToTop(){
        uiController.getCompositionAreaController().setAlign(Align.TOP);
    }

    @Override
    public List<Action> createActions(org.orbisgis.viewapi.main.frames.ext.MainWindow target) {
        List<Action> actions = new ArrayList<>();
        actions.add(new DefaultAction(MENU_MAPCOMPOSER, "Map Composer",
                new ImageIcon(MainWindow.class.getResource("map_composer.png")),
                EventHandler.create(ActionListener.class, this, "showMapComposer")).setParent(MENU_TOOLS));
        return actions;
    }
    public void showMapComposer(){this.setVisible(true);}

    @Override
    public void disposeActions(org.orbisgis.viewapi.main.frames.ext.MainWindow target, List<Action> actions) {
        this.dispose();
    }
}
