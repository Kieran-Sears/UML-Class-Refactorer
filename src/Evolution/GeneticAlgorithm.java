/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;

import DataTypes.MetaModel;
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

    
    private final ArrayList<Genome> population = new ArrayList();
    private float mutationRate;
    private float crossoverRate;

    public GeneticAlgorithm(MetaModel model, int populationSize, float mutationRate, float crossoverRate) {
         for (int i = 0; i < populationSize; i++) {
            Genome chromosome = new Genome(model);
            population.add(chromosome);
        }
         this.mutationRate = mutationRate;
         this.crossoverRate = crossoverRate;
    }

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

    public void evaluate(Genome chromosome) {
        chromosome.fitness.updateFitnessValues(chromosome);
        System.out.println(this.toString());
    }

    public void evolve() {
        for (Genome chromosome : population) {
            mutate(chromosome);
            crossover(chromosome);
            evaluate(chromosome);
        }
    }

    private void mutate(Genome chromosome) {

    }

    private void crossover(Genome chromosome) {

    }

    public void outputResultsToFile(File file) {
        PrintWriter writer = null;
        try {
            writer = new PrintWriter(file, "UTF-8");
            Iterator<Genome> iterator = population.iterator();
            while (iterator.hasNext()) {
                writer.println(iterator.next().components.toString());
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
        Iterator<Genome> iterator = population.iterator();
        while (iterator.hasNext()) {
            Genome next = iterator.next();
            string += "\n''''''''''''''''''''''''''''''\n";
            string += next.components.toString();
            string += next.dependencies.toString();
            string += next.fitness.toString();
        }
        System.out.println(string);
    }

    public HashMap<String, Object> returnResultsAsHashmap(){
        HashMap<String, Object> HM = new HashMap();
         Iterator<Genome> iterator = population.iterator();
        while (iterator.hasNext()) {
            Genome next = iterator.next();
            HM.put("components", next.components);
             HM.put("dependencies", next.dependencies);
              HM.put("fitness", next.fitness);
        }
        return HM;
    }
    
}
