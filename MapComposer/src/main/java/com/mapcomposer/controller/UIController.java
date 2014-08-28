package com.mapcomposer.controller;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.utils.interfaces.CARefresh;
import com.mapcomposer.model.graphicalelement.GraphicalElement;
import com.mapcomposer.model.graphicalelement.element.cartographic.Key;
import com.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import com.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import com.mapcomposer.model.graphicalelement.element.cartographic.Scale;
import com.mapcomposer.model.graphicalelement.element.illustration.Image;
import com.mapcomposer.model.graphicalelement.element.text.TextElement;
import com.mapcomposer.model.graphicalelement.utils.GEManager;
import com.mapcomposer.model.graphicalelement.utils.GERefresh;
import com.mapcomposer.view.ui.CompositionArea;
import com.mapcomposer.view.ui.ConfigurationShutter;
import com.mapcomposer.view.utils.CompositionJPanel;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Stack;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * This class manager the interaction between the user, the UI and the data model.
 */
public class UIController{
    /**Unique instance of the class*/
    private static UIController INSTANCE = null;
    /**Map doing the link between GraphicalElements and their CompositionJPanel*/
    private static LinkedHashMap<GraphicalElement, CompositionJPanel> map;
    /**Selected GraphicalElement */
    private static List<GraphicalElement> listGE;
    /**GraphicalElement stack giving the Z-index information*/
    private static Stack<GraphicalElement> stack;
    
    /**
     * Main constructor.
     */
    private UIController(){
        map = new LinkedHashMap<>();
        listGE = new ArrayList<>();
        stack = new Stack<>();
        //as example
        // map example
        MapImage mi = new MapImage();
        mi.setHeight(400);
        mi.setWidth(400);
        mi.setX(20);
        mi.setY(120);
        mi.setRotation(0);
        mi.setOwsContext("/home/sylvain/OrbisGIS/maps/MyMap.ows");
        map.put(mi, new CompositionJPanel(mi));
        CompositionArea.getInstance().addGE(getPanel(mi));
        map.get(mi).setPanel(GEManager.getInstance().render(mi.getClass()).render(mi));
        stack.push(mi);
        CompositionArea.getInstance().setComponentZOrder(map.get(mi), 0);
        // scale example
        Scale s = new Scale();
        s.setHeight(20);
        s.setWidth(400);
        s.setX(20);
        s.setY(520);
        s.setRotation(0);
        s.setOwsContext("/home/sylvain/OrbisGIS/maps/MyMap.ows");
        map.put(s, new CompositionJPanel(s));
        CompositionArea.getInstance().addGE(getPanel(s));
        map.get(s).setPanel(GEManager.getInstance().render(s.getClass()).render(s));
        stack.push(s);
        CompositionArea.getInstance().setComponentZOrder(map.get(s), 1);
        // orientation example
        Orientation o = new Orientation();
        o.setHeight(50);
        o.setWidth(50);
        o.setX(420);
        o.setY(120);
        o.setRotation(0);
        o.setOwsContext("/home/sylvain/OrbisGIS/maps/MyMap.ows");
        o.setIconPath("/home/sylvain/OrbisGIS/maps/arrow.png");
        map.put(o, new CompositionJPanel(o));
        CompositionArea.getInstance().addGE(getPanel(o));
        map.get(o).setPanel(GEManager.getInstance().render(o.getClass()).render(o));
        stack.push(o);
        CompositionArea.getInstance().setComponentZOrder(map.get(o), 2);
        // Key example
        Key k = new Key();
        k.setHeight(200);
        k.setWidth(80);
        k.setX(430);
        k.setY(320);
        k.setRotation(0);
        k.setOwsContext("/home/sylvain/OrbisGIS/maps/MyMap.ows");
        map.put(k, new CompositionJPanel(k));
        CompositionArea.getInstance().addGE(getPanel(k));
        map.get(k).setPanel(GEManager.getInstance().render(k.getClass()).render(k));
        stack.push(k);
        CompositionArea.getInstance().setComponentZOrder(map.get(k), 3);
        // scale example
        TextElement t = new TextElement();
        t.setHeight(20);
        t.setWidth(200);
        t.setX(20);
        t.setY(100);
        t.setRotation(0);
        t.setText("Map Composer");
        t.setFontSize(8);
        map.put(t, new CompositionJPanel(t));
        CompositionArea.getInstance().addGE(getPanel(t));
        map.get(t).setPanel(GEManager.getInstance().render(t.getClass()).render(t));
        stack.push(t);
        CompositionArea.getInstance().setComponentZOrder(map.get(t), 4);
        // Image example
        Image i = new Image();
        i.setHeight(200);
        i.setWidth(400);
        i.setX(470);
        i.setY(20);
        i.setRotation(0);
        i.setPath("/home/sylvain/OrbisGIS/maps/logo.png");
        map.put(i, new CompositionJPanel(i));
        CompositionArea.getInstance().addGE(getPanel(i));
        map.get(i).setPanel(GEManager.getInstance().render(i.getClass()).render(i));
        stack.push(i);
        CompositionArea.getInstance().setComponentZOrder(map.get(i), 5);
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
    
    /**
     * Change the Z-index of the displayed GraphicalElement.
     * @param deltaZ Change of the Z-index.
     */
    public void zindexChange(int deltaZ){
        for(GraphicalElement ge : listGE){
            if(stack.contains(ge)){
                Stack<GraphicalElement> temp = new Stack<>();
                if(deltaZ==-2){
                    temp.push(ge);
                    while(!stack.empty()){
                        if(stack.peek()!=ge)
                            temp.push(stack.pop());
                        else
                            stack.pop();
                    }
                    while(!temp.empty()){
                        stack.push(temp.pop());
                    }
                }
                else if(deltaZ==2){
                    while(!stack.empty()){
                        if(stack.peek()!=ge)
                            temp.push(stack.pop());
                        else
                            stack.pop();
                    }
                    temp.add(ge);
                    while(!temp.empty()){
                        stack.push(temp.pop());
                    }
                }
                else if(deltaZ==1){
                    if(stack.indexOf(ge)>0){
                        int target = stack.indexOf(ge)-1;
                        stack.remove(ge);
                        int i=0;
                        
                        while(!stack.empty()){
                            temp.push(stack.pop());
                        }
                        
                        while(!temp.empty()){
                            if(i==target)
                                stack.push(ge);
                            if(temp.peek()!=ge){
                                stack.push(temp.pop());
                            }
                            i++;
                        }
                    }
                }
                else if(deltaZ==-1){
                    if(stack.indexOf(ge)<stack.size()-1){
                        int target = stack.indexOf(ge)+1;
                        stack.remove(ge);
                        int i=0;
                        
                        while(!stack.empty()){
                            temp.push(stack.pop());
                        }
                        
                        while(!temp.empty()){
                            if(i==target){
                                stack.push(ge);
                            }
                            if(temp.peek()!=ge){
                                stack.push(temp.pop());
                            }
                            i++;
                        }
                        if(target==i){
                            stack.push(ge);
                        }
                    }
                }
                actuZIndex();
            }
        }
    }
    
    private void actuZIndex(){
        Stack<GraphicalElement> temp = new Stack<>();
        while(!stack.empty()){
            temp.push(stack.pop());
        }
        int i=0;
        while(!temp.empty()){
            CompositionArea.getInstance().setComponentZOrder(map.get(temp.peek()), i);
            stack.push(temp.pop());
            i++;
        }
    }
    
    /**
     * Selects a GraphicalElement and redisplays the ConfigurationAttributes.
     * @param ge GraphicalElement to select.
     */
    public void selectGE(GraphicalElement ge){
        UIController.listGE.add(ge);
        ConfigurationShutter.getInstance().dispalyConfiguration(getCommonAttributes());
    }
    
    /**
     * Unselects a GraphicalElement and redisplays the ConfigurationAttributes.
     * @param ge GraphicalElement to select.
     */
    public void unselectGE(GraphicalElement ge){
        UIController.listGE.remove(ge);
        if(listGE.isEmpty())
            ConfigurationShutter.getInstance().eraseConfiguration();
        else
            ConfigurationShutter.getInstance().dispalyConfiguration(getCommonAttributes());
    }
    
    /**
     * Remove all the selecte GraphicalElement
     */
    public void remove(){
        for(GraphicalElement ge : listGE){
            CompositionArea.getInstance().removeGE(map.get(ge));
        }
        listGE= new ArrayList<>();
    }

    /**
     * Read a List of ConfigurationAttribute to set the GraphicalElement.
     * This action is done when the button validate of the ConfigurationShutter is clicked. 
     * @param listCA List of ConfigurationAttributes to read.
     */
    public void validate(List<ConfigurationAttribute> listCA) {
        //Apply the function to all the selected GraphicalElements
        for(GraphicalElement ge : listGE){
            //Sets the ConfigurationAttribute
            for(ConfigurationAttribute ca : ge.getAllAttributes()){
                for(ConfigurationAttribute confShutterCA : listCA){
                    if(ca.getName().equals(confShutterCA.getName())){
                        if(!confShutterCA.isLocked()){
                            ca.setValue(confShutterCA.getValue());
                            if(ca instanceof CARefresh){
                                ((CARefresh)ca).refresh();
                            }
                        }
                        break;
                    }
                }
            }
            ConfigurationShutter.getInstance().close();
            //Refreshes the GraphicalElement
            if(ge instanceof GERefresh){
                ((GERefresh)ge).refresh();
            }
            map.get(ge).setPanel(GEManager.getInstance().render(ge.getClass()).render(ge));
        }
        //Unlock all the ConfigurationAttribute
        for(GraphicalElement ge : listGE){
            for(ConfigurationAttribute ca : ge.getAllAttributes()){
                ca.unlock();
            }
        }
        listGE=new ArrayList<>();
    }
    
    /**
     * Returns the list of ConfigurationAttributes that all the selected GraphicalElements have in common.
     * @return List of common ConfigurationAttributes.
     */
    private List<ConfigurationAttribute> getCommonAttributes(){
        List<ConfigurationAttribute> list = new ArrayList<>();
        try {
            Class<? extends GraphicalElement> c = listGE.get(0).getClass();
            for (GraphicalElement listGE1 : listGE) {
                c = listGE1.getCommonClass(c);
            }
            list = c.getConstructor(c).newInstance(listGE.get(0)).getAllAttributes();
            
            //Find if attributes are in common or not.
            //If not, the CA is locked
            for(GraphicalElement ge : listGE){
                List<ConfigurationAttribute> listCA =  c.getConstructor(c).newInstance(ge).getAllAttributes();
                for(int i=0; i<list.size(); i++){
                    if(list.get(i).getValue()!=listCA.get(i).getValue()){
                        list.get(i).lock();
                    }
                }
            }
        } catch (NoSuchMethodException | SecurityException | InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(UIController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return list;
    }

    public void addGE(Class<? extends GraphicalElement> aClass) {
        try {
            GraphicalElement ge = aClass.newInstance();
            map.put(ge, new CompositionJPanel(ge));
            CompositionArea.getInstance().addGE(getPanel(ge));
            map.get(ge).setPanel(GEManager.getInstance().render(ge.getClass()).render(ge));
            stack.push(ge);
            CompositionArea.getInstance().setComponentZOrder(map.get(ge), stack.size()-1);
            if(ge instanceof GERefresh){
                ((GERefresh)ge).refresh();
            }
            List<GraphicalElement> temp = listGE;
            listGE = new ArrayList<>();
            listGE.add(ge);
            validate(ge.getAllAttributes());
            listGE=temp;
        } catch (InstantiationException | IllegalAccessException ex) {
            Logger.getLogger(UIController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
