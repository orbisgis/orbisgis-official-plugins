package org.orbisgis.mapcomposer.model.utils;

import org.apache.commons.io.FileUtils;
import org.junit.Assert;
import org.junit.Test;
import org.orbisgis.mapcomposer.model.configurationattribute.utils.CAManager;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import org.orbisgis.mapcomposer.model.graphicalelement.element.cartographic.Scale;
import org.orbisgis.mapcomposer.model.graphicalelement.element.illustration.Image;
import org.orbisgis.mapcomposer.model.graphicalelement.element.text.TextElement;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.utils.GEManager;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class IOControllerTest {
    @Test
    public final void saveTest(){
        SaveAndLoadHandler sh = new SaveAndLoadHandler(new GEManager(), new CAManager());
        List<GraphicalElement> listGE = new ArrayList<>();
        listGE.add(new Document());
        listGE.add(new Image());
        listGE.add(new MapImage());
        listGE.add(new Orientation());
        listGE.add(new Scale());
        listGE.add(new TextElement());

        List<GraphicalElement> list=null;

        try {
            sh.save(listGE, "target/saveTest.xml");
        } catch (IOException e) {
            Assert.fail("\nFail to save listGE. Exception get : \n"+e.getMessage());
        }

        try {
            list=sh.load("target/saveTest.xml");
        } catch (IOException|ParserConfigurationException|SAXException e) {
            Assert.fail("\nFail to load listGE. Exception get : \n"+e.toString());
        }

        for(GraphicalElement ge : listGE)
            for(GraphicalElement g : list)
                if(!ge.equals(g))
                    Assert.assertNotSame("\nSaved and loaded GraphicalElement are different : saved = "+ge.toString()+", loaded = "+g.toString(), ge, g);

    }

    @Test
    public final void loadTest(){
        SaveAndLoadHandler sh = new SaveAndLoadHandler(new GEManager(), new CAManager());
        List<GraphicalElement> listGE=null;

        try {
            listGE=sh.load(IOControllerTest.class.getResource("loadTest.xml").getPath());
        } catch (IOException|ParserConfigurationException|SAXException e) {
            Assert.fail("\nFail to load loadTest.xml. Exception get : \n"+e.toString());
        }

        try {
            sh.save(listGE, "target/loadTest1.xml");
        } catch (IOException e) {
            Assert.fail("\nFail to save listGE. Exception get : \n"+e.getMessage());
        }

        try {
            FileUtils.contentEquals(new File("target/loadTest.xml"), new File("target/loadTest1.xml"));
        } catch (IOException e) {
            Assert.assertNotSame("\nSaved and loaded file are different.", new File("target/loadTest.xml"), new File("target/loadTest1.xml"));
        }
    }
}
