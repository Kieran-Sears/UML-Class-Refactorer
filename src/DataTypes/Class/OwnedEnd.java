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
public class OwnedEnd extends Component {
    
    // <ownedEnd aggregation="shared" association="f4OpX0qGAqAABgcq" isDerived="false" 
   // isDerivedUnion="false" isLeaf="false" isNavigable="true" isReadOnly="false" isStatic="false" 
  //          type="kKqBX0qGAqAABgYK" xmi:id="wEOpX0qGAqAABgcv" xmi:type="uml:Property">
    
    private String association;
    private String aggregation;
    private String type;
    private Boolean isDerived;
    private Boolean isDerivedUnion;
    private Boolean isLeaf;
    private Boolean isNavigable;
    private Boolean isReadOnly;
    private Boolean isStatic;

    public String getAssociation() {
        return association;
    }

    public void setAssociation(String association) {
        this.association = association;
    }

    public String getAggregation() {
        return aggregation;
    }

    public void setAggregation(String aggregation) {
        this.aggregation = aggregation;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Boolean getIsDerived() {
        return isDerived;
    }

    public void setIsDerived(Boolean isDerived) {
        this.isDerived = isDerived;
    }

    public Boolean getIsDerivedUnion() {
        return isDerivedUnion;
    }

    public void setIsDerivedUnion(Boolean isDerivedUnion) {
        this.isDerivedUnion = isDerivedUnion;
    }

    public Boolean getIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(Boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public Boolean getIsNavigable() {
        return isNavigable;
    }

    public void setIsNavigable(Boolean isNavigable) {
        this.isNavigable = isNavigable;
    }

    public Boolean getIsReadOnly() {
        return isReadOnly;
    }

    public void setIsReadOnly(Boolean isReadOnly) {
        this.isReadOnly = isReadOnly;
    }

    public Boolean getIsStatic() {
        return isStatic;
    }

    public void setIsStatic(Boolean isStatic) {
        this.isStatic = isStatic;
    }

    @Override
    public String toString() {
        return "OwnedEnd{" + "\nassociation=" + association + "\naggregation=" + aggregation + "\ntype=" + type + "\nisDerived=" + isDerived + "\nisDerivedUnion=" + isDerivedUnion + "\nisLeaf=" + isLeaf + "\nisNavigable=" + isNavigable + "\nisReadOnly=" + isReadOnly + "\nisStatic=" + isStatic + '}';
    }

   
    
    
}
