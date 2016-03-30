/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;

import DataTypes.Class.Association;
import DataTypes.CoreComponent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kieran
 */
public class MetaModel {

    // fitness is for each class in the model, stored by class xmi:id key
    private FitnessMetrics fitness;
    // shows connections between classes
    private RelationshipMatrix dependencies;
    // holds all the methods attributes and classes
    private ArrayList<CoreComponent> chromosome;
    // a list of associations present (composition, aggregation, generalisation etc)
    private ArrayList<Association> associations;

    public void initialiseDependenciesAndFitness(){
        dependencies = new RelationshipMatrix(chromosome, associations);
        chromosome = dependencies.changeAssociationsToAttributes(chromosome, associations);
        updateDependenciesAndFitness();
    }
    
    public void updateDependenciesAndFitness(){
     dependencies.sortMethodDependencies(chromosome);
     fitness = new FitnessMetrics(dependencies, chromosome);
    }
    
    
       // getters and setters 
    public FitnessMetrics getFitness() {    
        return fitness;
    }

    public void setFitness(FitnessMetrics fitness) {
        this.fitness = fitness;
    }

    public ArrayList<CoreComponent> getComponents() {
        return chromosome;
    }

    public void setComponents(ArrayList<CoreComponent> components) {
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
    // end of getters and setters
    
     // Printing results out
    public void outputResultsToFile(File file) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file, "UTF-8");
                writer.println(chromosome.toString());
                writer.println(dependencies.toString());
                writer.println(fitness.toString());
                writer.println("\n##################################\n");
            
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(GeneticAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }

    public void outputResultsToConsole() {
        String string = "";
            string += "\n''''''''''''''''''''''''''''''\n";
            string += chromosome.toString();
            string += dependencies.toString();
            string += fitness.toString();
        System.out.println(string);
    } 
    // end of printing
}
