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

import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement.Property;
import org.orbisgis.mapcomposer.view.utils.MapComposerIcon;

import java.awt.*;
import java.awt.event.*;
import java.beans.EventHandler;
import java.util.ArrayList;
import java.util.List;
import javax.swing.*;
import javax.swing.event.ChangeListener;

import org.orbisgis.view.components.actions.ActionCommands;
import org.orbisgis.viewapi.components.actions.DefaultAction;
import org.orbisgis.viewapi.main.frames.ext.MainFrameAction;

/**
 * Main window of the map composer. It contains several tool bar and the CompositionArea.
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
    public static final String REFRESH = "REFRESH";
    public static final String UNDO = "UNDO";
    public static final String REDO = "REDO";

    /** ActionCommands for the buttons. */
    private final ActionCommands actions = new ActionCommands();
    /** JToolBar for the buttons. It's registered in the action ActionCommands. */
    private final JToolBar iconToolBar = new JToolBar();

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

    private MainController mainController;
    private CompositionArea compositionArea;

    /** Public main constructor. */
    public MainWindow(MainController mainController){
        super("Map composer");
        this.mainController = mainController;
        this.compositionArea = new CompositionArea(mainController);
        //Sets the default size to the window
        this.setSize(1024, 768);
        this.setIconImage(MapComposerIcon.getIcon("map_composer").getImage());

        //Creates the panel containing the two tool bars.
        JPanel toolBarPanel = new JPanel();
        toolBarPanel.setLayout(new BoxLayout(toolBarPanel, BoxLayout.Y_AXIS));
        toolBarPanel.add(iconToolBar);
        toolBarPanel.add(spinnerToolBar);
        this.add(toolBarPanel, BorderLayout.PAGE_START);

        //Sets the button tool bar.
        iconToolBar.setFloatable(false);
        spinnerToolBar.setFloatable(false);
        actions.registerContainer(iconToolBar);

        actions.addAction(createAction(NEW_COMPOSER, "", "Create a new document (Ctrl + N)", "new_composer", mainController.getUIController(), "createDocument", KeyStroke.getKeyStroke("control N")));
        actions.addAction(createAction(CONFIGURATION, "", "Show the document configuration dialog (Ctrl + D)", "configuration", mainController.getUIController(), "showDocProperties", KeyStroke.getKeyStroke("control D")));
        actions.addAction(createAction(SAVE, "", "Save the document (Ctrl + S)", "save", mainController.getIOController(), "saveDocument", KeyStroke.getKeyStroke("control S")));
        actions.addAction(createAction(LOAD, "", "Open a document (Ctrl + L)", "open", mainController.getIOController(), "loadDocument", KeyStroke.getKeyStroke("control O")));
        actions.addAction(createAction(EXPORT_COMPOSER, "", "Export the document (Ctrl + E)", "export_composer", mainController.getIOController(), "export", KeyStroke.getKeyStroke("control E")));
        addSeparatorTo(iconToolBar);
        actions.addAction(createAction(ADD_MAP, "", "Add a map element (Alt + M)", "add_map", mainController.getUIController(), "createMap", KeyStroke.getKeyStroke("alt M")));
        actions.addAction(createAction(ADD_TEXT,  "", "Add a text element (Alt + T)",  "add_text", mainController.getUIController(), "createText", KeyStroke.getKeyStroke("alt T")));
        actions.addAction(createAction(ADD_LEGEND, "", "Add a legend element (Alt + L)", "add_legend", mainController.getUIController(), "createLegend", KeyStroke.getKeyStroke("alt L")));
        actions.addAction(createAction(ADD_ORIENTATION, "", "Add an orientation element (Alt + O)", "compass", mainController.getUIController(), "createOrientation", KeyStroke.getKeyStroke("alt O")));
        actions.addAction(createAction(ADD_SCALE, "", "Add a scale element (Alt + S)", "add_scale", mainController.getUIController(), "createScale", KeyStroke.getKeyStroke("alt S")));
        actions.addAction(createAction(ADD_PICTURE, "", "Add a picture element (Alt + I)", "add_picture", mainController.getUIController(), "createPicture", KeyStroke.getKeyStroke("alt I")));
        addSeparatorTo(iconToolBar);
        actions.addAction(createAction(DRAW_CIRCLE, "", "Add a circle element (Alt + C)", "draw_circle", mainController.getUIController(), "createCircle", KeyStroke.getKeyStroke("alt C")));
        actions.addAction(createAction(DRAW_POLYGON, "", "Add a polygon element (Alt + Y)", "draw_polygon", mainController.getUIController(), "createPolygon", KeyStroke.getKeyStroke("alt Y")));
        addSeparatorTo(iconToolBar);
        actions.addAction(createAction(MOVE_BACK, "", "Move to the back (Alt + PageDown)", "move_back", mainController.getUIController(), "moveBack", KeyStroke.getKeyStroke("alt PAGE_DOWN")));
        actions.addAction(createAction(MOVE_DOWN, "", "Move down (Alt + Down)", "move_down", mainController.getUIController(), "moveDown", KeyStroke.getKeyStroke("alt DOWN")));
        actions.addAction(createAction(MOVE_ON, "", "Move on (Alt + Up)", "move_on", mainController.getUIController(), "moveOn", KeyStroke.getKeyStroke("alt UP")));
        actions.addAction(createAction(MOVE_FRONT, "", "Move to the front (Alt + PageUp)", "move_front", mainController.getUIController(), "moveFront", KeyStroke.getKeyStroke("alt PAGE_UP")));
        addSeparatorTo(iconToolBar);
        actions.addAction(createAction(ALIGN_TO_LEFT, "", "Align to the left (Alt + numpad 4)", "align_to_left", mainController.getUIController(), "alignToLeft", KeyStroke.getKeyStroke("alt NUMPAD4")));
        actions.addAction(createAction(ALIGN_TO_CENTER, "", "Align to the center", "align_to_center", mainController.getUIController(), "alignToCenter", null));
        actions.addAction(createAction(ALIGN_TO_RIGHT, "", "Align to the right (Alt + numpad 6)", "align_to_right", mainController.getUIController(), "alignToRight", KeyStroke.getKeyStroke("alt NUMPAD6")));
        actions.addAction(createAction(ALIGN_TO_BOTTOM, "", "Align to the bottom (Alt + numpad 2)", "align_to_bottom", mainController.getUIController(), "alignToBottom", KeyStroke.getKeyStroke("alt NUMPAD2")));
        actions.addAction(createAction(ALIGN_TO_MIDDLE, "", "Align to the middle", "align_to_middle", mainController.getUIController(), "alignToMiddle", null));
        actions.addAction(createAction(ALIGN_TO_TOP, "", "Align to the top (Alt + numpad 8)", "align_to_top", mainController.getUIController(), "alignToTop", KeyStroke.getKeyStroke("alt NUMPAD8")));
        addSeparatorTo(iconToolBar);
        actions.addAction(createAction(PROPERTIES, "", "Show selected elements properties (Ctrl + P)", "properties", mainController.getUIController(), "showSelectedGEProperties", KeyStroke.getKeyStroke("control P")));
        actions.addAction(createAction(DELETE, "", "Delete selected elements (DELETE)", "delete", mainController, "removeSelectedGE", KeyStroke.getKeyStroke("DELETE")));
        actions.addAction(createAction(REFRESH, "", "Redraw selected elements (Ctrl + R)", "refresh", mainController.getCompositionAreaController(), "refreshSelectedGE", KeyStroke.getKeyStroke("control R")));
        actions.addAction(createAction(UNDO, "", "Undo the last action (Ctrl + Z)", "edit_undo", mainController, "undo", KeyStroke.getKeyStroke("control Z")));
        actions.addAction(createAction(REDO, "", "Redo the last action (Ctrl + Shift + Z)", "edit_redo", mainController, "redo", KeyStroke.getKeyStroke("control shift Z")));

        iconToolBar.add(new JSeparator(SwingConstants.VERTICAL));

        //Sets the spinners tool bar.
        spinnerX = createSpinner("X", " X : ", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        spinnerY = createSpinner("Y", " Y : ", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        spinnerW = createSpinner("WIDTH", " W : ", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        spinnerH = createSpinner("HEIGHT", " H : ", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        spinnerToolBar.add(new JLabel(MapComposerIcon.getIcon("rotation")));
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
    private void addSeparatorTo(JToolBar toolBar){
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
     * The spinner and its label are set with the given function argument.
     * The function return the spinner reference to permit to listen to their modification.
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
                        mainController.changeProperty(GraphicalElement.Property.X, (int) spinnerX.getValue());
                        break;
                    case "Y":
                        mainController.changeProperty(GraphicalElement.Property.Y, (int) spinnerY.getValue());
                        break;
                    case "WIDTH":
                        mainController.changeProperty(GraphicalElement.Property.WIDTH, (int) spinnerW.getValue());
                        break;
                    case "HEIGHT":
                        mainController.changeProperty(GraphicalElement.Property.HEIGHT, (int) spinnerH.getValue());
                        break;
                    case "ROTATION":
                        mainController.changeProperty(GraphicalElement.Property.ROTATION, (int) spinnerR.getValue());
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
                        mainController.changeProperty(GraphicalElement.Property.X, (int) spinnerX.getValue() - mwe.getWheelRotation());
                        break;
                    case "Y":
                        mainController.changeProperty(GraphicalElement.Property.Y, (int) spinnerY.getValue() - mwe.getWheelRotation());
                        break;
                    case "WIDTH":
                        mainController.changeProperty(GraphicalElement.Property.WIDTH, (int) spinnerW.getValue() - mwe.getWheelRotation());
                        break;
                    case "HEIGHT":
                        mainController.changeProperty(GraphicalElement.Property.HEIGHT, (int) spinnerH.getValue() - mwe.getWheelRotation());
                        break;
                    case "ROTATION":
                        mainController.changeProperty(GraphicalElement.Property.ROTATION, (int) spinnerR.getValue() - mwe.getWheelRotation());
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
                            MapComposerIcon.getIcon(actionIconName),
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

    @Override
    public List<Action> createActions(org.orbisgis.viewapi.main.frames.ext.MainWindow target) {
        List<Action> actions = new ArrayList<>();
        actions.add(new DefaultAction(MENU_MAPCOMPOSER, "Map Composer",
                MapComposerIcon.getIcon("map_composer"),
                EventHandler.create(ActionListener.class, this, "showMapComposer")).setParent(MENU_TOOLS));
        return actions;
    }

    public void showMapComposer(){this.setVisible(true);}

    @Override
    public void disposeActions(org.orbisgis.viewapi.main.frames.ext.MainWindow target, List<Action> actions) {
        this.dispose();
    }
}
