/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;

import DataTypes.Class.Association;
import DataTypes.Class.Attribute;
import DataTypes.Class.Class;
import DataTypes.Class.Operation;
import DataTypes.Class.Parameter;
import DataTypes.Component;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

/**
 *
 * @author Kieran
 */
public class RelationshipMatrix {

    private final HashMap<Integer, String> reverseLookupTable = new HashMap();
    private final HashMap<String, Integer> lookupTable = new HashMap();
    public int[][] associationMatrix;

    public RelationshipMatrix(ArrayList<Component> components, ArrayList<Association> associations) {

        // get number of classes for matrix
        ArrayList<Class> classes = new ArrayList();
        for (Component component : components) {
            if (component instanceof Class) {
                classes.add((Class) component);
            }
        }

        // initialise matrix and add dependencies
        this.associationMatrix = new int[classes.size()][classes.size()];
        int classCounter = 0;
        for (Class classe : classes) {
            lookupTable.put(classe.getID(), classCounter);
            reverseLookupTable.put(classCounter, classe.getName());
            for (int j = 0; j < associationMatrix.length; j++) {
                associationMatrix[classCounter][j] = 0;
            }
            classCounter++;
        }
    }

    public ArrayList<Component> changeAssociationsToAttributes(ArrayList<Component> components, ArrayList<Association> associations){
      // turn aggregations and compositions into dependencies
        for (Association association : associations) {
            // assuming all associations are attribute "collections of objects"
            Attribute attribute = new Attribute();
            attribute.setID(UUID.randomUUID().toString());

            // turn aggregation / composition into attribute of collection with Type "target"
            for (Component component : components) {
                if (component.getID().equalsIgnoreCase(association.getTarget())) {
                    Class classe = (DataTypes.Class.Class) component;
                    attribute.setName("collection<" + classe.getName() + ">");
                    attribute.setDependency(classe.getID());
                }
            }
            int indexOf = -1;
            for (Component component : components) {
                if (component.getID().equalsIgnoreCase(association.getSource())) {
                    Class classe = (DataTypes.Class.Class) component;
                    indexOf = components.indexOf(classe);
                }
            }
            // add the new attribute which was once an association. association array can now be ignored
            components.add(indexOf + 1, attribute);
        }
        return components;
    }
    
    public void sortMethodDependencies(ArrayList<Component> components) {
        // add method dependencies
        DataTypes.Class.Class classee = null;
        for (Component component : components) {
            // get the class the operation belongs to
            if (component instanceof DataTypes.Class.Class) {
                classee = (DataTypes.Class.Class) component;
                System.out.println("    class :" + classee.getName());
            }
            // cycle operations for this class and get their parameter types
            if (component instanceof Operation) {
                Operation operation = (Operation) component;
                System.out.println("#operation :" + operation.getName());
                ArrayList<Parameter> parameters = operation.getParameters();
                for (Parameter parameter : parameters) {
                    System.out.println("-parameter :" + parameter.getType());
                    // each param that uses another class is a dependency
                    addDependency(classee.getID(), parameter.getType());
                }
            }
            if (component instanceof Attribute) {
                Attribute attribute = (Attribute) component;
                if (attribute.getDependency() != null) {
                    System.out.println("#attribute " + attribute.getName());
                    System.out.println("-dependency " + attribute.getDependency());
                    addDependency(classee.getID(), attribute.getDependency());
                }
            }
        }
    }

    private void addDependency(String sourceID, String targetID) {
        Integer source = lookupTable.get(sourceID);
        Integer target = lookupTable.get(targetID);
        associationMatrix[source][target]++;
    }

    public String ReverseLookupClass(int id) {
        return reverseLookupTable.get(id);
    }

    public Integer lookupClass(String id) {
        return lookupTable.get(id);
    }

    @Override
    public String toString() {
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

}
