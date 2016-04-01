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
public class Parameter extends Component {

    private String direction;
    private String type;
    private String typeName;
    private String name;


    public String getType() {
        return type;
    }

    public void setType(String kind) {
        this.type = kind;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }

    public String getTypeName() {
        return typeName;
    }

    public void setTypeName(String typeName) {
        this.typeName = typeName;
    }

    
    

    @Override
    public String toString() {
        String string = "\n   Parameter";

        string += "\n   -type=" + typeName;
        return string;
    }

}
