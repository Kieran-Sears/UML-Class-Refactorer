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

    private String source;
    private String target;

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public String getTarget() {
        return target;
    }

    public void setTarget(String target) {
        this.target = target;
    }

    @Override
    public String toString() {
        return "Association{" + "source=" + source + ", target=" + target + '}';
    }
}
