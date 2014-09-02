package com.mapcomposer.model.configurationattribute.attribute;
import com.mapcomposer.controller.UIController;
import com.mapcomposer.model.configurationattribute.ListCA;
import com.mapcomposer.model.configurationattribute.utils.interfaces.CARefresh;
import com.mapcomposer.model.graphicalelement.element.cartographic.MapImage;

/**
 * The attribute contain the link to a MapImage
 */
public class LinkToMapImage extends ListCA<MapImage> implements CARefresh{

    public LinkToMapImage(String name) {
        super(name);
        if(this.getValue().size()>0)
            this.select(this.getValue().get(0));
    }
        

    /**
     * Verify if the files path still right.
     * If the file doesn't exist, it's path is removed from the list.
     */
    @Override
    public void refresh() {
        for(MapImage mi : this.getValue()){
            if(!UIController.getInstance().getGEMap().containsKey(mi)){
                this.remove(((MapImage)mi));
            }
        }
        
        for(Object ge : UIController.getInstance().getGEMap().keySet().toArray()){
            if(ge instanceof MapImage){
                if(!this.getValue().contains((MapImage)ge)){
                    this.add(((MapImage)ge));
                }
            }
        }
        
    }
}
