package org.orbisgis.mapcomposer.model.graphicalelement.utils;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.utils.CAManager;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import org.orbisgis.mapcomposer.model.utils.LinkToOrbisGIS;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.filechooser.FileFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParserFactory;

/**
 * Class containing all the graphicalElement to save.
 */
public class SaveHandler extends DefaultHandler {

    String[] compVersions={"1.0.2"};

    private List<GraphicalElement> listGE;
    private List<Class<? extends GraphicalElement>> listClassGE;
    private List<Class<? extends ConfigurationAttribute>> listClassCA;

    private boolean inGE = false;
    private boolean inCA = false;
    private boolean inField = false;

    private GraphicalElement ge;
    private ConfigurationAttribute ca;
    private StringBuffer sb = null;

    @Override
    public void startDocument() throws SAXException {
        listGE = new ArrayList<>();
        listClassGE = GEManager.getInstance().getRegisteredGEClasses();
        listClassCA = CAManager.getInstance().getRegisteredGEClasses();
    }

    @Override
    public void startElement(String uri, String localName, String qName, Attributes attributes) throws SAXException {
        if (inGE) {
            if (inCA) {
                inField = true;
                sb = new StringBuffer();
            } else {
                for (Class<? extends ConfigurationAttribute> c : listClassCA) {
                    if (c.getName().equals(qName)) {
                        try {
                            ca = c.newInstance();
                            inCA = true;
                        } catch (InstantiationException | IllegalAccessException e) {
                            throw new SAXException(e);
                        }
                    }
                }
            }
        } else {
            for (Class<? extends GraphicalElement> c : listClassGE) {
                if (c.getName().equals(qName)) {
                    try {
                        ge = c.newInstance();
                        inGE = true;
                    } catch (InstantiationException | IllegalAccessException e) {
                        throw new SAXException(e);
                    }
                }
            }
        }
        if(qName.equals("version")){
            sb = new StringBuffer();
        }
    }

    @Override
    public void endElement(String uri, String localName, String qName) throws SAXException {
        if (inField) {
            ca.setField(qName, sb.toString().replace("\n", "").replace("\t", ""));
            sb = null;
            inField = false;
        } else if (inCA) {
            ge.setAttribute(ca);
            inCA = false;
        } else if (inGE) {
            listGE.add(ge);
            inGE = false;
        }

        // Check if the version of the save is actually compatible with the mapcomposer version.
        if(qName.equals("version")) {
            boolean flag=false;
            for (int i = 0; i < compVersions.length; i++)
                if (sb.toString().equals(compVersions[i]))
                    flag = true;
            if (!flag) {
                String message = "File version " + sb.toString() + " isn't compatible with the MapComposer version. Should be ";
                for (String s : compVersions)
                    message += s + ";";
                throw new SAXException(message);
            }
        }
    }

    @Override
    public void characters(char[] ch, int start, int length) throws SAXException {
        if (sb != null) sb.append(new String(ch, start, length));
    }

    public List<GraphicalElement> load() throws IOException, ParserConfigurationException, SAXException {
        //Open the file chooser window
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(LinkToOrbisGIS.getInstance().getViewWorkspace().getCoreWorkspace().getWorkspaceFolder()));
        fc.setApproveButtonText("Open");
        fc.setDialogTitle("Open document project");
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File file) {
                if (file.isDirectory()) return true;
                return file.getAbsolutePath().toLowerCase().contains(".xml");
            }

            @Override
            public String getDescription() {
                return "XML Files (.xml)";
            }
        });
        //If the save is validated, do the marshall
        if (fc.showOpenDialog(new JFrame()) == JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getAbsolutePath();
            SAXParserFactory.newInstance().newSAXParser().parse(new File(path), this);
        }
        return listGE;
    }

    public void save(List<GraphicalElement> list) throws IOException, NoSuchMethodException {
        //Open the file chooser window
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(LinkToOrbisGIS.getInstance().getViewWorkspace().getCoreWorkspace().getWorkspaceFolder()));
        fc.setApproveButtonText("Save");
        fc.setDialogTitle("Save document project");
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileFilter() {

            @Override public boolean accept(File file) {
                if(file.isDirectory()) return true;
                return file.getAbsolutePath().toLowerCase().contains(".xml");
            }

            @Override public String getDescription() {return "XML Files (.xml)";}
        });
        //If the save is validated, do the marshall
        if(fc.showOpenDialog(new JFrame())==JFileChooser.APPROVE_OPTION) {
            String path = fc.getSelectedFile().getAbsolutePath();
            if(!path.contains(".xml")) path+=".xml";
            FileWriter fw = new FileWriter(path);
            fw.write("<synchronized>\n\t<version>1.0.2</version>\n");
            for (GraphicalElement ge : list) {
                fw.write("\t<" + ge.getClass().getName() + ">\n");
                for(ConfigurationAttribute ca : ge.getSavableAttributes()){
                    fw.write("\t\t<"+ca.getClass().getName()+">\n");
                    Iterator<Map.Entry<String, Object>> it =  ca.getSavableField().entrySet().iterator();
                    while(it.hasNext()){
                        Map.Entry<String, Object> entry = it.next();
                        fw.write("\t\t\t<" + entry.getKey() + ">\n");
                        fw.write("\t\t\t\t"+entry.getValue().toString()+"\n");
                        fw.write("\t\t\t</" + entry.getKey() + ">\n");
                    }
                    fw.write("\t\t</"+ca.getClass().getName()+">\n");
                }
                fw.write("\t</"+ ge.getClass().getName()+">\n");
            }
            fw.write("</synchronized>");
            fw.close();
        }
    }


}