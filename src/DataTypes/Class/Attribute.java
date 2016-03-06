/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTypes.Class;

import DataTypes.Component;

/**
 *
 * @author Kieran
 */
public class Attribute extends Component {
 
    public String name;
    public String dependency;
    
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String type) {
        this.dependency = type;
    }
    
    @Override
    public String toString() {
        return "Attribute{\n" + "\nname=" + name + "\ndependency=" + dependency + "\n\n}";
    }

 

    
}
