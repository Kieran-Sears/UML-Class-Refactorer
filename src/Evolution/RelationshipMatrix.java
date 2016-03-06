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

    HashMap<Integer, String> reverseLookupTable = new HashMap();
    HashMap<String, Integer> lookupTable = new HashMap();
    int[][] associationMatrix;

    public RelationshipMatrix(ArrayList<Component> components, ArrayList<Association> associations) {

        // get number of classes for matrix
        ArrayList<Class> classes = new ArrayList();
        for (int i = 0; i < components.size(); i++) {
            if (components.get(i) instanceof Class) {
                classes.add((Class) components.get(i));
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

        sortAssociations(components, associations);
        sortMethodDependencies(components, associations);
    }

    public void sortMethodDependencies(ArrayList<Component> components, ArrayList<Association> associations) {
        // add method dependencies
        DataTypes.Class.Class classee = null;
        for (Component component : components) {
            if (component instanceof DataTypes.Class.Class) {
                classee = (DataTypes.Class.Class) component;
            }
            if (component instanceof Operation) {
                ArrayList<Parameter> behaviourFeature = ((Operation) component).getBehaviourFeature();
                for (Parameter parameter : behaviourFeature) {
                    System.out.println("classee.getID() = " + classee.getID() + " / parameter.getType()" + parameter.getType());
                    addDependency(classee.getID(), parameter.getType());
                }
            }
        }
    }

    public void sortAssociations(ArrayList<Component> components, ArrayList<Association> associations) {
        // turn aggregations and compositions into dependencies
        for (Association association : associations) {
            // assuming all associations are attribute "collections of objects"
            Attribute attribute = new Attribute();
            // get original link between classes

            attribute.setID(UUID.randomUUID().toString());

            for (Component component : components) {
                if (component.getID().equalsIgnoreCase(association.getSource())) {
                    if (component instanceof DataTypes.Class.Class) {
                        for (Component componentz : components) {
                            if (componentz.getID().equalsIgnoreCase(association.getTarget())) {
                                if (componentz instanceof DataTypes.Class.Class) {
                                    addDependency(association.getSource(), association.getTarget());
                                }
                            }
                        }
                    }
                }
            }

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
                    addDependency(classe.getID(), attribute.getDependency());
                }
            }

            components.add(indexOf + 1, attribute);
        }

    }

    public void addDependency(String sourceID, String targetID) {
        Integer source = lookupTable.get(sourceID);
        Integer target = lookupTable.get(targetID);
        associationMatrix[source][target] = 1;
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
