package com.mapcomposer.controller;

import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.interfaces.RefreshCA;
import com.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.Document;
import com.mapcomposer.model.graphicalelement.utils.GEManager;
import com.mapcomposer.model.graphicalelement.utils.GERefresh;
import com.mapcomposer.view.ui.CompositionArea;
import com.mapcomposer.view.ui.ConfigurationShutter;
import com.mapcomposer.view.utils.CompositionJPanel;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JPanel;

/**
 * This class manager the interaction between the user, the UI and the data model.
 */
public class UIController{
    /**Unique instance of the class*/
    private static UIController INSTANCE = null;
    
    /**Map doing the link between GraphicalElements and their CompositionJPanel*/
    private static LinkedHashMap<GraphicalElement, CompositionJPanel> map;
    
    /**Selected GraphicalElement */
    private List<GraphicalElement> selectedGE;
    
    /**GraphicalElement stack giving the Z-index information*/
    private static Stack<GraphicalElement> zIndexStack;
    
    /**
     * Main constructor.
     */
    private UIController(){
        //Initialize the different attributes
        map = new LinkedHashMap<>();
        selectedGE = new ArrayList<>();
        zIndexStack = new Stack<>();
        Document d = new Document();
        map.put(d, new CompositionJPanel(d));
        CompositionArea.getInstance().setPanel(new JPanel(), d.getFormat().getPixelDimension());
        map.get(d).setPanel(GEManager.getInstance().render(d.getClass()).render(d));
    }
    
    /**
     * Returns the CompositionPanel corresponding to a GraphicalElement registered in map .
     * @param ge GraphicalElement.
     * @return The CompositionPanel corresponding to the GraphicalElement, null if it isn't registered.
     */
    public static CompositionJPanel getPanel(GraphicalElement ge){
        return map.get(ge);
    }
    
    /**
     * Returns the unique instance of the class.
     * @return The unique instance of the class.
     */
    public static UIController getInstance(){
        if(INSTANCE==null){
            INSTANCE=new UIController();
        }
        return INSTANCE;
    }
    
    public final static int toFront=2;
    public final static int front=1;
    public final static int back=-1;
    public final static int toBack=-2;
    /**
     * Change the Z-index of the displayed GraphicalElement.
     * -2 means the GE are brought to the back,
     * -1 means the GE are brought one step to the back
     * 1 means the GE are brought one step to the front
     * 2 means the GE are brought to the front.
     * @param deltaZ Change of the Z-index.
     */
    public void zindexChange(int deltaZ){
        for(GraphicalElement ge : selectedGE){
            if(zIndexStack.contains(ge)){
                Stack<GraphicalElement> temp = new Stack<>();
                switch (deltaZ){
                    case toBack:
                        temp.push(ge);
                        toBackFront(ge, temp);
                        break;
                    case toFront:
                        toBackFront(ge, temp);
                        temp.push(ge);
                        break;
                    case front:
                        if(zIndexStack.indexOf(ge)>0)
                            backFront(deltaZ, ge, temp);
                        break;
                    case back:
                        if(zIndexStack.indexOf(ge)<zIndexStack.size()-1)
                            backFront(deltaZ, ge, temp);
                        break;
                }
                while(!temp.empty()){
                    zIndexStack.push(temp.pop());
                }
                for(GraphicalElement g : zIndexStack){
                    CompositionArea.getInstance().setZIndex(map.get(g), zIndexStack.indexOf(g));
                }
            }
        }
    }
    
    /**
     * Move the GraphicalElement one step to the front or back and set its index.
     * @param deltaZ Variation of the Z-index of the GE.
     * @param ge GraphicalElement to move.
     * @param temp Temporary stack.
     */
    private void backFront(int deltaZ, GraphicalElement ge, Stack<GraphicalElement> temp){
        //Get the target index in the temporary stack (reverse order of the class stack)
        int target = zIndexStack.size()-(zIndexStack.indexOf(ge)-deltaZ);
        zIndexStack.remove(ge);
        int i=1;

        //While the object stack isn't empty, push element in the temporary stack.
        while(!zIndexStack.empty()){
            //Push the GE at the write index.
            if(i==target){
                temp.push(ge);
            }
            else
                temp.push(zIndexStack.pop());
            i++;
        }
        //If the GE has the higher index, it isn't already pushed.
        if(!temp.contains(ge)){
            temp.push(ge);
        }
    }   
    
    /**
     * Move the GraphicalElement to the front or back and set its index.
     * @param deltaZ Variation of the Z-index of the GE.
     * @param ge GraphicalElement to move.
     * @param temp Temporary stack.
     */
    private void toBackFront(GraphicalElement ge, Stack<GraphicalElement> temp){
        zIndexStack.remove(ge);
        while(!zIndexStack.empty())
            temp.push(zIndexStack.pop());
    }
    
    /**
     * Selects a GraphicalElement and redisplays the ConfigurationAttributes.
     * @param ge GraphicalElement to select.
     */
    public void selectGE(GraphicalElement ge){
        selectedGE.add(ge);
        ConfigurationShutter.getInstance().dispalyConfiguration(getCommonAttributes());
    }
    
    /**
     * Unselects a GraphicalElement and redisplays the ConfigurationAttributes.
     * @param ge GraphicalElement to select.
     */
    public void unselectGE(GraphicalElement ge){
        selectedGE.remove(ge);
        if(selectedGE.isEmpty())
            ConfigurationShutter.getInstance().eraseConfiguration();
        else
            ConfigurationShutter.getInstance().dispalyConfiguration(getCommonAttributes());
    }
    
    /**
     * Remove all the selected GraphicalElement
     */
    public void remove(){
        for(GraphicalElement ge : selectedGE){
            CompositionArea.getInstance().removeGE(map.get(ge));
            map.remove(ge);
            zIndexStack.remove(ge);
        }
        for(GraphicalElement ge : zIndexStack)
            CompositionArea.getInstance().setZIndex(map.get(ge), zIndexStack.indexOf(ge));
        selectedGE= new ArrayList<>();
        ConfigurationShutter.getInstance().eraseConfiguration();
    }

    /**
     * Read a List of ConfigurationAttribute to set the GraphicalElement.
     * This action is done when the button validate of the ConfigurationShutter is clicked. 
     * @param listCA List of ConfigurationAttributes to read.
     */
    public void validate(List<ConfigurationAttribute> listCA) {
        //Apply the function to all the selected GraphicalElements
        for(GraphicalElement ge : selectedGE){
            //Sets the ConfigurationAttribute
            for(ConfigurationAttribute ca : ge.getAllAttributes()){
                for(ConfigurationAttribute confShutterCA : listCA){
                    if(ca.getName().equals(confShutterCA.getName())){
                        if(!confShutterCA.isLocked()){
                            ca.setValue(confShutterCA.getValue());
                            if(ca instanceof RefreshCA){
                                ((RefreshCA)ca).refresh();
                            }
                        }
                        break;
                    }
                }
            }
            //ConfigurationShutter.getInstance().close();
            //Refreshes the GraphicalElement
            if(ge instanceof GERefresh){
                ((GERefresh)ge).refresh();
            }
            map.get(ge).setPanel(GEManager.getInstance().render(ge.getClass()).render(ge));
            if(ge instanceof Document)
                CompositionArea.getInstance().setPanel(GEManager.getInstance().render(ge.getClass()).render(ge), ((Document)ge).getFormat().getPixelDimension());
        }
        //Unlock all the ConfigurationAttribute
        for(GraphicalElement ge : selectedGE){
            for(ConfigurationAttribute ca : ge.getAllAttributes()){
                ca.setLock(false);
            }
        }
        selectedGE=new ArrayList<>();
    }
    
    /**
     * Returns the list of ConfigurationAttributes that all the selected GraphicalElements have in common.
     * @return List of common ConfigurationAttributes (common about the names and not values).
     */
    private List<ConfigurationAttribute> getCommonAttributes(){
        List<ConfigurationAttribute> list = selectedGE.get(0).getAllAttributes();
        List<ConfigurationAttribute> listRemove = new ArrayList<>();
        //Compare each the CA of the list to those of the GE from selectedGE
        boolean flag=false;
        for(ConfigurationAttribute caList : list){
            for(GraphicalElement ge : selectedGE){
                flag=false;
                for(ConfigurationAttribute caGE : ge.getAllAttributes()){
                    //refresh the attributes
                    if(caGE instanceof RefreshCA) ((RefreshCA)caGE).refresh();
                    
                    if(caList.isSameProperty(caGE)){
                        flag=true;
                        caList.setLock(!caList.equals(caGE));
                    }
                }
            }
            //If the CA isn't in common, it's added to a list to be removed after
            if(!flag){
                listRemove.add(caList);
            }
        }
        for(ConfigurationAttribute ca : listRemove){
            list.remove(ca);
        }
        
        return list;
    }

    /**
     * Adds a new GraphicalElement to the UIController and its JPanel to the COmpositionArea.
     * @param aClass Class of the new GraphicalElement.
     */
    public void addGE(Class<? extends GraphicalElement> aClass) {
        try {
            GraphicalElement ge = aClass.newInstance();
            //Register the GE and its CompositionJPanel.
            map.put(ge, new CompositionJPanel(ge));
            CompositionArea.getInstance().addGE(getPanel(ge));
            map.get(ge).setPanel(GEManager.getInstance().render(ge.getClass()).render(ge));
            zIndexStack.push(ge);
            CompositionArea.getInstance().setZIndex(map.get(ge), zIndexStack.size()-1);
            //Refresh the GE and redraw it.
            if(ge instanceof GERefresh){
                ((GERefresh)ge).refresh();
            }
            List<GraphicalElement> temp = selectedGE;
            selectedGE = new ArrayList<>();
            selectedGE.add(ge);
            validate(ge.getAllAttributes());
            selectedGE=temp;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(UIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public LinkedHashMap<GraphicalElement, CompositionJPanel> getGEMap(){
        return map;
    }
}
