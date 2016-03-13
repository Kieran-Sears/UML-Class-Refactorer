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

    public void initialiseGA(MetaModel mm, int populationSize, float mutationRate, float crossoverRate) {
        GA = new GeneticAlgorithm();
        GA.initialiseGA(mm, populationSize, mutationRate, crossoverRate);
        GA.printPopulationDependencies();
    }

    public ArrayList<MetaModel> evolvePopulation() {
            GA.evolvePopulation();
            GA.printPopulationDependencies();
            return GA.getPopulation();
    }

  
}
