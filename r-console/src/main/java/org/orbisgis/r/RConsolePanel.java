/*
 * OrbisGIS is an open source GIS application dedicated to research for
 * all geographic information science.
 * 
 * OrbisGIS is distributed under GPL 3 license. It is developped by the "GIS"
 * team of the Lab-STICC laboratory <http://www.lab-sticc.fr/>.
 *
 * Copyright (C) 2007-2014 IRSTV (FR CNRS 2488)
 * Copyright (C) 2015-2016 Lab-STICC CNRS, UMR 6285.
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
package org.orbisgis.r;

import java.awt.BorderLayout;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.beans.EventHandler;
import java.io.IOException;
import java.util.Arrays;
import java.util.concurrent.ExecutorService;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.swing.*;
import javax.swing.event.CaretListener;
import javax.swing.event.DocumentListener;
import org.apache.commons.io.FileUtils;
import org.fife.ui.rsyntaxtextarea.RSyntaxTextArea;
import org.fife.ui.rtextarea.RTextScrollPane;
import org.orbisgis.commons.progress.SwingWorkerPM;
import org.orbisgis.r.icons.RIcon;
import org.orbisgis.sif.UIFactory;
import org.orbisgis.sif.components.OpenFilePanel;
import org.orbisgis.sif.components.SaveFilePanel;
import org.orbisgis.sif.components.actions.ActionCommands;
import org.orbisgis.sif.components.actions.DefaultAction;
import org.orbisgis.sif.components.findReplace.FindReplaceDialog;
import org.orbisgis.sif.docking.DockingPanel;
import org.orbisgis.sif.docking.DockingPanelParameters;
import org.orbisgis.sif.edition.EditableElement;
import org.orbisgis.sif.edition.EditorDockable;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.renjin.script.RenjinScriptEngineFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

/**
 * Create the R console panel
 *
 * @author Erwan Bocher
 */
@Component(service = EditorDockable.class)
public class RConsolePanel extends JPanel implements EditorDockable {

    public static final String EDITOR_NAME = "R";
    private static final I18n I18N = I18nFactory.getI18n(RConsolePanel.class);
    private static final Logger LOGGER = LoggerFactory.getLogger("gui." + RConsolePanel.class);
    private static final Logger LOGGER_POPUP = LoggerFactory.getLogger("gui.popup" + RConsolePanel.class);
    private DockingPanelParameters parameters = new DockingPanelParameters();
    private ActionCommands actions = new ActionCommands();
    private RTextScrollPane centerPanel;
    private RSyntaxTextArea scriptPanel;
    private DefaultAction executeAction;
    private DefaultAction clearAction;
    private DefaultAction saveAction;
    private DefaultAction findAction;
    private FindReplaceDialog findReplaceDialog;
    private int line = 0;
    private int character = 0;
    private static final String MESSAGEBASE = "%d | %d";
    private JLabel statusMessage = new JLabel();
    private ExecutorService executorService;


    @Activate
    public void activate(){
        setLayout(new BorderLayout());
        add(getCenterPanel(), BorderLayout.CENTER);
        add(statusMessage, BorderLayout.SOUTH);
        init();
    }

    @Reference
    public void setExecutorService(ExecutorService executorService) {
        this.executorService = executorService;
    }

    public void unsetExecutorService(ExecutorService executorService) {
        this.executorService = null;
    }

    private void execute(SwingWorker swingWorker) {
        if (executorService != null) {
            executorService.execute(swingWorker);
        } else {
            swingWorker.execute();
        }
    }

    /**
     * Init the groovy panel with all docking parameters and set the necessary
     * properties to the console shell
     */
    private void init() {
        parameters.setName(EDITOR_NAME);
        parameters.setTitle(I18N.tr("R"));
        parameters.setTitleIcon(new ImageIcon(RConsolePanel.class.getResource("icon.png")));
        parameters.setDockActions(getActions().getActions());
    }

    /**
     * Get the action manager.
     *
     * @return ActionCommands instance.
     */
    public ActionCommands getActions() {
        return actions;
    }

    /**
     * The main panel to write and execute a groovy script.
     *
     * @return
     */
    private RTextScrollPane getCenterPanel() {
        if (centerPanel == null) {
            initActions();
            scriptPanel = new RSyntaxTextArea();
            scriptPanel.setLineWrap(true);
            scriptPanel.addCaretListener(EventHandler.create(CaretListener.class, this, "onScriptPanelCaretUpdate"));
            scriptPanel.getDocument().addDocumentListener(EventHandler.create(DocumentListener.class, this, "onUserSelectionChange"));
            scriptPanel.clearParsers();
            scriptPanel.setTabsEmulated(true);
            actions.setAccelerators(scriptPanel);
            // Actions will be set on the scriptPanel PopupMenu
            scriptPanel.getPopupMenu().addSeparator();
            actions.registerContainer(scriptPanel.getPopupMenu());
            centerPanel = new RTextScrollPane(scriptPanel);
            onUserSelectionChange();

        }
        return centerPanel;
    }

    /**
     * Create actions instances
     *
     * Each action is put in the Popup menu and the tool bar Their shortcuts are
     * registered also in the editor
     */
    private void initActions() {
        //Execute action
        executeAction = new DefaultAction(RConsoleActions.A_EXECUTE, I18N.tr("Execute"),
                I18N.tr("Execute the R script"),
                RIcon.getIcon("execute"),
                EventHandler.create(ActionListener.class, this, "onExecute"),
                KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, InputEvent.CTRL_DOWN_MASK));
        actions.addAction(executeAction);

        //Clear action
        clearAction = new DefaultAction(RConsoleActions.A_CLEAR,
                I18N.tr("Clear"),
                I18N.tr("Erase the content of the editor"),
                RIcon.getIcon("erase"),
                EventHandler.create(ActionListener.class, this, "onClear"),
                null);
        actions.addAction(clearAction);

        //Open action
        actions.addAction(new DefaultAction(RConsoleActions.A_OPEN,
                I18N.tr("Open"),
                I18N.tr("Load a file in this editor"),
                RIcon.getIcon("open"),
                EventHandler.create(ActionListener.class, this, "onOpenFile"),
                KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_DOWN_MASK)));
        //Save
        saveAction = new DefaultAction(RConsoleActions.A_SAVE,
                I18N.tr("Save"),
                I18N.tr("Save the editor content into a file"),
                RIcon.getIcon("save"),
                EventHandler.create(ActionListener.class, this, "onSaveFile"),
                KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_DOWN_MASK));
        actions.addAction(saveAction);
        //Find action
        findAction = new DefaultAction(RConsoleActions.A_SEARCH,
                I18N.tr("Search.."),
                I18N.tr("Search text in the document"),
                RIcon.getIcon("find"),
                EventHandler.create(ActionListener.class, this, "openFindReplaceDialog"),
                KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_DOWN_MASK)).addStroke(KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_DOWN_MASK));
        actions.addAction(findAction);
       
    }

    /**
     * Clear the content of the console
     */
    public void onClear() {
        if (scriptPanel.getDocument().getLength() != 0) {
            int answer = JOptionPane.showConfirmDialog(this,
                    I18N.tr("Do you want to clear the contents of the console?"),
                    I18N.tr("Clear script"), JOptionPane.YES_NO_OPTION);
            if (answer == JOptionPane.YES_OPTION) {
                scriptPanel.setText("");
            }
        }
    }

    /**
     * Open a dialog that let the user select a file and save the content of the
     * R editor into this file.
     */
    public void onSaveFile() {
        final SaveFilePanel outfilePanel = new SaveFilePanel(
                "rConsoleOutFile", I18N.tr("Save script"));
        outfilePanel.addFilter("R", I18N.tr("R script (*.r)"));
        outfilePanel.loadState();
        if (UIFactory.showDialog(outfilePanel)) {
            try {
                FileUtils.writeLines(outfilePanel.getSelectedFile(), Arrays.asList(scriptPanel.getText()));
            } catch (IOException e1) {
                LOGGER.error(I18N.tr("Cannot write the script."), e1);
            }
            LOGGER_POPUP.info(I18N.tr("The file has been saved."));
        }
    }

    /**
     * Open a dialog that let the user select a file and add or replace the
     * content of the R editor.
     */
    public void onOpenFile() {
        final OpenFilePanel inFilePanel = new OpenFilePanel("rConsoleInFile",
                I18N.tr("Open script"));
        inFilePanel.addFilter("R", I18N.tr("R Script (*.R)"));
        inFilePanel.loadState();
        if (UIFactory.showDialog(inFilePanel)) {
            int answer = JOptionPane.NO_OPTION;
            if (scriptPanel.getDocument().getLength() > 0) {
                answer = JOptionPane.showConfirmDialog(
                        this,
                        I18N.tr("Do you want to clear all before loading the file ?"),
                        I18N.tr("Open file"),
                        JOptionPane.YES_NO_CANCEL_OPTION);
            }
            String text;
            try {
                text = FileUtils.readFileToString(inFilePanel.getSelectedFile());
            } catch (IOException e1) {
                LOGGER.error(I18N.tr("Cannot write the script."), e1);
                return;
            }

            if (answer == JOptionPane.YES_OPTION) {
                scriptPanel.setText(text);
            } else if (answer == JOptionPane.NO_OPTION) {
                scriptPanel.append(text);
            }
        }
    }

    /**
     * Update the row:column label
     */
    public void onScriptPanelCaretUpdate() {
        line = scriptPanel.getCaretLineNumber() + 1;
        character = scriptPanel.getCaretOffsetFromLineStart();
        statusMessage.setText(String.format(MESSAGEBASE, line, character));
    }

    /**
     * Change the status of the button when the console is empty or not.
     */
    public void onUserSelectionChange() {
        String text = scriptPanel.getText().trim();
        if (text.isEmpty()) {
            executeAction.setEnabled(false);
            clearAction.setEnabled(false);
            saveAction.setEnabled(false);
            findAction.setEnabled(false);
        } else {
            executeAction.setEnabled(true);
            clearAction.setEnabled(true);
            saveAction.setEnabled(true);
            findAction.setEnabled(true);
        }
    }

    /**
     * User click on execute script button
     */
    public void onExecute() {
        if (executeAction.isEnabled()) {
            String text = scriptPanel.getText().trim();
            RJob rJob = new RJob(text, executeAction);
            execute(rJob);
        }
    }
    

    @Override
    public DockingPanelParameters getDockingParameters() {
        return parameters;
    }

    @Override
    public JComponent getComponent() {
        return this;
    }
    
    /**
     * Open one instanceof the find replace dialog
     */
    public void openFindReplaceDialog() {
        if (findReplaceDialog == null) {
            findReplaceDialog = new FindReplaceDialog(scriptPanel, UIFactory.getMainFrame());
        }
        findReplaceDialog.setAlwaysOnTop(true);
        findReplaceDialog.setVisible(true);
    }

    @Override
    public boolean match(EditableElement editableElement) {
        return false;
    }

    @Override
    public EditableElement getEditableElement() {
        return null;
    }

    @Override
    public void setEditableElement(EditableElement editableElement) {

    }

    /**
     * Execute the provided script in R
     */
    private static class RJob extends SwingWorkerPM {

        private String script;
        private Action executeAction;

        public RJob(String script, Action executeAction) {
            this.script = script;
            this.executeAction = executeAction;
            setTaskName(I18N.tr("Execute R script"));
        }

        @Override
        protected Object doInBackground() throws Exception {
            executeAction.setEnabled(false);
            try {
                ScriptEngineManager manager = new ScriptEngineManager();
                // create a Renjin engine:
                ScriptEngine engine = manager.getEngineByName("Renjin");
                // check if the engine has loaded correctly:
                if (engine == null) {
                    engine = new RenjinScriptEngineFactory().getScriptEngine();
                    LOGGER.error(I18N.tr("Renjin Script Engine not found on the classpath."));
                }
                engine.eval(script);
            } catch (RuntimeException | ScriptException e) {
                LOGGER.error(I18N.tr("Cannot execute the script"), e);
            } finally {
                executeAction.setEnabled(true);
            }
            return null;
        }
    }
}
