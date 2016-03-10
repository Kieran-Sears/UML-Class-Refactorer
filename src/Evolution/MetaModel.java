/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;

import DataTypes.Class.Association;
import DataTypes.Component;
import java.util.ArrayList;

/**
 *
 * @author Kieran
 */
public class MetaModel {

    // fitness is for each class in the model, stored by class xmi:id key
    FitnessMetrics fitness;
    // shows connections between classes
    RelationshipMatrix dependencies;
    // holds all the methods attributes and classes
    ArrayList<Component> chromosome;
    // a list of associations present (composition, aggregation, generalisation etc)
    ArrayList<Association> associations;

    public void initialiseDependenciesAndFitness(){
        dependencies = new RelationshipMatrix(chromosome, associations);
        chromosome = dependencies.changeAssociationsToAttributes(chromosome, associations);
        dependencies.sortMethodDependencies(chromosome);
        System.out.println(dependencies.toString());
        fitness = new FitnessMetrics(dependencies, chromosome);
    }
    
    public void updateDependenciesAndFitness(){
     dependencies.sortMethodDependencies(chromosome);
     fitness = new FitnessMetrics(dependencies, chromosome);
    }
    
    
    
    public FitnessMetrics getFitness() {    
        return fitness;
    }

    // getters and setters
    public void setFitness(FitnessMetrics fitness) {
        this.fitness = fitness;
    }

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


    public RelationshipMatrix getDependencies() {
        return dependencies;
    }

    public void setDependencies(RelationshipMatrix dependencies) {
        this.dependencies = dependencies;
    }

    public ArrayList<Component> getChromosome() {
        return chromosome;
    }

    public void setChromosome(ArrayList<Component> chromosome) {
        this.chromosome = chromosome;
    }
    
    
}
