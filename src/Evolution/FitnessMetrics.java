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


    public double cohesionBetweenObjectClasses(MetaModel model) {

        double internals = 0;
        double externals = 0;
        double numOfClasses = 0;
        Class classe = null;
        // go through components
        for (Component component : model.getComponents()) {
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
                    for (CoreComponent comp : model.getComponents()) {
                        if (comp.getID().equalsIgnoreCase(parameter.getType())) {
                            attributeName = comp.getName();
                        }
                    }

                    // find out if param is internal or ext to class
                    int i = 0;
                    DataTypes.Class.Class class2 = null;
                    while (i != -1) {
                        if (i >= model.getComponents().size()) {
                            i = -1;
                        } else {
                            CoreComponent get =model.getComponents().get(i);
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

    public double couplingBetweenObjectClasses(MetaModel model) {
        double runningTotal = 0;
        double numOfClasses = 0;
        for (Component component : model.getComponents()) {
            if (component instanceof DataTypes.Class.Class) {
                numOfClasses++;
                Class classe = (DataTypes.Class.Class) component;
                Integer classID = model.lookupTable.get(classe.getID());
                for (int i = 0; i < model.associationMatrix.length; i++) {
                    for (int j = 0; j < model.associationMatrix.length; j++) {
                        if (i == classID && i != j) { //   if ((i == classID || j == classID) && i != j) {
                            if (model.associationMatrix[i][j] != 0) {   
                                runningTotal++;
                            }
                        }
                    }
                }
            }
        }
    double percent =  (runningTotal / ((numOfClasses* (numOfClasses -1)) / 2)) * 100;
        return percent;
    }

    public double weightedMethodsPerClass(MetaModel model) {
        int totalOperations = 0;

        HashMap<Class, Integer> WMPC = new HashMap();
        Iterator<CoreComponent> iterator = model.getComponents().iterator();
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

    public double getOverallFitness(MetaModel model){
            return cohesionBetweenObjectClasses(model) + (100 - couplingBetweenObjectClasses(model)) + weightedMethodsPerClass(model);
    }
    

}
