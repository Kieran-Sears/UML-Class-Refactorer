/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;

import DataTypes.Component;
import DataTypes.CoreComponent;
import DesignPatterns.AntiPattern;
import DesignPatterns.AntiPatternAnalyser;
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
    private AntiPatternAnalyser analyser;
    private MetaModel[] eliteGroup;

    public void initialiseGA(MetaModel model, int populationSize, Boolean randomized) {
        eliteGroup = new MetaModel[10];
        MetaModel newModel = null;
        // add random new individuals to the population, randomness is important for initial exploration of search space
        for (int i = 1; i < populationSize; i++) {
            if (randomized) {
                newModel = generateNewRandomModel(model);
            } else {
                newModel = copyModel(model);
            }
            population.add(newModel);
        }
        population.add(model);
    }

    public MetaModel copyModel(MetaModel model) {
        MetaModel newModel = new MetaModel();
        ArrayList<CoreComponent> components = new ArrayList();
        for (CoreComponent component : model.getComponents()) {
            components.add(component);
        }
        newModel.setComponents(components);
        newModel.sortMethodDependencies();
        newModel.setNumberOfAttributes(model.getNumberOfAttributes());
        newModel.setNumberOfOperations(model.getNumberOfOperations());
        newModel.setNumberOfClasses(model.getNumberOfClasses());
        return newModel;
    }

    public MetaModel generateNewRandomModel(MetaModel model) {
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
        newModel.setComponents(newComponents);
        newModel.sortMethodDependencies();
        newModel.setNumberOfAttributes(model.getNumberOfAttributes());
        newModel.setNumberOfOperations(model.getNumberOfOperations());
        newModel.setNumberOfClasses(model.getNumberOfClasses());
        return newModel;
    }

    public ArrayList<MetaModel> selection(int populationSize, FitnessMetrics fitness) {
     
        ArrayList<MetaModel> best = new ArrayList();
        // check elite group isnt empty
        for (int i = 0; i < eliteGroup.length; i++) {
            MetaModel metaModel = eliteGroup[i];
            if (metaModel == null) {
                eliteGroup[i] = population.get(0);
            }
        }

        // adjust population in acordance with the populationSize parameter set by user if its been changed
        if (population.size() < populationSize) {
            for (int i = population.size(); i < populationSize; i++) {
                population.add(generateNewRandomModel(eliteGroup[new Random().nextInt(eliteGroup.length)]));
                //  population.add(generateNewRandomModel(population.get(new Random().nextInt(population.size()))));
            }
        }

        double randNum;
        double totalFitness = 0;
        MetaModel chosen = null;

        for (MetaModel individual : population) {
            totalFitness += fitness.getOverallFitness(individual);
            for (int i = 0; i < eliteGroup.length; i++) {
                if (fitness.getOverallFitness(individual) > fitness.getOverallFitness(eliteGroup[i])) {
                    eliteGroup[i] = individual;
                    i = eliteGroup.length;
                }
            }
        }

        for (int i = 0; i < eliteGroup.length; i++) {
            MetaModel metaModel = eliteGroup[i];
            best.add(metaModel);
        }

        for (int i = 0; i < populationSize; i++) { // -number for elite reintroduced members
            randNum = (double) (Math.random() * totalFitness);
            chosen = population.get(0);
            for (int j = 0; j < populationSize; j++) {
                randNum -= fitness.getOverallFitness(population.get(j));
                if (randNum < 0) {
                    chosen = population.get(i);
                }
            }
            best.add(chosen);
        }
     
        return best;
    }

    public ArrayList<MetaModel> evolvePopulation(Double mutationRate, int populationSize, FitnessMetrics fitness, ArrayList<AntiPattern> antiPatterns) {
        ArrayList<MetaModel> newPopulation = new ArrayList();
        ArrayList<MetaModel> selection = selection(populationSize, fitness);
        MetaModel mutatedModel = null;
        for (MetaModel model : selection) {
            if (!antiPatterns.isEmpty()) {
                for (AntiPattern antiPattern : antiPatterns) {
                    analyser = antiPattern;
                    boolean scanModel = analyser.scanModel(model);
                    if (scanModel) {
                        mutatedModel = analyser.PerformMutation(model);
                    } else {
                        mutatedModel = mutate(model, mutationRate);
                    }
                }
            } else {
                mutatedModel = mutate(model, mutationRate);
            }
            newPopulation.add(mutatedModel);
        }
        population = newPopulation;
        return newPopulation;
    }

//    public ArrayList<MetaModel> selection() {
//        double randNum;
//        int remainder = populationSize % 3;
//        int divisor = populationSize - remainder;
//        int count = divisor / 3;
//
//        ArrayList<MetaModel> bestIndividuals = new ArrayList();
//        ArrayList<MetaModel> lowestCoupledModels = new ArrayList();
//        ArrayList<MetaModel> highestCohesiveModels = new ArrayList();
//        ArrayList<MetaModel> bestMethodDistrobutionModels = new ArrayList();
//
//        // objective find the individuals with the lowest coupling and the highest cohesion
//        for (MetaModel individual : population) {
//            MetaModel chosen = null;
//            double totalCouplingFitness = 0;
//            double totalCohesionFitness = 0;
//            double totalWeightedMethodsFitness = 0;
//            double coupleMax = 0;
//            double coupleMin = -1;
//            double WMPCMax = 0;
//            double WMPCMin = -1;
//
//            double t;
//
//            totalCohesionFitness += individual.getFitness().getCohesionBetweenObjectClasses();
//
//            totalCouplingFitness += individual.getFitness().getCouplingBetweenObjectClasses();
//            if (coupleMax < individual.getFitness().getCohesionBetweenObjectClasses()) {
//                coupleMax = individual.getFitness().getCohesionBetweenObjectClasses();
//            }
//            if (coupleMin > individual.getFitness().getCohesionBetweenObjectClasses() || coupleMin == -1) {
//                coupleMin = individual.getFitness().getCohesionBetweenObjectClasses();
//            }
//
//            totalWeightedMethodsFitness += individual.getFitness().getWeightedMethodsPerClass();
//            if (WMPCMax < individual.getFitness().getWeightedMethodsPerClass()) {
//                WMPCMax = individual.getFitness().getWeightedMethodsPerClass();
//            }
//            if (WMPCMin > individual.getFitness().getWeightedMethodsPerClass() || coupleMin == -1) {
//                WMPCMin = individual.getFitness().getWeightedMethodsPerClass();
//            }
//
//            // finding the lowest coupled individuals
//            randNum = (double) (Math.random() * totalCouplingFitness);
//            chosen = population.get(0);
//            t = coupleMax + coupleMin;
//            for (int i = 0; i < populationSize; i++) {
//                randNum -= (t - population.get(i).getFitness().getCouplingBetweenObjectClasses());
//                if (randNum < 0) {
//
//                    chosen = population.get(i);
//                }
//            }
//
//            lowestCoupledModels.add(chosen);
//
//            // finding the highest cohesive individuals
//            randNum = (double) (Math.random() * totalCohesionFitness);
//            chosen = population.get(0);
//            for (int i = 0; i < populationSize; i++) {
//                randNum -= population.get(i).getFitness().getCohesionBetweenObjectClasses();
//                if (randNum < 0) {
//
//                    chosen = population.get(i);
//                }
//            }
//
//            highestCohesiveModels.add(chosen);
//
//            // finding the most significant distribution 
//            randNum = (double) (Math.random() * totalWeightedMethodsFitness);
//            chosen = population.get(0);
//            t = WMPCMax + WMPCMin;
//            for (int i = 0; i < populationSize; i++) {
//                randNum -= (t - population.get(i).getFitness().getWeightedMethodsPerClass());
//                if (randNum < 0) {
//
//                    chosen = population.get(i);
//                }
//            }
//
//            bestMethodDistrobutionModels.add(chosen);
//        }
//
//        // adding chosen from each group to the final parent selection
//        for (int i = 0; i < count; i++) {
//            bestIndividuals.add(lowestCoupledModels.get(i));
//            bestIndividuals.add(highestCohesiveModels.get(i));
//            bestIndividuals.add(bestMethodDistrobutionModels.get(i));
//        }
//
//        while (true) {
//            if (remainder == 0) {
//                break;
//            } else {
//                bestIndividuals.add(lowestCoupledModels.get((int) (Math.random() * populationSize)));
//                remainder--;
//            }
//            if (remainder == 0) {
//                break;
//            } else {
//                bestIndividuals.add(highestCohesiveModels.get((int) (Math.random() * populationSize)));
//                remainder--;
//            }
//            if (remainder == 0) {
//                break;
//            } else {
//                bestIndividuals.add(bestMethodDistrobutionModels.get((int) (Math.random() * populationSize)));
//                remainder--;
//            }
//
//        }
//        return bestIndividuals;
//    }
    public MetaModel mutate(MetaModel model, Double mutationRate) {

        MetaModel replacement = new MetaModel();
        ArrayList<CoreComponent> components = new ArrayList();
        for (CoreComponent component : model.getComponents()) {
            components.add(component);
        }

        HashMap<Integer, Integer> indexes = new HashMap();
        for (Component component : components) {
            if (!(component instanceof DataTypes.Class.Class)) {
                double random = (double) Math.random();
                if (random <= mutationRate) {
                    int indexOf = components.indexOf(component);
                    int replacementIndex = 1 + (int) (Math.random() * components.size() - 1);
                    while (replacementIndex == indexOf) {
                        replacementIndex = 1 + (int) (Math.random() * components.size() - 1);
                    }
                    indexes.put(indexOf, replacementIndex);
                }
            }
        }
        Iterator<Map.Entry<Integer, Integer>> iterator = indexes.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<Integer, Integer> next = iterator.next();
            CoreComponent get = components.get(next.getKey());
            components.remove(get);
            if (next.getKey() < next.getValue()) {
                components.add(next.getValue() - 1, get);
            } else {
                components.add(next.getValue(), get);
            }
        }

        replacement.setComponents(components);
        replacement.sortMethodDependencies();
        replacement.setNumberOfAttributes(model.getNumberOfAttributes());
        replacement.setNumberOfOperations(model.getNumberOfOperations());
        replacement.setNumberOfClasses(model.getNumberOfClasses());
        return replacement;
    }

    // end of evolution operators
    // getters and setters
}
