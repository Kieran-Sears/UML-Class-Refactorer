/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;

import DataTypes.Component;
import DataTypes.Class.Class;
import DataTypes.Class.Operation;
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

    RelationshipMatrix dependencies;
    ArrayList<CoreComponent> components;
    double couplingBetweenObjectClasses;
    double cohesionBetweenObjectClasses;
    double weightedMethodsPerClass;
    // double distanceFromMainSequence;

    public FitnessMetrics(RelationshipMatrix dependencies, ArrayList<CoreComponent> components) {
        this.dependencies = dependencies;
        this.components = components;
        couplingBetweenObjectClasses = couplingBetweenObjectClasses();
        cohesionBetweenObjectClasses = cohesionBetweenObjectClasses();
        weightedMethodsPerClass = weightedMethodsPerClass();
    }

    private double cohesionBetweenObjectClasses() {
        
        /*
            int numUses[][] = new int[highestClasses + 1][highestClasses + 1];
        //2.2 loop through each class
        classesPresent.stream().forEach((thisClass) -> {
            //get all the methods in this class
            ArrayList<String> methodsInthisClass = classMethodsMap.get(thisClass);
            if (methodsInthisClass != null) {
                //2.3 foreach methid in the class
                methodsInthisClass.stream().forEach((thisMethod) -> {
                    //get all of the attributes it uses
                    ArrayList<String> thisMethodAtts = UsesMap.get(thisMethod);
                    thisMethodAtts.stream().map((attrString) -> {
                        //and then what class they are in
                        int attClass = classAssignments.get(attrString);
                        System.out.println("dealing with method " + thisMethod + "in class " + thisClass + ": it uses attribute " + attrString + " which is is class " + attClass + "\n");
                        return attClass;
                    }).forEach((attClass) -> {
                        //2.5 finally increment the numberof uses
                        numUses[thisClass][attClass]++;
                    });
                });
            }
        });
        */
        
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
                //System.out.println("Class " + classe.getName() + " coupling between classes = " + (runningTotal - forDebugging));
            }
        }
        return runningTotal / numOfClasses;
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
        System.out.println("totalOperations " + totalOperations);
        System.out.println("nrOfClasses " + nrOfClasses);
        double average = ((double) totalOperations) / ((double) nrOfClasses);
        double total = 0;
        Iterator<Integer> iterator1 = WMPC.values().iterator();
        while (iterator1.hasNext()) {
            Integer next = iterator1.next();
            // sum of observed value for number of methods in each class vs. expected value (our average)
            total += ((next - average) * (next - average)) / average;
        }
        System.out.println("average " + average);
        System.out.println("total " + total);
        // degrees of freedom = number of classes minus 1
        ChiSquaredDistribution chi =  new ChiSquaredDistribution(nrOfClasses - 1);
        double cumulativeProbability = 1 - chi.cumulativeProbability(total);
     
        System.out.println("cumulativeProbability " + cumulativeProbability);
    
        return cumulativeProbability;
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
