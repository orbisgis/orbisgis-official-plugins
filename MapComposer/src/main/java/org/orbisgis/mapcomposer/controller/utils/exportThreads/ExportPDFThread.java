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

import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Image;
import com.itextpdf.text.Rectangle;
import com.itextpdf.text.pdf.PdfWriter;
import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.graphicalelement.element.text.TextElement;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.utils.GEManager;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.JProgressBar;
import java.awt.image.BufferedImage;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * This thread exports the document as a PDF file.
 * @author Sylvain PALOMINOS
 */

public class ExportPDFThread extends Thread {
    /** List of GraphicalElements to export*/
    private List<GraphicalElement> listGEToExport;
    /** Path of the export file */
    private String path;
    /** Progress bar where the progression is shown */
    private JProgressBar progressBar;
    /** GeManager used to get the GraphicalElement rendering methods. */
    private GEManager geManager;

    /** Translation*/
    private static final I18n i18n = I18nFactory.getI18n(ExportPDFThread.class);

    /**
     * Main constructor
     * @param listGEToExport List of GraphicalElement to export.
     * @param path File path to export.
     * @param progressBar Progress bar where the progression is shown.
     */
    public ExportPDFThread(List<GraphicalElement> listGEToExport, String path, JProgressBar progressBar, GEManager geManager){
        //As this class is a thread, the GE can be modified while being export, so they have to be cloned
        this.listGEToExport = new ArrayList<>();
        for(GraphicalElement ge : listGEToExport){
            this.listGEToExport.add(ge.deepCopy());
        }
        this.path = path;
        this.geManager = geManager;
        if(progressBar != null)
            this.progressBar = progressBar;
        else
            this.progressBar = new JProgressBar();
    }
    @Override
    public void run() {
        try{
            Document pdfDocument = null;
            int height = 0;
            //Find the Document GE to create the BufferedImage where all the GE will be drawn
            for(GraphicalElement ge : listGEToExport){
                if(ge instanceof org.orbisgis.mapcomposer.model.graphicalelement.element.Document){
                    pdfDocument = new Document(new Rectangle(ge.getWidth(), ge.getHeight()), 0, 0, 0, 0);
                    height = ge.getHeight();
                }
            }
            //If no Document was created, throw an exception
            if(pdfDocument == null){
                throw new IllegalArgumentException(i18n.tr("Error on export : The list of GraphicalElement to export does not contain any Document GE."));
            }
            //Open the document
            PdfWriter.getInstance(pdfDocument, new FileOutputStream(path));
            pdfDocument.open();

            progressBar.setValue(0);

            //Draw each GraphicalElement in the BufferedImage
            for(GraphicalElement ge : listGEToExport){
                if((ge instanceof org.orbisgis.mapcomposer.model.graphicalelement.element.Document))
                    continue;
                //If the alpha value is 0, the whole TextElement is transparent.
                // This should be resolved when the TextElement will be processed as a text and not an image.
                if(ge instanceof TextElement)
                    if(((TextElement)ge).getAlpha()==0)
                        ((TextElement)ge).setAlpha(1);
                BufferedImage bi = geManager.getRenderer(ge.getClass()).createImageFromGE(ge);
                Image img = Image.getInstance(bi, null);
                img.setAbsolutePosition(ge.getX(), height-ge.getHeight()-ge.getY());
                pdfDocument.add(img);
                progressBar.setValue((listGEToExport.indexOf(ge) * 100) / listGEToExport.size());
                progressBar.revalidate();
            }

            pdfDocument.close();
            progressBar.setValue(progressBar.getMaximum());

        } catch (IllegalArgumentException|IOException|DocumentException ex) {
            LoggerFactory.getLogger(MainController.class).error(ex.getMessage());
        }
    }
}
