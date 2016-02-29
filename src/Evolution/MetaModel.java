/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;

import DataTypes.Class.Association;
import DataTypes.Component;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Kieran
 */
public class MetaModel {

    // fitness is for each class in the model, stored by class xmi:id key
    HashMap<String, FitnessMetrics> fitness;
    // shows connections between classes
    DependencyMatrix dependencies;
    // holds all the methods attributes and classes
    ArrayList<Component> chromosome;
    // a list of associations present (composition, aggregation, generalisation etc)
    ArrayList<Association> associations;

    
    
    
    public void updateDependencyMatrix() {

    }

    public void updateFitnessValues() {
        dependencies = new DependencyMatrix(chromosome, associations);
        // check components have been added
        for (Component component : chromosome) {
            if (component instanceof DataTypes.Class.Class) {
                DataTypes.Class.Class classe = (DataTypes.Class.Class) component;
                FitnessMetrics fit = new FitnessMetrics();
                //TODO insert metric calls here
                fitness.put(classe.getID(), fit);
            }
        }
    }


    // getters and setters
    public ArrayList<Component> getComponents() {
        return chromosome;
    }

    public void setComponents(ArrayList<Component> components) {
        this.chromosome = components;
    }

    public ArrayList<Association> getAssociations() {
        return associations;
    }

    public void setAssociations(ArrayList<Association> associations) {
        this.associations = associations;
    }
}
