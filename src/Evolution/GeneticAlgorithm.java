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
    private float mutationRate;
    private float crossoverRate;

    // constructor
    public GeneticAlgorithm(MetaModel model, int populationSize, float mutationRate, float crossoverRate) {
        this.mutationRate = mutationRate;
        this.crossoverRate = crossoverRate;
        population.add(model);
        for (int i = 1; i < populationSize; i++) {
            MetaModel crossedover = crossover(model);
            MetaModel crossedAndMutated = mutate(crossedover);
            crossedAndMutated.updateDependencyMatrix();
            crossedAndMutated.updateFitnessValues();
            population.add(crossedAndMutated);
        }
    }
    

    public void evolvePopulation() {
        for (MetaModel model : population) {
            MetaModel crossedover = crossover(model);
            MetaModel crossedAndMutated = mutate(crossedover);
            crossedAndMutated.updateDependencyMatrix();
            crossedAndMutated.updateFitnessValues();
        }
    }
    // evolution operators

    private MetaModel mutate(MetaModel model) {
        // default mutation
        return model;
    }
    
     private MetaModel mutate(MetaModel model, MutationHeuristic heuristic) {
         // guided mutation
         // randomly move elements around
        return model;
    }

    private MetaModel crossover(MetaModel model) {
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
    public float getMutationRate() {
        return mutationRate;
    }

    public void setMutationRate(float mutationRate) {
        this.mutationRate = mutationRate;
    }

    public float getCrossoverRate() {
        return crossoverRate;
    }

    public void setCrossoverRate(float crossoverRate) {
        this.crossoverRate = crossoverRate;
    }
    // end of getters and setters
}
