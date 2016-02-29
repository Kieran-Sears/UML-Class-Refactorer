/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package umlrefactorer;

import Evolution.MetaModel;
import Evolution.GeneticAlgorithm;

/**
 *
 * @author Kieran
 */
public class EvolutionController {
    
    GeneticAlgorithm GA;
    
    
   public void initialiseGA(MetaModel mm, int populationSize, float mutationRate, float crossoverRate){
   GA = new GeneticAlgorithm(mm, populationSize, mutationRate, crossoverRate);
   }
    
   public void evolvePopulation(int numOfGenerations){
       for (int i = 0; i < numOfGenerations; i++) {
           GA.evolvePopulation();
           GA.outputResultsToConsole();
           // GA.returnResultsAsHashmap(); // TODO GUI
       }
      
   }
   
}
