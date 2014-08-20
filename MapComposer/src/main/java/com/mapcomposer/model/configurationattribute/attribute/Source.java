package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.utils.interfaces.CARefresh;
import com.mapcomposer.view.ui.ConfigurationShutter;
import java.io.File;
import javax.swing.JOptionPane;

/**
 * The Source attribute contain the path to a specified data source like OWS-Context, data, image ...
 */
public final class Source extends ConfigurationAttribute<String> implements CARefresh{

    /**
     * Main constructor.
     * @param name Name of the Source in its GraphicalElement.
     */
    public Source(String name) {
        super(name);
        this.setValue("/");
    }

    @Override
    public void refresh() {
        File f = new File(this.getValue());
        if(!f.exists()){
            JOptionPane.showMessageDialog(ConfigurationShutter.getInstance(), "Cannot load the file '"+this.getValue()+"'.");
        }
    }
    
}
