package org.orbisgis.mailto;

import org.orbisgis.coreapi.workspace.CoreWorkspace;
import org.orbisgis.viewapi.components.actions.DefaultAction;
import org.orbisgis.viewapi.sqlconsole.ui.ext.SQLAction;
import org.orbisgis.viewapi.sqlconsole.ui.ext.SQLConsoleEditor;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xnap.commons.i18n.I18n;
import org.xnap.commons.i18n.I18nFactory;

import javax.swing.Action;
import javax.swing.ImageIcon;
import javax.swing.JTextArea;
import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.IOException;
import java.net.URI;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.List;

/**
 * Add mail to menu in SQL Console
 * @author Nicolas Fortin
 */
@Component
public class MailToExt implements SQLAction {
    private static final I18n I18N = I18nFactory.getI18n(MailToExt.class);
    private static final Logger LOGGER = LoggerFactory.getLogger(MailToExt.class);
    public static final String A_MAILTO = "A_MAILTO";Syntax highlighting
    private static final String DEFAULT_SUBJECT = "SQL Script from OrbisGIS "
            + CoreWorkspace.MAJOR_VERSION + "." + CoreWorkspace.MINOR_VERSION + "." + CoreWorkspace.REVISION_VERSION;

    @Override
    public List<Action> createActions(SQLConsoleEditor sqlConsoleEditor) {
        return Arrays.asList(new Action[]{new MailToAction(sqlConsoleEditor)});
    }

    @Override
    public void disposeActions(SQLConsoleEditor sqlConsoleEditor, List<Action> actions) {

    }

    private static class MailToAction extends DefaultAction {
        private SQLConsoleEditor editor;

        private MailToAction(SQLConsoleEditor editor) {
            super(A_MAILTO, I18N.tr("Mail to"),new ImageIcon(MailToExt.class.getResource("mailto_menu.png")));
            this.editor = editor;
            setAfter(A_SAVE);
            setLogicalGroup("custom");
            setToolTipText(I18N.tr("Send the text through email"));
        }

        private static String enc(String str) throws IOException {
            return URLEncoder.encode(str, "UTF-8").replace("+", "%20");
        }

        @Override
        public void actionPerformed(ActionEvent ae) {
            if(Desktop.isDesktopSupported()) {
                try {
                    Desktop desktop = Desktop.getDesktop();
                    JTextArea textArea = editor.getTextArea();
                    StringBuilder sb = new StringBuilder("mailto:?subject=");
                    sb.append(enc(DEFAULT_SUBJECT));
                    sb.append("&body=");
                    if(textArea.getSelectionEnd() - textArea.getSelectionStart() > 1) {
                        // Export selection
                        sb.append(enc(textArea.getSelectedText()));
                    } else {
                        // Export all content
                        sb.append(enc(textArea.getText()));
                    }
                    desktop.mail(URI.create(sb.toString()));
                } catch (IOException ex) {
                    LOGGER.error(ex.getLocalizedMessage());
                }
            }
        }
    }
}
