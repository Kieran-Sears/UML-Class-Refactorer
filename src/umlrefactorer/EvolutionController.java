/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package umlrefactorer;

import DataTypes.MetaModel;
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
           GA.evolve();
           GA.outputResultsToConsole();
           // GA.returnResultsAsHashmap(); // TODO GUI
       }
      
   }
   
}
