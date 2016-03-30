/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package umlrefactorer;

import Evolution.MetaModel;
import Evolution.GeneticAlgorithm;
import java.util.ArrayList;

/**
 *
 * @author Kieran
 */
public class EvolutionController {

    GeneticAlgorithm GA;

    public void initialiseGA(MetaModel mm, int populationSize, float mutationRate) {
        GA = new GeneticAlgorithm();
        GA.initialiseGA(mm, populationSize, mutationRate);
        //GA.printPopulationDependencies();
    }

    public ArrayList<MetaModel> evolvePopulation() {
        ArrayList<MetaModel> selection = GA.selection();
        ArrayList<MetaModel> newPopulation = new ArrayList();
        for (MetaModel model : selection) {
            MetaModel crossedAndMutated = GA.mutate(model);
            crossedAndMutated.updateDependenciesAndFitness();
            newPopulation.add(crossedAndMutated);
        }
        GA.setPopulation(newPopulation);
        return newPopulation;
    }

}
