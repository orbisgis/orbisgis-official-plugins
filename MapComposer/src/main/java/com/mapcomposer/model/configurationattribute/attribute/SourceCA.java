package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.interfaces.RefreshCA;
import com.mapcomposer.view.ui.ConfigurationShutter;
import java.io.File;
import javax.swing.JOptionPane;

/**
 * The Source attribute contain the path to a specified data source like OWS-Context, data, image ...
 */
public final class SourceCA extends BaseCA<String> implements RefreshCA{
    /** Property itself */
    private String value;
    
    public SourceCA(){}
    
    @Override public void setValue(String value) {this.value=value;}

    @Override public String getValue() {return value;}

    @Override public boolean isSameValue(ConfigurationAttribute ca) {
        return ca.getValue().equals(value);
    }

    @Override
    public void refresh() {
        File f = new File(this.getValue());
        if(!f.exists()){
           System.out.println("Cannot load the file '"+this.getValue()+"'.");
        }
    }
}
