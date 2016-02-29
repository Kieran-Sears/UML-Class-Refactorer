/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;

import DataTypes.Class.Association;
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

    Class classe;
    int LackOfCohesion;
    int DepthOfInheritanceTree;
    int CouplingBetweenObjectClasses;
    int NumberOfChildren;
    int ResponseForAClass;
    int WeightedMethodsPerClass;
    double distanceFromMainSequence;


    public int LackOfCohesion() {
        return 0;
    }

    public int DepthOfInheritanceTree() {
        return 0;
    }

    public int CouplingBetweenObjectClasses() {
        return 0;
    }

    public int NumberOfChildren() {
        return 0;
    }

    public int ResponseForAClass() {
        return 0;
    }

    public int WeightedMethodsPerClass(ArrayList<Component> components) {
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
        }
        else {
        return nrOfOperations / nrOfClasses;
        }
    }

    public void cohesionOfMethods(){
 
    }
    
    public double distanceFromMainSequence(ArrayList<Component> components, DependencyMatrix dependencies) {

        int abstractClasses = 0;
        int efferentCoupling = 0;
        int afferentCoupling = 0;
        int totalClasses = 0;
        Iterator<Component> iterator = components.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (next instanceof Class) {
                totalClasses++;
                Class _class = (Class) next;
                if (_class.getIsAbstract()) {
                    abstractClasses++;
                }
                Integer dependencyID = dependencies.lookupTable.get(_class.getID());
                int[] efferentArray = dependencies.associationMatrix[dependencyID];
                for (int i = 0; i < efferentArray.length; i++) {
                    if (efferentArray[i] != 0) {
                        efferentCoupling++;
                    }
                }
                int[][] afferentArray = dependencies.associationMatrix;
                for (int i = 0; i < afferentArray.length; i++) {
                    if (i != dependencyID) {
                        int[] afferentArray1 = afferentArray[i];
                        for (int j = 0; j < afferentArray1.length; j++) {
                            if (j != dependencyID) {
                                if (afferentArray1[j] != 0) {
                                    afferentCoupling++;
                                }
                            }
                        }
                    }
                }
            }
        }

//        int abstractClasses = 0;
//        int efferentCoupling = 0;
//        int afferentCoupling = 0;
//        int totalClasses = 0;
//    D = (A + I − 1) / √2 
//  Where:
//    I = Efferent Coupling / ( Efferent Coupling + Afferent Coupling )
//    A = Abstract Classes / Total Classes
//      
//        Afferent couplings : A class afferent couplings is a measure of how many other classes use the specific class.
//        Efferent couplings : A class efferent couplings is a measure of how many different classes are used by the specific class.
        double D = ((abstractClasses / totalClasses) + (efferentCoupling / (efferentCoupling + afferentCoupling))) / Math.sqrt(2);

        return D; // TODO D.normalised to fall within 0 - 1 range
    }

    @Override
    public String toString() {
        return "Fitness{"
                + " \nLackOfCohesion=" + LackOfCohesion
                + ", \nDepthOfInheritanceTree=" + DepthOfInheritanceTree
                + ", \nCouplingBetweenObjectClasses=" + CouplingBetweenObjectClasses
                + ", \nNumberOfChildren=" + NumberOfChildren
                + ", \nResponseForAClass=" + ResponseForAClass
                + ", \nWeightedMethodsPerClass=" + WeightedMethodsPerClass
                + ", \ndistanceFromMainSequence=" + distanceFromMainSequence
                + ",\n}";
    }

}
