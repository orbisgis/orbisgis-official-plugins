package org.orbisgis.mapcomposer.view.configurationattribute;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.attribute.SourceCA;
import java.awt.FlowLayout;
import javax.swing.*;
import javax.swing.event.DocumentListener;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

import org.orbisgis.sif.UIFactory;
import org.orbisgis.sif.components.OpenFilePanel;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
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
        jtf.setColumns(40);
        //"Save" the CA inside the JTextField
        jtf.getDocument().putProperty("SourceCA", sourceCA);
        //add the listener for the text changes in the JTextField
        jtf.getDocument().addDocumentListener(EventHandler.create(DocumentListener.class, this, "saveDocumentText", "document"));
        //Load the last path use in a sourceCA
        OpenFilePanel openFilePanel = new OpenFilePanel("ConfigurationAttribute.SourceCA", "Select source");
        openFilePanel.addFilter(new String[]{"*"}, "All files");
        openFilePanel.loadState();
        jtf.setText(openFilePanel.getCurrentDirectory().getAbsolutePath());

        component.add(jtf);
        //Create the button Browse
        JButton button = new JButton("Browse");
        //"Save" the sourceCA and the JTextField in the button
        button.putClientProperty("SourceCA", sourceCA);
        button.putClientProperty("JTextField", jtf);
        //Add the listener for the click on the button
        button.addActionListener(EventHandler.create(ActionListener.class, this, "openLoadPanel", ""));
        
        component.add(button);
        return component;
    }

    public void openLoadPanel(ActionEvent event){
        OpenFilePanel openFilePanel = new OpenFilePanel("ConfigurationAttribute.SourceCA", "Select source");
        openFilePanel.addFilter(new String[]{"*"}, "All files");
        openFilePanel.loadState();
        if (UIFactory.showDialog(openFilePanel, true, true)) {
            JButton source = (JButton)event.getSource();
            SourceCA sourceCA = (SourceCA)source.getClientProperty("SourceCA");
            sourceCA.setValue(openFilePanel.getSelectedFile().getAbsolutePath());
            JTextField textField = (JTextField)source.getClientProperty("JTextField");
            textField.setText(openFilePanel.getSelectedFile().getAbsolutePath());
        }
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
