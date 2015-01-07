package org.orbisgis.mapcomposer.controller;

import static java.lang.Math.cos;
import static java.lang.Math.sin;

import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import org.orbisgis.mapcomposer.model.configurationattribute.interfaces.RefreshCA;
import org.orbisgis.mapcomposer.model.configurationattribute.utils.CAManager;
import org.orbisgis.mapcomposer.model.graphicalelement.element.Document;
import org.orbisgis.mapcomposer.model.graphicalelement.element.illustration.SimpleIllustrationGE;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.AlwaysOnBack;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.AlwaysOnFront;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GERefresh;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import org.orbisgis.mapcomposer.model.graphicalelement.interfaces.GraphicalElement.Property;
import org.orbisgis.mapcomposer.model.graphicalelement.utils.GEManager;
import org.orbisgis.mapcomposer.model.utils.SaveAndLoadHandler;
import org.orbisgis.mapcomposer.view.ui.MainWindow;
import org.orbisgis.mapcomposer.view.utils.CompositionAreaOverlay;
import org.orbisgis.mapcomposer.view.utils.CompositionJPanel;
import org.orbisgis.mapcomposer.view.utils.RenderWorker;
import org.orbisgis.mapcomposer.view.utils.UIDialogProperties;
import org.orbisgis.sif.SIFDialog;
import org.orbisgis.sif.UIFactory;
import org.orbisgis.sif.UIPanel;
import org.orbisgis.sif.components.SaveFilePanel;

import java.awt.Dimension;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.image.BufferedImage;
import java.beans.EventHandler;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.imageio.ImageIO;
import javax.swing.*;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.LoggerFactory;
import org.xml.sax.SAXException;

/**
 * This class manager the interaction between the MainWindows, the CompositionArea and and the data model.
 */
public class UIController{

    /** CAManager */
    private CAManager caManager;

    /** GEManager */
    private GEManager geManager;
    
    /**Map doing the link between GraphicalElements and their CompositionJPanel*/
    private HashMap<GraphicalElement, CompositionJPanel> elementJPanelMap;
    
    /**This list contain all the GraphicalElement selected by the user*/
    private List<GraphicalElement> selectedGE;

    /**This list contain all the GraphicalElements that will be modified (z-index change, validation of the CA)
     * It's different from the SelectedGE list to permit to modify elements without loosing the selected GE.
     * e.g. modify one element by double clicking while keeping GraphicalElements selected.
     */
    private List<GraphicalElement> toBeSet;
    
    /**GraphicalElement stack giving the Z-index information*/
    private Stack<GraphicalElement> zIndexStack;

    /** SaveAndLoadHandler */
    private SaveAndLoadHandler saveNLoadHandler;
    
    private MainWindow mainWindow;

    /** Instance of the new GraphicalElement to create. */
    private GraphicalElement newGE;

    /** Executor used for the RenderWorkers. */
    private ExecutorService executorService;
    
    /**
     * Main constructor.
     */
    public UIController(){
        //Initialize the different attributes
        caManager = new CAManager();
        geManager = new GEManager();
        elementJPanelMap = new LinkedHashMap<>();
        selectedGE = new ArrayList<>();
        toBeSet = new ArrayList<>();
        zIndexStack = new Stack<>();
        saveNLoadHandler = new SaveAndLoadHandler(geManager, caManager);
        mainWindow = new MainWindow(this);
        mainWindow.setLocationRelativeTo(null);
        executorService = Executors.newFixedThreadPool(1);

        UIFactory.setMainFrame(mainWindow);
    }

    /**
     * Returns true if the CompositionArea already contain a Document GE, false otherwise.
     * @return true if a document GE exist, false otherwise.
     */
    public boolean isDocumentCreated(){
        for(GraphicalElement ge : elementJPanelMap.keySet())
            if(ge instanceof Document)
                return true;
        return false;
    }
    
    public MainWindow getMainWindow() { return mainWindow; }

    /**
     * Returns the CAManager.
     * @return The CAManager
     */
    public CAManager getCAManager() { return caManager; }
    
    /**
     * Change the Z-index of the displayed GraphicalElement.
     * @param z Change of the Z-index.
     */
    public void changeZIndex(ZIndex z){
        Stack<GraphicalElement> temp = new Stack<>();
        Stack<GraphicalElement> tempBack = new Stack<>();
        Stack<GraphicalElement> tempFront = new Stack<>();
        //Get the GE implementing the AlwaysOnBack interface and put them at the back
        for(GraphicalElement ge : zIndexStack){
            if(ge instanceof AlwaysOnBack){
                tempBack.push(ge);
                temp.add(ge);
            }
        }
        //Get the GE implementing the AlwaysOnFront interface and put them at the front
        for(GraphicalElement ge : zIndexStack){
            if(ge instanceof AlwaysOnFront){
                tempFront.push(ge);
                temp.add(ge);
            }
        }
        //Remove the previous detected elements
        for(GraphicalElement ge : temp){
            zIndexStack.remove(ge);
        }
        temp = new Stack<>();

        //Move the others GE
        //In each cases : first sort the GE from the selectedGE list,
        //Then place each GE at the good index in the stack.
        switch (z){
            case TO_FRONT:
                Collections.sort(selectedGE, new Comparator<GraphicalElement>() {
                    @Override
                    public int compare(GraphicalElement ge1, GraphicalElement ge2) {
                        if(zIndexStack.indexOf(ge1)>zIndexStack.indexOf(ge2)) return -1;
                        if(zIndexStack.indexOf(ge1)<zIndexStack.indexOf(ge2)) return 1;
                        return 0;
                    }
                });
                for(GraphicalElement ge : selectedGE) {
                    zIndexStack.remove(ge);
                    zIndexStack.add(0, ge);
                }
                break;
            case FRONT:
                Collections.sort(selectedGE, new Comparator<GraphicalElement>() {
                    @Override
                    public int compare(GraphicalElement ge1, GraphicalElement ge2) {
                        if(zIndexStack.indexOf(ge1)>zIndexStack.indexOf(ge2)) return 1;
                        if(zIndexStack.indexOf(ge1)<zIndexStack.indexOf(ge2)) return -1;
                        return 0;
                    }
                });
                temp.addAll(zIndexStack);
                for(GraphicalElement ge : selectedGE) {
                    if (zIndexStack.indexOf(ge) > 0) {
                        int index = temp.indexOf(ge) - 1;
                        zIndexStack.remove(ge);
                        zIndexStack.add(index, ge);
                    }
                }
                break;
            case BACK:
                Collections.sort(selectedGE, new Comparator<GraphicalElement>() {
                    @Override
                    public int compare(GraphicalElement ge1, GraphicalElement ge2) {
                        if(zIndexStack.indexOf(ge1)>zIndexStack.indexOf(ge2)) return -1;
                        if(zIndexStack.indexOf(ge1)<zIndexStack.indexOf(ge2)) return 1;
                        return 0;
                    }
                });
                temp.addAll(zIndexStack);
                for(GraphicalElement ge : selectedGE) {
                    if (zIndexStack.indexOf(ge) < zIndexStack.size() - 1) {
                        int index = temp.indexOf(ge) + 1;
                        zIndexStack.remove(ge);
                        zIndexStack.add(index, ge);
                    }
                }
                break;
            case TO_BACK:
                Collections.sort(selectedGE, new Comparator<GraphicalElement>() {
                    @Override
                    public int compare(GraphicalElement ge1, GraphicalElement ge2) {
                        if(zIndexStack.indexOf(ge1)>zIndexStack.indexOf(ge2)) return 1;
                        if(zIndexStack.indexOf(ge1)<zIndexStack.indexOf(ge2)) return -1;
                        return 0;
                    }
                });
                for(GraphicalElement ge : selectedGE) {
                    zIndexStack.remove(ge);
                    zIndexStack.add(ge);
                }
                break;
        }

        //Add to the stack the GE of the front
        zIndexStack.addAll(tempFront);
        //Add to the stack the GE of the back
        zIndexStack.addAll(tempBack);
        //Set the z-index of the GE from their stack position
        for(GraphicalElement ge : zIndexStack){
            mainWindow.getCompositionArea().setZIndex(elementJPanelMap.get(ge), zIndexStack.indexOf(ge));
        }
        modifySelectedGE();
    }
    
    /**
     * Selects a GraphicalElement and redisplays the ConfigurationAttributes.
     * @param ge GraphicalElement to select.
     */
    public void selectGE(GraphicalElement ge){
        selectedGE.add(ge);
        elementJPanelMap.get(ge).select();
        refreshSpin();
    }

    /**
     * Refreshes the JSpinner value and state (enable or not) with the values from the selected GEs.
     * The method test each GraphicalElement from the selectedGE list to know if every single GE property (X and Y position, width, height, rotation) is the same for the GEs.
     * In the case where a property is the same for all the selected GE, the corresponding spinner is set to the common value and enabled.
     * In the case where a property is different for the selected GE, the corresponding spinner is set to 0 and disabled.
     * In the case where no GE are selected, all the spinners are set to 0 and disabled.
     */
    public void refreshSpin(){
        //Set the default state for the spinner (enabled and value to 0).
        boolean boolX=false, boolY=false, boolW=false, boolH=false, boolR=false;
        int x=0, y=0, w=0, h=0, r=0;
        //If GEs are selected, test each
        if(!selectedGE.isEmpty()){
            x=selectedGE.get(0).getX();
            y=selectedGE.get(0).getY();
            w=selectedGE.get(0).getWidth();
            h=selectedGE.get(0).getHeight();
            r=selectedGE.get(0).getRotation();
            //Test each GE
            for(GraphicalElement graph : selectedGE){
                if(x!=graph.getX()){ boolX=true;x=selectedGE.get(0).getX();}
                if(y!=graph.getY()){ boolY=true;y=selectedGE.get(0).getY();}
                if(w!=graph.getWidth()){ boolW=true;w=selectedGE.get(0).getWidth();}
                if(h!=graph.getHeight()){ boolH=true;h=selectedGE.get(0).getHeight();}
                if(r!=graph.getRotation()){ boolR=true;r=selectedGE.get(0).getRotation();}
            }
        }
        //If no GE are selected, lock all the spinners
        else {
            boolX = true;
            boolY = true;
            boolW = true;
            boolH = true;
            boolR = true;
        }
        //Sets the spinners.
        mainWindow.setSpinner(boolX, x, Property.X);
        mainWindow.setSpinner(boolY, y, Property.Y);
        mainWindow.setSpinner(boolW, w, Property.WIDTH);
        mainWindow.setSpinner(boolH, h, Property.HEIGHT);
        mainWindow.setSpinner(boolR, r, Property.ROTATION);
    }
    
    /**
     * Unselects a GraphicalElement and redisplays the ConfigurationAttributes.
     * @param ge GraphicalElement to select.
     */
    public void unselectGE(GraphicalElement ge){
        selectedGE.remove(ge);
        elementJPanelMap.get(ge).unselect();
        refreshSpin();
    }
    
    /**
     * Unselect all the GraphicalElement.
     * Reset the selectedGE list and unselect all the CompositionJPanel in the compositionArea.
     */
    public void unselectAllGE(){
        //Unselect all the GraphicalElements
        for(GraphicalElement ge : selectedGE)
            elementJPanelMap.get(ge).unselect();
        selectedGE= new ArrayList<>();
        refreshSpin();
    }
    
    /**
     * Removes all the selected GraphicalElement.
     */
    public void removeSelectedGE(){
        for(GraphicalElement ge : selectedGE){
            mainWindow.getCompositionArea().removeGE(elementJPanelMap.get(ge));
            elementJPanelMap.remove(ge);
            zIndexStack.remove(ge);
        }
        for(GraphicalElement ge : zIndexStack)
            mainWindow.getCompositionArea().setZIndex(elementJPanelMap.get(ge), zIndexStack.indexOf(ge));
        selectedGE=new ArrayList<>();
        mainWindow.getCompositionArea().refresh();
    }

    /**
     * Modify the position and size of all the selected GraphicalElements
     */
    public void modifySelectedGE(){
        for(GraphicalElement ge : selectedGE)
            modifyGE(ge);
    }

    /**
     * modify the position and the size of the given GraphicalElement.
     * It does the refresh of the GE, the resizing of the GE representation in the CompositionArea and refreshes the spinner state.
     * The GE representation is just moved and scaled, not completely re-renderer.
     * @param ge GraphicalElement to validate
     */
    public void modifyGE(GraphicalElement ge){
        if(ge instanceof GERefresh)
            ((GERefresh)ge).refresh();
        elementJPanelMap.get(ge).modify(ge.getX(), ge.getY(), ge.getWidth(), ge.getHeight(), ge.getRotation());
        if(ge instanceof Document)
            mainWindow.getCompositionArea().setDocumentDimension(new Dimension(ge.getWidth(), ge.getHeight()));
        refreshSpin();
    }

    /**
     * Redraws all the selected GraphicalElements
     */
    public void redrawSelectedGE(){
        for(GraphicalElement ge : selectedGE)
            redrawGE(ge);
    }

    /**
     * Redraws the given GraphicalElement.
     * It does the refresh of the GE, the actualization of the GE representation in the CompositionArea and refreshes the spinner state.
     * The GE representation is completely re-renderer.
     * @param ge GraphicalElement to validate
     */
    public void redrawGE(GraphicalElement ge){
        if(ge instanceof GERefresh)
            ((GERefresh)ge).refresh();
        unselectGE(ge);
        RenderWorker worker = new RenderWorker(elementJPanelMap.get(ge), geManager.getRenderer(ge.getClass()), ge);
        worker.execute();
        if(ge instanceof Document)
            mainWindow.getCompositionArea().setDocumentDimension(new Dimension(ge.getWidth(), ge.getHeight()));
        refreshSpin();
    }

    /**
     * Reads a list of ConfigurationAttribute and set the GraphicalElement with it.
     * This action is done when the button validate of the properties dialog is clicked.
     * @param caList List of ConfigurationAttributes to read.
     */
    public void validateCAList(List<ConfigurationAttribute> caList) {
        //Apply the function to all the selected GraphicalElements
        for(GraphicalElement ge : toBeSet){
            //Takes each CA from the list of CA to validate
            for(ConfigurationAttribute dialogPropertiesCA : caList) {
                if (!dialogPropertiesCA.getReadOnly()) {
                    if (dialogPropertiesCA instanceof RefreshCA)
                        ((RefreshCA) dialogPropertiesCA).refresh(this);
                    ge.setAttribute(dialogPropertiesCA);
                }
            }
            //If the GraphicalElement was already added to the document
            if(elementJPanelMap.containsKey(ge))
                redrawGE(ge);
            //Set the CompositionAreaOverlay ratio in the case of the GraphicalElement was not already added to the Document
            else{
                //Give the ratio to the CompositionAreaOverlay();
                if(newGE instanceof SimpleIllustrationGE) {
                    try {
                        BufferedImage bi = ImageIO.read(new File(((SimpleIllustrationGE) newGE).getPath()));
                        mainWindow.getCompositionArea().getOverlay().setRatio((float) bi.getWidth() / bi.getHeight());
                    } catch (IOException e) {
                        LoggerFactory.getLogger(UIController.class).error(e.getMessage());
                    }
                }
            }
        }
        toBeSet = new ArrayList<>();
    }
    
    /**
     * Returns the list of ConfigurationAttributes that all the selected GraphicalElements have in common.
     * @return List of common ConfigurationAttributes (common about the names and not values).
     */
    private List<ConfigurationAttribute> getCommonAttributes(){
        List<ConfigurationAttribute> list = toBeSet.get(0).getAllAttributes();
        List<ConfigurationAttribute> listToRemove = new ArrayList<>();
        //Compare each the CA of the list to those of the GE from selectedGE
        boolean flag=false;
        for(ConfigurationAttribute caList : list){
            for(GraphicalElement ge : toBeSet){
                flag=false;
                for(ConfigurationAttribute caGE : ge.getAllAttributes()){
                    //refresh the attributes
                    if(caGE instanceof RefreshCA) ((RefreshCA)caGE).refresh(this);
                    if(caList instanceof RefreshCA) ((RefreshCA)caList).refresh(this);
                    //Compare them and if both represent the same property, lock the ConfigurationAttribute
                    if(caList.isSameName(caGE)){
                        flag=true;
                        caList.setReadOnly(!caList.isSameValue(caGE));
                    }
                }
            }
            //If the CA isn't in common, it's added to a list to be removed after
            if(!flag){
                listToRemove.add(caList);
            }
        }
        for(ConfigurationAttribute ca : listToRemove){
            list.remove(ca);
        }
        
        return list;
    }

    /**
     * Returns the list of all the GraphicalElements added to the document.
     * @return The list of GraphicalElements
     */
    public List<GraphicalElement> getGEList(){
        List list = new ArrayList<GraphicalElement>();
        list.addAll(elementJPanelMap.keySet());
        return list;
    }

    /**
     * Remove all the displayed GE from the panel.
     */
    public void removeAllGE() {
        mainWindow.getCompositionArea().removeAllGE();
        elementJPanelMap = new LinkedHashMap<>();
        selectedGE = new ArrayList<>();
        zIndexStack = new Stack<>();
    }

    /**
     * Run saveProject function of the SaveHandler.
     */
    public void saveDocument(){
        try {
            saveNLoadHandler.saveProject(zIndexStack.subList(0, zIndexStack.size()));
        } catch (NoSuchMethodException|IOException ex) {
            LoggerFactory.getLogger(UIController.class).error(ex.getMessage());
        }
    }

    /**
     * Run loadProject function from the SaveHandler and draw loaded GE.
     */
    public void loadDocument(){
        try {
            removeAllGE();
            List<GraphicalElement> list = saveNLoadHandler.loadProject();
            //Add all the GE starting from the last one (to get the good z-index)
            for(int i=list.size()-1; i>=0; i--)
                addGE(list.get(i));
            mainWindow.getCompositionArea().refresh();
        } catch (ParserConfigurationException|SAXException|IOException ex) {
            LoggerFactory.getLogger(UIController.class).error(ex.getMessage());
        }
    }

    /**
     * Add to the project the given GE (that is just loaded)..
     * @param ge GE to add to the project.
     */
    private void addGE(GraphicalElement ge) {
        elementJPanelMap.put(ge, new CompositionJPanel(ge, this));
        mainWindow.getCompositionArea().addGE(elementJPanelMap.get(ge));
        if(ge instanceof GERefresh){
            ((GERefresh)ge).refresh();
        }
        RenderWorker worker = new RenderWorker(elementJPanelMap.get(ge), geManager.getRenderer(ge.getClass()), ge);
        executorService.submit(worker);
        zIndexStack.push(ge);
        //Apply the z-index change to only the GraphicalElement ge.
        List temp = selectedGE;
        selectedGE = new ArrayList<>();
        selectedGE.add(ge);
        changeZIndex(ZIndex.TO_FRONT);
        selectedGE = temp;
    }

    /**
     * DOes the instantiation of the new GraphicalElement and open the configuration dialog.
     * @param newGEClass
     */
    public void instantiateGE(Class<? extends GraphicalElement> newGEClass) {
        try {
            newGE = newGEClass.newInstance();
            //Refresh the ConfigurationAttributes to initialize them
            for(ConfigurationAttribute ca : newGE.getAllAttributes())
                if(ca instanceof RefreshCA)
                    ((RefreshCA)ca).refresh(this);

            showGEProperties(newGE).addWindowListener(EventHandler.create(WindowListener.class, this, "drawGE", "", "windowClosed"));
        } catch (InstantiationException | IllegalAccessException ex) {
            LoggerFactory.getLogger(UIController.class).error(ex.getMessage());
        }
    }

    /**
     * Allow the user to draw the size of the new GraphicalElement
     * @param winEvent
     */
    public void drawGE(WindowEvent winEvent){
        if(winEvent.getSource() instanceof SIFDialog && newGE != null) {
            SIFDialog sifDialog = (SIFDialog) winEvent.getSource();
            if(sifDialog.isAccepted()) {
                //If the image has an already set path value, get the image ratio
                if (newGE instanceof SimpleIllustrationGE) {
                    File f = new File(((SimpleIllustrationGE) newGE).getPath());
                    if (f.exists() && f.isFile()) {
                        try {
                            BufferedImage bi = ImageIO.read(f);
                            mainWindow.getCompositionArea().getOverlay().setRatio((float) bi.getWidth() / bi.getHeight());
                        } catch (IOException e) {
                            LoggerFactory.getLogger(UIController.class).error(e.getMessage());
                        }
                    }
                }
                mainWindow.getCompositionArea().setOverlayMode(CompositionAreaOverlay.Mode.NEW_GE);
            }
            else{
                newGE=null;
            }
        }
    }

    /**
     * Set the new GraphicalElement and then open the properties dialog.
     */
    public void setNewGE(int x, int y, int width, int height) {
        //Sets the newGE
        if (newGE!=null){
            newGE.setX(x);
            newGE.setY(y);
            newGE.setWidth(width);
            newGE.setHeight(height);
            addGE(newGE);
            redrawGE(newGE);
        }
        mainWindow.getCompositionArea().setOverlayMode(CompositionAreaOverlay.Mode.NONE);
        newGE=null;
    }
    
    public void setAlign( Align alignment) {
        if(selectedGE.size()>0){
            int xMin;
            int xMax;
            int yMin;
            int yMax;
            switch(alignment){
                case LEFT:
                    //Obtain the minimum x position of all the CompositionJPanel from the CompositionArea
                    //The x position get take into account the rotation of the GraphicalElement.
                    xMin=elementJPanelMap.get(selectedGE.get(0)).getX();
                    for (GraphicalElement ge : selectedGE)
                        if (elementJPanelMap.get(ge).getX() < xMin)
                            xMin = elementJPanelMap.get(ge).getX();
                    //Set all the GraphicalElement x position to the one get before
                    for(GraphicalElement ge : selectedGE){
                        //Convert the x position to the new one of the GraphicalElement taking into account its rotation angle.
                        double rad = Math.toRadians(ge.getRotation());
                        double newWidth = Math.floor(Math.abs(sin(rad) * ge.getHeight()) + Math.abs(cos(rad) * ge.getWidth()));
                        ge.setX(xMin-(ge.getWidth()-(int)newWidth)/2);
                    }
                    break;
                case CENTER:
                    xMin=selectedGE.get(0).getX();
                    xMax=selectedGE.get(0).getX()+selectedGE.get(0).getWidth();
                    for (GraphicalElement ge : selectedGE) {
                        if (ge.getX() < xMin)
                            xMin = ge.getX();
                        if (ge.getX()+ge.getWidth() > xMax)
                            xMax = ge.getX()+ge.getWidth();
                    }
                    int xMid = (xMax+xMin)/2;
                    for(GraphicalElement ge : selectedGE)
                        ge.setX(xMid-ge.getWidth()/2);
                    break;
                case RIGHT:
                    //Obtain the maximum x position of all the CompositionJPanel from the CompositionArea
                    //The x position get take into account the rotation of the GraphicalElement.
                    xMax=elementJPanelMap.get(selectedGE.get(0)).getX()+elementJPanelMap.get(selectedGE.get(0)).getWidth();
                    for (GraphicalElement ge : selectedGE)
                        if (elementJPanelMap.get(ge).getX()+elementJPanelMap.get(ge).getWidth() > xMax)
                            xMax = elementJPanelMap.get(ge).getX()+elementJPanelMap.get(ge).getWidth();
                    //Takes into account the border width of the ConfigurationJPanel (2 pixels)
                    xMax-=2;
                    //Set all the GraphicalElement x position to the one get before
                    for(GraphicalElement ge : selectedGE) {
                        //Convert the x position to the new one of the GraphicalElement taking into account its rotation angle.
                        double rad = Math.toRadians(ge.getRotation());
                        double newWidth = Math.ceil(Math.abs(sin(rad) * ge.getHeight()) + Math.abs(cos(rad) * ge.getWidth()));
                        ge.setX(xMax-ge.getWidth()+(ge.getWidth()-(int)newWidth)/2);
                    }
                    break;
                case TOP:
                    //Obtain the minimum y position of all the CompositionJPanel from the CompositionArea
                    //The y position get take into account the rotation of the GraphicalElement.
                    yMin=elementJPanelMap.get(selectedGE.get(0)).getY();
                    for (GraphicalElement ge : selectedGE)
                        if (ge.getY() < yMin)
                            yMin = elementJPanelMap.get(ge).getY();
                    //Set all the GraphicalElement y position to the one get before
                    for(GraphicalElement ge : selectedGE) {
                        //Convert the y position to the new one of the GraphicalElement taking into account its rotation angle.
                        double rad = Math.toRadians(ge.getRotation());
                        double newHeight = Math.floor(Math.abs(sin(rad) * ge.getWidth()) + Math.abs(cos(rad) * ge.getHeight()));
                        ge.setY(yMin - (ge.getHeight() - (int) newHeight) / 2);
                    }
                    break;
                case MIDDLE:
                    yMin=selectedGE.get(0).getY();
                    yMax=selectedGE.get(0).getY()+selectedGE.get(0).getHeight();
                    for (GraphicalElement ge : selectedGE) {
                        if (ge.getY() < yMin)
                            yMin = ge.getY();
                        if (ge.getY()+ge.getHeight() > yMax)
                            yMax = ge.getY()+ge.getHeight();
                    }
                    int yMid = (yMax+yMin)/2;
                    for(GraphicalElement ge : selectedGE)
                        ge.setY(yMid-ge.getHeight()/2);
                    break;
                case BOTTOM:
                    //Obtain the maximum y position of all the CompositionJPanel from the CompositionArea
                    //The y position get take into account the rotation of the GraphicalElement.
                    yMax=elementJPanelMap.get(selectedGE.get(0)).getY()+elementJPanelMap.get(selectedGE.get(0)).getHeight();
                    for (GraphicalElement ge : selectedGE)
                        if (elementJPanelMap.get(ge).getY()+elementJPanelMap.get(ge).getHeight() > yMax)
                            yMax = elementJPanelMap.get(ge).getY()+elementJPanelMap.get(ge).getHeight();
                    //Takes into account the border width ConfigurationJPanel (2 pixels)
                    yMax-=2;
                    //Set all the GraphicalElement y position to the one get before
                    for(GraphicalElement ge : selectedGE) {
                        //Convert the y position to the new one of the GraphicalElement taking into account its rotation angle.
                        double rad = Math.toRadians(ge.getRotation());
                        double newHeight = Math.ceil(Math.abs(sin(rad)*ge.getWidth())+Math.abs(cos(rad)*ge.getHeight()));
                        ge.setY(yMax - ge.getHeight() + (ge.getHeight()-(int)newHeight)/2);
                    }
                    break;
            }
            modifySelectedGE();
        }
    }

    /**
     * Open a dialog window with all the ConfigurationAttributes common to the selected GraphicalElement.
     */
    public void showSelectedGEProperties(){
        if(selectedGE.size()>0){
            //If the only one GraphicalElement is selected, the locking checkboxes are hidden
            toBeSet.addAll(selectedGE);
            //Create and show the properties dialog.
            UIPanel panel = new UIDialogProperties(getCommonAttributes(), this, false);
            SIFDialog dialog = UIFactory.getSimpleDialog(panel, mainWindow, true);
            dialog.setVisible(true);
            dialog.pack();
            dialog.setAlwaysOnTop(true);
        }
    }

    /**
     * Open a dialog window with all the ConfigurationAttributes from the given GraphicalElement.
     */
    public SIFDialog showGEProperties(GraphicalElement ge){
        toBeSet.add(ge);
        //Create and show the properties dialog.
        UIPanel panel = new UIDialogProperties(ge.getAllAttributes(), this, false);
        SIFDialog dialog = UIFactory.getSimpleDialog(panel, mainWindow, true);
        dialog.setVisible(true);
        dialog.pack();
        dialog.setAlwaysOnTop(true);
        dialog.setLocationRelativeTo(mainWindow);
        return dialog;
    }
    
    /**
     * Open a dialog window with all the document properties.
     */
    public void showDocProperties(){
        Document doc=null;
        for(GraphicalElement ge : zIndexStack)
            if(ge instanceof Document){
                doc=(Document)ge;
                break;
            }
        
        if(doc!=null){
            for(GraphicalElement graph : selectedGE)
                unselectGE(graph);
            toBeSet.add(doc);
            //Create and show the properties dialog.
            UIPanel panel = new UIDialogProperties(getCommonAttributes(), this, false);
            SIFDialog dialog = UIFactory.getSimpleDialog(panel, mainWindow, true);
            dialog.setVisible(true);
            dialog.pack();
            dialog.setAlwaysOnTop(true);
        }
    }

    public enum ZIndex{
        TO_FRONT, FRONT, BACK, TO_BACK;
    }
    
    public enum Align{
        LEFT, CENTER, RIGHT, TOP, MIDDLE, BOTTOM;
    }

    /**
     * Apply a change of a property to the selected GraphicalElements.
     * It's mainly used when the value of a spinner of the tool bar change of value.
     * @param prop
     * @param i
     */
    public void changeProperty(GraphicalElement.Property prop, int i){
        for(GraphicalElement ge : selectedGE){
            switch(prop){
                case X:
                    ge.setX(i);
                    break;
                case Y:
                    ge.setY(i);
                    break;
                case WIDTH:
                    ge.setWidth(i);
                    break;
                case HEIGHT:
                    ge.setHeight(i);
                    break;
                case ROTATION:
                    ge.setRotation(i);
                    break;
            }
        }
        modifySelectedGE();
    }
    
    /**
     * Exports to document into png file.
     * First renders again all the GraphicalElement to make sure that the graphical representation are at their best quality.
     * Then exports the CompositionArea.
     */
    public void export(){
        //Render again all the GE. All the RenderWorkers are saved into a list and the export will be done only when all will be terminated.
        RenderWorker lastRenderWorker = null;
        for(GraphicalElement ge : elementJPanelMap.keySet()){
            RenderWorker rw = new RenderWorker(elementJPanelMap.get(ge), geManager.getRenderer(ge.getClass()), ge);
            executorService.submit(rw);
            lastRenderWorker = rw;
        }
        executorService.shutdown();

        //Add to the last RenderWorker a listener to open a saveFilePanel just after the rendering is done
        if(lastRenderWorker!=null) {
            lastRenderWorker.addPropertyChangeListener(new PropertyChangeListener() {
                @Override
                public void propertyChange(PropertyChangeEvent propertyChangeEvent) {
                    //Verify if the property state is at DONE
                    if (propertyChangeEvent.getNewValue().equals(SwingWorker.StateValue.DONE)) {
                        //Creates and sets the file chooser
                        SaveFilePanel saveFilePanel = new SaveFilePanel("UIController.Export", "Export document");
                        saveFilePanel.addFilter(new String[]{"png"}, "PNG files");
                        saveFilePanel.loadState();
                        if(UIFactory.showDialog(saveFilePanel)){
                            String path = saveFilePanel.getSelectedFile().getAbsolutePath();

                            try{
                                //Removes the border, does the export, then adds them again
                                for(GraphicalElement ge : zIndexStack){
                                    elementJPanelMap.get(ge).enableBorders(false);
                                }
                                ImageIO.write(mainWindow.getCompositionArea().getDocBufferedImage(),"png",new File(path));

                                for(GraphicalElement ge : zIndexStack){
                                    elementJPanelMap.get(ge).enableBorders(true);
                                }
                            } catch (IOException ex) {
                                Logger.getLogger(UIController.class.getName()).log(Level.SEVERE, null, ex);
                            }
                        }
                    }
                }
            });
        }
    }
}
