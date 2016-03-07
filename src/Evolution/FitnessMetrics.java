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
import java.util.Iterator;

/**
 *
 * @author Kieran
 */
public class FitnessMetrics {

    RelationshipMatrix dependencies;
    ArrayList<Component> components;
    float lackOfCohesion;
    float couplingBetweenObjectClasses;
    float weightedMethodsPerClass;
   // float distanceFromMainSequence;

    public FitnessMetrics(RelationshipMatrix dependencies, ArrayList<Component> components) {
        this.dependencies = dependencies;
        this.components = components;
        lackOfCohesion = lackOfCohesion();
        couplingBetweenObjectClasses = couplingBetweenObjectClasses();
        weightedMethodsPerClass = weightedMethodsPerClass();
        // distanceFromMainSequence = distanceFromMainSequence();
    }

    private int lackOfCohesion() {
        return 0;
    }

    private float couplingBetweenObjectClasses() {
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

    private float weightedMethodsPerClass() {
        Iterator<Component> iterator = components.iterator();
        int nrOfClasses = 0;
        int nrOfOperations = 0;
        while (iterator.hasNext()) {
            Component next = iterator.next();
            if (next instanceof Class) {
                nrOfClasses++;
            }
            if (next instanceof Operation) {
                nrOfOperations++;
            }
        }
        if (nrOfClasses == 0) {
            throw new IllegalArgumentException("Argument 'nrOfClasses' is 0");
        } else {
            return nrOfOperations / nrOfClasses;
        }
    }

    private void cohesionOfMethods() {

    }

//    public float distanceFromMainSequence(ArrayList<Component> components, RelationshipMatrix dependencies) {
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
    @Override
    public String toString() {
        return " \nLackOfCohesion=" + lackOfCohesion
                + ", \nCouplingBetweenObjectClasses=" + couplingBetweenObjectClasses
                + ", \nWeightedMethodsPerClass=" + weightedMethodsPerClass
                // + ", \ndistanceFromMainSequence=" + distanceFromMainSequence
                + ",}\n\n";
    }

}
