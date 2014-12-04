package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.attribute.StringCA;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceCA;
import java.awt.FlowLayout;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.orbisgis.mapcomposer.view.utils.MouseListenerBrowse;
import java.awt.Component;
import java.beans.EventHandler;

/**
 * Renderer associated to the Source ConfigurationAttribute.
 * The JPanel returned by the createJComponentFromCA method look like :
 *  _____________________________________________________________________________________
 * |                                  ____________________________        _____________  |
 * | NameOfTheConfigurationAttribute | text field with the path   |      |Button browse| |
 * |                                 |____________________________|      |_____________| |
 * |_____________________________________________________________________________________|
 *
 * A button open a JFileChooser to permit to the user to find the source file.
 * @see org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceCA
 */
public class SourceRenderer implements CARenderer{

    @Override
    public JPanel createJComponentFromCA(ConfigurationAttribute ca) {
    //Create the panel
        JPanel panel = new JPanel();
        panel.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the panel all the swing components
        final SourceCA sourceCA = (SourceCA)ca;
        
        panel.add(new JLabel(sourceCA.getName()));
        //Display the SourceCA into a JTextField
        JTextField jtf = new JTextField(sourceCA.getValue());
        //"Save" the CA inside the JTextField
        jtf.getDocument().putProperty("SourceCA", sourceCA);
        jtf.getDocument().addDocumentListener(EventHandler.create(DocumentListener.class, this, "saveDocumentText", "document"));
        panel.add(jtf);
        JButton button = new JButton("Browse");
        button.addMouseListener(new MouseListenerBrowse(jtf));
        
        panel.add(button);
        return panel;
    }

    /**
     * Save the text contained by the Document in the ConfigurationAttribute set as property.
     * @param document
     */
    public void saveDocumentText(Document document){
        try {
            ((StringCA)document.getProperty("StringCA")).setValue(document.getText(0, document.getLength()));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
