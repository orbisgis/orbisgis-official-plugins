package com.mapcomposer.model.graphicalelement.utils;

import com.mapcomposer.model.graphicalelement.element.Document;
import com.mapcomposer.model.graphicalelement.element.SimpleDocumentGE;
import com.mapcomposer.model.graphicalelement.element.cartographic.MapImage;
import com.mapcomposer.model.graphicalelement.element.cartographic.Orientation;
import com.mapcomposer.model.graphicalelement.element.cartographic.Scale;
import com.mapcomposer.model.graphicalelement.element.cartographic.SimpleMapImageGE;
import com.mapcomposer.model.graphicalelement.element.cartographic.SimpleOrientationGE;
import com.mapcomposer.model.graphicalelement.element.cartographic.SimpleScaleGE;
import com.mapcomposer.model.graphicalelement.element.illustration.Image;
import com.mapcomposer.model.graphicalelement.element.illustration.SimpleIllustrationGE;
import com.mapcomposer.model.graphicalelement.element.text.SimpleTextGE;
import com.mapcomposer.model.graphicalelement.element.text.TextElement;
import com.mapcomposer.model.graphicalelement.interfaces.GraphicalElement;
import com.mapcomposer.model.utils.LinkToOrbisGIS;
import com.mapcomposer.view.ui.MainWindow;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileFilter;
import org.jibx.runtime.BindingDirectory;
import org.jibx.runtime.IBindingFactory;
import org.jibx.runtime.IMarshallingContext;
import org.jibx.runtime.IUnmarshallingContext;
import org.jibx.runtime.JiBXException;

/**
 * Class containig all the graphicalElement to save.
 */
public class SaveHandler {
    private List<MapImage> listMI;
    private List<Orientation> listO;
    private List<Scale> listS;
    private List<Image> listI;
    private List<TextElement> listT;
    private List<Document> listD;
    private final List<GraphicalElement> list;
    
    public SaveHandler(){
        listMI=new ArrayList<>();
        listO=new ArrayList<>();
        listS=new ArrayList<>();
        listI=new ArrayList<>();
        listT=new ArrayList<>();
        listD=new ArrayList<>();
        list=new ArrayList<>();
    }
    
    public List<GraphicalElement> getList(){
        return list;
    }
    
    public void save(final Stack<GraphicalElement> stack) throws JiBXException, FileNotFoundException{
        //Resets the elements list
        listMI=new ArrayList<>();
        listO=new ArrayList<>();
        listS=new ArrayList<>();
        listI=new ArrayList<>();
        listT=new ArrayList<>();
        listD=new ArrayList<>();
        list.removeAll(list);
        
        Stack<GraphicalElement> temp = new Stack<>();
        for (GraphicalElement ge : stack) {
            temp.push(ge);
        }
        int i=0;
        //Saves the z-index of the GE
        while(!temp.empty()){
            temp.peek().setZ(i);
            if(temp.peek() instanceof SimpleMapImageGE)
                listMI.add((MapImage)temp.peek());
            if(temp.peek() instanceof Orientation)
                listO.add((Orientation)temp.peek());
            if(temp.peek() instanceof Scale)
                listS.add((Scale)temp.peek());
            if(temp.peek() instanceof Image)
                listI.add((Image)temp.peek());
            if(temp.peek() instanceof TextElement)
                listT.add((TextElement)temp.peek());
            if(temp.peek() instanceof Document)
                listD.add((Document)temp.peek());
            i++;
            temp.pop();
        }
        //Open the file chooser window
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(LinkToOrbisGIS.getInstance().getViewWorkspace().getCoreWorkspace().getWorkspaceFolder()));
        fc.setApproveButtonText("Save");
        fc.setDialogTitle("Save document project");
        fc.setDialogType(JFileChooser.SAVE_DIALOG);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileFilter() {

            @Override public boolean accept(File file) {
                if(file.isDirectory()) return true;
                return file.getAbsolutePath().toLowerCase().contains(".xml");
            }

            @Override public String getDescription() {return "XML Files (.xml)";}
        });
        //If the save is validated, do the marshall
        if(fc.showOpenDialog(new JFrame())==JFileChooser.APPROVE_OPTION){
            String path;
            if(fc.getSelectedFile().exists() && fc.getSelectedFile().isFile())
                path=fc.getSelectedFile().getAbsolutePath();
            else 
                path=fc.getSelectedFile().getAbsolutePath()+".xml";

            IBindingFactory bfact = BindingDirectory.getFactory(SaveHandler.class);
            IMarshallingContext mctx = bfact.createMarshallingContext();
            mctx.marshalDocument(this, "UTF-8", null, new FileOutputStream(path));
            JOptionPane.showMessageDialog(MainWindow.getInstance(), "Save done.");
        }
        else
            JOptionPane.showMessageDialog(MainWindow.getInstance(), "Save abord.");
    }
    
    public void load() throws JiBXException, FileNotFoundException{
        //Open the file chooser window
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(LinkToOrbisGIS.getInstance().getViewWorkspace().getCoreWorkspace().getWorkspaceFolder()));
        fc.setApproveButtonText("Open");
        fc.setDialogTitle("Open document project");
        fc.setDialogType(JFileChooser.OPEN_DIALOG);
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.setFileFilter(new FileFilter() {

            @Override public boolean accept(File file) {
                if(file.isDirectory()) return true;
                return file.getAbsolutePath().toLowerCase().contains(".xml");
            }

            @Override public String getDescription() {return "XML Files (.xml)";}
        });
        //If the save is validated, do the marshall
        if(fc.showOpenDialog(new JFrame())==JFileChooser.APPROVE_OPTION){
            String path = fc.getSelectedFile().getAbsolutePath();
            IBindingFactory bfact = BindingDirectory.getFactory(SaveHandler.class);
            IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
            FileInputStream in = new FileInputStream(path);
            SaveHandler sh = (SaveHandler)uctx.unmarshalDocument(in, "UTF8");

            //Do the loading
            int size=0;
            System.out.println("size : "+size);
            if(sh.listI!=null)
                size+= sh.listI.size();
            if(sh.listMI!=null)
                size+= sh.listMI.size();
            if(sh.listO!=null)
                size+= sh.listO.size();
            if(sh.listS!=null)
                size+= sh.listS.size();
            if(sh.listT!=null)
                size+= sh.listT.size();
            if(sh.listD!=null)
                size+= sh.listD.size();

            for(int i=size-1; i>=0; i--){
                System.out.println("i : "+i);
                if(sh.listMI!=null)
                    for(SimpleMapImageGE ge : sh.listMI)
                        if(ge.getZ()==i)
                            list.add(new MapImage(ge));
                if(sh.listS!=null)
                    for(SimpleScaleGE ge : sh.listS)
                        if(ge.getZ()==i)
                            list.add(new Scale(ge));
                if(sh.listO!=null)
                    for(SimpleOrientationGE ge : sh.listO)
                        if(ge.getZ()==i)
                            list.add(new Orientation(ge));
                if(sh.listT!=null)
                    for(SimpleTextGE ge : sh.listT)
                        if(ge.getZ()==i)
                            list.add(new TextElement(ge));
                if(sh.listD!=null)
                    for(SimpleDocumentGE ge : sh.listD)
                        if(ge.getZ()==i)
                            list.add(new Document(ge));
                if(sh.listI!=null)
                    for(SimpleIllustrationGE ge : sh.listI)
                        if(ge.getZ()==i)
                            list.add(new Image(ge));
            }
            JOptionPane.showMessageDialog(MainWindow.getInstance(), "Load done.");
        }
        else
            JOptionPane.showMessageDialog(MainWindow.getInstance(), "Load abord.");
    }
}