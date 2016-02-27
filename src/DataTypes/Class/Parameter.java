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
    
    /*
     // takes in a class "type" book
     <ownedParameter kind="inout" name="book" type="kKqBX0qGAqAABgYK" xmi:id="VI8_uMqGAqAABggO" xmi:type="uml:Parameter">
     // returns a class "type" BookAdditionSuccess
     <ownedParameter direction="return" type="DHVIJMqGAqAABg9i" xmi:id="ewZIJMqGAqAABg5o_return" xmi:type="uml:Parameter"/>
     */

    private String direction;
    private String type;
    private String name;
    // private String dataType; // href to primative type - can ignore these for now

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

//    public String getDataType() {
//        return dataType;
//    }
//
//    public void setDataType(String dataType) {
//        this.dataType = dataType;
//    }
    @Override
    public String toString() {
        String string = "\n   Parameter{";

        string += "\n   direction=" + direction
                + "\n   type=" + type
                + "\n   name=" + name;

//        if (dataType != null) {
//            string += "\n   dataType=" + dataType;
//        }
        string += "}\n";
        return string;
    }

}
