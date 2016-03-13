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
public class Class extends CoreComponent  {
    
 
    private String visibility;
    private Boolean isAbstract;
  

    public String getVisibility() {
        return visibility;
    }

    public void setVisibility(String visibility) {
        this.visibility = visibility;
    }

    public Boolean getIsAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(Boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    @Override
    public String toString() {
        return "\nClass{" + "\nname=" + this.getName() + "\n\n";
    }
  
    
}
