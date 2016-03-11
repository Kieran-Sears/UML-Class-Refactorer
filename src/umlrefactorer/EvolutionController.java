/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package umlrefactorer;

import Evolution.MetaModel;
import Evolution.GeneticAlgorithm;
import java.util.HashMap;

/**
 *
 * @author Kieran
 */
public class EvolutionController {

    GeneticAlgorithm GA;

    public void initialiseGA(MetaModel mm, int populationSize, float mutationRate, float crossoverRate) {
        GA = new GeneticAlgorithm();
        GA.initialiseGA(mm, populationSize, mutationRate, crossoverRate);
    }

    public HashMap<String, Object> evolvePopulation() {
            GA.evolvePopulation();
            // GA.outputResultsToConsole();
        return  GA.returnResultsAsHashmap();
    }

}
