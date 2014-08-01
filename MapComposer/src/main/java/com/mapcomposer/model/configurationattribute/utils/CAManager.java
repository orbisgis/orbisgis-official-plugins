package com.mapcomposer.model.configurationattribute.utils;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Choice;
import com.mapcomposer.model.configurationattribute.attribute.ColorCA;
import com.mapcomposer.model.configurationattribute.attribute.FileList;
import com.mapcomposer.model.configurationattribute.attribute.Source;
import com.mapcomposer.model.configurationattribute.attribute.Text;
import com.mapcomposer.model.configurationattribute.attribute.Numeric;
import com.mapcomposer.model.configurationattribute.attribute.OwsContext;
import com.mapcomposer.view.configurationattribute.CARenderer;
import com.mapcomposer.view.configurationattribute.ChoiceRenderer;
import com.mapcomposer.view.configurationattribute.ColorRenderer;
import com.mapcomposer.view.configurationattribute.FileListRenderer;
import com.mapcomposer.view.configurationattribute.NumericRenderer;
import com.mapcomposer.view.configurationattribute.OwsContextRenderer;
import com.mapcomposer.view.configurationattribute.SourceRenderer;
import com.mapcomposer.view.configurationattribute.TextRenderer;
import java.util.HashMap;
import java.util.Map;

/**
* The class manages the link between the ConfigurationAttribute (CA) and their Renderer.
* When a CA need to be displayed, the Renderer will be get via this class.
* So to be used, both should be registered in the CAManager.
*/
public class CAManager {
    /** Instance of the manager */
    private static CAManager INSTANCE;
    /** HashMap linking the CA to its Renderer */
    private static Map<Class<? extends ConfigurationAttribute>, CARenderer> map;
    
    /**
* Private void constructor.
*/
    private CAManager(){
        map = new HashMap<>();
        //Adding the original CA and their Renderer
        map.put(Numeric.class, new NumericRenderer());
        map.put(Choice.class, new ChoiceRenderer());
        map.put(FileList.class, new FileListRenderer());
        map.put(Source.class, new SourceRenderer());
        map.put(Text.class, new TextRenderer());
        map.put(OwsContext.class, new OwsContextRenderer());
        map.put(ColorCA.class, new ColorRenderer());
    }
    
    /**
* Static method giving the unique instance of the class.
* @return The unique instance of the class.
*/
    public static synchronized CAManager getInstance(){
        if(INSTANCE==null){
            INSTANCE = new CAManager();
        }
        return INSTANCE;
    }
    
    /**
* Register in the Map a CA and it's Renderer. This step is essential because a CA
* of a graphical element can't be displayed if it isn't added with its Renderer this way.
* @param caClass Class of the CA.
* @param rendererClass Renderer class associated with the previous class.
* @return True if the values are successfully added, false otherwise.
*/
    public boolean registerCA(Class<? extends ConfigurationAttribute> caClass , CARenderer rendererClass){
        if(caClass != null && rendererClass != null){
            map.put(caClass, rendererClass);
            return true;
        }else{
            return false;
        }
    }
    
    /**
* Give back the Renderer corresponding to the CA given as parameter.
* @param ca ConfigurationAttribute to render.
* @return The Renderer of the ConfigurationAttribute.
*/
    public CARenderer getRenderer(ConfigurationAttribute ca){
        return map.get(ca.getClass());
    }
}