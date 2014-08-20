package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.utils.interfaces.CARefresh;
import com.mapcomposer.model.utils.LinkToOrbisGIS;
import com.mapcomposer.view.ui.ConfigurationShutter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;
import org.orbisgis.coremap.layerModel.LayerException;
import org.orbisgis.coremap.layerModel.OwsMapContext;
import org.orbisgis.progress.NullProgressMonitor;

/**
 * ConfigurationAttribute representing a specified OwsMapContext given by OrbisGIS.
 */
public class OwsContext extends Source implements CARefresh{

    /** Instance of the OwsMapContext corresponding to the path of the Source*/
    private OwsMapContext omc;
    
    /**
     * Main constructor.
     * @param name Name of the OwsContext in its GraphicalElement.
     */
    public OwsContext(String name) {
        super(name);
        omc=new OwsMapContext(LinkToOrbisGIS.getInstance().getDataManager());
    }
    
    @Override
    public boolean setValue(String path){
        if(super.setValue(path)){
            //verification of the file
            if(path.contains(".ows")){
                try {
                    reloadOMC();
                } catch (FileNotFoundException ex) {
                    Logger.getLogger(OwsContext.class.getName()).log(Level.SEVERE, null, ex);
                    JOptionPane.showMessageDialog(ConfigurationShutter.getInstance(), "Cannot load the OWS-Context '"+this.getValue()+"'.");
                }
            }
            return true;
        }
        return false;
    }
    
    /**
     * Returns the OwsMapContext generated with it's source path.
     * @return The OwsMapContext corresponding to the source.
     */
    public OwsMapContext getOwsContext(){
        return omc;
    }

    @Override
    public void refresh() {
        try {
            reloadOMC();
        } catch (FileNotFoundException ex) {
            JOptionPane.showMessageDialog(ConfigurationShutter.getInstance(), "Cannot load the OWS-Context '"+this.getValue()+"'.");
        }
    }
    
    /**
     * Reload the OWS-Context corresponding to the value of ConfigurationAttribute. 
     * @throws FileNotFoundException 
     */
    private void reloadOMC() throws FileNotFoundException{
        try {
            if(omc.isOpen()){
                omc.close(new NullProgressMonitor());
            }
            omc.read(new FileInputStream(new File(this.getValue())));
            omc.open(new NullProgressMonitor());
        } catch (LayerException ex) {
            Logger.getLogger(OwsContext.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
