/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;

import DataTypes.Component;
import DataTypes.CoreComponent;
import DesignPatterns.AntiPattern;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Random;

/**
 *
 * @author Kieran
 */
public class GeneticAlgorithm {
    
    private ArrayList<MetaModel> population = new ArrayList();
    private int populationSize;
    private double mutationRate;
   

    public void initialiseGA(MetaModel model, int populationSize, double mutationRate) {
        this.populationSize = populationSize;
        this.mutationRate = mutationRate;

        // add random new individuals to the population, randomness is important for initial exploration of search space
        for (int i = 1; i < populationSize; i++) {
            MetaModel newModel = new MetaModel();
            ArrayList<CoreComponent> components = new ArrayList();
            for (CoreComponent component : model.getComponents()) {
                components.add(component);
            }
            ArrayList<CoreComponent> newComponents = new ArrayList();
            newComponents.add(components.get(0));
            components.remove(0);
            Random rand = new Random();
            while (components.size() > 0) {
                int index = rand.nextInt(components.size());
                newComponents.add(components.get(index));
                components.remove(index);
            }
            newModel.setDependencies(model.getDependencies());
            newModel.setComponents(newComponents);
            newModel.updateDependenciesAndFitness();
            population.add(newModel);
        }

        population.add(model);
    }

    public ArrayList<MetaModel> selection() {
        double randNum;
        int remainder = populationSize % 3;
        int divisor = populationSize - remainder;
        int count = divisor / 3;

        ArrayList<MetaModel> bestIndividuals = new ArrayList();
        ArrayList<MetaModel> lowestCoupledModels = new ArrayList();
        ArrayList<MetaModel> highestCohesiveModels = new ArrayList();
        ArrayList<MetaModel> bestMethodDistrobutionModels = new ArrayList();

        // objective find the individuals with the lowest coupling and the highest cohesion
        for (MetaModel individual : population) {
            MetaModel chosen = null;
            double totalCouplingFitness = 0;
            double totalCohesionFitness = 0;
            double totalWeightedMethodsFitness = 0;
            double coupleMax = 0;
            double coupleMin = -1;
            double WMPCMax = 0;
            double WMPCMin = -1;

            double t;

            totalCohesionFitness += individual.getFitness().getCohesionBetweenObjectClasses();

            totalCouplingFitness += individual.getFitness().getCouplingBetweenObjectClasses();
            if (coupleMax < individual.getFitness().getCohesionBetweenObjectClasses()) {
                coupleMax = individual.getFitness().getCohesionBetweenObjectClasses();
            }
            if (coupleMin > individual.getFitness().getCohesionBetweenObjectClasses() || coupleMin == -1) {
                coupleMin = individual.getFitness().getCohesionBetweenObjectClasses();
            }

            totalWeightedMethodsFitness += individual.getFitness().getWeightedMethodsPerClass();
            if (WMPCMax < individual.getFitness().getWeightedMethodsPerClass()) {
                WMPCMax = individual.getFitness().getWeightedMethodsPerClass();
            }
            if (WMPCMin > individual.getFitness().getWeightedMethodsPerClass() || coupleMin == -1) {
                WMPCMin = individual.getFitness().getWeightedMethodsPerClass();
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
                randNum -= population.get(i).getFitness().getCohesionBetweenObjectClasses();
                if (randNum < 0) {
                    chosen = population.get(i);
                }
            }
            highestCohesiveModels.add(chosen);

            // finding the most significant distribution 
            randNum = (double) (Math.random() * totalWeightedMethodsFitness);
            chosen = population.get(0);
            t = WMPCMax + WMPCMin;
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

        while (true) {
            if (remainder == 0) {
                break;
            } else {
                bestIndividuals.add(lowestCoupledModels.get((int) (Math.random() * populationSize)));
                remainder--;
            }
            if (remainder == 0) {
                break;
            } else {
                bestIndividuals.add(highestCohesiveModels.get((int) (Math.random() * populationSize)));
                remainder--;
            }
            if (remainder == 0) {
                break;
            } else {
                bestIndividuals.add(bestMethodDistrobutionModels.get((int) (Math.random() * populationSize)));
                remainder--;
            }

        }
        return bestIndividuals;
    }

    public MetaModel mutate(MetaModel model) {

        HashMap<Integer, Integer> indexes = new HashMap();
        ArrayList<CoreComponent> chromosome = model.getComponents();

        for (Component component : chromosome) {
            if (!(component instanceof DataTypes.Class.Class)) {
                double random = (double) Math.random();
                if (random <= mutationRate) {
                    int indexOf = chromosome.indexOf(component);
                    int replacementIndex = 1 + (int) (Math.random() * chromosome.size() - 1);
                    while (replacementIndex == indexOf) {
                        replacementIndex = 1 + (int) (Math.random() * chromosome.size() - 1);
                    }
                    indexes.put(indexOf, replacementIndex);
                }
            }
        }
        Iterator<Map.Entry<Integer, Integer>> iterator = indexes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> next = iterator.next();
            CoreComponent get = chromosome.get(next.getKey());
            chromosome.remove(get);
            if (next.getKey() < next.getValue()) {
                chromosome.add(next.getValue() - 1, get);
            } else {
                chromosome.add(next.getValue(), get);
            }
        }
        model.setComponents(chromosome);
        return model;
    }

    public MetaModel mutate(MetaModel model, AntiPattern heuristic) {

        return model;
    }

    // end of evolution operators
    // getters and setters
    public double getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(double mutationRate) {
        this.mutationRate = mutationRate;
    }


    public ArrayList<MetaModel> getPopulation() {
        return population;
    }

    public void setPopulation(ArrayList<MetaModel> population) {
        this.population = population;
    }

    public int getPopulationSize() {
        return populationSize;
    }

    public void setPopulationSize(int populationSize) {
        this.populationSize = populationSize;
    }

// end of getters and setters
    public void printPopulationDependencies() {
        for (MetaModel pop : population) {
            System.out.println(pop.getDependencies().toString());
        }
    }
}
