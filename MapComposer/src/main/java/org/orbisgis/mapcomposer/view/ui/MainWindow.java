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

import bibliothek.gui.dock.ToolbarDockStation;
import bibliothek.gui.dock.common.CControl;
import bibliothek.gui.dock.common.CGrid;
import bibliothek.gui.dock.common.CLocation;
import bibliothek.gui.dock.common.DefaultSingleCDockable;
import bibliothek.gui.dock.common.action.CButton;
import bibliothek.gui.dock.themes.border.BorderModifier;
import bibliothek.gui.dock.toolbar.CToolbarContentArea;
import bibliothek.gui.dock.toolbar.CToolbarItem;
import bibliothek.gui.dock.toolbar.location.CToolbarAreaLocation;
import org.orbisgis.corejdbc.DataManager;
import org.orbisgis.mainframe.api.MainFrameAction;
import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement.Property;
import org.orbisgis.mapcomposer.view.utils.MapComposerIcon;
import org.orbisgis.sif.UIFactory;
import org.orbisgis.sif.components.actions.DefaultAction;
import org.orbisgis.wkguiapi.ViewWorkspace;
import org.osgi.service.cm.Configuration;
import org.osgi.service.cm.ConfigurationAdmin;
import org.osgi.service.cm.ConfigurationException;
import org.osgi.service.cm.ManagedService;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.Action;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JSpinner;
import javax.swing.KeyStroke;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.Border;
import javax.swing.event.ChangeListener;
import java.awt.Dimension;
import java.awt.event.ActionListener;
import java.awt.event.MouseWheelEvent;
import java.awt.event.MouseWheelListener;
import java.beans.EventHandler;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

/**
 * Main window of the map composer. It contains several tool bar and the CompositionArea.
 * It does the link between the user interactions and the UIController
 *
 * @author Sylvain PALOMINOS
 */
@Component
public class MainWindow extends JFrame implements MainFrameAction, ManagedService {

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
    public static final String ADD_SCALE = "ADD_SCALE";
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

    /** Object for the translation*/
    private static final I18n i18n = I18nFactory.getI18n(MainWindow.class);

    /** ConfigurationAdmin instance */
    private ConfigurationAdmin configurationAdmin;

    private CControl control;

    /** Public main constructor. */
    public MainWindow(){
        super("Map composer");
        this.setLocationRelativeTo(null);
        UIFactory.setMainFrame(this);
        this.mainController = new MainController();
        this.mainController.setMainWindow(this);
        this.compositionArea = new CompositionArea(mainController);
        this.mainController.getCompositionAreaController().setCompositionArea(compositionArea);
        //Sets the default size to the window
        this.setSize(1024, 768);
        this.setIconImage(MapComposerIcon.getIcon("map_composer").getImage());
        this.control = new CControl(this);
    }

    /**
     * Returns the CompositionArea
     * @return The CompositionArea
     */
    public CompositionArea getCompositionArea(){return compositionArea;}

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
    public List<Action> createActions(org.orbisgis.mainframe.api.MainWindow  target) {
        List<Action> actions = new ArrayList<>();
        actions.add(new DefaultAction(MENU_MAPCOMPOSER, "Map Composer",
                MapComposerIcon.getIcon("map_composer"),
                EventHandler.create(ActionListener.class, this, "showMapComposer")).setParent(MENU_TOOLS));
        return actions;
    }

    private CToolbarContentArea area;

    public void showMapComposer() {
        control.putProperty(ToolbarDockStation.SIDE_GAP, 2);
        control.putProperty(ToolbarDockStation.GAP, 2);
        control.getController().getThemeManager().setBorderModifier("dock.border.displayer.basic.base", new BorderModifier() {
            @Override
            public Border modify(Border border) {
                return null;
            }
        });

        area = new CToolbarContentArea(control, "base");
        area.getNorthArea().setBorder(BorderFactory.createRaisedBevelBorder());
        area.getNorthArea().setVisible(false);
        control.addStationContainer(area);
        this.add(area);

        constructUI();

        if(configurationAdmin != null){
            try {
                Dictionary dictionary = configurationAdmin.getConfiguration(MainWindow.class.getName()).getProperties();
                if(dictionary!=null) {
                    byte[] byteArray = (byte[])dictionary.get("layout");
                    if (byteArray != null) {
                        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(byteArray);
                        control.read(new DataInputStream(byteArrayInputStream));
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        this.setVisible(true);
    }

    private void constructUI(){
        CToolbarAreaLocation stationLocation = area.getNorthToolbar().getStationLocation();

        addCToolbarCItem(NEW_COMPOSER, i18n.tr("Create a new document"), "new_composer", mainController.getUIController(), "createDocument", KeyStroke.getKeyStroke("control N"), stationLocation, 0, 0, 0, 0);
        addCToolbarCItem(CONFIGURATION, i18n.tr("Show the document configuration dialog"), "configuration", mainController.getUIController(), "showDocProperties", KeyStroke.getKeyStroke("control D"), stationLocation, 0, 0, 0, 1);
        addCToolbarCItem(SAVE, i18n.tr("Save the document"), "save", mainController, "saveDocument", KeyStroke.getKeyStroke("control S"), stationLocation, 0, 0, 0, 2);
        addCToolbarCItem(LOAD, i18n.tr("Open a document"), "open", mainController, "loadDocument", KeyStroke.getKeyStroke("control O"), stationLocation, 0, 0, 0, 3);
        addCToolbarCItem(EXPORT_COMPOSER, i18n.tr("Export the document"), "export_composer", mainController, "export", KeyStroke.getKeyStroke("control E"), stationLocation, 0, 0, 0, 4);
        addCToolbarCItem(ADD_MAP, i18n.tr("Add a map element"), "add_map", mainController.getUIController(), "createMap", KeyStroke.getKeyStroke("alt M"), stationLocation, 0, 0, 1, 0);
        addCToolbarCItem(ADD_TEXT, i18n.tr("Add a text element"), "add_text", mainController.getUIController(), "createText", KeyStroke.getKeyStroke("alt T"), stationLocation, 0, 0, 1, 1);
        addCToolbarCItem(ADD_LEGEND, i18n.tr("Add a legend element"), "add_legend", mainController.getUIController(), "createLegend", KeyStroke.getKeyStroke("alt L"), stationLocation, 0, 0, 1, 2);
        addCToolbarCItem(ADD_ORIENTATION, i18n.tr("Add an orientation element"), "compass", mainController.getUIController(), "createOrientation", KeyStroke.getKeyStroke("alt O"), stationLocation, 0, 0, 1, 3);
        addCToolbarCItem(ADD_SCALE, i18n.tr("Add a scale element"), "add_scale", mainController.getUIController(), "createScale", KeyStroke.getKeyStroke("alt S"), stationLocation, 0, 0, 1, 4);
        addCToolbarCItem(ADD_PICTURE, i18n.tr("Add a picture element"), "add_picture", mainController.getUIController(), "createPicture", KeyStroke.getKeyStroke("alt I"), stationLocation, 0, 0, 1, 5);
        addCToolbarCItem(DRAW_CIRCLE, i18n.tr("Add a circle element"), "draw_circle", mainController.getUIController(), "createCircle", KeyStroke.getKeyStroke("alt C"), stationLocation, 0, 0, 2, 0);
        addCToolbarCItem(DRAW_POLYGON, i18n.tr("Add a polygon element"), "draw_polygon", mainController.getUIController(), "createPolygon", KeyStroke.getKeyStroke("alt Y"), stationLocation, 0, 0, 2, 1);
        addCToolbarCItem(MOVE_BACK, i18n.tr("Move to the back"), "move_back", mainController.getUIController(), "moveBack", KeyStroke.getKeyStroke("alt PAGE_DOWN"), stationLocation, 0, 0, 3, 0);
        addCToolbarCItem(MOVE_DOWN, i18n.tr("Move down"), "move_down", mainController.getUIController(), "moveDown", KeyStroke.getKeyStroke("alt DOWN"), stationLocation, 0, 0, 3, 1);
        addCToolbarCItem(MOVE_ON, i18n.tr("Move on"), "move_on", mainController.getUIController(), "moveOn", KeyStroke.getKeyStroke("alt UP"), stationLocation, 0, 0, 3, 2);
        addCToolbarCItem(MOVE_FRONT, i18n.tr("Move to the front"), "move_front", mainController.getUIController(), "moveFront", KeyStroke.getKeyStroke("alt PAGE_UP"), stationLocation, 0, 0, 3, 3);
        addCToolbarCItem(ALIGN_TO_LEFT, i18n.tr("Align to the left"), "align_to_left", mainController.getUIController(), "alignToLeft", KeyStroke.getKeyStroke("alt NUMPAD4"), stationLocation, 0, 0, 4, 0);
        addCToolbarCItem(ALIGN_TO_CENTER, i18n.tr("Align to the center"), "align_to_center", mainController.getUIController(), "alignToCenter", null, stationLocation, 0, 0, 4, 1);
        addCToolbarCItem(ALIGN_TO_RIGHT, i18n.tr("Align to the right"), "align_to_right", mainController.getUIController(), "alignToRight", KeyStroke.getKeyStroke("alt NUMPAD6"), stationLocation, 0, 0, 4, 2);
        addCToolbarCItem(ALIGN_TO_BOTTOM, i18n.tr("Align to the bottom"), "align_to_bottom", mainController.getUIController(), "alignToBottom", KeyStroke.getKeyStroke("alt NUMPAD2"), stationLocation, 0, 0, 4, 3);
        addCToolbarCItem(ALIGN_TO_MIDDLE, i18n.tr("Align to the middle"), "align_to_middle", mainController.getUIController(), "alignToMiddle", null, stationLocation, 0, 0, 4, 4);
        addCToolbarCItem(ALIGN_TO_TOP, i18n.tr("Align to the top"), "align_to_top", mainController.getUIController(), "alignToTop", KeyStroke.getKeyStroke("alt NUMPAD8"), stationLocation, 0, 0, 4, 5);
        addCToolbarCItem(PROPERTIES, i18n.tr("Show selected elements properties"), "properties", mainController.getUIController(), "showSelectedGEProperties", KeyStroke.getKeyStroke("control P"), stationLocation, 0, 0, 5, 0);
        addCToolbarCItem(DELETE, i18n.tr("Delete selected elements"), "delete", mainController, "removeSelectedGE", KeyStroke.getKeyStroke("DELETE"), stationLocation, 0, 0, 5, 1);
        addCToolbarCItem(REFRESH, i18n.tr("Redraw selected elements"), "refresh", mainController.getCompositionAreaController(), "refreshSelectedGE", KeyStroke.getKeyStroke("control R"), stationLocation, 0, 0, 5, 2);
        addCToolbarCItem(UNDO, i18n.tr("Undo the last action"), "edit_undo", mainController, "undo", KeyStroke.getKeyStroke("control Z"), stationLocation, 0, 0, 5, 3);
        addCToolbarCItem(REDO, i18n.tr("Redo the last action"), "edit_redo", mainController, "redo", KeyStroke.getKeyStroke("control shift Z"), stationLocation, 0, 0, 5, 4);

        //Sets the spinners tool bar.
        addToolbarComponent("spinnerXLabel", new JLabel(" X :"), stationLocation, 0, 1, 0, 0);
        spinnerX = createSpinner("X", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        addToolbarComponent("spinnerX", spinnerX, stationLocation, 0, 1, 0, 1);

        addToolbarComponent("spinnerYLabel", new JLabel(" Y :"), stationLocation, 0, 1, 0, 2);
        spinnerY = createSpinner("Y", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        addToolbarComponent("spinnerY", spinnerY, stationLocation, 0, 1, 0, 3);

        addToolbarComponent("spinnerWidthLabel", new JLabel(" WIDTH :"), stationLocation, 0, 1, 0, 4);
        spinnerW = createSpinner("WIDTH", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        addToolbarComponent("spinnerWidth", spinnerW, stationLocation, 0, 1, 0, 5);

        addToolbarComponent("spinnerHeightLabel", new JLabel(" HEIGHT :"), stationLocation, 0, 1, 0, 6);
        spinnerH = createSpinner("HEIGHT", 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        addToolbarComponent("spinnerHeight", spinnerH, stationLocation, 0, 1, 0, 7);

        addToolbarComponent("spinnerRotationIcon", new JLabel(MapComposerIcon.getIcon("rotation")), stationLocation, 0, 1, 0, 8);
        spinnerR = createSpinner("ROTATION", 0, -360, 360);
        addToolbarComponent("spinnerRotation", spinnerR, stationLocation, 0, 1, 0, 9);

        DefaultSingleCDockable dockable = new DefaultSingleCDockable("composition_area", "composition area");
        dockable.setCloseable(false);
        dockable.setResizeLocked(true);
        dockable.setLocation(CLocation.base().normal());
        dockable.setTitleShown(false);
        dockable.setMaximizable(false);
        dockable.setMinimizable(false);
        dockable.add(compositionArea);

        CGrid grid = new CGrid(control);
        grid.add(0, 0, 1, 1, dockable);
        area.deploy(grid);
    }

    private void addCToolbarCItem(String actionId, String actionToolTip, String actionIconName, Object target, String ActionFunctionName, KeyStroke keyStroke, CToolbarAreaLocation stationLocation, int group, int column, int line, int item){
        CButton button = new CButton(actionId, MapComposerIcon.getIcon(actionIconName));
        button.setTooltip(actionToolTip);
        button.addActionListener(EventHandler.create(ActionListener.class, target, ActionFunctionName));

        CToolbarItem cItem = new CToolbarItem(actionId);
        cItem.setItem(button);
        cItem.setLocation(stationLocation.group(group).toolbar(column, line).item(item));
        control.addDockable(cItem);
        cItem.setVisible(true);

        area.registerKeyboardAction(EventHandler.create(ActionListener.class, target, ActionFunctionName), actionId, keyStroke, JComponent.WHEN_IN_FOCUSED_WINDOW);
    }

    private void addToolbarComponent(String id, JComponent component, CToolbarAreaLocation stationLocation, int group, int column, int line, int item){
        CToolbarItem cItem = new CToolbarItem(id);
        cItem.setItem(component);
        cItem.setLocation(stationLocation.group(group).toolbar(column, line).item(item));
        control.addDockable(cItem);
        cItem.setVisible(true);
    }

    /**
     * Creates and adds to the spinnerToolBar a spinner and its label.
     * The spinner and its label are set with the given function argument.
     * The function return the spinner reference to permit to listen to their modification.
     * @param name Name of the spinner.
     * @param value Actual value of the spinner.
     * @param minValue Minimum value of the spinner.
     * @param maxValue Maximum value of the spinner.
     * @return The reference to the created spinner.
     */
    private JSpinner createSpinner(String name, int value, int minValue, int maxValue){
        JSpinner spin = new JSpinner(new SpinnerNumberModel(value, minValue, maxValue, 1));
        spin.setName(name);
        spin.addChangeListener(EventHandler.create(ChangeListener.class, this, "spinChange", "source"));
        spin.addMouseWheelListener(EventHandler.create(MouseWheelListener.class, this, "mouseWheel", ""));
        spin.setMaximumSize(new Dimension(64, 32));
        spin.setMinimumSize(new Dimension(32, 32));
        spin.setPreferredSize(new Dimension(64, 32));
        spin.setEnabled(false);
        return spin;
    }

    @Override
    public void disposeActions(org.orbisgis.mainframe.api.MainWindow  target, List<Action> actions) {
        this.dispose();
    }

    protected DataManager getDataManager(){
        return mainController.getDataManager();
    }
    protected ViewWorkspace getViewWorkspace(){
        return mainController.getViewWorkspace();
    }

    @Reference
    protected void setDataManager(DataManager dataManager) {
        this.mainController.setDataManager(dataManager);
    }

    @Reference
    protected void setViewWorkspace(ViewWorkspace viewWorkspace) {
        this.mainController.setViewWorkspace(viewWorkspace);
    }

    protected void unsetDataManager(DataManager dataManager) {
        this.mainController.setDataManager(null);
    }

    protected void unsetViewWorkspace(ViewWorkspace viewWorkspace) {
        this.mainController.setViewWorkspace(null);
    }

    @Reference
    protected void setConfigurationAdmin(ConfigurationAdmin configurationAdmin){
        this.configurationAdmin = configurationAdmin;
    }

    protected void unsetConfigurationAdmin(ConfigurationAdmin configurationAdmin){
        this.configurationAdmin = null;
    }

    @Override
    public void updated(Dictionary<String, ?> properties) throws ConfigurationException {
        this.setBounds((Integer) properties.get("window_x"), (Integer) properties.get("window_y"), (Integer) properties.get("window_width"), (Integer) properties.get("window_height"));
        compositionArea.configure((Integer) properties.get("unit"));
    }

    @Deactivate
    public void close(){
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        if(control!=null) {
            control.save("layout");
            try {
                control.write(new DataOutputStream(byteArrayOutputStream));
            } catch (IOException e) {
                LoggerFactory.getLogger(MainWindow.class).error(e.getMessage());
            }
            control.destroy();
        }
        try {
            Configuration configuration = configurationAdmin.getConfiguration(MainWindow.class.getName());
            Dictionary<String, Object> props = configuration.getProperties();
            if (props == null) {
                props = new Hashtable<>();
            }
            props.put("window_width", this.getWidth());
            props.put("window_height", this.getHeight());
            props.put("window_x", this.getX());
            props.put("window_y", this.getY());
            props.put("unit", compositionArea.getUnit());
            props.put("layout", byteArrayOutputStream.toByteArray());
            configuration.update(props);
        } catch (IOException e) {
            LoggerFactory.getLogger(MainWindow.class).error(e.getMessage());
        }
    }
}
