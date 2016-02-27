/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;

import DataTypes.Component;
import DataTypes.Class.Class;
import DataTypes.Class.Operation;
import java.util.Iterator;

/**
 *
 * @author Kieran
 */
public class Fitness {

    Genome chromosome;
    int LackOfCohesion;
    int DepthOfInheritanceTree;
    int CouplingBetweenObjectClasses;
    int NumberOfChildren;
    int ResponseForAClass;
    int WeightedMethodsPerClass;
    double distanceFromMainSequence;

    public void updateFitnessValues(Genome chromosome) {
        this.chromosome = chromosome;
        this.LackOfCohesion = LackOfCohesion();
        this.DepthOfInheritanceTree = DepthOfInheritanceTree();
        this.CouplingBetweenObjectClasses = CouplingBetweenObjectClasses();
        this.NumberOfChildren = NumberOfChildren();
        this.ResponseForAClass = ResponseForAClass();
        this.WeightedMethodsPerClass = WeightedMethodsPerClass();
        //this.distanceFromMainSequence = distanceFromMainSequence();
    }

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

    public int WeightedMethodsPerClass() {
        Iterator<Component> iterator = chromosome.components.iterator();
        int nrOfClasses = 0;
        int nrOfOperations = 0;
        while (iterator.hasNext()) {
            Component next = iterator.next();
            if (next instanceof Class) {
                nrOfClasses++;
            }
            if (next instanceof Operation) {
                nrOfOperations++;
            } else {
                // System.out.println("Erm Mac, we got an unknown here");
            }
        }
        if (nrOfClasses == 0) {
            throw new IllegalArgumentException("Argument 'nrOfClasses' is 0");
        }
        else {
        return nrOfOperations / nrOfClasses;
        }
    }

    public double distanceFromMainSequence() {

        int abstractClasses = 0;
        int efferentCoupling = 0;
        int afferentCoupling = 0;
        int totalClasses = 0;
        Iterator<Component> iterator = chromosome.components.iterator();
        while (iterator.hasNext()) {
            Object next = iterator.next();
            if (next instanceof Class) {
                totalClasses++;
                Class _class = (Class) next;
                if (_class.getIsAbstract()) {
                    abstractClasses++;
                }
                Integer dependencyID = chromosome.dependencies.lookupTable.get(_class.getID());
                int[] efferentArray = chromosome.dependencies.associationMatrix[dependencyID];
                for (int i = 0; i < efferentArray.length; i++) {
                    if (efferentArray[i] != 0) {
                        efferentCoupling++;
                    }
                }
                int[][] afferentArray = chromosome.dependencies.associationMatrix;
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
