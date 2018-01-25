/*
 * OrbisGIS is a GIS application dedicated to scientific spatial simulation.
 * This cross-platform GIS is developed at French IRSTV institute and is able to
 * manipulate and create vector and raster spatial information.
 *
 * OrbisGIS is distributed under GPL 3 license. It is produced by the "Atelier SIG"
 * team of the IRSTV Institute <http://www.irstv.fr/> CNRS FR 2488.
 *
 * Copyright (C) 2007-2012 IRSTV (FR CNRS 2488)
 * Copyright (C) 2015-2018 Lab-STICC CNRS, UMR 6285.
 *
 * This file is part of OrbisGIS.
 *
 * OrbisGIS is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * OrbisGIS is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * OrbisGIS. If not, see <http://www.gnu.org/licenses/>.
 *
 * For more information, please consult: <http://www.orbisgis.org/>
 * or contact directly:
 * info_at_ orbisgis.org
 */
package org.orbisgis.groovy;

import org.apache.commons.io.FileUtils;
import org.orbisgis.commons.progress.ProgressMonitor;
import org.orbisgis.sif.UIFactory;
import org.orbisgis.sif.components.SaveFilePanel;
import org.orbisgis.sif.docking.XElement;
import org.orbisgis.sif.edition.AbstractEditableElement;
import org.orbisgis.sif.edition.EditableElementException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.text.JTextComponent;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.IOException;

/**
 * Editable element dedicated to the Groovy editor
 *
 * @author Sylvain PALOMINOS
 */
public class GroovyElement extends AbstractEditableElement implements DocumentListener {

    private static final String TYPE = "GroovyElement";
    private static final String SAVE_ID = "groovyEditorOutFile";
    private static final String GROOVY_EXT = "groovy";
    private static final String GROOVY_DESC = "Groovy script (*.groovy)";
    private static final I18n I18N = I18nFactory.getI18n(GroovyElement.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(GroovyElement.class);

    public final static String PROP_DOCUMENT_PATH = "PROP_DOCUMENT_PATH";
    private File documentPath;
    private JTextComponent document;

    @Override
    public String getTypeId() {
        return TYPE;
    }

    @Override
    public void open(ProgressMonitor progressMonitor) throws UnsupportedOperationException, EditableElementException {
        if(document != null) {
            try {
                if(documentPath!= null && documentPath.isFile() && documentPath.exists()) {
                    document.setText(FileUtils.readFileToString(documentPath));
                }
                document.getDocument().addDocumentListener(this);
            } catch (IOException ex) {
                LOGGER.error(ex.getLocalizedMessage(), ex);
            }
        }
    }

    @Override
    public void save() throws UnsupportedOperationException, EditableElementException {
        if(document != null) {
            if(getDocumentPathString().isEmpty()) {
                final SaveFilePanel outfilePanel = new SaveFilePanel(
                        SAVE_ID, I18N.tr("Save script"));
                outfilePanel.addFilter(GROOVY_EXT, I18N.tr(GROOVY_DESC));
                outfilePanel.loadState();
                if (UIFactory.showDialog(outfilePanel)) {
                    setDocumentPath(outfilePanel.getSelectedFile());
                }
            }
            if(documentPath != null) {
                try {
                    FileUtils.writeStringToFile(documentPath, document.getText());
                    setModified(false);
                } catch (IOException e1) {
                    LOGGER.error(I18N.tr("IO error."), e1);
                }
            }
        }
    }

    @Override
    public void close(ProgressMonitor progressMonitor) throws UnsupportedOperationException, EditableElementException {
        if(document != null) {
            document.getDocument().removeDocumentListener(this);
        }
    }

    @Override
    public Object getObject() throws UnsupportedOperationException {
        return documentPath;
    }
    public void setDocument(JTextComponent document) {
        this.document = document;
    }

    public File getDocumentPath() {
        return documentPath;
    }

    public String getDocumentPathString() {
        if(documentPath != null) {
            return documentPath.getAbsolutePath();
        } else {
            return "";
        }
    }

    public void setDocumentPath(File documentPath) {
        File oldFile = this.documentPath;
        this.documentPath = documentPath;
        propertyChangeSupport.firePropertyChange(PROP_DOCUMENT_PATH, oldFile, documentPath);
    }

    @Override
    public void insertUpdate(DocumentEvent documentEvent) {
        setModified(true);
    }

    @Override
    public String toString() {
        if(documentPath != null) {
            return documentPath.getName();
        } else {
            return I18N.tr("Unsaved Groovy script");
        }
    }

    @Override
    public void removeUpdate(DocumentEvent documentEvent) {
        setModified(true);
    }

    @Override
    public void changedUpdate(DocumentEvent documentEvent) {
        // Ignore
    }
}
