/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;

import DataTypes.Class.Association;
import DataTypes.Class.Attribute;
import DataTypes.Class.Operation;
import DataTypes.Class.Parameter;
import DataTypes.Component;
import DataTypes.CoreComponent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kieran
 */
public class MetaModel {

    // holds all the methods attributes and classes
    private ArrayList<CoreComponent> components;
    private int NumberOfClasses;
    private int NumberOfAttributes;
    private int NumberOfOperations;
    public HashMap<Integer, String> reverseLookupTable = new HashMap();
    public HashMap<String, Integer> lookupTable = new HashMap();
    public int[][] associationMatrix;

    public MetaModel() {
        NumberOfClasses = 0;
        NumberOfAttributes = 0;
        NumberOfOperations = 0;
    }

    public void sortMethodDependencies() {

        lookupTable.clear();
        reverseLookupTable.clear();

        // get number of classes for matrix
        ArrayList<DataTypes.Class.Class> classes = new ArrayList();
        for (Component component : components) {
            if (component instanceof DataTypes.Class.Class) {
                classes.add((DataTypes.Class.Class) component);
            }
        }

        // initialise matrix
        this.associationMatrix = new int[classes.size()][classes.size()];
        int classCounter = 0;
        for (DataTypes.Class.Class classe : classes) {
            lookupTable.put(classe.getID(), classCounter);
            reverseLookupTable.put(classCounter, classe.getName());
            for (int j = 0; j < associationMatrix.length; j++) {
                associationMatrix[classCounter][j] = 0;
            }
            classCounter++;
        }

        // add method dependencies
        DataTypes.Class.Class classee = null;
        for (Component component : components) {
            // search each class in turn
            if (component instanceof DataTypes.Class.Class) {
                classee = (DataTypes.Class.Class) component;
            }
            // cycle operations for this class and get their parameter types
            if (component instanceof Operation) {
                Operation operation = (Operation) component;
                ArrayList<Parameter> parameters = operation.getParameters();
                for (Parameter parameter : parameters) {
                    // each param that uses another class is a dependency
                    addDependency(classee.getID(), parameter.getType());
                    //  System.out.println(classee.getName() + " dependent on " + reverseLookupTable.get(lookupTable.get(parameter.getType())) + " because of " + operation.getName() + " (" + reverseLookupTable.get(lookupTable.get(parameter.getType())) + ")");
                }
            }
            if (component instanceof Attribute) {
                Attribute attribute = (Attribute) component;
                if (attribute.getDependency() != null) {
                    addDependency(classee.getID(), attribute.getDependency());
                    //  System.out.println(classee.getName() + " dependent on " + reverseLookupTable.get(lookupTable.get(attribute.getDependency())) + " because of " + attribute.getName());
                }
            }
        }
        //  System.out.println(this.toString());
    }

    public ArrayList<CoreComponent> changeAssociationsToAttributes(ArrayList<Association> associations) {
        // turn aggregations and compositions into dependencies
        for (Association association : associations) {
            // assuming all associations are attribute "collections of objects"
            Attribute attribute = new Attribute();
            attribute.setID(UUID.randomUUID().toString());

            // turn aggregation / composition into attribute of collection with Type "target"
            for (Component component : components) {
                if (component.getID().equalsIgnoreCase(association.getTarget())) {
                    DataTypes.Class.Class classe = (DataTypes.Class.Class) component;
                    attribute.setName("collection<" + classe.getName() + ">");
                    attribute.setDependency(classe.getID());
                }
            }
            int indexOf = -1;
            for (Component component : components) {
                if (component.getID().equalsIgnoreCase(association.getSource())) {
                    DataTypes.Class.Class classe = (DataTypes.Class.Class) component;
                    indexOf = components.indexOf(classe);
                }
            }
            // add the new attribute which was once an association. association array can now be ignored
            components.add(indexOf + 1, attribute);
        }
        return components;
    }

    private void addDependency(String sourceID, String targetID) {
        Integer source = lookupTable.get(sourceID);
        Integer target = lookupTable.get(targetID);
        associationMatrix[source][target]++;
    }

    // getters and setters 
    public ArrayList<CoreComponent> getComponents() {
        return components;
    }

    public void setComponents(ArrayList<CoreComponent> components) {
        this.components = components;
    }

    public int getNumberOfClasses() {
        return NumberOfClasses;
    }

    public void incrementClassCount() {
        this.NumberOfClasses++;
    }

    public int getNumberOfAttributes() {
        return NumberOfAttributes;
    }

    public void incrementAttributeCount() {
        this.NumberOfAttributes++;
    }

    public int getNumberOfOperations() {
        return NumberOfOperations;
    }

    public void incrementOperationCount() {
        this.NumberOfOperations++;
    }

    public void setNumberOfClasses(int NumberOfClasses) {
        this.NumberOfClasses = NumberOfClasses;
    }

    public void setNumberOfAttributes(int NumberOfAttributes) {
        this.NumberOfAttributes = NumberOfAttributes;
    }

    public void setNumberOfOperations(int NumberOfOperations) {
        this.NumberOfOperations = NumberOfOperations;
    }

    
    
    // end of getters and setters
    // Printing results out
    public void outputResultsToFile(File file) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file, "UTF-8");
            writer.println(components.toString());
            writer.println(printDependencyMatrix());
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
        string += components.toString();
        string += printDependencyMatrix();
        System.out.println(string);
    }

    public String printDependencyMatrix() {
        String string = "\nDependencyMatrix=\n";
        for (int i = 0; i < associationMatrix.length; i++) {
            string += String.format("%-20s", reverseLookupTable.get(i)) + ": ";
            int[] associationMatrix1 = associationMatrix[i];
            for (int j = 0; j < associationMatrix1.length; j++) {
                int b = associationMatrix1[j];
                string += reverseLookupTable.get(j) + ":" + b + ", ";
            }
            string += "\n";
        }
        string += "\n";
        return string;
    }

    // end of printing
}
