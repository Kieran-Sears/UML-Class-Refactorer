/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;

import DataTypes.Component;
import DataTypes.Class.Class;
import DataTypes.Class.Operation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import org.apache.commons.math3.distribution.ChiSquaredDistribution;

/**
 *
 * @author Kieran
 */
public class FitnessMetrics {

    RelationshipMatrix dependencies;
    ArrayList<Component> components;
    double couplingBetweenObjectClasses;
    double cohesionBetweenObjectClasses;
    double weightedMethodsPerClass;
    // double distanceFromMainSequence;

    public FitnessMetrics(RelationshipMatrix dependencies, ArrayList<Component> components) {
        this.dependencies = dependencies;
        this.components = components;
        couplingBetweenObjectClasses = couplingBetweenObjectClasses();
        cohesionBetweenObjectClasses = cohesionBetweenObjectClasses();
        weightedMethodsPerClass = weightedMethodsPerClass();
    }

    private double cohesionBetweenObjectClasses() {
         int runningTotal = 0;
        int numOfClasses = 0;
        // go through components
        for (Component component : components) {
            // isolate the current class
            if (component instanceof DataTypes.Class.Class) {
                numOfClasses++;
                Class classe = (DataTypes.Class.Class) component;
                Integer classID = dependencies.lookupClass(classe.getID());
              //  int forDebugging = runningTotal;
                // find dependencies for the current class
                for (int i = 0; i < dependencies.associationMatrix.length; i++) {
                    for (int j = 0; j < dependencies.associationMatrix.length; j++) {
                        if (i != classID || j != classID) {
                            if (dependencies.associationMatrix[i][j] == 1) {
                                runningTotal++;
                            }
                        }
                    }
                }
              //  System.out.println("Class " + classe.getName() + " cohesion between classes = " + (runningTotal - forDebugging));
            }
        }
        return runningTotal / numOfClasses;
    }

    private double couplingBetweenObjectClasses() {
        int runningTotal = 0;
        int numOfClasses = 0;
        for (Component component : components) {
            if (component instanceof DataTypes.Class.Class) {
                numOfClasses++;
                Class classe = (DataTypes.Class.Class) component;
                Integer classID = dependencies.lookupClass(classe.getID());
                int forDebugging = runningTotal;
                for (int i = 0; i < dependencies.associationMatrix.length; i++) {
                    for (int j = 0; j < dependencies.associationMatrix.length; j++) {
                        if (i == classID || j == classID) {
                            if (dependencies.associationMatrix[i][j] == 1) {
                                runningTotal++;
                            }
                        }
                    }
                }
                System.out.println("Class " + classe.getName() + " coupling between classes = " + (runningTotal - forDebugging));
            }
        }
        return runningTotal / numOfClasses;
    }

    private double weightedMethodsPerClass() {
        int totalOperations = 0;
      
 
        HashMap<Class, Integer> WMPC = new HashMap();
        Iterator<Component> iterator = components.iterator();
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
                    System.out.println("no class for this operation, check a class is first component in chromosome");
                }
            }
        }

        int nrOfClasses = WMPC.size();
        double average = totalOperations / nrOfClasses;
        double total = 0;
        Iterator<Integer> iterator1 = WMPC.values().iterator();
        while (iterator1.hasNext()) {
            Integer next = iterator1.next();
            total += ((next - average) * (next - average)) / average;
        }


        // dof = nrOfClasses -1
        return new ChiSquaredDistribution(nrOfClasses - 1).cumulativeProbability(total);
    }

//    public double distanceFromMainSequence(ArrayList<Component> components, RelationshipMatrix dependencies) {
//
////    D = (A + I − 1) / √2 
////  Where:
////    I = Efferent Coupling / ( Efferent Coupling + Afferent Coupling )
////    A = Abstract Classes / Total Classes
////      
////        Afferent couplings : A class afferent couplings is a measure of how many other classes use the specific class.
////        Efferent couplings : A class efferent couplings is a measure of how many different classes are used by the specific class.
//        int abstractClasses = 0;
//        int efferentCoupling = 0;
//        int afferentCoupling = 0;
//        int totalClasses = 0;
//        Iterator<Component> iterator = components.iterator();
//        while (iterator.hasNext()) {
//            Object next = iterator.next();
//            if (next instanceof Class) {
//                totalClasses++;
//                Class _class = (Class) next;
//                if (_class.getIsAbstract()) {
//                    abstractClasses++;
//                }
//                Integer dependencyID = dependencies.lookupTable.get(_class.getID());
//                int[] efferentArray = dependencies.associationMatrix[dependencyID];
//                for (int i = 0; i < efferentArray.length; i++) {
//                    if (efferentArray[i] != 0) {
//                        efferentCoupling++;
//                    }
//                }
//                int[][] afferentArray = dependencies.associationMatrix;
//                for (int i = 0; i < afferentArray.length; i++) {
//                    if (i != dependencyID) {
//                        int[] afferentArray1 = afferentArray[i];
//                        for (int j = 0; j < afferentArray1.length; j++) {
//                            if (j != dependencyID) {
//                                if (afferentArray1[j] != 0) {
//                                    afferentCoupling++;
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//        }
//        return ((abstractClasses / totalClasses) + (efferentCoupling / (efferentCoupling + afferentCoupling))) / Math.sqrt(2);
//    }
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
        return "\nCouplingBetweenObjectClasses=" + couplingBetweenObjectClasses
                + ", \nWeightedMethodsPerClass=" + weightedMethodsPerClass
                + ",}\n\n";
    }

}
