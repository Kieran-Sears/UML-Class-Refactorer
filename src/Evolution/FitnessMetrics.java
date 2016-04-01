/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;

import DataTypes.Component;
import DataTypes.Class.Class;
import DataTypes.Class.Operation;
import DataTypes.Class.Parameter;
import DataTypes.CoreComponent;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;

/**
 *
 * @author Kieran
 */
public class FitnessMetrics {

   private RelationshipMatrix dependencies;
   private ArrayList<CoreComponent> components;
    private double couplingBetweenObjectClasses;
  private  double cohesionBetweenObjectClasses;
  private  double weightedMethodsPerClass;
    // double distanceFromMainSequence;

    public FitnessMetrics(RelationshipMatrix dependencies, ArrayList<CoreComponent> components) {
        this.dependencies = dependencies;
        this.components = components;
        couplingBetweenObjectClasses = couplingBetweenObjectClasses();
        cohesionBetweenObjectClasses = cohesionBetweenObjectClasses();
        weightedMethodsPerClass = weightedMethodsPerClass();
    }

    private double cohesionBetweenObjectClasses() {

        double internals = 0;
        double externals = 0;
        double numOfClasses = 0;
        Class classe = null;
        // go through components
        for (Component component : components) {
            // isolate the current class
            if (component instanceof DataTypes.Class.Class) {
                numOfClasses++;
                classe = (DataTypes.Class.Class) component;
            }
            // get each operation in class in turn
            if (component instanceof DataTypes.Class.Operation) {
                Operation operation = (DataTypes.Class.Operation) component;
                ArrayList<Parameter> parameters = operation.getParameters();
                // cycle through operation's parameters
                for (Parameter parameter : parameters) {
                    String attributeName = null;
                    for (CoreComponent comp : components) {
                        if (comp.getID().equalsIgnoreCase(parameter.getType())) {
                            attributeName = comp.getName();
                        }
                    }

                    // find out if param is internal or ext to class
                    int i = 0;
                    DataTypes.Class.Class class2 = null;
                    while (i != -1) {
                        if (i >= components.size()) {
                            i = -1;
                        } else {
                            CoreComponent get = components.get(i);
                            if (get instanceof DataTypes.Class.Class) {
                                class2 = (Class) get;
                            }
                            if (class2.getID().equalsIgnoreCase(classe.getID()) && get instanceof DataTypes.Class.Attribute && get.getName().equalsIgnoreCase("collection<" + attributeName + ">")) {
                                internals++;
                            }
                            if (!class2.getID().equalsIgnoreCase(classe.getID()) && get instanceof DataTypes.Class.Attribute && get.getName().equalsIgnoreCase("collection<" + attributeName + ">")) {
                                externals++;
                            }
                            i++;
                        }
                    }
                }
            }
        }

        double percentageOfCohesiveMethods =  ( internals / (internals + externals)) * 100;
        if (percentageOfCohesiveMethods == Double.POSITIVE_INFINITY) {
            return 0;
        } else {
            return percentageOfCohesiveMethods;
        }
    }

    private double couplingBetweenObjectClasses() {
        double runningTotal = 0;
        double numOfClasses = 0;
        for (Component component : components) {
            if (component instanceof DataTypes.Class.Class) {
                numOfClasses++;
                Class classe = (DataTypes.Class.Class) component;
                Integer classID = dependencies.lookupClass(classe.getID());
                for (int i = 0; i < dependencies.associationMatrix.length; i++) {
                    for (int j = 0; j < dependencies.associationMatrix.length; j++) {
                        if (i == classID && i != j) { //   if ((i == classID || j == classID) && i != j) {
                            if (dependencies.associationMatrix[i][j] != 0) {   
                                runningTotal++;
                            }
                        }
                    }
                }
            }
        }
    double percent =  (runningTotal / ((numOfClasses* (numOfClasses -1)) / 2)) * 100;
        System.out.println("percent " + percent);
        return percent;
    }

    private double weightedMethodsPerClass() {
        int totalOperations = 0;

        HashMap<Class, Integer> WMPC = new HashMap();
        Iterator<CoreComponent> iterator = components.iterator();
        Class classe = null;

        while (iterator.hasNext()) {
            Component next = iterator.next();
            if (next instanceof Class) {
                classe = (Class) next;
                WMPC.put(classe, 0);
            }
            if (next instanceof Operation) {
                totalOperations++;
                if (WMPC.containsKey(classe)) {
                    Integer get = WMPC.get(classe);
                    get++;
                    WMPC.put(classe, get);
                } else {
                    //  System.out.println("no class for this operation, check a class is first component in chromosome");
                }
            }
        }

        int nrOfClasses = WMPC.size();
        double average = ((double) totalOperations) / ((double) nrOfClasses);
        double total = 0;
        Iterator<Integer> iterator1 = WMPC.values().iterator();
        while (iterator1.hasNext()) {
            Integer next = iterator1.next();
            // sum of observed value for number of methods in each class vs. expected value (our average)
            total += ((next - average) * (next - average)) / average;
        }
        // degrees of freedom = number of classes minus 1
        ChiSquaredDistribution chi = new ChiSquaredDistribution(nrOfClasses - 1);
        double cumulativeProbability = (1 - chi.cumulativeProbability(total)) * 100;
        return cumulativeProbability;
    }

    public double getOverallFitness(){
            return cohesionBetweenObjectClasses + (100 - couplingBetweenObjectClasses) + weightedMethodsPerClass;
    }
    
    public double getCouplingBetweenObjectClasses() {
        return couplingBetweenObjectClasses;
    }

    public double getWeightedMethodsPerClass() {
        return weightedMethodsPerClass;
    }

    public double getCohesionBetweenObjectClasses() {
        return cohesionBetweenObjectClasses;
    }

    @Override
    public String toString() {
        return "FitnessMetrics{" + "couplingBetweenObjectClasses=" + couplingBetweenObjectClasses + ", cohesionBetweenObjectClasses=" + cohesionBetweenObjectClasses + ", weightedMethodsPerClass=" + weightedMethodsPerClass + '}';
    }

}
