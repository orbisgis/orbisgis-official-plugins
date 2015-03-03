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
import com.itextpdf.text.pdf.PdfContentByte;
import com.itextpdf.text.pdf.PdfGraphics2D;
import com.itextpdf.text.pdf.PdfLayer;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import org.orbisgis.core_export.PdfRenderer;
import org.orbisgis.mapcomposer.controller.MainController;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.utils.GEManager;
import org.orbisgis.mapcomposer.view.graphicalelement.RendererRaster;
import org.orbisgis.mapcomposer.view.graphicalelement.RendererVector;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.imageio.ImageIO;
import javax.swing.JProgressBar;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

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

    private int width, height;

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
            //Find the Document GE to create the BufferedImage where all the GE will be drawn
            for(GraphicalElement ge : listGEToExport){
                if(ge instanceof org.orbisgis.mapcomposer.model.graphicalelement.element.Document){
                    pdfDocument = new Document(new Rectangle(ge.getWidth(), ge.getHeight()));
                    height = ge.getHeight();
                    width = ge.getWidth();
                }
            }
            //If no Document was created, throw an exception
            if(pdfDocument == null){
                throw new IllegalArgumentException(i18n.tr("Error on export : The list of GraphicalElement to export does not contain any Document GE."));
            }
            //Open the document
            PdfWriter writer = PdfWriter.getInstance(pdfDocument, new FileOutputStream(path));
            writer.setUserProperties(true);
            writer.setRgbTransparencyBlending(true);
            writer.setTagged();
            pdfDocument.open();

            PdfContentByte cb = writer.getDirectContent();

            progressBar.setValue(0);

            //Draw each GraphicalElement in the BufferedImage
            for(GraphicalElement ge : listGEToExport){
                if((ge instanceof org.orbisgis.mapcomposer.model.graphicalelement.element.Document))
                    continue;


                double rad = Math.toRadians(ge.getRotation());
                double newHeight = Math.abs(sin(rad)*ge.getWidth())+Math.abs(cos(rad)*ge.getHeight());
                double newWidth = Math.abs(sin(rad)*ge.getHeight())+Math.abs(cos(rad)*ge.getWidth());

                int maxWidth = Math.max((int)newWidth, ge.getWidth());
                int maxHeight = Math.max((int)newHeight, ge.getHeight());

                PdfLayer layer;
                PdfTemplate pdfTemplate = cb.createTemplate(maxWidth, maxHeight);
                Graphics2D g2dTemplate = pdfTemplate.createGraphics(maxWidth, maxHeight);

                if(geManager.getRenderer(ge.getClass()) instanceof RendererVector) {
                    layer = new PdfLayer("test", writer);
                    cb.beginLayer(layer);
                    ((RendererVector)geManager.getRenderer(ge.getClass())).drawGE(g2dTemplate, ge);
                    cb.addTemplate(pdfTemplate, ge.getX() + (ge.getWidth() - maxWidth) / 2, -ge.getY() + height - ge.getHeight() + (ge.getHeight() - maxHeight) / 2);
                }

                else {
                    layer = new PdfLayer("test", writer);
                    cb.beginLayer(layer);
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    BufferedImage bi = ((RendererRaster)geManager.getRenderer(ge.getClass())).createGEImage(ge);
                    ImageIO.write(bi, "png", baos);
                    Image image = Image.getInstance(baos.toByteArray());
                    image.setAbsolutePosition(ge.getX() + (ge.getWidth() - maxWidth) / 2, -ge.getY() + height - ge.getHeight() + (ge.getHeight() - maxHeight) / 2);
                    pdfDocument.add(image);
                }

                if(layer != null) {
                    g2dTemplate.dispose();
                    cb.endLayer();
                }

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
