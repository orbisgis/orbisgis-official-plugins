package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.utils.interfaces.CARefresh;
import com.mapcomposer.view.ui.MainWindow;
import java.io.File;

/**
 * The Source attribute contain the path to a specified data source like OWS-Context, data, image ...
 */
public class Source extends ConfigurationAttribute<String> implements CARefresh{

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
            //TODO implement the showAlert function.
            //MainWindow.getInstance().showAlert("The file ''"+this.getName()+" : "this.getValue()+"'' doesn't exists anymore.\n The value will be reseted");
        }
    }
    
}
