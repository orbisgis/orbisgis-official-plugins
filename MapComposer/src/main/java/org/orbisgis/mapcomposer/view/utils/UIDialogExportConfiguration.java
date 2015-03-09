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

package org.orbisgis.mapcomposer.view.utils;

import net.miginfocom.swing.MigLayout;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.utils.GEManager;
import org.orbisgis.sif.UIPanel;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTabbedPane;
import javax.swing.event.ChangeListener;
import java.awt.Component;
import java.awt.event.ActionListener;
import java.beans.EventHandler;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This class is the UIPanel of the export configuration dialog.
 * This UIPanel allow the user to select which export he wants to do and to configure it.
 *
 * @author Sylvain PALOMINOS
 */

public class UIDialogExportConfiguration implements UIPanel {

    /** Object for the translation*/
    private static final I18n i18n = I18nFactory.getI18n(UIDialogExportConfiguration.class);

    /** TabbedPane containing a tab for each export type */
    private JTabbedPane tabbedPane;

    /** Main JPanel**/
    private JPanel panel;

    /**Map of GraphicalElement and boolean.
     * The boolean tells if the vector rendering should be use or not (if not use the raster rendering)
     **/
    private Map<GraphicalElement, Boolean> exportData;

    /** Type of the export to do **/
    private int exportType = EXPORT_NONE;

    public static final int EXPORT_NONE = 0;
    public static final int EXPORT_PNG = 1;
    public static final int EXPORT_PDF = 2;
    public static final int EXPORT_WEB = 3;

    /** List of ratio buttons for the vector export */
    private List<JRadioButton> listVectorRatio;
    /** List of ratio buttons for the raster export */
    private List<JRadioButton> listRasterRatio;
    /** List of GraphicalElements names */
    private List<JLabel> names;
    /** Main ratio button for vector export. If selected all the GE will be rendered as vector image */
    private JRadioButton mainVectorRatio;
    /** Main ratio button for raster export. If selected all the GE will be rendered as raster image */
    private JRadioButton mainRasterRatio;
    /** Button group containing the main ratio button */
    private ButtonGroup mainButtonGroup;
    /** Button expanding the selection between vector or raster export*/
    private JButton expand;

    private List<Class<? extends GraphicalElement>> listGEClasses;

    public UIDialogExportConfiguration(List<GraphicalElement> listGEToExport, GEManager geManager) {
        listVectorRatio = new ArrayList<>();
        listRasterRatio = new ArrayList<>();
        names = new ArrayList<>();

        //Build the export data map
        exportData = new HashMap<>();
        for(GraphicalElement ge : listGEToExport){
            exportData.put(ge, false);
        }

        //Sets the main panel and the different panels used in the TabbedPane
        panel = new JPanel();

        JPanel panelPNG = new JPanel();

        JPanel panelRasterVector = new JPanel(new MigLayout("hidemode 3"));
        panelRasterVector.setBorder(BorderFactory.createTitledBorder("Vector/Raster"));
        JPanel panelPDF = new JPanel(new MigLayout());
        panelPDF.add(panelRasterVector);

        //Construct the raster/vector panel
        //Adds the expand button and the main ratio button
        mainVectorRatio = new JRadioButton();
        JLabel vectorLabel = new JLabel(i18n.tr("vector"));
        mainRasterRatio = new JRadioButton();
        JLabel rasterLabel = new JLabel(i18n.tr("raster"));
        expand = new JButton();
        expand.putClientProperty("isExpanded", true);
        expand.addActionListener(EventHandler.create(ActionListener.class, this, "hideShowRatio"));
        panelRasterVector.add(expand, "cell 0 0 1 1");
        panelRasterVector.add(mainVectorRatio, "cell 1 0 1 1");
        panelRasterVector.add(vectorLabel, "cell 2 0 1 1");
        panelRasterVector.add(mainRasterRatio, "cell 3 0 1 1");
        panelRasterVector.add(rasterLabel, "cell 4 0 1 1");
        mainButtonGroup = new ButtonGroup();
        mainButtonGroup.add(mainVectorRatio);
        mainButtonGroup.add(mainRasterRatio);
        mainVectorRatio.addActionListener(EventHandler.create(ActionListener.class, this, "selectAll", "source"));
        mainRasterRatio.addActionListener(EventHandler.create(ActionListener.class, this, "selectAll", "source"));

        //Adds a line of ratio button for each registered classes of GraphicalElement
        listGEClasses = geManager.getRegisteredGEClasses();
        int i=1;
        for(Class geClass : listGEClasses){
            JLabel geName = new JLabel(geClass.getSimpleName());
            JRadioButton vectorButton = new JRadioButton();
            JRadioButton rasterButton = new JRadioButton();
            panelRasterVector.add(geName, "cell 0 " + i + " 1 1");
            panelRasterVector.add(vectorButton, "cell 1 "+i+" 1 1");
            panelRasterVector.add(rasterButton, "cell 3 "+i+" 1 1");
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(vectorButton);
            buttonGroup.add(rasterButton);
            names.add(geName);
            listVectorRatio.add(vectorButton);
            listRasterRatio.add(rasterButton);
            vectorButton.addChangeListener(EventHandler.create(ChangeListener.class, this, "updateMainRatio"));
            rasterButton.addActionListener(EventHandler.create(ActionListener.class, this, "updateMainRatio"));
            i++;
        }

        //Sets the TabbedPane
        tabbedPane = new JTabbedPane();
        tabbedPane.addTab(i18n.tr("PNG"), null, panelPNG, i18n.tr("Export as PNG"));
        tabbedPane.addTab(i18n.tr("PDF"), null, panelPDF, i18n.tr("Export as PDF"));
        panel.add(tabbedPane);

        hideShowRatio();
    }

    /**
     * Select all the ratio button corresponding of the given main ratio button in argument
     * @param mainRatioButton
     */
    public void selectAll(Object mainRatioButton){
        if(mainRatioButton == mainVectorRatio){
            for(JRadioButton button : listVectorRatio){
                button.setSelected(true);
            }
            mainVectorRatio.setSelected(true);
        }
        if(mainRatioButton == mainRasterRatio){
            for(JRadioButton button : listRasterRatio){
                button.setSelected(true);
            }
            mainRasterRatio.setSelected(true);
        }
    }

    /**
     * Updates the main ratio button according to the state of all the others ration button.
     */
    public void updateMainRatio(){
        boolean rasterOnly = true;
        boolean vectorOnly = true;
        for(JRadioButton button : listVectorRatio) {
            if(!button.isSelected()) {
                vectorOnly = false;
            }
        }
        for(JRadioButton button : listRasterRatio) {
            if(!button.isSelected()) {
                rasterOnly = false;
            }
        }
        if(rasterOnly){
            mainRasterRatio.setSelected(true);
        }
        else if(vectorOnly){
            mainVectorRatio.setSelected(true);
        }
        else {
            mainButtonGroup.clearSelection();
        }
    }

    /**
     * Hides or shows the ratio button for each GraphicalElement.
     */
    public void hideShowRatio(){
        boolean show = (boolean)expand.getClientProperty("isExpanded");
        expand.putClientProperty("isExpanded", !show);
        for(JComponent c : listRasterRatio){
            c.setVisible(show);
        }
        for(JComponent c : listVectorRatio){
            c.setVisible(show);
        }
        for(JComponent c : names){
            c.setVisible(show);
        }
    }

    @Override
    public URL getIconURL() {
        return null;
    }

    @Override
    public String getTitle() {
        return i18n.tr("Export configuration");
    }

    @Override
    public String validateInput() {
        switch (tabbedPane.getSelectedIndex()){
            case 0:
                exportType = EXPORT_PNG;
                break;
            case 1:
                Map<GraphicalElement, Boolean> map = new HashMap();
                for(int i = 0; i < listGEClasses.size(); i++){
                    for(GraphicalElement ge : exportData.keySet()){
                        if(listGEClasses.get(i).isInstance(ge)){
                            map.put(ge, listVectorRatio.get(i).isSelected());
                        }
                    }
                }
                exportType = EXPORT_PDF;
                exportData = map;
                break;
        }

        return null;
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    /**
     * Returns the export type.
     * @return The export type.
     */
    public int getExportType(){
        return exportType;
    }

    /**
     * Returns the export data.
     * @return The export data.
     */
    public Map getExportData(){
        return exportData;
    }
}
