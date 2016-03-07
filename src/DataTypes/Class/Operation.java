/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTypes.Class;

import DataTypes.Component;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Kieran
 */
public class Operation extends Component  {
 
    private String name;
    private ArrayList<Parameter> parameters;

  

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<Parameter> behaviourFeature) {
        this.parameters = behaviourFeature;
    }

    @Override
    public String toString() {
        String string = "\nOperation:" + "\n name=" + name  + "\n";
        Iterator<Parameter> iterator = parameters.iterator();
        while (iterator.hasNext()) {
            Parameter next = iterator.next();
            string += next.toString()+ "\n";
        }
        return string + "\n";
    }
    
}
