/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package Evolution;

import DataTypes.Class.*;
import DataTypes.Component;
import DataTypes.MetaModel;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Kieran
 */
public class Genome {

    Fitness fitness;
    ArrayList<Component> components;
    DependencyMatrix dependencies;

    
    public Genome(MetaModel model) {
        ArrayList<Association> associations = model.getAssociations();

        fitness = new Fitness();
        components = model.getComponents();
        dependencies = new DependencyMatrix(components, associations);
        
    }

    @Override
    public String toString() {
        String string;
        string = "Genome:\n" + "chromosome=\n";
        Iterator<Component> iterator = components.iterator();
        while (iterator.hasNext()) {
            string +=  iterator.next().toString();
        }
        string += "dependencies=\n" + dependencies.toString();
        string += "fitness=\n" + fitness.toString();
        return string;
    }

}
