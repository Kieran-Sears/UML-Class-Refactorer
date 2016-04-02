/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package umlrefactorer;

import DataTypes.CoreComponent;
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
    }

    public ArrayList<MetaModel> evolvePopulation(int numberOfGens) {
        ArrayList<MetaModel> newPopulation = null;
        for (int i = 0; i < numberOfGens; i++) {
            newPopulation = new ArrayList();
            ArrayList<MetaModel> selection = GA.selection();
            for (MetaModel model : selection) {
                MetaModel model2 = new MetaModel();
                ArrayList<CoreComponent> components = new ArrayList();
                for (CoreComponent component : model.getComponents()) {
                    components.add(component);
                }
                model2.setDependencies(model.getDependencies());
                model2.setComponents(components);
                MetaModel crossedAndMutated = GA.mutate(model2);
                crossedAndMutated.updateDependenciesAndFitness();
                newPopulation.add(crossedAndMutated);
            }
            GA.setPopulation(newPopulation);
        }

        return newPopulation;
    }

}
