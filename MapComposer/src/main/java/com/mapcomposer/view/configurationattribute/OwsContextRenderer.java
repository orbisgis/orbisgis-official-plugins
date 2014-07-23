/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package com.mapcomposer.view.configurationattribute;

import com.mapcomposer.model.configurationattribute.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.attribute.Source;
import com.mapcomposer.model.utils.LinkToOrbisGIS;
import java.awt.FlowLayout;
import java.io.File;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * Renderer associated to the OwsContext ConfigurationAttribute.
 */
public class OwsContextRenderer implements CARenderer{

    @Override
    public JPanel render(ConfigurationAttribute ca) {
        JPanel pan = new JPanel();
        pan.setLayout(new FlowLayout());
        
        Source source = (Source)ca;
        
        pan.add(new JLabel(source.getName()));
        File f = new File(LinkToOrbisGIS.getInstance().getViewWorkspace().getCoreWorkspace().getWorkspaceFolder()+"/maps/");
        JComboBox list = new JComboBox(f.listFiles());
        list.setSelectedItem(ca.getValue());
        pan.add(list);
        return pan;
    }
    
}
