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

package org.orbisgis.mapcomposer.controller.utils.exportThreads;

import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.utils.GEManager;

import javax.swing.JComponent;
import javax.swing.JProgressBar;
import java.util.Map;
import java.util.Stack;

/**
 * Interface to define thread exports. The export should be executed in the override method run()
 * The method constructExportPanel() return the UI for configuring the export.
 * It also should use the addData method to register the GraphicalElement to export and if the export should use vector rendering.
 * Then, once the GraphicalElement registered, the file chooser dialog is display and the path to the new file is set.
 * Then, the MapComposer sets the GEManager (access to the rendering methods), and to the progress bar.
 * To finish, the method run() is called to do the export.
 *
 * @author Sylvain PALOMINOS
 */

public interface ExportThread extends Runnable {

    /**
     * Sets the data that will be exported by the thread.
     * If the GraphicalElement in argument is already registered, update the boolean value.
     * @param ge A GraphicalElement to export.
     * @param isVector A boolean that indicate if the GraphicalElement should be export as a vector image or not
     */
    public void addData(GraphicalElement ge, Boolean isVector);

    /**
     * Sets the path of the file where the document should be exported
     * @param path Path to the new file.
     */
    public void setPath(String path);

    /**
     * Sets the GEManager that give the access to the rendering methods.
     * @param geManager Access to the rendering methods.
     */
    public void setGEManager(GEManager geManager);

    /**
     * Sets the progressBar to use to display the export state.
     * @param progressBar ProgressBar where the export state is displayed.
     */
    public void setProgressBar(JProgressBar progressBar);

    /**
     * Constructs the UI for the export configuration which will be displayed before the file chooser.
     * This methods should construct an user interface to allow him to configure the different aspects of the export.
     * This interface should also uses addData() method to update the data to export.
     * Be careful, the GraphicalElement should be copied (deep copy) before registered to avoid concurrent modification during the export
     * @param stackGEToExport Stack of GraphicalElements to export.
     * @return A JComponent containing all the swing component to allows the export configuration.
     */
    public JComponent constructExportPanel(Stack<GraphicalElement> stackGEToExport);

    /**
     * Returns the short name of the export. It will be displayed in the tab of the export configuration panel.
     * @return The short name the the export.
     */
    public String getName();

    /**
     * Returns the description of the export. It will be shown in the tooltip text of the corresponding tab of the export configuration panel.
     * @return The description of the export.
     */
    public String getDescription();

    /**
     * Returns a map of file filters associated to the export.
     * The returned map is build with the String of the file extension (i.e. "png" or "pdf" or "html" ...) and the description of the file extension.
     * @return A map of file extension and description.
     */
    public Map<String, String> getFileFilters();
}
