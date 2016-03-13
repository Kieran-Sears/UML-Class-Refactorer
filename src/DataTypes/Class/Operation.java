/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTypes.Class;


import DataTypes.CoreComponent;
import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Kieran
 */
public class Operation extends CoreComponent  {
 

    private ArrayList<Parameter> parameters;

  

    public ArrayList<Parameter> getParameters() {
        return parameters;
    }

    public void setParameters(ArrayList<Parameter> behaviourFeature) {
        this.parameters = behaviourFeature;
    }

    @Override
    public String toString() {
        String string = "\nOperation:" + "\n name=" + this.getName()  + "\n";
        Iterator<Parameter> iterator = parameters.iterator();
        while (iterator.hasNext()) {
            Parameter next = iterator.next();
            string += next.toString()+ "\n";
        }
        return string + "\n";
    }
    
}
