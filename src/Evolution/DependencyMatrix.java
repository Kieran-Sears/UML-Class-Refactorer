/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;

import DataTypes.Class.Association;
import DataTypes.Class.Class;
import DataTypes.Class.Operation;
import DataTypes.Class.OwnedEnd;
import DataTypes.Class.Parameter;
import DataTypes.Component;
import java.util.ArrayList;
import java.util.HashMap;

/**
 *
 * @author Kieran
 */
public class DependencyMatrix {

    HashMap<Integer, String> reverseLookupTable = new HashMap();
    HashMap<String, Integer> lookupTable = new HashMap();
    int[][] associationMatrix;

    public DependencyMatrix(ArrayList<Component> components, ArrayList<Association> associations) {

        // get number of classes for matrix
        ArrayList<Class> classes = new ArrayList();
        for (int i = 0; i < components.size(); i++) {
            if (components.get(i) instanceof Class) {
                classes.add((Class) components.get(i));
            }
        }

        // initialise matrix and add associations
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
        for (Association association : associations) {
             OwnedEnd source = (OwnedEnd) association.getOwnedEnds()[0];
            OwnedEnd target = (OwnedEnd) association.getOwnedEnds()[1];
            addDependency(source.getType(), target.getType(), source.getAggregation());
        }
        
        // add method dependencies
        DataTypes.Class.Class classee = null;
        for (Component component : components) {
            if (component instanceof DataTypes.Class.Class){
            classee = (DataTypes.Class.Class) component;
            }
            if (component instanceof Operation){
                ArrayList<Parameter> behaviourFeature = ((Operation) component).getBehaviourFeature();
                for (Parameter parameter : behaviourFeature) {
                  addDependency(classee.getID(), parameter.getType(), "dependency");
                }
            }
        }
        
    }

    public void addDependency(String sourceID, String targetID, String dependencyType) {
        Integer source = lookupTable.get(sourceID);
        Integer target = lookupTable.get(targetID);
        associationMatrix[source][target] = getDependencyIndex(dependencyType);
    }

    private Integer getDependencyIndex(String dependencyType) {
        switch (dependencyType) {
            case "dependency": // one class1 depends on class2 for an instance or method parameter but is not related to class1's state
                return 1;
            case "none": // association
                return 2;
            case "aggregate": // aggregate association
                return 3;
            case "composite": // composite association
                return 4;
            default:
                throw new AssertionError();
        }
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
