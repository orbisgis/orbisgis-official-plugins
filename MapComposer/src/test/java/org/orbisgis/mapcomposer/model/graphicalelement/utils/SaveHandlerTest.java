package org.orbisgis.mapcomposer.model.graphicalelement.utils;

import org.junit.Assert;
import org.junit.Test;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class SaveHandlerTest {

    @Test
    public final void testSaveNLoad(){
        SaveHandler sh = new SaveHandler();
        List<GraphicalElement> listGE = new ArrayList<GraphicalElement>();
        listGE.add(GEFactory.createDocument());
        listGE.add(GEFactory.createImage());
        listGE.add(GEFactory.createMapImage());
        listGE.add(GEFactory.createOrientation());
        listGE.add(GEFactory.createScale());
        listGE.add(GEFactory.createTextElement());

        List<GraphicalElement> list=null;

        try {
            sh.save(listGE, "target/test.xml");
        } catch (IOException e) {
            Assert.fail("\nFail to save listGE. Exception get : \n"+e.getMessage());
        }

        try {
            list=sh.load("target/test.xml");
        } catch (IOException|ParserConfigurationException|SAXException e) {
            Assert.fail("\nFail to load listGE. Exception get : \n"+e.toString());
        }

        for(GraphicalElement ge : listGE)
            for(GraphicalElement g : list)
                if(!ge.equals(g))
                    Assert.assertNotSame("\nSaved and loaded GraphicalElement are differents : saved = "+ge.toString()+", loaded = "+g.toString(), ge, g);

    }
}
