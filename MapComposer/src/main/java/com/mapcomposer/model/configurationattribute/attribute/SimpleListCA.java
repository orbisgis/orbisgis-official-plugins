package com.mapcomposer.model.configurationattribute.attribute;

import com.mapcomposer.model.configurationattribute.interfaces.ConfigurationAttribute;
import com.mapcomposer.model.configurationattribute.interfaces.ListCA;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * Simple ListConfigurationAttribute.
 * ListConfigurationAttribute simple implementation allowing to create simples ListCA.
 * All the interface functions are implemented.
 * @param <T> Type of the values contained by the list.
 */
public abstract class SimpleListCA<T> implements ListCA<T> {
    /** Index of the value selected.*/
    private int index;
    /** Name of the property */
    private final String name;
    /** Property itself */
    private List<T> t;
    /** Lock. If the property is locked, it can't be changed. */
    private boolean lock;

    /**
     * Main constructor.
     * @param name Name of the value in its GraphicalElement.
     */
    public SimpleListCA(String name) {
        this.name=name;
        this.t=new ArrayList<>();
        this.index = -1;
        this.lock=false;
    }

    @Override
    public void add(T value){
        this.getValue().add(value);
    }

    @Override
    public T getSelected(){
        if(index>=0 && index<this.getValue().size()){
            return this.getValue().get(index);
        }
        else{
            return null;
        }
    }

    @Override
    public void select(T choice){
        this.index = this.getValue().indexOf(choice);
    }
    
    @Override
    public boolean remove(T value){
        if(this.getValue().indexOf(value)==this.index){
            this.index=-1;
        }
        return this.getValue().remove(value);
    }

    @Override
    public void setLock(boolean lock){
        this.lock=lock;
    }

    @Override
    public boolean isLocked(){
        return lock;
    }
    
    @Override
    public boolean setValue(List<T> value){
        if(!lock)
            this.t=value;
        return lock;
    }
    
    @Override
    public List<T> getValue(){
        return t;
    }
    
    @Override
    public String getName(){
        return name;
    }
    
    @Override
    public boolean equals(Object other){
        if (other == null) return false;
        if (other == this) return true;
        if(!(other instanceof SimpleCA)) return false;
        else {
            return ((ConfigurationAttribute)other).getName().equals(name) &&
                   ((ConfigurationAttribute)other).getValue().equals(t);
        }
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 13 * hash + Objects.hashCode(this.name);
        hash = 13 * hash + Objects.hashCode(this.t);
        return hash;
    }

    @Override
    public boolean isSameProperty(ConfigurationAttribute ca) {
        return this.getName().equals(ca.getName());
    }
}
