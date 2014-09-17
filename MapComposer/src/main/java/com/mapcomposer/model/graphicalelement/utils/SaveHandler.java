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
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
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
        listMI=new ArrayList<>();
        listO=new ArrayList<>();
        listS=new ArrayList<>();
        listI=new ArrayList<>();
        listT=new ArrayList<>();
        listD=new ArrayList<>();
        list.removeAll(list);
        
        Stack<GraphicalElement> temp = new Stack<>();
        System.out.println("stack size : "+stack.size());
        for(int i=stack.size()-1; i>=0; i--){
            System.out.println("i : "+i);
            System.out.println("get : "+stack.get(i));
            temp.push(stack.get(i));
        }
        System.out.println("save size : "+temp.size());
        while(!temp.empty()){
            int i=0;
            temp.peek().setZ(i);
            if(temp.peek() instanceof SimpleMapImageGE){
                System.out.println("mapImage");
                listMI.add((MapImage)temp.peek());
            }
            if(temp.peek() instanceof Orientation){
                listO.add((Orientation)temp.peek());
            }
            if(temp.peek() instanceof Scale){
                listS.add((Scale)temp.peek());
            }
            if(temp.peek() instanceof Image){
                listI.add((Image)temp.peek());
            }
            if(temp.peek() instanceof TextElement){
                listT.add((TextElement)temp.peek());
            }
            if(temp.peek() instanceof Document){
                listD.add((Document)temp.peek());
            }
            i++;
            temp.pop();
        }
        
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(LinkToOrbisGIS.getInstance().getViewWorkspace().getCoreWorkspace().getWorkspaceFolder()));
        fc.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        fc.showOpenDialog(new JFrame());
        String path;
        if(listD.size()>0)
            path = fc.getSelectedFile().getAbsolutePath()+"/"+listD.get(0).getName()+"_save.xml";
        path = fc.getSelectedFile().getAbsolutePath()+"/save.xml";
        
        IBindingFactory bfact = BindingDirectory.getFactory(SaveHandler.class);
        IMarshallingContext mctx = bfact.createMarshallingContext();
        mctx.marshalDocument(this, "UTF-8", null, new FileOutputStream(path));
    }
    
    public void load() throws JiBXException, FileNotFoundException{
        JFileChooser fc = new JFileChooser();
        fc.setCurrentDirectory(new File(LinkToOrbisGIS.getInstance().getViewWorkspace().getCoreWorkspace().getWorkspaceFolder()));
        fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
        fc.showOpenDialog(new JFrame());
        String path = fc.getSelectedFile().getAbsolutePath();
        IBindingFactory bfact = BindingDirectory.getFactory(SaveHandler.class);
        IUnmarshallingContext uctx = bfact.createUnmarshallingContext();
        FileInputStream in = new FileInputStream(path);
        SaveHandler sh = (SaveHandler)uctx.unmarshalDocument(in, "UTF8");
        
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
    }
}