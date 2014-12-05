package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceCA;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.orbisgis.mapcomposer.view.utils.MouseListenerBrowse;
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
    public JComponent createJComponentFromCA(ConfigurationAttribute ca) {
    //Create the component
        JComponent component = new JPanel();
        component.setLayout(new FlowLayout(FlowLayout.LEFT));

    //Add to the component all the swing components
        final SourceCA sourceCA = (SourceCA)ca;
        
        component.add(new JLabel(sourceCA.getName()));
        //Display the SourceCA into a JTextField
        JTextField jtf = new JTextField(sourceCA.getValue());
        //"Save" the CA inside the JTextField
        jtf.getDocument().putProperty("SourceCA", sourceCA);
        jtf.getDocument().addDocumentListener(EventHandler.create(DocumentListener.class, this, "saveDocumentText", "document"));
        component.add(jtf);
        JButton button = new JButton("Browse");
        button.addMouseListener(new MouseListenerBrowse(jtf));
        
        component.add(button);
        return component;
    }

    /**
     * Save the text contained by the Document in the ConfigurationAttribute set as property.
     * @param document
     */
    public void saveDocumentText(Document document){
        try {
            ((SourceCA)document.getProperty("SourceCA")).setValue(document.getText(0, document.getLength()));
        } catch (BadLocationException e) {
            e.printStackTrace();
        }
    }
}
