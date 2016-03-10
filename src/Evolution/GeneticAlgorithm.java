/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;

import DesignPatterns.MutationHeuristic;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Kieran
 */
public class GeneticAlgorithm {

    // attributes
    private final ArrayList<MetaModel> population = new ArrayList();
    private int populationSize;
    private double mutationRate;
    private double crossoverRate;

    public void initialiseGA(MetaModel model, int populationSize, double mutationRate, double crossoverRate) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        model.initialiseDependenciesAndFitness();
        population.add(model);

        for (int i = 1; i < populationSize; i++) {
            MetaModel crossedAndMutated = mutate(model);
            model.updateDependenciesAndFitness();
            population.add(crossedAndMutated);
        }
    }

    public void evolvePopulation() {
        rouletteSelection(population);
        for (MetaModel model : population) {
            MetaModel crossedAndMutated = mutate(model);
            crossedAndMutated.dependencies.sortMethodDependencies(crossedAndMutated.chromosome);
            crossedAndMutated.updateDependenciesAndFitness();
        }
    }

    public ArrayList<MetaModel> rouletteSelection(ArrayList<MetaModel> population) {
        double randNum;
        int remainder = populationSize % 3;
        int divisor = populationSize - remainder;
        int count = divisor / 3;

        ArrayList<MetaModel> bestIndividuals = new ArrayList();
        ArrayList<MetaModel> lowestCoupledModels = new ArrayList(); // minimization
        ArrayList<MetaModel> highestCohesiveModels = new ArrayList();
        ArrayList<MetaModel> bestMethodDistrobutionModels = new ArrayList();

        // objective find the individuals with the lowest coupling and the highest cohesion
        for (MetaModel individual : population) {
            MetaModel chosen = null;
            double totalCouplingFitness = 0;
            double coupleMax = 0;
            double coupleMin = 0;
            double WMPCMax = 0;
            double WMPCMin = 0;
            double totalCohesionFitness = 0;
            double totalWeightedMethodsFitness = 0;
            double t;
            
            totalCohesionFitness += individual.getFitness().getCohesionBetweenObjectClasses();
            
            totalCouplingFitness += individual.getFitness().getCouplingBetweenObjectClasses();
            if (coupleMax < individual.getFitness().getCohesionBetweenObjectClasses()) {
                coupleMax = individual.getFitness().getCohesionBetweenObjectClasses();
            }
            if (coupleMin > individual.getFitness().getCohesionBetweenObjectClasses() || coupleMin == 0) {
                coupleMin = individual.getFitness().getCohesionBetweenObjectClasses();
            }
            
            totalWeightedMethodsFitness += individual.getFitness().getWeightedMethodsPerClass();
            if (WMPCMax < individual.getFitness().getWeightedMethodsPerClass()) {
                WMPCMax = individual.getFitness().getWeightedMethodsPerClass();
            }
            if (coupleMin > individual.getFitness().getWeightedMethodsPerClass() || coupleMin == 0) {
                coupleMin = individual.getFitness().getWeightedMethodsPerClass();
            }
            

             // finding the lowest coupled individuals
            randNum = (double) (Math.random() * totalCouplingFitness);
            chosen = population.get(0);
            t = coupleMax + coupleMin;
            for (int i = 0; i < populationSize; i++) {
                randNum -= (t - population.get(i).getFitness().getCouplingBetweenObjectClasses());
                if (randNum < 0) {
                    chosen = population.get(i);
                }
            }
            lowestCoupledModels.add(chosen);

            // finding the highest cohesive individuals
            randNum = (double) (Math.random() * totalCohesionFitness);
            chosen = population.get(0);
            for (int i = 0; i < populationSize; i++) {
                randNum -= population.get(i).getFitness().getCouplingBetweenObjectClasses();
                if (randNum < 0) {
                    chosen = population.get(i);
                }
            }
            highestCohesiveModels.add(chosen);
        
        
          // finding the most significant distribution 
            randNum = (double) (Math.random() * totalCohesionFitness);
            chosen = population.get(0);
            t = coupleMax + coupleMin;
            for (int i = 0; i < populationSize; i++) {
                randNum -= (t - population.get(i).getFitness().getWeightedMethodsPerClass());
                if (randNum < 0) {
                    chosen = population.get(i);
                }
            }
            bestMethodDistrobutionModels.add(chosen);
        }

        // adding chosen from each group to the final parent selection
        for (int i = 0; i < count; i++) {
            bestIndividuals.add(lowestCoupledModels.get(i));
            bestIndividuals.add(highestCohesiveModels.get(i));
            bestIndividuals.add(bestMethodDistrobutionModels.get(i));
        }
        while (remainder != 0) {
            bestIndividuals.add(lowestCoupledModels.get((int) (Math.random() * populationSize)));
            remainder--;
            bestIndividuals.add(highestCohesiveModels.get((int) (Math.random() * populationSize)));
            remainder--;
            bestIndividuals.add(bestMethodDistrobutionModels.get((int) (Math.random() * populationSize)));
            remainder--;
        }

        return bestIndividuals;
    }

    // evolution operators
    private MetaModel mutate(MetaModel model) {
        // default random mutation
        return model;
    }

    private MetaModel mutate(MetaModel model, MutationHeuristic heuristic) {
        // guided mutation
        // if close to a target pattern converge on it
        // if close to an anti-pattern mutate away from it - random?
        return model;
    }

    // end of evolution operators
    // Printing results out
    public void outputResultsToFile(File file) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file, "UTF-8");
            Iterator<MetaModel> iterator = population.iterator();
            while (iterator.hasNext()) {
                writer.println(iterator.next().chromosome.toString());
                writer.println(iterator.next().dependencies.toString());
                writer.println(iterator.next().fitness.toString());
                writer.println("\n##################################\n");
            }
            writer.close();
        } catch (FileNotFoundException | UnsupportedEncodingException ex) {
            Logger.getLogger(GeneticAlgorithm.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            writer.close();
        }
    }

    public void outputResultsToConsole() {
        String string = "";
        Iterator<MetaModel> iterator = population.iterator();
        while (iterator.hasNext()) {
            MetaModel next = iterator.next();
            string += "\n''''''''''''''''''''''''''''''\n";
            string += next.chromosome.toString();
            string += next.dependencies.toString();
            string += next.fitness.toString();
        }
        System.out.println(string);
    }

    public HashMap<String, Object> returnResultsAsHashmap() {
        HashMap<String, Object> HM = new HashMap();
        Iterator<MetaModel> iterator = population.iterator();
        while (iterator.hasNext()) {
            MetaModel next = iterator.next();
            HM.put("components", next.chromosome);
            HM.put("dependencies", next.dependencies);
            HM.put("fitness", next.fitness);
        }
        return HM;
    }
    // end of printing

    // getters and setters
    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }

    public double getCrossoverRate() {
        return crossoverRate;
    }

    public void setCrossoverRate(double crossoverRate) {
        this.crossoverRate = crossoverRate;
    }
    // end of getters and setters
}
