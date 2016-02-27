/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTypes;
import DataTypes.Class.Association;
import java.util.ArrayList;

/**
 *
 * @author Kieran
 */
public class MetaModel {
    
      
    ArrayList<Component> components;
    ArrayList<Association> associations;


    public ArrayList<Component> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<Component> components) {
        this.components = components;
    }

    public ArrayList<Association> getAssociations() {
        return associations;
    }

    public void setAssociations(ArrayList<Association> associations) {
        this.associations = associations;
    }

  
    
    public void printModelToConsole(){
        components.stream().forEach((component) -> {
            System.out.println("**\n\n" + component.toString());
        });
        associations.stream().forEach((association) -> {
            System.out.println("***\n\n" + association.toString());
        });
     
    }
    
}
