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
import com.itextpdf.text.pdf.PdfLayer;
import com.itextpdf.text.pdf.PdfTemplate;
import com.itextpdf.text.pdf.PdfWriter;
import net.miginfocom.swing.MigLayout;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.utils.GEManager;
import org.orbisgis.mapcomposer.view.graphicalelement.RendererRaster;
import org.orbisgis.mapcomposer.view.graphicalelement.RendererVector;
import org.orbisgis.mapcomposer.view.utils.MapComposerIcon;
import org.orbisgis.sif.SIFDialog;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.imageio.ImageIO;
import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
import javax.swing.JRadioButton;
import java.awt.Graphics2D;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.beans.EventHandler;
import java.io.ByteArrayOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

/**
 * This thread exports the document as a PDF file.
 * @author Sylvain PALOMINOS
 */

public class ExportPDFThread implements ExportThread {
    /** Path of the export file */
    private String path;
    /** Progress bar where the progression is shown */
    private JProgressBar progressBar;
    /** GeManager used to get the GraphicalElement rendering methods. */
    private GEManager geManager;

    private int height;

    /**Map of GraphicalElement and boolean.
     * The boolean tells if the vector rendering should be use or not (if not use the raster rendering)
     **/
    private Map<GraphicalElement, Boolean> geIsVectorMap;

    /** Stack of the GraphicalElement ordered by z index */
    private Stack<GraphicalElement> geStack;

    /** Translation*/
    private static final I18n i18n = I18nFactory.getI18n(ExportPDFThread.class);

    /** List of radio buttons for the vector export */
    private List<JRadioButton> listVectorRadio;
    /** List of radio buttons for the raster export */
    private List<JRadioButton> listRasterRadio;
    /** List of GraphicalElements names */
    private List<JLabel> names;
    /** Main radio button for vector export. If selected all the GE will be rendered as vector image */
    private JRadioButton mainVectorRadio;
    /** Main radio button for raster export. If selected all the GE will be rendered as raster image */
    private JRadioButton mainRasterRadio;
    /** Button group containing the main radio button */
    private ButtonGroup mainButtonGroup;
    /** Button expanding the selection between vector or raster export*/
    private JCheckBox expand;
    private List<Class<? extends GraphicalElement>> listGEOnlyRaster;
    private List<Class<? extends GraphicalElement>> listGEOnlyVector;

    /**
     * Main constructor
     */
    public ExportPDFThread(){
        this.geIsVectorMap = new HashMap<>();
        this.geStack = new Stack<>();
        this.listGEOnlyRaster = new ArrayList<>();
        this.listGEOnlyVector = new ArrayList<>();
    }

    @Override
    public void run() {
        try{
            Document pdfDocument = null;
            //Find the Document GE to create the BufferedImage where all the GE will be drawn
            for(GraphicalElement ge : geIsVectorMap.keySet()){
                if(ge instanceof org.orbisgis.mapcomposer.model.graphicalelement.element.Document){
                    pdfDocument = new Document(new Rectangle(ge.getWidth(), ge.getHeight()));
                    height = ge.getHeight();
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

            progressBar.setIndeterminate(true);
            progressBar.setStringPainted(true);
            progressBar.setString(i18n.tr("Exporting the document ..."));

            int geCount = 0;
            int numberOfGe[] = new int[geManager.getRegisteredGEClasses().size()];
            for(int i=0; i<numberOfGe.length; i++) {numberOfGe[i] = 0;}
            //Draw each GraphicalElement in the BufferedImage
            for(GraphicalElement ge : geStack){
                if((ge instanceof org.orbisgis.mapcomposer.model.graphicalelement.element.Document))
                    continue;

                double rad = Math.toRadians(ge.getRotation());
                double newHeight = Math.abs(sin(rad)*ge.getWidth())+Math.abs(cos(rad)*ge.getHeight());
                double newWidth = Math.abs(sin(rad)*ge.getHeight())+Math.abs(cos(rad)*ge.getWidth());

                int maxWidth = Math.max((int)newWidth, ge.getWidth());
                int maxHeight = Math.max((int)newHeight, ge.getHeight());

                String layerName = ge.getGEName() + numberOfGe[geManager.getRegisteredGEClasses().indexOf(ge.getClass())];

                if(geIsVectorMap.get(ge)) {
                    PdfTemplate pdfTemplate = cb.createTemplate(maxWidth, maxHeight);
                    Graphics2D g2dTemplate = pdfTemplate.createGraphics(maxWidth, maxHeight);
                    PdfLayer layer = new PdfLayer(layerName, writer);
                    cb.beginLayer(layer);
                    ((RendererVector)geManager.getRenderer(ge.getClass())).drawGE(g2dTemplate, ge);
                    cb.addTemplate(pdfTemplate, ge.getX() + (ge.getWidth() - maxWidth) / 2, -ge.getY() + height - ge
                            .getHeight() + (ge.getHeight() - maxHeight) / 2);
                    g2dTemplate.dispose();
                    cb.endLayer();
                }

                else {
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    BufferedImage bi = ((RendererRaster)geManager.getRenderer(ge.getClass())).createGEImage(ge, null);
                    ImageIO.write(bi, "png", baos);
                    Image image = Image.getInstance(baos.toByteArray());
                    image.setAbsolutePosition(ge.getX() + (ge.getWidth() - maxWidth) / 2, -ge.getY() + height - ge.getHeight() + (ge.getHeight() - maxHeight) / 2);

                    PdfTemplate pdfTemplate = cb.createTemplate(maxWidth, maxHeight);
                    Graphics2D g2dTemplate = pdfTemplate.createGraphics(maxWidth, maxHeight);
                    PdfLayer layer = new PdfLayer(layerName, writer);
                    cb.beginLayer(layer);
                    g2dTemplate.drawImage(bi, 0, 0, null);
                    cb.addTemplate(pdfTemplate, ge.getX() + (ge.getWidth() - maxWidth) / 2, -ge.getY() + height - ge.getHeight() + (ge.getHeight() - maxHeight) / 2);
                    g2dTemplate.dispose();
                    cb.endLayer();
                }
                numberOfGe[geManager.getRegisteredGEClasses().indexOf(ge.getClass())] ++;

                progressBar.setIndeterminate(false);
                progressBar.setValue((geCount * 100) / geIsVectorMap.keySet().size());
                progressBar.revalidate();
                geCount ++;
            }

            pdfDocument.close();
            //Wait a bit before erasing the progress bar
            progressBar.setValue(progressBar.getMaximum());
            progressBar.setString(i18n.tr("Document successfully exported."));
            try {
                Thread.sleep(1500);
            } catch (InterruptedException e) {
                LoggerFactory.getLogger(ExportPDFThread.class).error(e.getMessage());
            }
            progressBar.setValue(0);
            progressBar.setStringPainted(false);

        } catch (IllegalArgumentException|IOException|DocumentException ex) {
            LoggerFactory.getLogger(ExportPDFThread.class).error(ex.getMessage());
        }
    }

    @Override
    public void addData(GraphicalElement ge, Boolean isVector) {
        if(geIsVectorMap.containsKey(ge)) {
            geIsVectorMap.remove(ge);
        }
        geIsVectorMap.put(ge, isVector);
    }

    @Override
    public void setPath(String path) {
        this.path = path;
    }

    @Override
    public void setGEManager(GEManager geManager) {
        this.geManager = geManager;
    }

    @Override
    public void setProgressBar(JProgressBar progressBar) {
        if(progressBar != null) {
            this.progressBar = progressBar;
        }
        else {
            this.progressBar = new JProgressBar();
        }
    }

    @Override
    public JComponent constructExportPanel(Stack<GraphicalElement> StackGEToExport) {
        listVectorRadio = new ArrayList<>();
        listRasterRadio = new ArrayList<>();
        names = new ArrayList<>();

        for(GraphicalElement ge : StackGEToExport){
            addData(ge, false);
        }
        geStack = StackGEToExport;

        JPanel panelRasterVector = new JPanel(new MigLayout("hidemode 3"));
        panelRasterVector.setBorder(BorderFactory.createTitledBorder("Vector/Raster"));
        JPanel panelPDF = new JPanel(new MigLayout());
        panelPDF.add(panelRasterVector);

        //Construct the raster/vector panel
        //Adds the expand button and the main radio button
        mainVectorRadio = new JRadioButton();
        JLabel vectorLabel = new JLabel(i18n.tr("vector"));
        mainRasterRadio = new JRadioButton();
        JLabel rasterLabel = new JLabel(i18n.tr("raster"));
        expand = new JCheckBox();
        expand.addActionListener(EventHandler.create(ActionListener.class, this, "hideShowRadio"));
        expand.setSelectedIcon(MapComposerIcon.getIcon("go-down"));
        expand.setIcon(MapComposerIcon.getIcon("go-next"));
        panelRasterVector.add(expand, "cell 0 0 1 1");
        panelRasterVector.add(mainVectorRadio, "cell 1 0 1 1");
        panelRasterVector.add(vectorLabel, "cell 2 0 1 1");
        panelRasterVector.add(mainRasterRadio, "cell 3 0 1 1");
        panelRasterVector.add(rasterLabel, "cell 4 0 1 1");
        mainButtonGroup = new ButtonGroup();
        mainButtonGroup.add(mainVectorRadio);
        mainButtonGroup.add(mainRasterRadio);
        mainVectorRadio.addActionListener(EventHandler.create(ActionListener.class, this, "selectAll", "source"));
        mainRasterRadio.addActionListener(EventHandler.create(ActionListener.class, this, "selectAll", "source"));

        //Adds a line of radio button for each registered classes of GraphicalElement
        int i=1;
        for(Class geClass : geManager.getRegisteredGEClasses()){
            JLabel geName;
            try {
                geName = new JLabel(((GraphicalElement)geClass.newInstance()).getGEName());
            } catch (InstantiationException|IllegalAccessException e) {
                geName = new JLabel(geClass.getName());
            }
            geName.setVisible(false);
            JRadioButton vectorButton = new JRadioButton();
            vectorButton.setVisible(false);
            JRadioButton rasterButton = new JRadioButton();
            rasterButton.setVisible(false);
            panelRasterVector.add(geName, "cell 0 " + i + " 1 1");
            if(geManager.getRenderer(geClass) instanceof RendererVector) {
                panelRasterVector.add(vectorButton, "cell 1 " + i + " 1 1");
            }
            else{
                vectorButton.setEnabled(false);
                rasterButton.setEnabled(false);
                rasterButton.setSelected(true);

                if(!listGEOnlyRaster.contains(geClass)){
                    listGEOnlyRaster.add(geClass);
                }
            }
            if(geManager.getRenderer(geClass) instanceof RendererRaster) {
                panelRasterVector.add(rasterButton, "cell 3 " + i + " 1 1");
            }
            else{
                rasterButton.setEnabled(false);
                vectorButton.setEnabled(false);
                vectorButton.setSelected(true);

                if(!listGEOnlyVector.contains(geClass)){
                    listGEOnlyVector.add(geClass);
                }
            }
            ButtonGroup buttonGroup = new ButtonGroup();
            buttonGroup.add(vectorButton);
            buttonGroup.add(rasterButton);
            names.add(geName);
            listVectorRadio.add(vectorButton);
            listRasterRadio.add(rasterButton);
            vectorButton.addActionListener(EventHandler.create(ActionListener.class, this, "updateMainRadio", "source"));
            rasterButton.addActionListener(EventHandler.create(ActionListener.class, this, "updateMainRadio", "source"));
            i++;
        }

        return panelPDF;
    }

    @Override
    public String getName() {
        return i18n.tr("PDF");
    }

    @Override
    public String getDescription() {
        return i18n.tr("Export the document as a PDF file.");
    }

    /**
     * Select all the radio button corresponding of the given main radio button in argument
     * @param mainRadioButton Main radio button.
     */
    public void selectAll(Object mainRadioButton){
        List<GraphicalElement> listGE = new ArrayList<>();
        for(GraphicalElement ge : geIsVectorMap.keySet()){
            listGE.add(ge);
        }
        if(mainRadioButton == mainVectorRadio){
            for(JRadioButton button : listVectorRadio){
                if(listRasterRadio.get(listVectorRadio.indexOf(button)).isEnabled()) {
                    button.setSelected(true);
                }
            }
            mainVectorRadio.setSelected(true);
            for(GraphicalElement ge : listGE){
                if(!listGEOnlyRaster.contains(ge.getClass())) {
                    this.addData(ge, true);
                }
            }
        }
        if(mainRadioButton == mainRasterRadio){
            for(JRadioButton button : listRasterRadio){
                if(listVectorRadio.get(listRasterRadio.indexOf(button)).isEnabled()) {
                    button.setSelected(true);
                }
            }
            mainRasterRadio.setSelected(true);
            for(GraphicalElement ge : listGE){
                if(!listGEOnlyVector.contains(ge.getClass())) {
                    this.addData(ge, false);
                }
            }
        }
    }

    /**
     * Updates the main radio button according to the state of all the others radio button.
     * @param object Radio button.
     */
    public void updateMainRadio(Object object){
        JRadioButton radioButton = (JRadioButton)object;
        List<GraphicalElement> listGE = new ArrayList<>();
        for(GraphicalElement ge : geIsVectorMap.keySet()){
            listGE.add(ge);
        }
        boolean rasterOnly = true;
        boolean vectorOnly = true;
        if(listVectorRadio.contains(radioButton)){
            rasterOnly = false;
            for(JRadioButton button : listVectorRadio) {
                if(!button.isSelected() && button.isEnabled()) {
                    vectorOnly = false;
                }
            }
            Class geClass = geManager.getRegisteredGEClasses().get(listVectorRadio.indexOf(radioButton));
            for(GraphicalElement ge : listGE){
                if(geClass.isInstance(ge)){
                    this.addData(ge, radioButton.isSelected());
                }
            }
        }
        else if(listRasterRadio.contains(radioButton)){
            vectorOnly = false;
            for(JRadioButton button : listRasterRadio) {
                if(!button.isSelected() && button.isEnabled()) {
                    rasterOnly = false;
                }
            }
            Class geClass = geManager.getRegisteredGEClasses().get(listRasterRadio.indexOf(radioButton));
            for(GraphicalElement ge : listGE){
                if(geClass.isInstance(ge)){
                    this.addData(ge, !radioButton.isSelected());
                }
            }
        }

        if(rasterOnly){
            mainRasterRadio.setSelected(true);
        }
        else if(vectorOnly){
            mainVectorRadio.setSelected(true);
        }
        else {
            mainButtonGroup.clearSelection();
        }
    }

    /**
     * Hides or shows the radio button for each GraphicalElement.
     */
    public void hideShowRadio(){
        boolean show = expand.isSelected();
        for(JComponent c : listRasterRadio){
            c.setVisible(show);
        }
        for(JComponent c : listVectorRadio){
            c.setVisible(show);
        }
        for(JComponent c : names){
            c.setVisible(show);
        }
        ((SIFDialog)expand.getTopLevelAncestor()).pack();
    }

    @Override
    public Map<String, String> getFileFilters() {
        Map<String, String> ret = new HashMap<>();
        ret.put("pdf", i18n.tr("PDF file"));
        return ret;
    }
}
