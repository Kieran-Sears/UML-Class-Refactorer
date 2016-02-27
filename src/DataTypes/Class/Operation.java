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
      //e.g.  <ownedOperation isAbstract="false" isLeaf="false" isOrdered="false" isQuery="false" 
          //  isStatic="false" isUnique="true" name="doMenu" visibility="public" xmi:id="AWzxX0qGAqAABgZo" 
         //   xmi:type="uml:Operation">
   
    private boolean isAbstract;
    private boolean isLeaf;
    private boolean isOrdered;
    private boolean isQuery;
    private boolean isStatic;
    private boolean isUnique; 
    private String name;
    private String visibility;
    private ArrayList<Parameter> parameters;

    public boolean isIsAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public boolean isIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public boolean isIsOrdered() {
        return isOrdered;
    }

    public void setIsOrdered(boolean isOrdered) {
        this.isOrdered = isOrdered;
    }

    public boolean isIsQuery() {
        return isQuery;
    }

    public void setIsQuery(boolean isQuery) {
        this.isQuery = isQuery;
    }

    public boolean isIsStatic() {
        return isStatic;
    }

    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
    }

    public boolean isIsUnique() {
        return isUnique;
    }

    public void setIsUnique(boolean isUnique) {
        this.isUnique = isUnique;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

   

    public ArrayList<Parameter> getBehaviourFeature() {
        return parameters;
    }

    public void setBehaviourFeature(ArrayList<Parameter> behaviourFeature) {
        this.parameters = behaviourFeature;
    }

    @Override
    public String toString() {
        String string = "\nOperation:" + "\n name=" + name + "\n visibility=" + visibility + "\n isAbstract=" + isAbstract + "\n";
        Iterator<Parameter> iterator = parameters.iterator();
        while (iterator.hasNext()) {
            Parameter next = iterator.next();
            string += next.toString()+ "\n";
        }
        return string + "\n";
    }
    
}
