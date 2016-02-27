/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package DataTypes.Class;

import DataTypes.Component;
import java.net.URL;

/**
 *
 * @author Kieran
 */
public class Attribute extends Component {
    // <ownedAttribute aggregation="none" isDerived="false" isDerivedUnion="false" isID="false"
    // isLeaf="false" isReadOnly="false" isStatic="false" name="cardNumber" 
    //        visibility="private" xmi:id="ilwpX0qGAqAABgbW" xmi:type="uml:Property">

    public String aggregation;
    public boolean isDerived;
    public boolean isDerivedUnion;
    public boolean isID;
    public boolean isLeaf;
    public boolean isReadOnly;
    public boolean isStatic;
    public String name;
    public String visibility;

    public String getAggregation() {
        return aggregation;
    }

    public void setAggregation(String aggregation) {
        this.aggregation = aggregation;
    }

    public boolean isIsDerived() {
        return isDerived;
    }

    public void setIsDerived(boolean isDerived) {
        this.isDerived = isDerived;
    }

    public boolean isIsDerivedUnion() {
        return isDerivedUnion;
    }

    public void setIsDerivedUnion(boolean isDerivedUnion) {
        this.isDerivedUnion = isDerivedUnion;
    }

    public boolean isIsID() {
        return isID;
    }

    public void setIsID(boolean isID) {
        this.isID = isID;
    }

    public boolean isIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public boolean isIsReadOnly() {
        return isReadOnly;
    }

    public void setIsReadOnly(boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public boolean isIsStatic() {
        return isStatic;
    }

    public void setIsStatic(boolean isStatic) {
        this.isStatic = isStatic;
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

    @Override
    public String toString() {
        return "Attribute{\n" + "aggregation=" + aggregation + "\nisDerived=" + isDerived + "\nisDerivedUnion=" + isDerivedUnion + "\nisID=" + isID + "\nisLeaf=" + isLeaf + "\nisReadOnly=" + isReadOnly + "\nisStatic=" + isStatic + "\nname=" + name + "\nvisibility=" + visibility + '}';
    }

 

    
}
