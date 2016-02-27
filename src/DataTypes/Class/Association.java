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
public class Association extends Component {
    // e.g.
    // <packagedElement isAbstract="false" isDerived="false" isLeaf="false" memberEnd="gEOpX0qGAqAABgcs wEOpX0qGAqAABgcv" xmi:id="f4OpX0qGAqAABgcq" xmi:type="uml:Association">

   
    private OwnedEnd[] ownedEnds;
    public String memberEnd;
    public boolean isDerived;
    public String xmi_type;
    public boolean isLeaf;
    public boolean isAbstract;

    public String getmemberEnd() {
        return memberEnd;
    }

    public void setmemberEnd(String memberEnd) {
        this.memberEnd = memberEnd;
    }

    public boolean isIsSpecification() {
        return isDerived;
    }

    public void setIsSpecification(boolean isSpecification) {
        this.isDerived = isSpecification;
    }

    public boolean isIsLeaf() {
        return isLeaf;
    }

    public void setIsLeaf(boolean isLeaf) {
        this.isLeaf = isLeaf;
    }

    public boolean isIsAbstract() {
        return isAbstract;
    }

    public void setIsAbstract(boolean isAbstract) {
        this.isAbstract = isAbstract;
    }

    public String getMemberEnd() {
        return memberEnd;
    }

    public void setMemberEnd(String memberEnd) {
        this.memberEnd = memberEnd;
    }

    public boolean isIsDerived() {
        return isDerived;
    }

    public void setIsDerived(boolean isDerived) {
        this.isDerived = isDerived;
    }

    public String getXmi_type() {
        return xmi_type;
    }

    public void setXmi_type(String xmi_type) {
        this.xmi_type = xmi_type;
    }


    public OwnedEnd[] getOwnedEnds() {
        return ownedEnds;
    }

    public void setOwnedEnds(OwnedEnd[] ownedEnds) {
        this.ownedEnds = ownedEnds;
    }

    @Override
    public String toString() {
        String string = "Association{\nmemberEnd=" + memberEnd + "\nisDerived=" + isDerived + "\nxmi_type=" + xmi_type + "\nisLeaf=" + isLeaf + "\nisAbstract=" + isAbstract + "\n";
        for (OwnedEnd end : ownedEnds) {
            string += end.toString() + "\n";
        }
        return string += "\n";
    }
}
