/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTypes.Class;

import DataTypes.CoreComponent;

/**
 *
 * @author Kieran
 */
public class Attribute extends CoreComponent {
 
 
    private String dependency;
    
 

    public String getDependency() {
        return dependency;
    }

    public void setDependency(String type) {
        this.dependency = type;
    }
    
    @Override
    public String toString() {
        return "Attribute{" + "\nname=" + this.getName() + "\ndependency=" + dependency + "}\n\n";
    }

 

    
}
