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
public class Class extends Component  {
    
    
    // e.g. <packagedElement xmi:type="uml:Class" name="A" xmi:id="0x1f502_4" visibility="public" isAbstract="false">
    
    private String name;
    private String visibility;
    private Boolean isAbstract;
  
    
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

    public Boolean getIsAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(Boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    @Override
    public String toString() {
        return "\nClass{" + "\nname=" + name + "\n\n";
    }
  
    
}
