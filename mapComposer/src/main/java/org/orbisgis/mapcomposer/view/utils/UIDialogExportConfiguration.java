/*
* MapComposer is an OrbisGIS plugin dedicated to the creation of cartographic
* documents.
*
* This plugin was firstly developed  at French IRSTV institute as part of the MApUCE project,
* funded by the French Agence Nationale de la Recherche (ANR) under contract ANR-13-VBDU-0004.
* 
* Since 2015, MapComposer is developed and maintened by the GIS group of the DECIDE team of the 
* Lab-STICC CNRS laboratory, see <http://www.lab-sticc.fr/>.
*
* The GIS group of the DECIDE team is located at :
*
* Laboratoire Lab-STICC – CNRS UMR 6285
* Equipe DECIDE
* UNIVERSITÉ DE BRETAGNE-SUD
* Institut Universitaire de Technologie de Vannes
* 8, Rue Montaigne - BP 561 56017 Vannes Cedex
*
* Copyright (C) 2007-2014 CNRS (IRSTV FR CNRS 2488)
* Copyright (C) 2015-2017 CNRS (Lab-STICC UMR CNRS 6285)
*
* The MapComposer plugin is distributed under GPL 3 license. 
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

import org.orbisgis.mapcomposer.controller.utils.exportThreads.ExportPDFThread;
import org.orbisgis.mapcomposer.controller.utils.exportThreads.ExportImageThread;
import org.orbisgis.mapcomposer.controller.utils.exportThreads.ExportThread;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.utils.GEManager;
import org.orbisgis.sif.UIPanel;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import java.awt.Component;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;

/**
 * This class is the UIPanel of the export configuration dialog.
 * This UIPanel allow the user to select which export he wants to do and to configure it.
 *
 * @author Sylvain PALOMINOS
 */

public class UIDialogExportConfiguration implements UIPanel {

    /** Object for the translation*/
    private static final I18n i18n = I18nFactory.getI18n(UIDialogExportConfiguration.class);

    /** Main panel */
    private final JPanel panel;

    /** TabbedPane containing a tab for each export type */
    private JTabbedPane tabbedPane;

    /** List of ExportThread that can be used and configured */
    private List<ExportThread> exportThreadList;

    /** ExportThread that will be used */
    private ExportThread exportThread;

    /** GEManager containing all the rendering methods */
    private GEManager geManager;

    /** Stack of GraphicalElements to export */
    private Stack<GraphicalElement> stackGEToExport;

    /**
     * Main constructor
     * @param stackGEToExport Stack of GraphicalElement to export.
     * @param geManager GeManager containing the rendering methods.
     */
    public UIDialogExportConfiguration(Stack<GraphicalElement> stackGEToExport, GEManager geManager) {
        this.panel = new JPanel();
        this.exportThreadList = new ArrayList<>();
        this.geManager = geManager;
        this.stackGEToExport = stackGEToExport;

        //Sets and add the JTabbedPane
        tabbedPane = new JTabbedPane();
        panel.add(tabbedPane);

        //Instantiate the basics ExportThread
        this.addExportThread(new ExportPDFThread());
        this.addExportThread(new ExportImageThread());
    }

    /**
     * Adds an export thread.
     * @param exportThread New instance of ExportThread implementation.
     */
    public void addExportThread(ExportThread exportThread){
        exportThreadList.add(exportThread);
        exportThread.setGEManager(geManager);
        tabbedPane.addTab(exportThread.getName(), null, exportThread.constructExportPanel(stackGEToExport), exportThread.getDescription());
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
        exportThread = exportThreadList.get(tabbedPane.getSelectedIndex());
        return null;
    }

    @Override
    public Component getComponent() {
        return panel;
    }

    public ExportThread getExportThread(){
        return exportThread;
    }
}
